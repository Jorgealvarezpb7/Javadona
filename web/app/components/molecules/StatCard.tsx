import { StatLabel } from "../atoms/StatLabel";
import { StatValue } from "../atoms/StatValue";

interface StatCardProps {
  label: string;
  value?: number;
  loading?: boolean;
  error?: boolean;
  featured?: boolean;
}

function ArrowIcon() {
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

export function StatCard({
  label,
  value,
  loading = false,
  error = false,
  featured = false,
}: StatCardProps) {
  const displayValue = loading ? "—" : error ? "N/A" : (value?.toLocaleString() ?? "—");

  return (
    <div
      className={[
        "relative flex flex-col gap-4 rounded-2xl p-6",
        featured ? "bg-zinc-900 border border-zinc-800" : "bg-zinc-900/60 border border-zinc-800/50",
      ].join(" ")}
    >
      <div className="flex items-center justify-between">
        <StatLabel>{label}</StatLabel>
        <button
          className={[
            "flex h-8 w-8 items-center justify-center rounded-full transition-colors",
            featured
              ? "bg-white/10 text-white hover:bg-white/20"
              : "bg-zinc-800 text-zinc-400 hover:bg-zinc-700",
          ].join(" ")}
          aria-label={`View ${label}`}
        >
          <ArrowIcon />
        </button>
      </div>

      <div className="flex flex-col gap-1">
        <StatValue>
          {loading ? (
            <span className="inline-block h-12 w-24 animate-pulse rounded-lg bg-zinc-800" />
          ) : (
            displayValue
          )}
        </StatValue>
      </div>
    </div>
  );
}
