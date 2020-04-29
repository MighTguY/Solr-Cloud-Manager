import Dashboard from "./components/Layout/Dashboard/Dashboard";
import ZkFileViewer from "./components/Layout/Zookeeper/ZkFileViewer/ZkFileViewer";
import ZkStatus from "./components/Layout/Zookeeper/ZkStatus/ZkStatus";
import CloudManager from "./components/Layout/CloudManager/CloudManager";
import Collection from "./components/Layout/Collection/Collection";
import BackupManager from "./components/Layout/BackupManager/BackupManager";
import CDCRManager from "./components/Layout/CDCRManager/CDCRManager";
import AliasManager from "./components/Layout/AliasManager/AliasManager";

var dashboardRoutes = [
  {
    id: 0,
    path: "/dashboard",
    name: "Dashboard",
    icon: "fa fa-home fw",
    component: Dashboard,
    type: "inline",
    layout: "/solr"
  },
  {
    id: 8,
    path: "/manager/cloud",
    name: "Solr Cloud Manager",
    icon: "fa fa-sitemap fw",
    component: CloudManager,
    type: "inline",
    layout: "/solr"
  },
  {
    id: 1,
    name: "Zookeeper",
    icon: "fa fa-superpowers fw",
    type: "dropdown",
    to: "/zk",
    activeRoutes: ["/zk/files", "/zk/status"],
    routes: [
      {
        id: 2,
        path: "/zk/files",
        name: "Files",
        icon: "fa fa-clone fw",
        component: ZkFileViewer,
        type: "inline",
        layout: "/solr"
      },
      {
        id: 3,
        path: "/zk/status",
        name: "Status",
        icon: "fa fa-life-ring fw",
        type: "inline",
        component: ZkStatus,
        layout: "/solr"
      },
    ]
  },
  
  {
    id: 4,
    name: "More",
    icon: "fa fa-magic fw",
    type: "dropdown",
    to: "/manager",
    activeRoutes: ["/manager/alias", "/manager/cdcr"],
    routes: [
      {
        id: 5,
        path: "/manager/alias",
        name: "Alias Manager",
        icon: "fa fa-tags fw",
        component: AliasManager,
        type: "inline",
        layout: "/solr"
      },
      {
        id: 6,
        path: "/manager/cdcr",
        name: "CDCR",
        icon: "fa fa-arrows-h fw",
        type: "inline",
        component: CDCRManager,
        layout: "/solr"
      },
      {
        id: 7,
        path: "/manager/backup",
        name: "Backup Restore",
        icon: "fa fa-camera fw",
        type: "inline",
        component: BackupManager,
        layout: "/solr"
      },
    ]
  },

  {
    id: 14,
    name: "Collections",
    icon: "fa fa-server fw",
    type: "dropdown",
    to: "/core",
    activeRoutes: ["/core/collection1", "/core/collection2"],
    routes: [
      {
        id: 15,
        path: "/core/collection1",
        name: "Collection 1",
        icon: "fa fa-cloud fw",
        component: Collection,
        type: "inline",
        layout: "/solr"
      },
      {
        id: 16,
        path: "/core/collection2",
        name: "Collection 2",
        icon: "fa fa-cloud fw",
        type: "inline",
        component: Collection,
        layout: "/solr"
      }
    ]
  }
];


export default dashboardRoutes;