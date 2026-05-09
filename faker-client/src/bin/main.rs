use anyhow::{Context, Result};
use clap::Parser;
use faker_client::fakers;
use faker_client::models::{StaticConfig, StaticSalesPoint};
use rand::prelude::*;
use std::collections::HashSet;
use std::sync::Arc;
use std::time::Duration;
use tokio::sync::RwLock;
use tokio::task::JoinSet;

use faker_client::client::ApiClient;
use faker_client::models::{
    CreateSaleRequest, CreateSalesPointRequest, PaymentMethod, SaleLineRequest,
    StockIncrementRequest, StockLineItem,
};

#[derive(Parser)]
#[command(
    name = "faker-client",
    about = "Continuously seed the Javadona API Gateway with interrelated fake data"
)]
struct Cli {
    /// API Gateway base URL
    #[arg(long, default_value = "http://localhost:8080")]
    base_url: String,

    /// Path to the static config TOML file
    #[arg(long, default_value = "faker-static.toml")]
    config: String,

    /// Interval between ticks in seconds
    #[arg(long, default_value_t = 5)]
    interval: u64,

    /// Minimum ops per tick
    #[arg(long, default_value_t = 3)]
    min_ops: usize,

    /// Maximum ops per tick
    #[arg(long, default_value_t = 8)]
    max_ops: usize,

    /// RNG seed for reproducible runs (random if omitted)
    #[arg(long)]
    seed: Option<u64>,
}

// ── Shared state ──────────────────────────────────────────────────────────────

#[derive(Default)]
struct State {
    // (id, name)
    customers: Vec<(String, String)>,
    // (id, name, base_price)
    products: Vec<(String, String, f64)>,
    // (id, name) — sourced exclusively from faker-static.toml
    sales_points: Vec<(String, String)>,
    // sale ids eligible for refund
    sales: Vec<String>,
    // dedup sets used across the entire run
    used_docs: HashSet<String>,
    used_barcodes: HashSet<String>,
}

type SharedState = Arc<RwLock<State>>;

// ── Operations ────────────────────────────────────────────────────────────────

#[derive(Debug, Clone)]
enum Op {
    CreateCustomer,
    CreateProduct,
    CreateSale {
        customer_id: String,
        sales_point_id: String,
        lines: Vec<(String, String, f64, i32)>, // (pid, pname, price, qty)
        payment: PaymentMethod,
    },
    AddRewardPoints {
        customer_id: String,
        points: u32,
    },
    RefundSale {
        sale_id: String,
    },
    AddFrequentSalesPoint {
        customer_id: String,
        sales_point_id: String,
    },
    GetCustomer {
        id: String,
    },
    GetProduct {
        id: String,
    },
    IncrementStock {
        sales_point_id: String,
        lines: Vec<(String, i32)>, // (product_id, qty)
    },
}

fn build_ops(state: &State, rng: &mut StdRng, n: usize) -> Vec<Op> {
    let has_customers = !state.customers.is_empty();
    let has_products = !state.products.is_empty();
    let has_sales_points = !state.sales_points.is_empty();
    let has_sales = !state.sales.is_empty();
    let can_sell = has_customers && has_products && has_sales_points;

    let payment_methods = [
        PaymentMethod::Cash,
        PaymentMethod::Card,
        PaymentMethod::Bizum,
    ];

    let mut ops = Vec::with_capacity(n);
    for _ in 0..n {
        let mut weights: Vec<(u32, &str)> = vec![(12, "create_customer"), (12, "create_product")];
        if can_sell {
            weights.push((40, "create_sale"));
            weights.push((15, "increment_stock"));
        }
        if has_customers {
            weights.push((8, "get_customer"));
        }
        if has_products {
            weights.push((8, "get_product"));
        }
        if has_customers && has_sales_points {
            weights.push((10, "add_frequent"));
        }
        if has_customers {
            weights.push((5, "add_rewards"));
        }
        if has_sales {
            weights.push((5, "refund"));
        }

        let total: u32 = weights.iter().map(|(w, _)| w).sum();
        let mut pick = rng.gen_range(0..total);
        let bucket = weights
            .iter()
            .find(|(w, _)| {
                if pick < *w {
                    true
                } else {
                    pick -= w;
                    false
                }
            })
            .map(|(_, b)| *b)
            .unwrap_or("create_customer");

        let op = match bucket {
            "create_customer" => Op::CreateCustomer,
            "create_product" => Op::CreateProduct,
            "create_sale" => {
                let (cid, _) = state.customers.choose(rng).unwrap();
                let (spid, _) = state.sales_points.choose(rng).unwrap();
                let n_items = rng.gen_range(1..=5.min(state.products.len()));
                let chosen: Vec<_> = state.products.choose_multiple(rng, n_items).collect();
                let lines = chosen
                    .iter()
                    .map(|(pid, pname, price)| {
                        (pid.clone(), pname.clone(), *price, rng.gen_range(1..=4i32))
                    })
                    .collect();
                Op::CreateSale {
                    customer_id: cid.clone(),
                    sales_point_id: spid.clone(),
                    lines,
                    payment: *payment_methods.choose(rng).unwrap(),
                }
            }
            "increment_stock" => {
                let (spid, _) = state.sales_points.choose(rng).unwrap();
                let n_items = rng.gen_range(1..=state.products.len().min(10));
                let lines = state
                    .products
                    .choose_multiple(rng, n_items)
                    .map(|(pid, _, _)| (pid.clone(), rng.gen_range(10..100i32)))
                    .collect();
                Op::IncrementStock {
                    sales_point_id: spid.clone(),
                    lines,
                }
            }
            "get_customer" => {
                let (id, _) = state.customers.choose(rng).unwrap();
                Op::GetCustomer { id: id.clone() }
            }
            "get_product" => {
                let (id, _, _) = state.products.choose(rng).unwrap();
                Op::GetProduct { id: id.clone() }
            }
            "add_frequent" => {
                let (cid, _) = state.customers.choose(rng).unwrap();
                let (spid, _) = state.sales_points.choose(rng).unwrap();
                Op::AddFrequentSalesPoint {
                    customer_id: cid.clone(),
                    sales_point_id: spid.clone(),
                }
            }
            "add_rewards" => {
                let (cid, _) = state.customers.choose(rng).unwrap();
                Op::AddRewardPoints {
                    customer_id: cid.clone(),
                    points: rng.gen_range(1..=100),
                }
            }
            "refund" => {
                let idx = rng.gen_range(0..state.sales.len());
                Op::RefundSale {
                    sale_id: state.sales[idx].clone(),
                }
            }
            _ => Op::CreateCustomer,
        };
        ops.push(op);
    }
    ops
}

async fn execute_op(op: Op, api: ApiClient, state: SharedState, rng_seed: u64) {
    let mut rng = StdRng::seed_from_u64(rng_seed);
    match op {
        Op::CreateCustomer => {
            let req = {
                let mut st = state.write().await;
                fakers::customer(&mut rng, &mut st.used_docs)
            };
            match api.create_customer(&req).await {
                Ok(c) => {
                    if let (Some(id), Some(fname), Some(lname)) = (c.id, c.first_name, c.last_name)
                    {
                        println!("  [+] customer  {fname} {lname} [{id}]");
                        state
                            .write()
                            .await
                            .customers
                            .push((id, format!("{fname} {lname}")));
                    }
                }
                Err(e) => eprintln!("  [!] create_customer: {e}"),
            }
        }
        Op::CreateProduct => {
            let req = {
                let mut st = state.write().await;
                fakers::product(&mut rng, &mut st.used_barcodes)
            };
            match api.create_product(&req).await {
                Ok(p) => {
                    if let (Some(id), Some(name), Some(price)) = (p.id, p.name, p.base_price) {
                        println!("  [+] product   {name} €{price:.2} [{id}]");
                        state.write().await.products.push((id, name, price));
                    }
                }
                Err(e) => eprintln!("  [!] create_product: {e}"),
            }
        }
        Op::CreateSale {
            customer_id,
            sales_point_id,
            lines,
            payment,
        } => {
            let sale_lines: Vec<SaleLineRequest> = lines
                .iter()
                .map(|(pid, pname, price, qty)| SaleLineRequest {
                    product_id: pid.clone(),
                    product_name: pname.clone(),
                    quantity: *qty,
                    unit_price: *price,
                })
                .collect();
            let req = CreateSaleRequest {
                customer_id: customer_id.clone(),
                sales_point_id: sales_point_id.clone(),
                payment_method: payment,
                lines: sale_lines,
            };
            match api.create_sale(&req).await {
                Ok(s) => {
                    if let Some(id) = s.id {
                        let total = s.total_amount.unwrap_or(0.0);
                        println!("  [+] sale      €{total:.2} [{id}]");
                        state.write().await.sales.push(id);
                    }
                }
                Err(e) => eprintln!("  [!] create_sale: {e}"),
            }
        }
        Op::IncrementStock {
            sales_point_id,
            lines,
        } => {
            let req = StockIncrementRequest {
                sales_point_id: sales_point_id.clone(),
                lines: lines
                    .iter()
                    .map(|(pid, qty)| StockLineItem {
                        product_id: pid.clone(),
                        quantity: *qty,
                    })
                    .collect(),
            };
            match api.increment_stock(&req).await {
                Ok(_) => println!("  [+] stock     {sales_point_id} ({} SKUs)", lines.len()),
                Err(e) => eprintln!("  [!] increment_stock: {e}"),
            }
        }
        Op::AddRewardPoints {
            customer_id,
            points,
        } => match api.add_reward_points(&customer_id, points).await {
            Ok(r) => println!(
                "  [+] rewards   +{points} → balance {} [{customer_id}]",
                r.reward_points.unwrap_or(0)
            ),
            Err(e) => eprintln!("  [!] add_reward_points: {e}"),
        },
        Op::RefundSale { sale_id } => match api.refund_sale(&sale_id).await {
            Ok(_) => println!("  [+] refund    [{sale_id}]"),
            Err(e) => eprintln!("  [!] refund_sale: {e}"),
        },
        Op::AddFrequentSalesPoint {
            customer_id,
            sales_point_id,
        } => match api
            .add_frequent_sales_point(&customer_id, &sales_point_id)
            .await
        {
            Ok(_) => println!("  [+] freq_sp   {customer_id} → {sales_point_id}"),
            Err(e) => eprintln!("  [!] add_frequent_sales_point: {e}"),
        },
        Op::GetCustomer { id } => match api.get_customer(&id).await {
            Ok(c) => println!(
                "  [~] customer  {} {} [{id}]",
                c.first_name.as_deref().unwrap_or("?"),
                c.last_name.as_deref().unwrap_or("?"),
            ),
            Err(e) => eprintln!("  [!] get_customer: {e}"),
        },
        Op::GetProduct { id } => match api.get_product(&id).await {
            Ok(p) => println!(
                "  [~] product   {} [{id}]",
                p.name.as_deref().unwrap_or("?"),
            ),
            Err(e) => eprintln!("  [!] get_product: {e}"),
        },
    }
}

// ── Init: reconcile sales points from config ───────────────────────────────────

async fn init_sales_points(
    api: &ApiClient,
    state: &SharedState,
    config_sps: &[StaticSalesPoint],
) -> Result<()> {
    println!("[init] Fetching existing sales points...");

    // Fetch up to 200 so we cover any pre-existing data
    let existing = api
        .list_sales_points(200)
        .await
        .context("list_sales_points")?;

    // Index existing by name for O(1) lookup
    let existing_by_name: std::collections::HashMap<&str, &str> = existing
        .iter()
        .filter_map(|sp| {
            let id = sp.id.as_deref()?;
            let name = sp.name.as_deref()?;
            Some((name, id))
        })
        .collect();

    let mut st = state.write().await;

    // First load all already-existing config sales points into state
    for sp in config_sps {
        if let Some(&id) = existing_by_name.get(sp.name.as_str()) {
            println!("  [=] salespoint {} already exists [{id}]", sp.name);
            st.sales_points.push((id.to_string(), sp.name.clone()));
        }
    }

    // Collect names that still need to be created (drop the write lock first)
    let missing: Vec<StaticSalesPoint> = config_sps
        .iter()
        .filter(|sp| !existing_by_name.contains_key(sp.name.as_str()))
        .cloned()
        .collect();

    drop(st);

    for sp in missing {
        let req = CreateSalesPointRequest {
            name: sp.name.clone(),
            street: sp.street.clone(),
            city: sp.city.clone(),
            postal_code: sp.postal_code.clone(),
            province: sp.province.clone(),
            phone_number: sp.phone_number.clone(),
            opens_at: sp.opens_at.clone(),
            closes_at: sp.closes_at.clone(),
        };
        match api.create_sales_point(&req).await {
            Ok(created) => {
                if let (Some(id), Some(name)) = (created.id, created.name) {
                    println!("  [+] salespoint {name} [{id}]");
                    state.write().await.sales_points.push((id, name));
                }
            }
            Err(e) => eprintln!("  [!] create_sales_point {}: {e}", sp.name),
        }
    }

    println!(
        "[init] Sales points ready: {}\n",
        state.read().await.sales_points.len()
    );
    Ok(())
}

// ── Bootstrap: seed initial products + customers ──────────────────────────────

async fn bootstrap(api: &ApiClient, state: &SharedState, rng: &mut StdRng) {
    println!("[boot] Creating 10 products and 5 customers...");

    for _ in 0..10usize {
        let req = {
            let mut st = state.write().await;
            fakers::product(rng, &mut st.used_barcodes)
        };
        match api.create_product(&req).await {
            Ok(p) => {
                if let (Some(id), Some(name), Some(price)) = (p.id, p.name, p.base_price) {
                    println!("  [+] product   {name} €{price:.2} [{id}]");
                    state.write().await.products.push((id, name, price));
                }
            }
            Err(e) => eprintln!("  [!] boot product: {e}"),
        }
    }

    for _ in 0..5usize {
        let req = {
            let mut st = state.write().await;
            fakers::customer(rng, &mut st.used_docs)
        };
        match api.create_customer(&req).await {
            Ok(c) => {
                if let (Some(id), Some(fname), Some(lname)) = (c.id, c.first_name, c.last_name) {
                    println!("  [+] customer  {fname} {lname} [{id}]");
                    state
                        .write()
                        .await
                        .customers
                        .push((id, format!("{fname} {lname}")));
                }
            }
            Err(e) => eprintln!("  [!] boot customer: {e}"),
        }
    }

    // Stock all known products at every sales point
    let (sp_ids, product_ids): (Vec<_>, Vec<_>) = {
        let st = state.read().await;
        (
            st.sales_points.iter().map(|(id, _)| id.clone()).collect(),
            st.products.iter().map(|(id, _, _)| id.clone()).collect(),
        )
    };
    for sp_id in &sp_ids {
        let lines: Vec<StockLineItem> = product_ids
            .iter()
            .map(|pid| StockLineItem {
                product_id: pid.clone(),
                quantity: rng.gen_range(50..200),
            })
            .collect();
        let req = StockIncrementRequest {
            sales_point_id: sp_id.clone(),
            lines,
        };
        match api.increment_stock(&req).await {
            Ok(_) => println!("  [+] stock     {sp_id} ({} SKUs)", product_ids.len()),
            Err(e) => eprintln!("  [!] boot stock: {e}"),
        }
    }

    println!("[boot] Done.\n");
}

// ── Main ──────────────────────────────────────────────────────────────────────

#[tokio::main]
async fn main() -> Result<()> {
    let cli = Cli::parse();

    let seed = cli.seed.unwrap_or_else(rand::random);
    let mut rng = StdRng::seed_from_u64(seed);

    println!("Javadona faker-client (continuous mode)");
    println!("  Gateway  : {}", cli.base_url);
    println!("  Config   : {}", cli.config);
    println!("  Interval : {}s", cli.interval);
    println!("  Ops/tick : {}–{}", cli.min_ops, cli.max_ops);
    println!("  RNG seed : {seed}");
    println!("  Press Ctrl+C to stop.\n");

    let raw = std::fs::read_to_string(&cli.config)
        .with_context(|| format!("read config {}", cli.config))?;
    let config: StaticConfig =
        toml::from_str(&raw).with_context(|| format!("parse config {}", cli.config))?;

    let api = ApiClient::new(&cli.base_url);
    let state: SharedState = Arc::new(RwLock::new(State::default()));

    init_sales_points(&api, &state, &config.sales_points).await?;
    bootstrap(&api, &state, &mut rng).await;

    let interval = Duration::from_secs(cli.interval);
    let mut tick = 0u64;

    loop {
        tokio::select! {
            _ = tokio::signal::ctrl_c() => {
                println!("\nStopped.");
                break;
            }
            _ = tokio::time::sleep(interval) => {
                tick += 1;
                let n = rng.gen_range(cli.min_ops..=cli.max_ops);
                println!("── tick {tick}  ({n} ops) ──────────────────────────");

                let ops = {
                    let st = state.read().await;
                    build_ops(&st, &mut rng, n)
                };

                let mut set = JoinSet::new();
                for op in ops {
                    let api2 = api.clone();
                    let state2 = Arc::clone(&state);
                    let op_seed: u64 = rng.next_u64();
                    set.spawn(async move {
                        execute_op(op, api2, state2, op_seed).await;
                    });
                }
                while set.join_next().await.is_some() {}
            }
        }
    }

    Ok(())
}
