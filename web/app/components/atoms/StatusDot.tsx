export type StatusVariant = "success" | "warning" | "error" | "neutral";

interface StatusDotProps {
  status: StatusVariant;
  label?: string;
}

const colorMap: Record<StatusVariant, string> = {
  success: "bg-emerald-500",
  warning: "bg-amber-500",
  error: "bg-red-500",
  neutral: "bg-zinc-500",
};

export function StatusDot({ status, label }: StatusDotProps) {
  return (
    <span className="flex items-center gap-2">
      <span className={`inline-block h-2 w-2 shrink-0 rounded-full ${colorMap[status]}`} />
      {label && <span className="text-zinc-300">{label}</span>}
    </span>
  );
}
