use serde::{Deserialize, Serialize};

// ── Static config (faker-static.toml) ────────────────────────────────────────

#[derive(Debug, Deserialize)]
pub struct StaticConfig {
    pub sales_points: Vec<StaticSalesPoint>,
}

#[derive(Debug, Clone, Deserialize)]
pub struct StaticSalesPoint {
    pub name: String,
    pub street: String,
    pub city: String,
    pub postal_code: String,
    pub province: String,
    pub phone_number: String,
    pub opens_at: String,
    pub closes_at: String,
}

// ── Paginated response wrapper ────────────────────────────────────────────────

#[derive(Debug, Deserialize)]
pub struct SpringPage<T> {
    pub content: Vec<T>,
}

#[derive(Debug, Clone, Copy, Serialize, Deserialize)]
#[serde(rename_all = "SCREAMING_SNAKE_CASE")]
pub enum DocumentType {
    Dni,
    Nie,
}

#[derive(Debug, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct CreateCustomerRequest {
    pub first_name: String,
    pub last_name: String,
    pub date_of_birth: String,
    pub phone_number: String,
    pub document_type: DocumentType,
    pub document_value: String,
    pub email: String,
    pub street: String,
    pub city: String,
    pub postal_code: String,
    pub province: String,
}

#[derive(Debug, Clone, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct CustomerResponse {
    pub id: Option<String>,
    pub first_name: Option<String>,
    pub last_name: Option<String>,
    pub email: Option<String>,
    pub reward_points: Option<i32>,
    pub status: Option<String>,
}

#[derive(Debug, Serialize)]
pub struct RewardPointsRequest {
    pub points: u32,
}

#[derive(Debug, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct RewardPointsResponse {
    pub customer_id: Option<String>,
    pub reward_points: Option<i32>,
}

#[derive(Debug, Clone, Copy, Serialize, Deserialize)]
#[serde(rename_all = "SCREAMING_SNAKE_CASE")]
pub enum ProductCategory {
    Dairy,
    Bakery,
    Produce,
    Meat,
    Beverages,
    Frozen,
    PersonalCare,
    Cleaning,
    Other,
}

#[derive(Debug, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct CreateProductRequest {
    pub name: String,
    #[serde(skip_serializing_if = "Option::is_none")]
    pub description: Option<String>,
    pub category: ProductCategory,
    pub barcode_value: String,
    pub base_price: f64,
}

#[derive(Debug, Clone, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct ProductResponse {
    pub id: Option<String>,
    pub name: Option<String>,
    pub category: Option<String>,
    pub barcode_value: Option<String>,
    pub base_price: Option<f64>,
    pub active: Option<bool>,
}

#[derive(Debug, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct StockLineItem {
    pub product_id: String,
    pub quantity: i32,
}

#[derive(Debug, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct StockIncrementRequest {
    pub sales_point_id: String,
    pub lines: Vec<StockLineItem>,
}

#[derive(Debug, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct CreateSalesPointRequest {
    pub name: String,
    pub street: String,
    pub city: String,
    pub postal_code: String,
    pub province: String,
    pub phone_number: String,
    pub opens_at: String,
    pub closes_at: String,
}

#[derive(Debug, Clone, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct SalesPointResponse {
    pub id: Option<String>,
    pub name: Option<String>,
    pub city: Option<String>,
    pub status: Option<String>,
}

#[derive(Debug, Clone, Copy, Serialize, Deserialize)]
#[serde(rename_all = "SCREAMING_SNAKE_CASE")]
pub enum PaymentMethod {
    Cash,
    Card,
    Bizum,
}

#[derive(Debug, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct SaleLineRequest {
    pub product_id: String,
    pub product_name: String,
    pub quantity: i32,
    pub unit_price: f64,
}

#[derive(Debug, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct CreateSaleRequest {
    pub customer_id: String,
    pub sales_point_id: String,
    pub payment_method: PaymentMethod,
    pub lines: Vec<SaleLineRequest>,
}

#[derive(Debug, Clone, Deserialize)]
#[serde(rename_all = "camelCase")]
pub struct SaleResponse {
    pub id: Option<String>,
    pub customer_id: Option<String>,
    pub sales_point_id: Option<String>,
    pub sale_date: Option<String>,
    pub total_amount: Option<f64>,
    pub payment_method: Option<String>,
    pub status: Option<String>,
}
