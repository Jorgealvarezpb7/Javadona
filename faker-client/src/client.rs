use anyhow::{Context, Result};
use reqwest::Client;
use serde::{Serialize, de::DeserializeOwned};

use crate::models::*;

pub struct ApiClient {
    http: Client,
    base: String,
}

impl ApiClient {
    pub fn new(base_url: &str) -> Self {
        Self {
            http: Client::new(),
            base: base_url.trim_end_matches('/').to_string(),
        }
    }

    fn url(&self, path: &str) -> String {
        format!("{}{}", self.base, path)
    }

    async fn post_json<Req, Res>(&self, path: &str, body: &Req) -> Result<Res>
    where
        Req: Serialize,
        Res: DeserializeOwned,
    {
        let url = self.url(path);
        let res = self
            .http
            .post(&url)
            .json(body)
            .send()
            .await
            .with_context(|| format!("POST {url}"))?;

        let status = res.status();
        if !status.is_success() {
            let body = res.text().await.unwrap_or_default();
            anyhow::bail!("POST {url} → {status}: {body}");
        }

        res.json::<Res>()
            .await
            .with_context(|| format!("deserialise POST {url}"))
    }

    async fn post_json_void<Req>(&self, path: &str, body: &Req) -> Result<()>
    where
        Req: Serialize,
    {
        let url = self.url(path);
        let res = self
            .http
            .post(&url)
            .json(body)
            .send()
            .await
            .with_context(|| format!("POST {url}"))?;

        let status = res.status();
        if !status.is_success() {
            let body = res.text().await.unwrap_or_default();
            anyhow::bail!("POST {url} → {status}: {body}");
        }
        Ok(())
    }

    async fn post_empty<Res>(&self, path: &str) -> Result<Res>
    where
        Res: DeserializeOwned,
    {
        let url = self.url(path);
        let res = self
            .http
            .post(&url)
            .header("Content-Length", "0")
            .send()
            .await
            .with_context(|| format!("POST {url}"))?;

        let status = res.status();
        if !status.is_success() {
            let body = res.text().await.unwrap_or_default();
            anyhow::bail!("POST {url} → {status}: {body}");
        }

        res.json::<Res>()
            .await
            .with_context(|| format!("deserialise POST {url}"))
    }

    async fn post_empty_void(&self, path: &str) -> Result<()> {
        let url = self.url(path);
        let res = self
            .http
            .post(&url)
            .header("Content-Length", "0")
            .send()
            .await
            .with_context(|| format!("POST {url}"))?;

        let status = res.status();
        if !status.is_success() {
            let body = res.text().await.unwrap_or_default();
            anyhow::bail!("POST {url} → {status}: {body}");
        }
        Ok(())
    }

    async fn patch_json<Req, Res>(&self, path: &str, body: &Req) -> Result<Res>
    where
        Req: Serialize,
        Res: DeserializeOwned,
    {
        let url = self.url(path);
        let res = self
            .http
            .patch(&url)
            .json(body)
            .send()
            .await
            .with_context(|| format!("PATCH {url}"))?;

        let status = res.status();
        if !status.is_success() {
            let body = res.text().await.unwrap_or_default();
            anyhow::bail!("PATCH {url} → {status}: {body}");
        }

        res.json::<Res>()
            .await
            .with_context(|| format!("deserialise PATCH {url}"))
    }

    // ── Customers ─────────────────────────────────────────────────────────────

    pub async fn create_customer(&self, req: &CreateCustomerRequest) -> Result<CustomerResponse> {
        self.post_json("/api/v1/customers", req).await
    }

    pub async fn add_reward_points(
        &self,
        customer_id: &str,
        points: u32,
    ) -> Result<RewardPointsResponse> {
        self.patch_json(
            &format!("/api/v1/customers/{customer_id}/rewards"),
            &RewardPointsRequest { points },
        )
        .await
    }

    pub async fn add_frequent_sales_point(&self, customer_id: &str, sp_id: &str) -> Result<()> {
        self.post_empty_void(&format!(
            "/api/v1/customers/{customer_id}/sales-points/{sp_id}"
        ))
        .await
    }

    // ── Inventory ─────────────────────────────────────────────────────────────

    pub async fn create_product(&self, req: &CreateProductRequest) -> Result<ProductResponse> {
        self.post_json("/api/v1/inventory/products", req).await
    }

    pub async fn increment_stock(&self, req: &StockIncrementRequest) -> Result<()> {
        self.post_json_void("/api/v1/inventory/stock/increment", req)
            .await
    }

    // ── Sales Points ──────────────────────────────────────────────────────────

    pub async fn create_sales_point(
        &self,
        req: &CreateSalesPointRequest,
    ) -> Result<SalesPointResponse> {
        self.post_json("/api/v1/sales-points", req).await
    }

    // ── Sales ─────────────────────────────────────────────────────────────────

    pub async fn create_sale(&self, req: &CreateSaleRequest) -> Result<SaleResponse> {
        self.post_json("/api/v1/sales", req).await
    }

    pub async fn refund_sale(&self, sale_id: &str) -> Result<SaleResponse> {
        self.post_empty(&format!("/api/v1/sales/{sale_id}/refund"))
            .await
    }
}
