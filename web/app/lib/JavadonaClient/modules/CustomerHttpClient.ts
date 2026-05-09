import { HttpClient, type HttpClientOptions } from "../HttpClient";

export type DocumentType = "DNI" | "NIE";

export interface CreateCustomerRequest {
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  phoneNumber: string;
  documentType: DocumentType;
  documentValue: string;
  email: string;
  street: string;
  city: string;
  postalCode: string;
  province: string;
}

export interface UpdateCustomerRequest {
  firstName?: string;
  lastName?: string;
  dateOfBirth?: string;
  phoneNumber?: string;
  street?: string;
  city?: string;
  postalCode?: string;
  province?: string;
}

export interface CustomerResponse {
  id?: string;
  documentType?: string;
  documentValue?: string;
  firstName?: string;
  lastName?: string;
  dateOfBirth?: string;
  phoneNumber?: string;
  email?: string;
  street?: string;
  city?: string;
  postalCode?: string;
  province?: string;
  rewardPoints?: number;
  frequentSalesPoints?: string[];
  status?: string;
}

export interface RewardPointsRequest {
  points?: number;
}

export interface RewardPointsResponse {
  customerId?: string;
  rewardPoints?: number;
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

export interface PageCustomerResponse {
  totalPages?: number;
  totalElements?: number;
  size?: number;
  content?: CustomerResponse[];
  number?: number;
  sort?: SortObject[];
  pageable?: PageableObject;
  first?: boolean;
  last?: boolean;
  numberOfElements?: number;
  empty?: boolean;
}

export interface FrequentSalesPointsResponse {
  customerId?: string;
  salesPointIds?: string[];
}

export class CustomerHttpClient extends HttpClient {
  constructor(options: HttpClientOptions) {
    super(options);
  }

  getAllCustomers(pageable?: Pageable): Promise<PageCustomerResponse> {
    const params: Record<string, string> = {};
    if (pageable?.page !== undefined) params.page = String(pageable.page);
    if (pageable?.size !== undefined) params.size = String(pageable.size);
    if (pageable?.sort?.length) params.sort = pageable.sort.join(",");
    return this.get("", params);
  }

  createCustomer(body: CreateCustomerRequest): Promise<CustomerResponse> {
    return this.post("", body);
  }

  getCustomerById(id: string): Promise<CustomerResponse> {
    return this.get(id);
  }

  updateCustomer(id: string, body: UpdateCustomerRequest): Promise<CustomerResponse> {
    return this.put(id, body);
  }

  deleteCustomer(id: string): Promise<void> {
    return this.delete(id);
  }

  getCustomerByDocument(type: DocumentType, value: string): Promise<CustomerResponse> {
    return this.get(`/document/${type}/${value}`);
  }

  getRewardPoints(id: string): Promise<RewardPointsResponse> {
    return this.get(`/${id}/rewards`);
  }

  addRewardPoints(id: string, body: RewardPointsRequest): Promise<RewardPointsResponse> {
    return this.patch(`/${id}/rewards`, body);
  }

  getFrequentSalesPoints(id: string): Promise<FrequentSalesPointsResponse> {
    return this.get(`/${id}/sales-points`);
  }

  addFrequentSalesPoint(id: string, spId: string): Promise<void> {
    return this.post(`/${id}/sales-points/${spId}`, undefined);
  }
}
