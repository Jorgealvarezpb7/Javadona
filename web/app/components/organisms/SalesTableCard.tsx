import { useState, useEffect } from "react";
import { DataTableCard, type ColumnDef } from "../molecules/DataTableCard";
import { Avatar } from "../atoms/Avatar";
import { StatusDot } from "../atoms/StatusDot";
import type { StatusVariant } from "../atoms/StatusDot";
import { JavadonaClient } from "~/lib/JavadonaClient";
import type { SaleResponse } from "~/lib/JavadonaClient/modules/SalesHttpClient";

const client = new JavadonaClient("http://localhost:8080");

function saleStatus(status?: string): StatusVariant {
  if (status === "COMPLETED") return "success";
  if (status === "REFUNDED") return "error";
  return "warning";
}

const columns: ColumnDef<SaleResponse>[] = [
  {
    key: "sale",
    header: "Sale",
    render: (row) => (
      <div className="flex items-center gap-3">
        <Avatar initials={row.paymentMethod?.slice(0, 2) ?? "SA"} />
        <div>
          <div className="font-mono text-xs text-white">
            {row.id?.slice(0, 8)}…
          </div>
          <div className="text-xs text-zinc-500">
            {row.lines?.length ?? 0} item(s)
          </div>
        </div>
      </div>
    ),
  },
  {
    key: "date",
    header: "Date",
    render: (row) => {
      const d = row.saleDate ? new Date(row.saleDate) : null;
      return d ? (
        <div>
          <div className="text-zinc-300">
            {d.toLocaleDateString("en-GB", {
              day: "2-digit",
              month: "short",
              year: "numeric",
            })}
          </div>
          <div className="text-xs text-zinc-500">
            {d.toLocaleTimeString("en-GB", {
              hour: "2-digit",
              minute: "2-digit",
            })}
          </div>
        </div>
      ) : (
        "—"
      );
    },
  },
  {
    key: "payment",
    header: "Payment",
    render: (row) => (
      <span className="text-zinc-300">{row.paymentMethod ?? "—"}</span>
    ),
  },
  {
    key: "status",
    header: "Status",
    render: (row) => (
      <StatusDot
        status={saleStatus(row.status)}
        label={row.status ?? "—"}
      />
    ),
  },
  {
    key: "total",
    header: "Total",
    align: "right",
    render: (row) => (
      <span className="text-zinc-300">
        {row.totalAmount != null ? `€${row.totalAmount.toFixed(2)}` : "—"}
      </span>
    ),
  },
];

export function SalesTableCard() {
  const [rows, setRows] = useState<SaleResponse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    client.sales
      .getAllSales({ page: 0, size: 5 })
      .then((r) => setRows(r.content ?? []))
      .finally(() => setLoading(false));
  }, []);

  return (
    <DataTableCard
      title="Sales"
      subtitle="Recent transactions"
      columns={columns}
      rows={rows}
      loading={loading}
      getRowKey={(r) => r.id ?? Math.random().toString()}
    />
  );
}
