<template>
  <div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] flex flex-col justify-start min-h-screen relative">
    <header class="bg-[#99b305] text-black sticky top-0 z-10 w-full">
      <section class="w-full py-2 flex justify-between items-center px-4">
        <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" alt="Logo" class="w-1/8 max-w-[200px] ml-4" />
      </section>
    </header>

    <!--<main class="max-w-6xl mx-auto mt-[80px] px-6">-->
    <main class="mt-[80px]">
      <img
          src="@/assets/xx_Images/xx_Images/cloud.png"
          alt="Wolke"
          class="w-[160px] sm:min-w-[320px] absolute z-2 translate-x-[-42vw] translate-y-[-10vh] sm:translate-x-[-42vw] sm:translate-y-[-12vh]"
        />
        <img
          src="@/assets/xx_Images/xx_Images/sun.png"
          alt="Sonne"
          class="w-[70px] sm:min-w-[150px] absolute z-1 translate-x-[-38vw] translate-y-[-4vh] sm:translate-x-[-38vw] sm:translate-y-[-3vh]"
        />
        <img
          src="@/assets/xx_Images/xx_Images/Cloud2.png"
          alt="Wolke"
          class="w-[160px] sm:min-w-[320px] absolute z-2 translate-x-[-45vw] translate-y-[-2vh] sm:translate-x-[-45vw] sm:translate-y-[7vh]"
        />
        <img
          src="@/assets/xx_Images/xx_Images/Cloud2.png"
          alt="Wolke"
          class="w-[140px] sm:min-w-[320px] absolute z-1 translate-x-[40vw] translate-y-[-3vh] sm:translate-x-[30vw] sm:translate-y-[-8vh]"
        />
    </main>

    <section class="flex flex-col justify-center items-center gap-2 w-full px-4 sm:px-6 mt-10 sm:mt-0">
    <!-- Überschrift -->
    <h1 class="font-pixelsplitter text-[30px] sm:text-[30px] md:text-[40px] text-center mt-2">
      Bestätige deine <br> E-Mail-Adresse
    </h1>

    <!-- Beschreibung Handy -->
    <p class="font-vcr text-center text-[14px] sm:hidden leading-tight">
      Um deine Registrierung <br> abzuschließen, überprüfe bitte <br>
      dein E-Mail Postfach. <br> Dort findest du eine
      E-Mail mit <br> einem Bestätigungslink. <br> Klicke
      auf den Link in dieser E- <br> Mail, um deine Registrierung zu <br> bestätigen und loszulegen!
    </p>

    <!-- Beschreibung Laptop -->
    <p class="font-vcr text-center hidden sm:block text-[16px] leading-tight">
      Um deine Registrierung abzuschließen, <br> überprüfe bitte dein E-Mail Postfach. <br> Dort findest du eine E-Mail mit einem <br> Bestätigungslink. <br>
      Klicke auf den Link in dieser E-Mail, um <br> deine Registrierung zu bestätigen und <br> loszulegen!
    </p>

    <!-- Hinweis -->
    <p class="font-vcr text-center text-[12px] sm:text-[14px]">
      Keine E-Mail erhalten?
    </p>

    <!-- Button -->
    <button type="submit" class="hover-button max-w-[120px] w-full sm:max-w-[150px] mx-auto">
      <img src="@/assets/xx_Images/xx_Images/Buttons/button_nochmal_versuchen.png" alt="Speichern" />
    </button>
  </section>


       <!-- Laptop Charakter -->
        <div class="hidden md:flex justify-center mt-[-120px] ml-[850px]">
          <img
            src="@/assets/xx_Images/xx_Images/MainCharacterSideRightAnimationv2.gif"
            alt="Desktop Character Animation"
            class="w-[300px] md:w-[500px] lg:w-[200px]"
          />
        </div>

        <!-- Handy Charakters -->
        <div class="flex md:hidden justify-center mt-[-10px]">
          <img
            src="@/assets/xx_Images/xx_Images/MainCharacterFrontAnimationv2.gif"
            alt="Mobile Character Animation"
            class="w-[150px] sm:w-[300px]"
          />
        </div>


        <!-- Gras-Bild unten -->
        <div class="w-full absolute bottom-0 z-0 overflow-hidden">
          <img
            src="@/assets/xx_Images/xx_Images/gras_combined_scaled.png"
            alt="Gras"
            class="w-full sm:max-w-full sm:object-cover sm:object-center sm:translate-y-[90%] object-cover object-center"
          />
        </div>

  </div>
</template>

  <script>
  
  export default {
    name: 'EMailVerif',
  };
  </script>

<style scoped>
.hover-button img {
  transition: opacity 0.2s ease-in-out, transform 0.2s ease-in-out;
}
.hover-button img:hover {
  opacity: 0.8;
  transform: scale(1.05);
}
</style>
  
  