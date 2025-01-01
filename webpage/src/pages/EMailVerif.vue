<template>

<div
    v-if="isLoading"
    class="absolute top-0 left-0 flex justify-center items-center h-screen w-full bg-white bg-opacity-50 z-50"></div>

  <div class="font-vcr m-0 text-black w-full min-h-screen bg-[#f6f5f1] flex flex-col justify-start relative">

    <!-- Header -->
    <header class="bg-[#99b305] text-black sticky top-0 z-10">
      <section class="w-full py-2 flex justify-between items-center px-4">
          <img
            src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png"
            alt="Logo"
            class="w-3/6 max-w-[180px] sm:max-w-[200px] mr-9"
          />
      </section>
    </header>

    <!--<main class="max-w-6xl mx-auto mt-[80px] px-6">-->
    <main class="mt-[80px]">
      <img
          src="@/assets/xx_Images/xx_Images/cloud.png"
          alt="Wolke"
          class="lg:min-w-[320px] md:min-w-[240px] sm:min-w-[160px] w-[160px] absolute z-2 translate-x-[-42vw] translate-y-[-10vh] sm:translate-y-[-12vh]"
        />
        <img
          src="@/assets/xx_Images/xx_Images/sun.png"
          alt="Sonne"
          class="lg:min-w-[150px] md:min-w-[100px] sm:min-w-[70px] w-[70px] absolute z-1 translate-x-[-38vw] lg:translate-y-[-3vh] md:translate-y-[-3vh] sm:translate-y-[-5vh] translate-y-[-5vh]"
        />
        <img
          src="@/assets/xx_Images/xx_Images/Cloud2.png"
          alt="Wolke"
          class="lg:min-w-[320px] md:min-w-[240px] sm:min-w-[160px] w-[160px] absolute z-2 translate-x-[-45vw] lg:translate-y-[7vh] md:translate-y-[3vh] sm:translate-y-[-2vh] translate-y-[-2vh]"
        />
        <img
          src="@/assets/xx_Images/xx_Images/Cloud2.png"
          alt="Wolke"
          class="lg:min-w-[320px] md:min-w-[240px] sm:min-w-[160px] w-[160px] absolute z-1 
          lg:translate-x-[30vw] md:translate-x-[35vw] sm:translate-x-[40vw] translate-x-[40vw] sm:translate-y-[-8vh] translate-y-[-3vh]"
        />
    </main>

    <section class="flex flex-col justify-center items-center gap-2 w-full px-4 sm:px-6 mt-10 sm:mt-0">
    <!-- Überschrift -->
    <h1 class="font-pixelsplitter lg:text-[40px] md:text-[30px] sm:text-[20px] text-[20px] text-center mt-2">
      Bestätige deine <br> E-Mail-Adresse
    </h1>

    <!-- Beschreibung Laptop -->
    <p class="font-vcr text-center sm:block lg:text-[16px] md:text-[15px] sm:text-[13px] text-[13px] leading-tight">
      Um deine Registrierung abzuschließen, <br> überprüfe bitte dein E-Mail Postfach. <br> Dort findest du eine E-Mail mit einem <br> Bestätigungslink. <br>
      Klicke auf den Link in dieser E-Mail, um <br> deine Registrierung zu bestätigen und <br> loszulegen!
    </p>

    <!-- Hinweis -->
    <p class="font-vcr text-center lg:text-[14px] md:text-[13px] sm:text-[12px] text-[12px]">
      Keine E-Mail erhalten?
    </p>

    <!-- Button -->
    <button type="submit" class="hover-button lg:w-[150px] md:w-[140px] sm:w-[120px] w-[120px] mx-auto z-10" @click="resend">
      <img src="@/assets/xx_Images/xx_Images/Buttons/button_nochmal_versuchen.png" alt="Speichern" />
    </button>
  </section>

       <!-- Laptop Charakter -->
        <div class="hidden md:flex justify-center translate-x-[30vw] lg:translate-y-[-17vh] md:translate-y-[-13vh]">
          <img
            src="@/assets/xx_Images/xx_Images/MainCharacterSideRightAnimationv2.gif"
            alt="Desktop Character Animation"
            class="lg:w-[200px] md:w-[180px] sm:w-[180px]"
          />
        </div>

        <!-- Handy Charakters -->
        <div class="flex md:hidden justify-center mt-[-10px]">
          <img
            src="@/assets/xx_Images/xx_Images/MainCharacterFrontAnimationv2.gif"
            alt="Mobile Character Animation"
            class="w-[120px]"
          />
        </div>

        <section 
          class="relative w-full overflow-hidden bg-grass-background z-0
          lg:-mt-[238px] md:-mt-[207px] sm:-mt-[100px] mt-[-100px]
          lg:h-[calc(100vh-238px-210px)] md:h-[calc(100vh-207px-210px)] sm:h-[calc(100vh-100px-210px)] h-[calc(100vh-100px-100px-210px)]"> <!--es ist sm und was noch kleineres is nur mt-->
          <div class="absolute top-0 w-full">
            <img 
              src="@/assets/xx_Images/xx_Images/gras_combined_scaled.png" 
              alt="Gras" 
              class="w-full absolute bottom-0"
            />
          </div>
        </section>
      
  </div>
</template>

  <script>
  import { useStore } from "vuex";
  import { reactive, ref } from "vue";
  import { useRoute } from 'vue-router';
  import { shallowRef } from 'vue';
  import { Toaster, toast } from "vue-sonner";

  export default {
    name: 'EMailVerif',
    setup() {
    const data = reactive({
      email: ""
    });
    const isLoading = ref(false);
    const store = useStore();

    data.email = store.getters.getEmail;

    const resend = async () => {
      try {
        const res = await fetch("https://da.linguexplorer.com/api/resend", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          credentials: "include",
          body: JSON.stringify(data),
        });
        if (!res.ok) {
          isLoading.value = false;
          const err = await res.json();
      
        } 
      } catch (error) {
        console.error("Fehler beim Senden der Anfrage:", error);
      }
    };

    return {
      data,
      resend,
      isLoading,
    };
  }
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
  
  