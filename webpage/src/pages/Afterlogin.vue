<template>
  <div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-between">
    <!-- Header -->
    <header class="flex justify-start mt-40 px-40">
      <!-- Logo links oben -->
      <img
        src="@/assets/xx_Images/xx_Images/wordmark/wordmark_scaled.png"
        alt="Linguexplorer"
        class="w-8/12 max-w-[600px]"
      />
    </header>

    <!-- Rechts oben Button -->
    <div class="absolute top-4 right-4">
      <button class="hover-button">
        <router-link to="/accInfoSeite"  href="#">

        <img
          src="@/assets/xx_Images/xx_Images/Buttons/SettingsIcon.png"
          alt="Nach oben"
          class="max-h-[100px] hover:opacity-80"
        />
      </router-link>

      </button>
    </div>

    <main class="flex flex-col items-center justify-center flex-grow mt-[-90px]">
      <div class="flex flex-col lg:flex-row items-center justify-between gap-17 w-full max-w-5xl px-6">
        <!-- Buttons links -->
        <div class="flex flex-col space-y-9 items-center">
          <button class="hover-button">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/neuesSpiel_green.png"
              alt="Jetzt spielen"
              class="max-h-[55px] hover:opacity-80"
            />
          </button>

          <button class="hover-button">
            <img
             src="@/assets/xx_Images/xx_Images/Buttons/spielstandLaden_green.png"
              alt="Spielstand laden"
              class="max-h-[55px] hover:opacity-80"
            />
          </button>
        </div>

        <div class="flex justify-center mt-[-90px]">
          <img
            src="@/assets/xx_Images/xx_Images/MainCharacterFrontAnimationv2.gif"
            alt="Character Animation"
            class="w-[200px] md:w-[200px] lg:w-[250px]"
          />
        </div>
      </div>
    </main>
    <footer class="mt-auto">
        <section class="mx-auto p-4 flex justify-end items-center">
          <button class="max-w-[200px] p-4">
            <router-link to="/" class="nav-link" href="#">
              <img
                src="@/assets/xx_Images/xx_Images/Buttons/button zrk.png"
                alt="Zurück"
                class="hover:opacity-80"
              />
            </router-link>
          </button>
        </section>
      </footer>

  </div>
</template>

<script>
import { onMounted , ref} from 'vue';
import { useStore } from 'vuex';
import { toast } from 'vue-sonner'

export default {
  name: "Afterlogin",
  setup() {
    const message = ref("Du bist nicht mehr eingeloggt!");
    const store = useStore();
    onMounted(async () => {
      try {
        const res = await fetch("http://localhost:8000/api/user", {
          headers: { "Content-Type": "application/json" },
          credentials: "include",
        });
        if (!res.ok) {
          await store.dispatch("logout");
        } else {
          await store.dispatch("login");
        }
      } catch (e) {
        console.error("Fehler beim Senden der Anfrage:", e);
      }
    });

    return {
      message,
    };
  },
  computed: {
    isLoggedIn() {
      return this.$store.getters.isAuthenticated;
    },
  },
};
</script>

<style scoped>
.hover-button img {
  transition: transform 0.2s ease-in-out;
}
.hover-button img:hover {
  transform: scale(1.1);
}
</style>
