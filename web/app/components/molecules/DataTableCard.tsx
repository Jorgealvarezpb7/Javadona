export interface ColumnDef<TRow> {
  key: string;
  header: string;
  render: (row: TRow) => React.ReactNode;
  align?: "left" | "right";
}

interface DataTableCardProps<TRow> {
  title: string;
  subtitle: string;
  columns: ColumnDef<TRow>[];
  rows: TRow[];
  loading?: boolean;
  getRowKey: (row: TRow) => string;
}

function ExportIcon() {
  return (
    <svg
      width="16"
      height="16"
      viewBox="0 0 16 16"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
      aria-hidden="true"
    >
      <path
        d="M3 13L13 3M13 3H6M13 3V10"
        stroke="currentColor"
        strokeWidth="1.5"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
}

function SkeletonRow({ cols }: { cols: number }) {
  return (
    <tr className="border-t border-zinc-800/50">
      {Array.from({ length: cols }).map((_, i) => (
        <td key={i} className="px-6 py-4">
          <div className="h-4 animate-pulse rounded bg-zinc-800" />
        </td>
      ))}
    </tr>
  );
}

export function DataTableCard<TRow>({
  title,
  subtitle,
  columns,
  rows,
  loading = false,
  getRowKey,
}: DataTableCardProps<TRow>) {
  return (
    <div className="overflow-hidden rounded-2xl border border-zinc-800 bg-zinc-900">
      <div className="flex items-start justify-between px-6 py-5">
        <div>
          <h2 className="text-sm font-semibold text-white">{title}</h2>
          <p className="mt-0.5 text-xs text-zinc-500">{subtitle}</p>
        </div>
        <button
          className="flex h-7 w-7 items-center justify-center rounded-lg text-zinc-500 transition-colors hover:bg-zinc-800 hover:text-white"
          aria-label="Export"
        >
          <ExportIcon />
        </button>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full">
          <thead>
            <tr className="border-t border-zinc-800">
              {columns.map((col) => (
                <th
                  key={col.key}
                  className={`px-6 py-3 text-xs font-medium text-zinc-500 ${
                    col.align === "right" ? "text-right" : "text-left"
                  }`}
                >
                  {col.header}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {loading
              ? Array.from({ length: 5 }).map((_, i) => (
                  <SkeletonRow key={i} cols={columns.length} />
                ))
              : rows.length === 0
              ? (
                  <tr className="border-t border-zinc-800/50">
                    <td
                      colSpan={columns.length}
                      className="px-6 py-10 text-center text-sm text-zinc-600"
                    >
                      No records found
                    </td>
                  </tr>
                )
              : rows.map((row) => (
                  <tr
                    key={getRowKey(row)}
                    className="border-t border-zinc-800/50 transition-colors hover:bg-zinc-800/30"
                  >
                    {columns.map((col) => (
                      <td
                        key={col.key}
                        className={`px-6 py-4 text-sm ${
                          col.align === "right" ? "text-right" : ""
                        }`}
                      >
                        {col.render(row)}
                      </td>
                    ))}
                  </tr>
                ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
