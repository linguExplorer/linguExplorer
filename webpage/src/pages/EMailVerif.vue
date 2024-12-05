<template>
  <div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] flex flex-col justify-start min-h-screen relative">
    <header class="bg-[#99b305] text-black sticky top-0 z-10 w-full">
        <section class="w-full py-2 flex justify-between items-center px-4">
            <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" alt="Logo" class="w-1/8 max-w-[200px] ml-4" />
        </section>
    </header>

    <main class="max-w-6xl mx-auto mt-[80px] px-6">
            <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="min-w-[320px] xxl:min-w-[380px] absolute z-2 translate-x-[-42vw] translate-y-[-12vh] 
            xxl:translate-x-[-42vw] xxl:translate-y-[-11vh]" />
            <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="min-w-[150px] xxl:min-w-[180px] absolute z-1 translate-x-[-38vw] translate-y-[-3vh]
            xxl:translate-x-[-38vw] xxl:translate-y-[-3vh]" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="min-w-[320px] xxl:min-w-[380px] absolute z-2 translate-x-[-45vw] translate-y-[7vh]
        xxl:translate-x-[-45vw] xxl:translate-y-[5vh]" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="min-w-[320px] xxl:min-w-[380px] absolute z-1 translate-x-[30vw] translate-y-[-8vh]
        xxl:translate-x-[30vw] xxl:translate-y-[-8vh]" />

    </main>

    <section class="flex flex-col justify-center items-center gap-[0px] min-w-[700px]">
      
            <h1 class="font-pixelsplitter text-[40px] mb-6">Bestätige deine <br> E-Mail-Adresse</h1>

            <p class="font-size:18px font-vcr text-center" >Um deine Registrierung abzuschließen,
                überprüfe <br> bitte dein E-Mail Postfach. Dort findest <br> du eine E-Mail mit einem Bestätigungslink <br>
              Klicke auf den Link in dieser E-Mail, um <br> deine Registrierung zu bestätigen und <br> loszulegen!  </p>

              <p class="font-vcr text-center" style="font-size: 14px; margin-top: 20px; margin-bottom: 20px;">
               Keine E-Mail erhalten?
              </p>
        
            <button type="submit" class="hover-button max-w-[150px] w-full" >
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button_nochmal_versuchen.png" alt="Speichern" />
            </button>
            
        </section>

        <div class="flex justify-center mt-[-105px] ml-[850px]">
          <img
            src="@/assets/xx_Images/xx_Images/MainCharacterSideRightAnimationv2.gif"
            alt="Character Animation"
            class="w-[300px] md:w-[500px] lg:w-[200px]"
          />
        </div>


         <!-- Gras-Bild unten -->
        <div class="w-full absolute bottom-0 z-0 overflow-hidden">
          <img
            src="@/assets/xx_Images/xx_Images/gras_combined_scaled.png"
            alt="Gras"
            class="w-full"
            style="transform: translateY(90%);"
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
  
  