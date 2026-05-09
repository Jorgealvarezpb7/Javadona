import { HttpClient, type HttpClientOptions } from "../HttpClient";

export type PaymentMethod = "CASH" | "CARD" | "BIZUM";
export type SaleStatus = "PENDING" | "COMPLETED" | "REFUNDED";

export interface SaleLineRequest {
  productId: string;
  productName: string;
  unitPrice: number;
  quantity?: number;
}

export interface CreateSaleRequest {
  customerId: string;
  salesPointId: string;
  paymentMethod: PaymentMethod;
  lines: SaleLineRequest[];
}

export interface SaleLineResponse {
  id?: string;
  productId?: string;
  productName?: string;
  quantity?: number;
  unitPrice?: number;
  subTotal?: number;
}

export interface SaleResponse {
  id?: string;
  customerId?: string;
  salesPointId?: string;
  saleDate?: string;
  lines?: SaleLineResponse[];
  totalAmount?: number;
  paymentMethod?: PaymentMethod;
  status?: SaleStatus;
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
  paged?: boolean;
  unpaged?: boolean;
  pageSize?: number;
  pageNumber?: number;
}

export interface PageSaleResponse {
  totalPages?: number;
  totalElements?: number;
  size?: number;
  content?: SaleResponse[];
  number?: number;
  sort?: SortObject[];
  pageable?: PageableObject;
  first?: boolean;
  last?: boolean;
  numberOfElements?: number;
  empty?: boolean;
}

export class SalesHttpClient extends HttpClient {
  constructor(options: HttpClientOptions) {
    super(options);
  }

  private pageableParams(pageable?: Pageable): Record<string, string> {
    const params: Record<string, string> = {};
    if (pageable?.page !== undefined) params.page = String(pageable.page);
    if (pageable?.size !== undefined) params.size = String(pageable.size);
    if (pageable?.sort?.length) params.sort = pageable.sort.join(",");
    return params;
  }

  getAllSales(pageable?: Pageable): Promise<PageSaleResponse> {
    return this.get("", this.pageableParams(pageable));
  }

  createSale(body: CreateSaleRequest): Promise<SaleResponse> {
    return this.post("", body);
  }

  getSaleById(id: string): Promise<SaleResponse> {
    return this.get(id);
  }

  refundSale(id: string): Promise<SaleResponse> {
    return this.post(`/${id}/refund`, undefined);
  }

  getSalesBySalesPoint(
    salesPointId: string,
    pageable?: Pageable,
  ): Promise<PageSaleResponse> {
    return this.get(
      `/sales-point/${salesPointId}`,
      this.pageableParams(pageable),
    );
  }

  getSalesByCustomer(
    customerId: string,
    pageable?: Pageable,
  ): Promise<PageSaleResponse> {
    return this.get(`/customer/${customerId}`, this.pageableParams(pageable));
  }
}
