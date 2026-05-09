const BASE_URL =
  (typeof process !== "undefined" && process.env.API_GATEWAY_URL) ||
  "http://localhost:8080";

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

async function parseResponse<T>(res: Response): Promise<T> {
  const contentType = res.headers.get("content-type") ?? "";
  const body = contentType.includes("application/json")
    ? await res.json()
    : await res.text();

  if (!res.ok) {
    throw new HttpError(res.status, res.statusText, body);
  }

  return body as T;
}

function buildUrl(path: string, params?: Record<string, string>): string {
  const url = new URL(`/api/v1${path}`, BASE_URL);
  if (params) {
    Object.entries(params).forEach(([k, v]) => url.searchParams.set(k, v));
  }
  return url.toString();
}

export const httpClient = {
  get<T>(path: string, params?: Record<string, string>): Promise<T> {
    return fetch(buildUrl(path, params), {
      method: "GET",
      headers: { Accept: "application/json" },
    }).then(parseResponse<T>);
  },

  post<T>(path: string, body: unknown): Promise<T> {
    return fetch(buildUrl(path), {
      method: "POST",
      headers: { "Content-Type": "application/json", Accept: "application/json" },
      body: JSON.stringify(body),
    }).then(parseResponse<T>);
  },

  put<T>(path: string, body: unknown): Promise<T> {
    return fetch(buildUrl(path), {
      method: "PUT",
      headers: { "Content-Type": "application/json", Accept: "application/json" },
      body: JSON.stringify(body),
    }).then(parseResponse<T>);
  },

  patch<T>(path: string, body: unknown): Promise<T> {
    return fetch(buildUrl(path), {
      method: "PATCH",
      headers: { "Content-Type": "application/json", Accept: "application/json" },
      body: JSON.stringify(body),
    }).then(parseResponse<T>);
  },

  delete<T>(path: string): Promise<T> {
    return fetch(buildUrl(path), {
      method: "DELETE",
      headers: { Accept: "application/json" },
    }).then(parseResponse<T>);
  },
} as const;
