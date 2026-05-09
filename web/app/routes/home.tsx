import { DashboardGrid } from "~/components/organisms/DashboardGrid";
import { CustomersTableCard } from "~/components/organisms/CustomersTableCard";
import { ProductsTableCard } from "~/components/organisms/ProductsTableCard";
import { SalesPointsTableCard } from "~/components/organisms/SalesPointsTableCard";
import { SalesTableCard } from "~/components/organisms/SalesTableCard";

import type { Route } from "./+types/home";

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
      <div className="mx-auto max-w-7xl space-y-8">
        <header>
          <h1 className="text-4xl font-bold text-white">Javadona</h1>
        </header>
        <DashboardGrid />
        <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
          <CustomersTableCard />
          <ProductsTableCard />
          <SalesPointsTableCard />
          <SalesTableCard />
        </div>
      </div>
    </main>
  );
}
