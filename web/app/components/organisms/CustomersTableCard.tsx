import { useState, useEffect } from "react";
import { DataTableCard, type ColumnDef } from "../molecules/DataTableCard";
import { Avatar } from "../atoms/Avatar";
import { StatusDot } from "../atoms/StatusDot";
import { JavadonaClient } from "~/lib/JavadonaClient";
import type { CustomerResponse } from "~/lib/JavadonaClient/modules/CustomerHttpClient";

const client = new JavadonaClient("http://localhost:8080");

const columns: ColumnDef<CustomerResponse>[] = [
  {
    key: "name",
    header: "Name",
    render: (row) => (
      <div className="flex items-center gap-3">
        <Avatar initials={`${row.firstName?.[0] ?? ""}${row.lastName?.[0] ?? ""}`} />
        <div>
          <div className="font-medium text-white">
            {row.firstName} {row.lastName}
          </div>
          <div className="text-xs text-zinc-500">
            {row.documentType}: {row.documentValue}
          </div>
        </div>
      </div>
    ),
  },
  {
    key: "email",
    header: "Email",
    render: (row) => <span className="text-zinc-400">{row.email ?? "—"}</span>,
  },
  {
    key: "status",
    header: "Status",
    render: (row) => (
      <StatusDot
        status={row.status === "ACTIVE" ? "success" : "neutral"}
        label={row.status ?? "—"}
      />
    ),
  },
  {
    key: "rewards",
    header: "Reward Points",
    align: "right",
    render: (row) => (
      <span className="text-zinc-300">
        {row.rewardPoints?.toLocaleString() ?? "—"}
      </span>
    ),
  },
];

export function CustomersTableCard() {
  const [rows, setRows] = useState<CustomerResponse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    client.customer
      .getAllCustomers({ page: 0, size: 5 })
      .then((r) => setRows(r.content ?? []))
      .finally(() => setLoading(false));
  }, []);

  return (
    <DataTableCard
      title="Customers"
      subtitle="Recent customer records"
      columns={columns}
      rows={rows}
      loading={loading}
      getRowKey={(r) => r.id ?? Math.random().toString()}
    />
  );
}
