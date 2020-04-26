import Dashboard from "./components/Layout/Dashboard/Dashboard";
import ErrorComponent from "./components/Layout/Error/ErrorComponent";

const dashboardRoutes = [
    {
      path: "/dashboard",
      name: "Dashboard",
      icon: "pe-7s-graph",
      component: Dashboard,
      layout: "/solr"
    },
    {
      path: "/error",
      name: "Error",
      icon: "pe-7s-graph",
      component: ErrorComponent,
      layout: "/solr"
    }
  ];

  export default dashboardRoutes;