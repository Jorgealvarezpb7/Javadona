interface StatLabelProps {
  children: React.ReactNode;
}

export function StatLabel({ children }: StatLabelProps) {
  return (
    <span className="text-sm font-medium text-zinc-400 tracking-wide">
      {children}
    </span>
  );
}
