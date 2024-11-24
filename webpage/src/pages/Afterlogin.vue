<template>
  <div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-between">
    <!-- Header -->
    <header class="flex justify-start mt-20 px-40">
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
        <img
          src="@/assets/xx_Images/xx_Images/Buttons/button_up.png"
          alt="Nach oben"
          class="max-h-[55px] hover:opacity-80"
        />
      </button>
    </div>

    <main class="flex flex-col items-center justify-center flex-grow">
      <div class="flex flex-col lg:flex-row items-center justify-between gap-16 w-full max-w-5xl px-6">
        <!-- Buttons links -->
        <div class="flex flex-col space-y-6 items-center">
          <button class="hover-button">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/button jzt spielen.png"
              alt="Jetzt spielen"
              class="max-h-[55px] hover:opacity-80"
            />
          </button>

          <button class="hover-button">
            <img
             src="@/assets/xx_Images/xx_Images/Buttons/button jzt spielen.png"
              alt="Spielstand laden"
              class="max-h-[55px] hover:opacity-80"
            />
          </button>
        </div>

        <!-- Charakter rechts -->
        <div class="flex justify-center">
          <img
            src="@/assets/xx_Images/xx_Images/Charactee New.png"
            alt="Character"
            class="w-[200px] md:w-[300px] lg:w-[380px]"
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
