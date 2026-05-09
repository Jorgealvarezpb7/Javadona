export class HttpError extends Error {
  constructor(
    public readonly status: number,
    public readonly statusText: string,
    public readonly body: unknown,
  ) {
    super(`HTTP ${status} ${statusText}`);
    this.name = "HttpError";
  }
}

export interface HttpClientOptions {
  baseUrl: URL;
  basePath: string;
}

export class HttpClient {
  protected readonly baseUrl: URL;
  protected readonly basePath: string;

  constructor(options: HttpClientOptions) {
    this.baseUrl = options.baseUrl;
    this.basePath = options.basePath;
  }

  protected buildUrl(path: string, params?: Record<string, string>): string {
    const url = new URL(`${this.basePath}${path}`, this.baseUrl);

    if (params) {
      Object.entries(params).forEach(([k, v]) => url.searchParams.set(k, v));
    }

    return url.toString();
  }

  protected defaultHeaders(): HeadersInit {
    return { Accept: "application/json" };
  }

  protected async parseResponse<T>(res: Response): Promise<T> {
    const contentType = res.headers.get("content-type") ?? "";
    const body = contentType.includes("application/json")
      ? await res.json()
      : await res.text();

    if (!res.ok) {
      throw new HttpError(res.status, res.statusText, body);
    }

    return body as T;
  }

  get<T>(path: string, params?: Record<string, string>): Promise<T> {
    return fetch(this.buildUrl(path, params), {
      method: "GET",
      headers: this.defaultHeaders(),
    }).then((res) => this.parseResponse<T>(res));
  }

  post<T>(path: string, body: unknown): Promise<T> {
    return fetch(this.buildUrl(path), {
      method: "POST",
      headers: { ...this.defaultHeaders(), "Content-Type": "application/json" },
      body: JSON.stringify(body),
    }).then((res) => this.parseResponse<T>(res));
  }

  put<T>(path: string, body: unknown): Promise<T> {
    return fetch(this.buildUrl(path), {
      method: "PUT",
      headers: { ...this.defaultHeaders(), "Content-Type": "application/json" },
      body: JSON.stringify(body),
    }).then((res) => this.parseResponse<T>(res));
  }

  patch<T>(path: string, body: unknown): Promise<T> {
    return fetch(this.buildUrl(path), {
      method: "PATCH",
      headers: { ...this.defaultHeaders(), "Content-Type": "application/json" },
      body: JSON.stringify(body),
    }).then((res) => this.parseResponse<T>(res));
  }

  delete<T>(path: string): Promise<T> {
    return fetch(this.buildUrl(path), {
      method: "DELETE",
      headers: this.defaultHeaders(),
    }).then((res) => this.parseResponse<T>(res));
  }
}
