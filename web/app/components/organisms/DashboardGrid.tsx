import { useState, useEffect } from "react";
import { StatCard } from "../molecules/StatCard";
import { JavadonaClient } from "~/lib/JavadonaClient";

const client = new JavadonaClient("http://localhost:8080");

interface Stats {
  customers?: number;
  products?: number;
  salesPoints?: number;
  sales?: number;
}

interface LoadingState {
  customers: boolean;
  products: boolean;
  salesPoints: boolean;
  sales: boolean;
}

interface ErrorState {
  customers: boolean;
  products: boolean;
  salesPoints: boolean;
  sales: boolean;
}

export function DashboardGrid() {
  const [stats, setStats] = useState<Stats>({});
  const [loading, setLoading] = useState<LoadingState>({
    customers: true,
    products: true,
    salesPoints: true,
    sales: true,
  });
  const [errors, setErrors] = useState<ErrorState>({
    customers: false,
    products: false,
    salesPoints: false,
    sales: false,
  });

  useEffect(() => {
    client.customer
      .getAllCustomers({ page: 0, size: 1 })
      .then((r) => setStats((s) => ({ ...s, customers: r.totalElements })))
      .catch(() => setErrors((e) => ({ ...e, customers: true })))
      .finally(() => setLoading((l) => ({ ...l, customers: false })));

    client.inventory
      .listProducts({ page: 0, size: 1 })
      .then((r) => setStats((s) => ({ ...s, products: r.totalElements })))
      .catch(() => setErrors((e) => ({ ...e, products: true })))
      .finally(() => setLoading((l) => ({ ...l, products: false })));

    client.salesPoint
      .listAll({ page: 0, size: 1 })
      .then((r) => setStats((s) => ({ ...s, salesPoints: r.totalElements })))
      .catch(() => setErrors((e) => ({ ...e, salesPoints: true })))
      .finally(() => setLoading((l) => ({ ...l, salesPoints: false })));

    client.sales
      .getAllSales({ page: 0, size: 1 })
      .then((r) => setStats((s) => ({ ...s, sales: r.totalElements })))
      .catch(() => setErrors((e) => ({ ...e, sales: true })))
      .finally(() => setLoading((l) => ({ ...l, sales: false })));
  }, []);

  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
      <StatCard
        label="Customers"
        value={stats.customers}
        loading={loading.customers}
        error={errors.customers}
        featured
      />
      <StatCard
        label="Products"
        value={stats.products}
        loading={loading.products}
        error={errors.products}
      />
      <StatCard
        label="Sales Points"
        value={stats.salesPoints}
        loading={loading.salesPoints}
        error={errors.salesPoints}
      />
      <StatCard
        label="Sales"
        value={stats.sales}
        loading={loading.sales}
        error={errors.sales}
      />
    </div>
  );
}
