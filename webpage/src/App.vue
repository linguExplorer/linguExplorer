

<template>
  <main>
    <CookieBanner></CookieBanner>
    <router-view />
    <!-- Hauptmenü -->
    <MainMenu />
  </main>
</template>


<script >
import CookieBanner from './components/CookieBanner.vue';
import MainMenu from './components/MainMenu.vue'
export default {
  components: {
    CookieBanner,
    MainMenu  //registrieren
  },
}

</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: black;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}

/* Abstand für den gesamten Inhalt, um unter der Navigationsleiste sichtbar zu sein */
#app main {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: left;
}

</style>
