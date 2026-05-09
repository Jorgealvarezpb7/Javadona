import { HttpClient, type HttpClientOptions } from "../HttpClient";

export type SalesPointStatus = "OPEN" | "CLOSED" | "RENOVATING";

export interface LocalTime {
  hour?: number;
  minute?: number;
  second?: number;
  nano?: number;
}

export interface CreateSalesPointRequest {
  name: string;
  street: string;
  city: string;
  postalCode: string;
  province: string;
  phoneNumber: string;
  opensAt: LocalTime;
  closesAt: LocalTime;
}

export interface UpdateSalesPointRequest {
  name?: string;
  street?: string;
  city?: string;
  postalCode?: string;
  province?: string;
  phoneNumber?: string;
  opensAt?: LocalTime;
  closesAt?: LocalTime;
}

export interface SalesPointStatusRequest {
  status: SalesPointStatus;
}

export interface SalesPointResponse {
  id?: string;
  name?: string;
  street?: string;
  city?: string;
  postalCode?: string;
  province?: string;
  phoneNumber?: string;
  opensAt?: string;
  closesAt?: string;
  status?: string;
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

export interface PageSalesPointResponse {
  totalPages?: number;
  totalElements?: number;
  size?: number;
  content?: SalesPointResponse[];
  number?: number;
  sort?: SortObject[];
  pageable?: PageableObject;
  first?: boolean;
  last?: boolean;
  numberOfElements?: number;
  empty?: boolean;
}

export class SalesPointHttpClient extends HttpClient {
  constructor(options: HttpClientOptions) {
    super(options);
  }

  listAll(pageable?: Pageable): Promise<PageSalesPointResponse> {
    const params: Record<string, string> = {};
    if (pageable?.page !== undefined) params.page = String(pageable.page);
    if (pageable?.size !== undefined) params.size = String(pageable.size);
    if (pageable?.sort?.length) params.sort = pageable.sort.join(",");
    return this.get("", params);
  }

  create(body: CreateSalesPointRequest): Promise<SalesPointResponse> {
    return this.post("", body);
  }

  getById(id: string): Promise<SalesPointResponse> {
    return this.get(id);
  }

  update(
    id: string,
    body: UpdateSalesPointRequest,
  ): Promise<SalesPointResponse> {
    return this.put(id, body);
  }

  deleteSalesPoint(id: string): Promise<void> {
    return this.delete(id);
  }

  changeStatus(
    id: string,
    body: SalesPointStatusRequest,
  ): Promise<SalesPointResponse> {
    return this.patch(`/${id}/status`, body);
  }

  getByCity(city: string): Promise<SalesPointResponse[]> {
    return this.get(`/city/${city}`);
  }
}
