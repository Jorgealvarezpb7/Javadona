import { useState, useEffect } from "react";
import { DataTableCard, type ColumnDef } from "../molecules/DataTableCard";
import { Avatar } from "../atoms/Avatar";
import { StatusDot } from "../atoms/StatusDot";
import { JavadonaClient } from "~/lib/JavadonaClient";
import type { ProductResponse } from "~/lib/JavadonaClient/modules/InventoryHttpClient";

const client = new JavadonaClient("http://localhost:8080");

const columns: ColumnDef<ProductResponse>[] = [
  {
    key: "name",
    header: "Name",
    render: (row) => (
      <div className="flex items-center gap-3">
        <Avatar initials={row.name?.slice(0, 2) ?? "??"} />
        <div>
          <div className="font-medium text-white">{row.name}</div>
          <div className="text-xs text-zinc-500">{row.category}</div>
        </div>
      </div>
    ),
  },
  {
    key: "barcode",
    header: "Barcode",
    render: (row) => (
      <span className="font-mono text-xs text-zinc-400">
        {row.barcodeValue ?? "—"}
      </span>
    ),
  },
  {
    key: "status",
    header: "Status",
    render: (row) => (
      <StatusDot
        status={row.active ? "success" : "neutral"}
        label={row.active ? "Active" : "Inactive"}
      />
    ),
  },
  {
    key: "price",
    header: "Price",
    align: "right",
    render: (row) => (
      <span className="text-zinc-300">
        {row.basePrice != null ? `€${row.basePrice.toFixed(2)}` : "—"}
      </span>
    ),
  },
];

export function ProductsTableCard() {
  const [rows, setRows] = useState<ProductResponse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    client.inventory
      .listProducts({ page: 0, size: 5 })
      .then((r) => setRows(r.content ?? []))
      .finally(() => setLoading(false));
  }, []);

  return (
    <DataTableCard
      title="Products"
      subtitle="Recent product catalogue"
      columns={columns}
      rows={rows}
      loading={loading}
      getRowKey={(r) => r.id ?? Math.random().toString()}
    />
  );
}
