import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import Impressum from "../pages/Impressum.vue"; // Importiere die Impressum-Komponente
import Spielbeschreibung from "../pages/Spielbeschreibung.vue"; // Importiere die Spielbeschreibung-Komponente
import Datenschutz from "../pages/Datenschutz.vue"; // Importiere die Datenschutz-Komponente

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/impressum", // Route für Impressum
    name: "impressum",
    component: Impressum,
  },
  {
    path: "/spielbeschreibung", // Route für Spielbeschreibung
    name: "spielbeschreibung",
    component: Spielbeschreibung,
  },
  {
    path: "/datenschutz", // Route für Datenschutz
    name: "datenschutz",
    component: Datenschutz,
  },
  {
    path: "/about",
    name: "about",
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
