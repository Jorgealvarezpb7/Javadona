import { CustomerHttpClient } from "./modules/CustomerHttpClient";
import { InventoryHttpClient } from "./modules/InventoryHttpClient";
import { SalesHttpClient } from "./modules/SalesHttpClient";
import { SalesPointHttpClient } from "./modules/SalesPointHttpClient";

export class JavadonaClient {
  private baseUrl: URL;
  private customerHttpClient: CustomerHttpClient;
  private inventoryHttpClient: InventoryHttpClient;
  private salesHttpClient: SalesHttpClient;
  private salesPointHttpClient: SalesPointHttpClient;

  constructor(baseUrl: string) {
    this.baseUrl = new URL(baseUrl);
    this.customerHttpClient = new CustomerHttpClient({
      baseUrl: this.baseUrl,
      basePath: "/api/v1/customers",
    });
    this.inventoryHttpClient = new InventoryHttpClient({
      baseUrl: this.baseUrl,
      basePath: "/api/v1/inventory",
    });
    this.salesHttpClient = new SalesHttpClient({
      baseUrl: this.baseUrl,
      basePath: "/api/v1/sales",
    });
    this.salesPointHttpClient = new SalesPointHttpClient({
      baseUrl: this.baseUrl,
      basePath: "/api/v1/sales-points",
    });
  }

  get customer() {
    return this.customerHttpClient;
  }

  get inventory() {
    return this.inventoryHttpClient;
  }

  get sales() {
    return this.salesHttpClient;
  }

  get salesPoint() {
    return this.salesPointHttpClient;
  }
}
