import { Welcome } from "../welcome/welcome";
import { useEffect } from "react";
import { JavadonaClient } from "~/lib/JavadonaClient";

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
  useEffect(() => {
    const httpClient = new JavadonaClient("http://localhost:8080");
    httpClient.customer
      .getAllCustomers({ page: 0, size: 10 })
      .then((response) => {
        console.log("Fetched customers:", response);
      })
      .catch((error) => {
        console.error("Error fetching customers:", error);
      });
  }, []);
  return <Welcome />;
}
