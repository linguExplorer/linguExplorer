<template>

    <div v-if='!cookieConsent'>

        <div 
      class="fixed inset-0 bg-white bg-opacity-50 z-40"
    ></div>

    <div class="fixed bottom-4  left-4 z-50">
      <div class="w-3/6  flex flex-row items-center justify-center
      bg-[#f6f5f1] border-[#99b305] border-4 px-5">

      <img src="@/assets/xx_Images/cookie.png"  class="max-h-[250px]" />

      <div class="flex flex-col items-center justify-center gap-2 py-10 px-8 ">

        <h2 class="font-pixelsplitter text-2xl">WIR NUTZEN COOKIES</h2>
      
      <p class="text-[15px] font-vcr text-center  pt-5 pb-10">
        Wir verwenden Cookies, um unsere Website zu verbessern und Ihnen eine optimale Nutzung zu ermöglichen. 
        Einige Cookies sind notwendig, während andere uns helfen, die Leistung und Ihr Erlebnis zu verbessern. 
        Sie können Ihre Einstellungen anpassen oder alle Cookies akzeptieren.
      </p>

      <div class="flex flex-row justify-center items-center gap-20">
  <button class="play-button" @click="confirmCookie">
    <img src="@/assets/xx_Images/xx_Images/Buttons/cookieAkz.png" class=" hover:opacity-80" />
  </button>

  <button class="play-button" @click="confirmCookie">
    <img src="@/assets/xx_Images/xx_Images/Buttons/notwendige.png" class="hover:opacity-80" />
  </button>
</div>
    </div>
    </div>
</div>
</div>

    
</template>


<script>
export default {

data() {
    return {
        cookieConsent: false
    };
},
created(){
    const consent = sessionStorage.getItem('cookieConsent');
    if (consent === 'true') {
        this.cookieConsent = true;
    }
},
methods: {
    confirmCookie() {
        sessionStorage.setItem('cookieConsent', true);
        this.cookieConsent = true;
    }
}
};
</script>