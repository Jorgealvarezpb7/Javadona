import type { Route } from "./+types/home";
import { DashboardGrid } from "~/components/organisms/DashboardGrid";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Javadona" },
    {
      name: "description",
      content: "Javadona's platform for managing supermarket operations.",
    },
  ];
}

export default function Home() {
  return (
    <main className="min-h-screen bg-zinc-950 p-8">
      <div className="mx-auto max-w-7xl">
        <header className="mb-8">
          <h1 className="text-4xl font-bold text-white">Javadona</h1>
        </header>
        <DashboardGrid />
      </div>
    </main>
  );
}
