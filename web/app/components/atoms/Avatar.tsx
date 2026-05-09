const palette = [
  "bg-blue-600",
  "bg-violet-600",
  "bg-emerald-600",
  "bg-amber-600",
  "bg-rose-600",
  "bg-cyan-600",
  "bg-indigo-600",
  "bg-teal-600",
];

function colorFor(initials: string): string {
  const code = (initials.charCodeAt(0) || 0) + (initials.charCodeAt(1) || 0);
  return palette[code % palette.length];
}

interface AvatarProps {
  initials: string;
}

export function Avatar({ initials }: AvatarProps) {
  const upper = initials.slice(0, 2).toUpperCase();
  return (
    <div
      className={`flex h-9 w-9 shrink-0 items-center justify-center rounded-full text-xs font-semibold text-white ${colorFor(upper)}`}
    >
      {upper}
    </div>
  );
}
