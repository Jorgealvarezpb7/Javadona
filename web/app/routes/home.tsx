import type { Route } from "./+types/home";
import { Welcome } from "../welcome/welcome";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Javadona" },
    { name: "description", content: "Javadona's platform for managing supermarket operations." },
  ];
}

export default function Home() {
  return <Welcome />;
}
