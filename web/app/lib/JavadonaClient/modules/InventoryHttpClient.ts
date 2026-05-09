import { HttpClient, type HttpClientOptions } from "../HttpClient";

export type ProductCategory =
  | "DAIRY"
  | "BAKERY"
  | "PRODUCE"
  | "MEAT"
  | "BEVERAGES"
  | "FROZEN"
  | "PERSONAL_CARE"
  | "CLEANING"
  | "OTHER";

export interface CreateProductRequest {
  name: string;
  category: ProductCategory;
  barcodeValue: string;
  basePrice: number;
  description?: string;
}

export interface UpdateProductRequest {
  name?: string;
  description?: string;
  category?: ProductCategory;
  barcodeValue?: string;
  basePrice?: number;
}

export interface ProductResponse {
  id?: string;
  name?: string;
  description?: string;
  category?: string;
  barcodeValue?: string;
  basePrice?: number;
  active?: boolean;
}

export interface StockAdjustmentRequest {
  newStock?: number;
  minimumStock?: number;
}

export interface StockResponse {
  id?: string;
  salesPointId?: string;
  productId?: string;
  currentStock?: number;
  minimumStock?: number;
  lastUpdated?: string;
}

export interface StockLineItem {
  productId?: string;
  quantity?: number;
}

export interface StockIncrementRequest {
  salesPointId?: string;
  lines?: StockLineItem[];
}

export interface StockDecrementRequest {
  salesPointId?: string;
  lines?: StockLineItem[];
}

export interface Pageable {
  page?: number;
  size?: number;
  sort?: string[];
}

export interface SortObject {
  direction?: string;
  nullHandling?: string;
  ascending?: boolean;
  property?: string;
  ignoreCase?: boolean;
}

export interface PageableObject {
  offset?: number;
  sort?: SortObject[];
  unpaged?: boolean;
  paged?: boolean;
  pageSize?: number;
  pageNumber?: number;
}

export interface PageProductResponse {
  totalPages?: number;
  totalElements?: number;
  size?: number;
  content?: ProductResponse[];
  number?: number;
  sort?: SortObject[];
  pageable?: PageableObject;
  numberOfElements?: number;
  first?: boolean;
  last?: boolean;
  empty?: boolean;
}

export class InventoryHttpClient extends HttpClient {
  constructor(options: HttpClientOptions) {
    super(options);
  }

  listProducts(pageable?: Pageable): Promise<PageProductResponse> {
    const params: Record<string, string> = {};
    if (pageable?.page !== undefined) params.page = String(pageable.page);
    if (pageable?.size !== undefined) params.size = String(pageable.size);
    if (pageable?.sort?.length) params.sort = pageable.sort.join(",");
    return this.get("/products", params);
  }

  createProduct(body: CreateProductRequest): Promise<ProductResponse> {
    return this.post("/products", body);
  }

  getProduct(id: string): Promise<ProductResponse> {
    return this.get(`/products/${id}`);
  }

  updateProduct(
    id: string,
    body: UpdateProductRequest,
  ): Promise<ProductResponse> {
    return this.put(`/products/${id}`, body);
  }

  deactivateProduct(id: string): Promise<void> {
    return this.patch(`/products/${id}/deactivate`, undefined);
  }

  getProductByBarcode(ean: string): Promise<ProductResponse> {
    return this.get(`/products/barcode/${ean}`);
  }

  getStockForSalesPoint(salesPointId: string): Promise<StockResponse[]> {
    return this.get(`/stock/${salesPointId}`);
  }

  getLowStockItems(salesPointId: string): Promise<StockResponse[]> {
    return this.get(`/stock/${salesPointId}/low-stock`);
  }

  getStockForProduct(
    salesPointId: string,
    productId: string,
  ): Promise<StockResponse> {
    return this.get(`/stock/${salesPointId}/${productId}`);
  }

  adjustStock(
    salesPointId: string,
    productId: string,
    body: StockAdjustmentRequest,
  ): Promise<StockResponse> {
    return this.put(`/stock/${salesPointId}/${productId}`, body);
  }

  incrementStock(body: StockIncrementRequest): Promise<void> {
    return this.post("/stock/increment", body);
  }

  decrementStock(body: StockDecrementRequest): Promise<void> {
    return this.post("/stock/decrement", body);
  }
}
