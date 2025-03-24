<template>
  <div class="w-full h-full font-vcr text-black bg-[#f6f5f1] fixed flex items-center justify-center">
      <div class="absolute lg:top-4 lg:right-4 md:top-4 md:right-4 sm:bottom-4 sm:right-4 bottom-4 right-4">
          <button class="hover-button">
            <router-link to="/accInfoSeite">
              <img
                src="@/assets/xx_Images/xx_Images/Buttons/SettingsIcon.png"
                alt="Einstellungen"
                class="lg:w-[60px] md:w-[50px] sm:w-[40px] w-[40px] hover:opacity-80"
              />
            </router-link>
          </button>
        </div>

      <div class="transform origin-center lg:rotate-0 md:rotate-0 sm:rotate-90 rotate-90 flex flex-col">
        <header class="w-full justify-left px-4">
          <img
            src="@/assets/xx_Images/xx_Images/wordmark/wordmark_scaled.png"
            alt="Linguexplorer"
            class="lg:w-[600px] md:w-[400px] sm:w-[250px] w-[250px]"
          />
        </header>
    
        <div class="lg:w-[900px] md:w-[600px] sm:w-[400px] w-[400px] flex flex-row items-center justify-between px-4 py-4 gap-4">
          <div class="flex flex-col space-y-4 items-center">
            <button class="hover-button" @click="download">
              <img
                src="@/assets/xx_Images/xx_Images/Buttons/spiel_herunterladen 1.png"
                alt="Jetzt spielen"
                class="lg:h-[100px] md:h-[80px] sm:h-[60px] h-[60px] hover:opacity-80"
              />
            </button>
        
            <div v-if="showNoSavesMessage" class="text-red-500 mt-2">
                Keine Spielstände gefunden.
              </div>
          </div>
          <div class="flex justify-center lg:mt-[-60px] md:mt-[-50px] sm:mt-[-40px] mt-[-40px]">
            <img
              src="@/assets/xx_Images/xx_Images/MainCharacterFrontAnimationv2.gif"
              alt="Character Animation"
              class="lg:w-[200px] md:w-[160px] sm:w-[100px] w-[100px]"
            />
          </div>
        </div>
      </div>
    </div>
  </template>

<script>
import { onMounted , ref} from 'vue';
import { useStore } from 'vuex';
import { reactive } from "vue";
import { useRoute } from 'vue-router';
import { shallowRef } from 'vue';
import { Toaster, toast } from "vue-sonner";

export default {
  name: "Afterlogin",
  setup() {
    const message = ref("Du bist nicht mehr eingeloggt!");
    const store = useStore();
    const userid = ref(0);
    onMounted(async () => {
      try {
        const res = await fetch("https://da.linguexplorer.com/api/user", {
          headers: { "Content-Type": "application/json" },
          credentials: "include",
        });
        if (!res.ok) {
          await store.dispatch("logout");
        } else {
          const content = await res.json();
          userid.value = content.id
          await store.dispatch("login");
        }
      } catch (e) {
        console.error("Fehler beim Senden der Anfrage:", e);
      }
    });


    const download = async () => {
  try {
    const res = await fetch(`https://da.linguexplorer.com/api/download?userid=${userid.value}`, {
      headers: { "Content-Type": "application/json" },
      credentials: "include",
    });

    if (!res.ok) {
      console.log('Fehler beim Herunterladen der Datei');
      return;
    }

    const contentDisposition = res.headers.get('Content-Disposition');
    const filename = contentDisposition
      ? contentDisposition.split('filename=')[1].replace(/['"]/g, '')
      : 'linguExplorer.exe';

    
    const blob = await res.blob();

    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename; 
    document.body.appendChild(a);
    a.click(); 

    // Aufräumen
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);

    console.log('Datei erfolgreich heruntergeladen');
  } catch (e) {
    console.error("Fehler beim Senden der Anfrage:", e);
  }
};



    return {
      message,
      download

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
