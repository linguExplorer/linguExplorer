<template>
  <div class="min-h-screen w-full bg-[#f6f5f1]">
    <!-- Header -->
    <header class="bg-[#99b305] text-black sticky top-0 z-10">
      <section class="w-full py-2 flex justify-between items-center px-4">
        <img
          src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png"
          alt="Logo"
          class="w-3/6 max-w-[180px] sm:max-w-[200px] mr-9"
        />
        <div class="flex justify-center items-center space-x-2 sm:space-x-4">
          <!-- Button -->
          <template v-if="isLoggedIn">
            <button class="nav-button-spielbeschreibung hover-button">
              <router-link to="/afterlogin" class="nav-link" href="#">
                <img
                  src="@/assets/xx_Images/xx_Images/Buttons/button jzt spielen.png"
                  alt="Jetzt spielen"
                  class="lg:max-h-[46px] md:max-h-[45px] sm:max-h-[35px] max-h-[35px] px-2"
                />
              </router-link>
            </button>
          </template>
          <template v-else>
            <button class="nav-button-spielbeschreibung hover-button">
              <router-link to="/anmelden" class="nav-link" href="#">
                <img
                  src="@/assets/xx_Images/xx_Images/Buttons/button anmelden green.png"
                  alt="Jetzt spielen"
                  class="lg:max-h-[46px] md:max-h-[45px] sm:max-h-[35px] max-h-[35px] px-2"
                />
              </router-link>
            </button>
          </template>
        </div>
      </section>
    </header>

    <!-- Content -->
    <main class="max-w-full mx-auto">
      <!-- Background Images -->
      <!--<img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="w-[160px] sm:min-w-[320px] absolute z-2 translate-x-[-42vw] translate-y-[-10vh] sm:translate-x-[-42vw] sm:translate-y-[-1vh]" />-->
      <img
        src="@/assets/xx_Images/xx_Images/sun.png"
        alt="Sonne"
        class="lg:min-w-[150px] md:min-w-[100px] sm:min-w-[70px] w-[70px] absolute z-1 translate-x-[-40vw] translate-y-[0vh] sm:translate-x-[-45.3vw] sm:translate-y-[3vh]"
      />
      <img
        src="@/assets/xx_Images/xx_Images/cloud.png"
        alt="Wolke"
        class="lg:min-w-[320px] md:min-w-[220px] sm:min-w-[160px] w-[160px] absolute z-2 translate-x-[-50vw] translate-y-[3vh] sm:translate-x-[-38vw] sm:translate-y-[10vh]"
      />
      <img
        src="@/assets/xx_Images/xx_Images/Cloud2.png"
        alt="Wolke"
        class="lg:min-w-[320px] md:min-w-[230px] sm:min-w-[140px] w-[140px] absolute z-1 translate-x-[40vw] translate-y-[5vh] sm:translate-x-[30vw] sm:translate-y-[13vh]"
      />

      <h1
        class="font-pixelsplitter lg:text-[68px] md:text-[48px] sm:text-[24px] text-[24px] mt-8 sm:mt-16"
      >
        Willkommen
      </h1>
      <h1
        class="font-pixelsplitter lg:text-[68px] md:text-[48px] sm:text-[24px] text-[24px]"
      >
        zu
      </h1>

      <img
        src="@/assets/xx_Images/xx_Images/wordmark/wordmark_scaled.png"
        alt="Linguexplorer"
        class="lg:w-[550px] md:w-[450px] sm:w-[230px] w-[230px] mb-4 sm:mb-10"
      />

      <!-- Button direkt unter dem Linguexplorer-Logo -->
      <template v-if="isLoggedIn">
        <button class="hover-button">
          <router-link to="/afterlogin" class="nav-link" href="#">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/button play now.png"
              alt="Jetzt spielen"
              class="lg:max-h-[80px] md:max-h-[60px] sm:max-h-[40px] max-h-[40px] hover:opacity-80"
            />
          </router-link>
        </button>
      </template>
      <template v-else>
        <button class="hover-button">
          <router-link to="/anmelden" class="nav-link" href="#">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/button play now.png"
              alt="Jetzt spielen"
              class="lg:max-h-[80px] md:max-h-[60px] sm:max-h-[40px] max-h-[40px] hover:opacity-80"
            />
          </router-link>
        </button>
      </template>

      <!-- Character Image - neben dem "Jetzt spielen" button -->
      <img
        src="@/assets/xx_Images/xx_Images/Charactee New.png"
        alt="Character"
        class="lg:w-[370px] md:w-[270px] sm:w-[180px] w-[140px] absolute z-1 sm:translate-x-[-35vw] translate-x-[-25vw] lg:translate-y-[61.5vh] md:translate-y-[45vh] sm:translate-y-[34.5vh] translate-y-[30vh]"
      />

      <!-- Gras Bild -->
      <section
        class="bg-grass-background w-full relative z-0 lg:mt-[52px] md:mt-[-24px] sm:mt-[-12px] mt-[-12px]"
      >
        <!--bg-[#ffb305]-->
        <ul
          class="list-none lg:mx-36 md:mx-20 sm:mx-9 mx-9 my-36 flex flex-col items-center"
        >
          <li
            class="lg:w-[1050px] md:w-[700px] sm:w-[300px] w-[300px] flex flex-col items-center justify-center bg-[#f6f5f1] lg:mt-32 md:mt-20 sm:mt-0 mt-0"
          >
            <div class="flex flex-row items-center justify-center gap-2 m-8">
              <h2
                class="font-pixelsplitter lg:text-3xl md:text-2xl sm:text-xl text-xl"
              >
                Was ist
              </h2>
              <img
                src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png"
                alt="Logo"
                class="w-1/3"
              />
              <h2
                class="font-pixelsplitter lg:text-3xl md:text-2xl sm:text-xl text-xl"
              >
                ?
              </h2>
            </div>
            <p
              class="font-vcr text-left px-10 sm:px-20 lg:text-[20px] md:text-[16px] sm:text-[14px] text-[14px] pt-2 pb-16"
            >
              linguExplorer ist eine innovative Lernanwendung, die Schülerinnen
              und Schüler auf eine unterhaltsame Reise durch die englische
              Sprache mitnimmt. Diese Reise ist speziell darauf ausgelegt, den
              Herausforderungen der Fremdsprachenerlernung entgegenzutreten, die
              viele Jugendliche in ihrer Schullaufbahn bis zur Matura erleben.
            </p>
          </li>

          <li
            class="lg:w-[1050px] md:w-[700px] sm:w-[300px] w-[300px] flex flex-col items-center justify-center bg-[#f6f5f1] lg:mt-32 md:mt-20 sm:mt-20 mt-20"
          >
            <h2
              class="font-pixelsplitter text-center px-14 pt-14 lg:text-3xl md:text-2xl sm:text-xl text-xl"
            >
              Ziele des Spiels:
            </h2>
            <p
              class="font-vcr text-left px-10 sm:px-20 lg:text-[20px] md:text-[16px] sm:text-[14px] text-[14px] pt-12 pb-8"
            >
              Das primäre Ziel von linguExplorer ist es, die Motivation der
              Spieler zu steigern, indem sie auf spielerische Weise die
              englische Sprache erlernen. Das Spiel verbindet Lernen und Spaß,
              um den Schülern zu helfen, ihre Englischkenntnisse langfristig zu
              verbessern. Durch die interaktiven und herausfordernden Aufgaben
              soll nicht nur das Lernen gefördert, sondern auch ein tieferes
              Verständnis und ein besseres Gefühl für die Sprache vermittelt
              werden.
            </p>
            <p
              class="font-vcr text-left px-10 sm:px-20 lg:text-[20px] md:text-[16px] sm:text-[14px] text-[14px] pt-0 pb-16"
            >
              linguExplorer soll den Nutzern die Möglichkeit geben, durch
              regelmäßige Interaktion mit der Sprache ihr Selbstvertrauen und
              ihre Fähigkeiten im Umgang mit Englisch zu stärken, und dies auf
              eine Weise, die im traditionellen Unterricht oft fehlt.
            </p>
          </li>

          <li
            class="lg:w-[1050px] md:w-[700px] sm:w-[300px] w-[300px] flex flex-col items-center justify-center bg-[#f6f5f1] lg:mt-32 md:mt-20 sm:mt-20 mt-20"
          >
            <h2
              class="font-pixelsplitter text-center px-14 pt-14 lg:text-3xl md:text-2xl sm:text-xl text-xl"
            >
              Gameplay:
            </h2>
            <p
              class="font-vcr text-left px-10 sm:px-20 lg:text-[20px] md:text-[16px] sm:text-[14px] text-[14px] pt-12 pb-8"
            >
              linguExplorer ist ein Singleplayer-Lernspiel, das auf der
              Kombination von Spielspaß und Bildung basiert, ein sogenanntes
              Serious Game. Das Hauptziel des Spiels ist es, Englischkenntnisse
              durch das Erforschen einer offenen Spielwelt zu verbessern. Die
              Spieler können sich auf einer Map bewegen und verschiedene Orte
              besuchen, die jeweils unterschiedliche Themenbereiche und
              sprachliche Herausforderungen repräsentieren.
            </p>
            <p
              class="font-vcr text-left px-10 sm:px-20 lg:text-[20px] md:text-[16px] sm:text-[14px] text-[14px] pt-0 pb-8"
            >
              An diesen Orten stehen dem Spieler eine Vielzahl von Minispielen
              zur Verfügung, die den Fokus auf spezifische sprachliche
              Fertigkeiten legen.
            </p>
            <p
              class="font-vcr text-left px-10 sm:px-20 lg:text-[20px] md:text-[16px] sm:text-[14px] text-[14px] pt-0 pb-16"
            >
              Zusätzlich bietet das Spiel ein Phrasenheft als hilfreiches
              Werkzeug. Hier werden alle während des Spiels erlernten Vokabeln
              und Phrasen gesammelt. Dieses Phrasenheft steht den Spielern
              jederzeit zur Verfügung und ermöglicht es ihnen, Inhalte
              nachzuschlagen und ihren Lernfortschritt zu verfolgen.
            </p>
          </li>
        </ul>
      </section>
    </main>

    <footer id="footer" class="bg-[#99b305] text-black">
      <section
        class="max-w-full lg:text-[20px] md:text-[16px] sm:text-[14px] text-[14px] mx-10 sm:mx-20 p-[3px] flex justify-between items-center"
      >
        <nav class="flex flex-row gap-4" aria-label="footer">
          <router-link to="/impressum" href="#">
            <a href="#" class="hover:opacity-90 font-vcr">Impressum</a>
          </router-link>
          <router-link to="/datenschutz" href="#">
            <a href="#" class="hover:opacity-90 font-vcr"
              >Datenschutzerklärung</a
            >
          </router-link>
        </nav>
        <a href="#top" class="hover:opacity-90 font-vcr">nach oben</a>
      </section>
    </footer>
  </div>
</template>
<script>
import { mapState, mapActions } from "vuex";
import { onMounted, ref } from "vue";
import { useStore } from "vuex";
import { Toaster, toast } from "vue-sonner";
import { useRoute } from 'vue-router';
import ToastComponent from "@/components/ToastComponent.vue";
import { shallowRef } from 'vue';

export default {
  name: "SpielbeschreibungPage",
  setup() {
    const message = ref("Du bist nicht mehr eingeloggt!");
    const store = useStore();
    onMounted(async () => {
      const route = useRoute();

      const error = route.query.error;
      if (error === 'invalid-link') {
        toast.custom(shallowRef(ToastComponent), { duration: 3000,
    
    message: 'Dies ist eine benutzerdefinierte Toast-Nachricht!'
  });
      }
      try {
        const res = await fetch("https://da.linguexplorer.com/api/user", {
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
