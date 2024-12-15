<template>
  <!-- Wrapper: Gedrehte Ansicht für kleine Bildschirme -->
  <div
    class="font-vcr m-0 text-black bg-[#f6f5f1] fixed top-0 left-0 w-screen-full h-screen-full
           flex items-center justify-center overflow-hidden"
  >
    <!-- Rotierter Inhalt -->
    <div
      class="w-screen-full h-screen-full transform origin-center sm:rotate-0 rotate-90 flex flex-col justify-between"
    >
      <!-- Header -->
      <header class="flex justify-start mt-40 px-0 bg-[#fff5ff]">
        <img
          src="@/assets/xx_Images/xx_Images/wordmark/wordmark_scaled.png"
          alt="Linguexplorer"
          class="w-8/12 sm:w-5/12"
        />
      </header>

      <!-- Rechts oben Button -->
      <div class="absolute top-4 right-4 bg-[#55f5ff]">
        <button class="hover-button">
          <router-link to="/accInfoSeite">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/SettingsIcon.png"
              alt="Einstellungen"
              class="max-h-[40px] w-[40px] sm:max-h-[60px] sm:w-[60px] hover:opacity-80"
            />
          </router-link>
        </button>
      </div>

      <!-- Main Inhalt -->
      <main class="flex flex-col items-center justify-center flex-grow bg-[#ff555f] w-screen-full">
        <div
          class="flex flex-col lg:flex-row items-center justify-between gap-0 w-full max-w-1xl sm:max-w-5xl px-1"> 
          <!-- Buttons links -->
          <div class="flex flex-col space-y-8 items-center">
            <button class="hover-button">
              <img
                src="@/assets/xx_Images/xx_Images/Buttons/neuesSpiel_green.png"
                alt="Jetzt spielen"
                class="max-h-[50px] hover:opacity-80"
              />
            </button>

            <button class="hover-button">
              <img
                src="@/assets/xx_Images/xx_Images/Buttons/spielstandLaden_green.png"
                alt="Spielstand laden"
                class="max-h-[50px] hover:opacity-80"
              />
            </button>
          </div>

          <!-- Charakter Animation -->
          <div class="flex justify-center">
            <img
              src="@/assets/xx_Images/xx_Images/MainCharacterFrontAnimationv2.gif"
              alt="Character Animation"
              class="w-[150px] sm:w-[200px]"
            />
          </div>
          
        </div>
      </main>
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
