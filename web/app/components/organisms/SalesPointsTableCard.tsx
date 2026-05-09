import { useState, useEffect } from "react";
import { DataTableCard, type ColumnDef } from "../molecules/DataTableCard";
import { Avatar } from "../atoms/Avatar";
import { StatusDot } from "../atoms/StatusDot";
import type { StatusVariant } from "../atoms/StatusDot";
import { JavadonaClient } from "~/lib/JavadonaClient";
import type { SalesPointResponse } from "~/lib/JavadonaClient/modules/SalesPointHttpClient";

const client = new JavadonaClient("http://localhost:8080");

function salesPointStatus(status?: string): StatusVariant {
  if (status === "OPEN") return "success";
  if (status === "RENOVATING") return "warning";
  return "neutral";
}

const columns: ColumnDef<SalesPointResponse>[] = [
  {
    key: "name",
    header: "Name",
    render: (row) => (
      <div className="flex items-center gap-3">
        <Avatar initials={row.name?.slice(0, 2) ?? "??"} />
        <div>
          <div className="font-medium text-white">{row.name}</div>
          <div className="text-xs text-zinc-500">{row.province}</div>
        </div>
      </div>
    ),
  },
  {
    key: "city",
    header: "City",
    render: (row) => (
      <div>
        <div className="text-zinc-300">{row.city}</div>
        <div className="text-xs text-zinc-500">{row.postalCode}</div>
      </div>
    ),
  },
  {
    key: "hours",
    header: "Hours",
    render: (row) => (
      <span className="text-zinc-400">
        {row.opensAt ?? "—"} – {row.closesAt ?? "—"}
      </span>
    ),
  },
  {
    key: "status",
    header: "Status",
    render: (row) => (
      <StatusDot
        status={salesPointStatus(row.status)}
        label={row.status ?? "—"}
      />
    ),
  },
];

export function SalesPointsTableCard() {
  const [rows, setRows] = useState<SalesPointResponse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    client.salesPoint
      .listAll({ page: 0, size: 5 })
      .then((r) => setRows(r.content ?? []))
      .finally(() => setLoading(false));
  }, []);

  return (
    <DataTableCard
      title="Sales Points"
      subtitle="Recent store locations"
      columns={columns}
      rows={rows}
      loading={loading}
      getRowKey={(r) => r.id ?? Math.random().toString()}
    />
  );
}
