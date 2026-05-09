import { CustomerHttpClient } from "./modules/CustomerHttpClient";

export class JavadonaClient {
  private baseUrl: URL;
  private customerHttpClient: CustomerHttpClient;

  constructor(baseUrl: string) {
    this.baseUrl = new URL(baseUrl);
    this.customerHttpClient = new CustomerHttpClient({
      baseUrl: this.baseUrl,
      basePath: "/api/v1/customers",
    });
  }

  get customer() {
    return this.customerHttpClient;
  }
}
