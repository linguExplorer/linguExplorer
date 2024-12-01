import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import Impressum from "../pages/Impressum.vue"; 
import Spielbeschreibung from "../pages/Spielbeschreibung.vue"; 
import Datenschutz from "../pages/Datenschutz.vue"; 
import Anmelden from "../pages/Anmelden.vue";
import NewPassword from "../pages/NewPassword.vue";
import ForgotPassword from "../pages/ForgotPassword.vue";
import Registrieren from "../pages/Registrieren.vue";
import ComingSoon from "@/pages/ComingSoon.vue";
import Afterlogin from "@/pages/Afterlogin.vue";
import AccInfoSeite from "@/pages/AccInfoSeite.vue";
import EMailVerif from "@/pages/EMailVerif.vue";
import CookieBanner from "@/components/CookieBanner.vue";
import AccInfoSeiteS from "@/pages/AccInfoSeiteS.vue";
const routes = [
  {
    path: "/cookie",
    name: "cookie",
    component: CookieBanner,
  },

  {
    path: "/",
    name: "home",
    component: Spielbeschreibung,
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
    path: "/forgotPassword", // Route für Forgot Password
    name: "forgotPassword",
    component: ForgotPassword,
  },
  {
    path: "/newPassword", /*/:ref",*/ // Route für New Password
    name: "newPassword",
    component: NewPassword,
  },
  {
    path: "/registrieren", // Route für Registrieren
    name: "registrieren",
    component: Registrieren,
  },
  {
    path: "/comingSoon", // Route für ComingSoonPage
    name: "comingSoon",
    component: ComingSoon,
  },
  {
    path: "/afterlogin", // Route für Afterloginpage
    name: "afterlogin",
    component: Afterlogin,
  },
  {
    path: "/accInfoSeite", // Route für AccInfoSeite
    name: "accInfoSeite",
    component: AccInfoSeite,
  },
  {
    path: "/accInfoSeiteS", // Route für AccInfoSeite Standart
    name: "accInfoSeiteS",
    component: AccInfoSeiteS,
  },
  {
    path: "/eMailVerif", // Route für EMailVerifpage
    name: "eMailVerif",
    component: EMailVerif,
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
