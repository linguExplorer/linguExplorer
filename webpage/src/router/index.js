import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import Impressum from "../pages/Impressum.vue"; 
import Spielbeschreibung from "../pages/Spielbeschreibung.vue"; 
import Datenschutz from "../pages/Datenschutz.vue"; 
import Anmelden from "../pages/Anmelden.vue";
import ForgotPassword from "../pages/forgot_password.vue"; 
import NewPassword from "../pages/new_password.vue"; 
import Registrieren from "../pages/Registrieren.vue";

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
    path: "/anmelden", // Route für Anmelden
    name: "anmelden",
    component: Anmelden,
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
