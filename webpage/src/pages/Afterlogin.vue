<template>
  <!-- Wrapper: Gedrehte Ansicht für kleine Bildschirme -->
  <div class="w-full h-full font-vcr text-black bg-[#f6f5f1] fixed flex items-center justify-center">

    <!-- Rechts oben Button -->
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

    <!-- Rotierter Inhalt -->
    <div class="transform origin-center lg:rotate-0 md:rotate-0 sm:rotate-90 rotate-90 flex flex-col">

      <!-- Header -->
      <header class="w-full justify-left px-4">
        <img
          src="@/assets/xx_Images/xx_Images/wordmark/wordmark_scaled.png"
          alt="Linguexplorer"
          class="lg:w-[600px] md:w-[400px] sm:w-[250px] w-[250px]"
        />
      </header>

      <!-- Main Inhalt -->
      <div class="lg:w-[900px] md:w-[600px] sm:w-[400px] w-[400px] flex flex-row items-center justify-between px-4 py-4 gap-4">
        <!-- Buttons links -->
        <div class="flex flex-col space-y-4 items-center">
          <button class="hover-button">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/neuesSpiel_green.png"
              alt="Jetzt spielen"
              class="lg:h-[50px] md:h-[40px] sm:h-[30px] h-[30px] hover:opacity-80"
            />
          </button>
          <button class="hover-button">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/spielstandLaden_green.png"
              alt="Spielstand laden"
              class="lg:h-[50px] md:h-[40px] sm:h-[30px] h-[30px] hover:opacity-80"
            />
          </button>
        </div>

        <!-- Charakter -->
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
export default {
  name: "Afterlogin",
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
