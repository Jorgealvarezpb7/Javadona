interface StatValueProps {
  children: React.ReactNode;
}

export function StatValue({ children }: StatValueProps) {
  return (
    <span className="text-5xl font-bold text-white tracking-tight">
      {children}
    </span>
  );
}
