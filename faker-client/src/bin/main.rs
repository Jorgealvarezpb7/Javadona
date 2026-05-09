use anyhow::Result;
use clap::Parser;
use faker_client::fakers;
use rand::prelude::*;
use std::collections::HashSet;

use faker_client::client::ApiClient;
use faker_client::models::{
    CreateSaleRequest, CustomerResponse, PaymentMethod, ProductResponse, SaleLineRequest,
    SaleResponse, SalesPointResponse, StockIncrementRequest, StockLineItem,
};

#[derive(Parser)]
#[command(
    name = "faker-client",
    about = "Seed the Javadona API Gateway with interrelated fake data"
)]
struct Cli {
    /// API Gateway base URL
    #[arg(long, default_value = "http://localhost:8080")]
    base_url: String,

    /// Number of sales points to create
    #[arg(long, default_value_t = 5)]
    sales_points: usize,

    /// Number of products to create
    #[arg(long, default_value_t = 20)]
    products: usize,

    /// Number of customers to create
    #[arg(long, default_value_t = 10)]
    customers: usize,

    /// Max sales per customer
    #[arg(long, default_value_t = 6)]
    max_sales: usize,

    /// RNG seed for reproducible runs (random if omitted)
    #[arg(long)]
    seed: Option<u64>,
}

fn ok(label: &str) {
    println!("    [+] {label}");
}

fn err(label: &str, e: &anyhow::Error) {
    eprintln!("    [!] {label}: {e}");
}

async fn phase_sales_points(
    api: &ApiClient,
    rng: &mut StdRng,
    count: usize,
) -> Vec<SalesPointResponse> {
    println!("\n[1/5] Creating {count} sales points...");
    let mut out = Vec::with_capacity(count);
    for i in 0..count {
        let req = fakers::sales_point(rng, i);
        match api.create_sales_point(&req).await {
            Ok(sp) => {
                ok(&format!(
                    "{} — {} [{}]",
                    sp.name.as_deref().unwrap_or("?"),
                    sp.city.as_deref().unwrap_or("?"),
                    sp.id.as_deref().unwrap_or("?"),
                ));
                out.push(sp);
            }
            Err(e) => err(&format!("sales_point[{i}]"), &e),
        }
    }
    out
}

async fn phase_products(api: &ApiClient, rng: &mut StdRng, count: usize) -> Vec<ProductResponse> {
    println!("\n[2/5] Creating {count} products...");
    let mut used_barcodes = HashSet::new();
    let mut out = Vec::with_capacity(count);
    for i in 0..count {
        let req = fakers::product(rng, &mut used_barcodes);
        match api.create_product(&req).await {
            Ok(p) => {
                ok(&format!(
                    "{} ({}) — €{:.2} [{}]",
                    p.name.as_deref().unwrap_or("?"),
                    p.category.as_deref().unwrap_or("?"),
                    p.base_price.unwrap_or(0.0),
                    p.id.as_deref().unwrap_or("?"),
                ));
                out.push(p);
            }
            Err(e) => err(&format!("product[{i}]"), &e),
        }
    }
    out
}

async fn phase_stock(
    api: &ApiClient,
    rng: &mut StdRng,
    sales_points: &[SalesPointResponse],
    products: &[ProductResponse],
) {
    println!(
        "\n[3/5] Stocking {} products at {} sales points...",
        products.len(),
        sales_points.len()
    );
    for sp in sales_points {
        let Some(sp_id) = sp.id.as_deref() else {
            continue;
        };

        let lines: Vec<_> = products
            .iter()
            .filter_map(|p| {
                p.id.as_deref().map(|pid| StockLineItem {
                    product_id: pid.to_string(),
                    quantity: rng.gen_range(50..200),
                })
            })
            .collect();

        let req = StockIncrementRequest {
            sales_point_id: sp_id.to_string(),
            lines,
        };

        match api.increment_stock(&req).await {
            Ok(_) => ok(&format!(
                "{} stocked ({} SKUs)",
                sp.name.as_deref().unwrap_or(sp_id),
                products.len()
            )),
            Err(e) => err(&format!("stock@{sp_id}"), &e),
        }
    }
}

async fn phase_customers(api: &ApiClient, rng: &mut StdRng, count: usize) -> Vec<CustomerResponse> {
    println!("\n[4/5] Creating {count} customers...");
    let mut used_docs = HashSet::new();
    let mut out = Vec::with_capacity(count);
    for i in 0..count {
        let req = fakers::customer(rng, &mut used_docs);
        match api.create_customer(&req).await {
            Ok(c) => {
                ok(&format!(
                    "{} {} — {} [{}]",
                    c.first_name.as_deref().unwrap_or("?"),
                    c.last_name.as_deref().unwrap_or("?"),
                    c.email.as_deref().unwrap_or("?"),
                    c.id.as_deref().unwrap_or("?"),
                ));
                out.push(c);
            }
            Err(e) => err(&format!("customer[{i}]"), &e),
        }
    }
    out
}

async fn phase_sales(
    api: &ApiClient,
    rng: &mut StdRng,
    max_sales: usize,
    customers: &[CustomerResponse],
    sales_points: &[SalesPointResponse],
    products: &[ProductResponse],
) -> Vec<SaleResponse> {
    println!("\n[5/5] Creating sales and interrelations...");
    let payment_methods = [
        PaymentMethod::Cash,
        PaymentMethod::Card,
        PaymentMethod::Bizum,
    ];
    let mut all_sales: Vec<SaleResponse> = Vec::new();

    for customer in customers {
        let Some(cid) = customer.id.as_deref() else {
            continue;
        };
        let name = format!(
            "{} {}",
            customer.first_name.as_deref().unwrap_or("?"),
            customer.last_name.as_deref().unwrap_or("?"),
        );
        println!("    Customer: {name} [{cid}]");

        let n_freq = rng.gen_range(1..=sales_points.len().min(3));
        let frequent: Vec<&SalesPointResponse> =
            sales_points.choose_multiple(rng, n_freq).collect();

        for sp in &frequent {
            if let Some(sp_id) = sp.id.as_deref()
                && let Err(e) = api.add_frequent_sales_point(cid, sp_id).await
            {
                err(&format!("freq_sp {sp_id}"), &e);
            }
        }
        println!("      Frequent sales points: {}", frequent.len());

        let n_sales = rng.gen_range(2..=max_sales);
        let mut spent_total = 0.0f64;
        let mut sales_count = 0usize;

        for _ in 0..n_sales {
            let sp = if !frequent.is_empty() && rng.gen_bool(0.70) {
                *frequent.choose(rng).unwrap()
            } else {
                sales_points.choose(rng).unwrap()
            };
            let Some(sp_id) = sp.id.as_deref() else {
                continue;
            };

            // Pick 1-5 products → product ↔ sale interrelation
            let n_items = rng.gen_range(1..=5.min(products.len()));
            let chosen: Vec<&ProductResponse> = products.choose_multiple(rng, n_items).collect();

            let lines: Vec<SaleLineRequest> = chosen
                .iter()
                .filter_map(|p| {
                    let pid = p.id.as_deref()?;
                    let pname = p.name.as_deref()?;
                    let price = p.base_price?;
                    Some(SaleLineRequest {
                        product_id: pid.to_string(),
                        product_name: pname.to_string(),
                        quantity: rng.gen_range(1..=4),
                        unit_price: price,
                    })
                })
                .collect();

            if lines.is_empty() {
                continue;
            }

            let req = CreateSaleRequest {
                customer_id: cid.to_string(),
                sales_point_id: sp_id.to_string(),
                payment_method: *payment_methods.choose(rng).unwrap(),
                lines,
            };

            match api.create_sale(&req).await {
                Ok(sale) => {
                    spent_total += sale.total_amount.unwrap_or(0.0);
                    sales_count += 1;
                    all_sales.push(sale);
                }
                Err(e) => err("create_sale", &e),
            }
        }

        println!("      Sales: {sales_count}  |  Total spent: €{spent_total:.2}");

        // Reward points: 1 point per €10 spent → customer ↔ purchases interrelation
        let points = (spent_total / 10.0) as u32;
        if points > 0 {
            match api.add_reward_points(cid, points).await {
                Ok(r) => println!(
                    "      Reward points added: {points}  (balance: {})",
                    r.reward_points.unwrap_or(0)
                ),
                Err(e) => err("add_reward_points", &e),
            }
        }
    }

    all_sales
}

async fn phase_refunds(api: &ApiClient, rng: &mut StdRng, sales: &[SaleResponse]) {
    let count = ((sales.len() as f64) * 0.10).ceil() as usize;
    if count == 0 {
        return;
    }
    println!("\n  Refunding {count} sales (~10%)...");
    for sale in sales.choose_multiple(rng, count) {
        let Some(sid) = sale.id.as_deref() else {
            continue;
        };
        match api.refund_sale(sid).await {
            Ok(_) => ok(&format!("refunded sale {sid}")),
            Err(e) => err(&format!("refund {sid}"), &e),
        }
    }
}

#[tokio::main]
async fn main() -> Result<()> {
    let cli = Cli::parse();

    let seed = cli.seed.unwrap_or_else(rand::random);
    let mut rng = StdRng::seed_from_u64(seed);

    println!("Javadona faker-client");
    println!("  Gateway : {}", cli.base_url);
    println!("  RNG seed: {seed}  (re-use to reproduce this run)");

    let api = ApiClient::new(&cli.base_url);

    let sales_points = phase_sales_points(&api, &mut rng, cli.sales_points).await;
    let products = phase_products(&api, &mut rng, cli.products).await;
    phase_stock(&api, &mut rng, &sales_points, &products).await;
    let customers = phase_customers(&api, &mut rng, cli.customers).await;
    let sales = phase_sales(
        &api,
        &mut rng,
        cli.max_sales,
        &customers,
        &sales_points,
        &products,
    )
    .await;
    phase_refunds(&api, &mut rng, &sales).await;

    let refunded = (sales.len() as f64 * 0.10).ceil() as usize;
    println!("\nDone.");
    println!("  Sales points : {}", sales_points.len());
    println!("  Products     : {}", products.len());
    println!("  Customers    : {}", customers.len());
    println!("  Sales        : {}", sales.len());
    println!("  Refunds      : ~{refunded}");

    Ok(())
}
