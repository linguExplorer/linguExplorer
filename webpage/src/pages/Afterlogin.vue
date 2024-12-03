<template>
  <div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-between">
    <!-- Header -->
    <header class="flex justify-start mt-40 px-60 md:px-40">
      <!-- Logo links oben -->
      <img
        src="@/assets/xx_Images/xx_Images/wordmark/wordmark_scaled.png"
        alt="Linguexplorer"
        class="w-8/12 max-w-[600px] sm:w-6/12 md:w-8/12"
      />
    </header>

    <!-- Rechts oben Button -->
    <div class="absolute top-4 right-4">
      <button class="hover-button">
        <router-link to="/accInfoSeite"  href="#">

        <img
          src="@/assets/xx_Images/xx_Images/Buttons/SettingsIcon.png"
          alt="Nach oben"
          class="max-h-[40px] w-[40px] sm:max-h-[60px] sm:w-[60px] hover:opacity-80"
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
              class="max-h-[45px] hover:opacity-80 sm:max-h-[55px]"
            />
          </button>

          <button class="hover-button">
            <img
             src="@/assets/xx_Images/xx_Images/Buttons/spielstandLaden_green.png"
              alt="Spielstand laden"
              class="max-h-[45px] hover:opacity-80 sm:max-h-[55px]"
            />
          </button>
        </div>

        <div class="flex justify-center mt-[-90px]">
          <img
            src="@/assets/xx_Images/xx_Images/MainCharacterFrontAnimationv2.gif"
            alt="Character Animation"
            class="w-[150px] sm:w-[200px] md:w-[200px] lg:w-[250px]"
          />
        </div>
      </div>
    </main>

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
