<template>
  <div class="min-h-screen bg-[#f6f5f1]">

    <!-- Header -->
    <header class="bg-[#99b305] text-black sticky top-0 z-10">
      <section class="max-w-full mx-auto px-4 sm:px-6 py-2 flex justify-between items-center">
        <!-- Logo -->
        <img 
          src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" 
          alt="Logo" 
          class="w-[50%] sm:w-1/6" 
        />
        <div class="flex justify-center items-center space-x-2 sm:space-x-4">
          <!-- Button -->
          <button class="nav-button-spielbeschreibung hover-button">
            <img 
              src="@/assets/xx_Images/xx_Images/Buttons/button jzt spielen.png" 
              alt="Jetzt spielen" 
              class="max-h-[40px] sm:max-h-[55px] px-2" 
            />
          </button>
        </div>
      </section>
    </header>

    <!-- Content -->
    <main class="max-w-full mx-auto">

       <!-- Background Images -->
      <!--<img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="w-[160px] sm:min-w-[320px] absolute z-2 translate-x-[-42vw] translate-y-[-10vh] sm:translate-x-[-42vw] sm:translate-y-[-1vh]" />-->
      <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="w-[70px] sm:min-w-[150px] absolute z-1 translate-x-[-40vw] translate-y-[0vh] sm:translate-x-[-45.3vw] sm:translate-y-[3vh]" />
      <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="w-[160px] sm:min-w-[320px] absolute z-2 translate-x-[-50vw] translate-y-[3vh] sm:translate-x-[-38vw] sm:translate-y-[10vh]" />
      <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="w-[140px] sm:min-w-[320px] absolute z-1 translate-x-[40vw] translate-y-[5vh] sm:translate-x-[30vw] sm:translate-y-[13vh]" />
      
      <!--<section id="top" class="flex flex-col w-full justify-center items-center bg-cloud-background bg-cover">-->
          <h1 class="font-pixelsplitter text-[24px] sm:text-[48px] lg:text-[68px] mt-8 sm:mt-16">Willkommen</h1>
          <h1 class="font-pixelsplitter text-[24px] sm:text-[48px] lg:text-[68px] ">zu</h1>
      
        <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_scaled.png" 
        alt="Linguexplorer" 
        class="w-[30%] sm:w-[50%] mb-4 sm:mb-10" />

      <!-- Button direkt unter dem Linguexplorer-Logo -->
        <!-- Character Image - neben dem "Jetzt spielen" button -->
          <button class="hover-button">
            <router-link to="/anmelden" class="nav-link"  href="#">
            <img src="@/assets/xx_Images/xx_Images/Buttons/button play now.png" 
            alt="Jetzt spielen" 
            class="max-h-[40px] sm:max-h-[80px] hover:opacity-80" />
            </router-link>
          </button>

          <img src="@/assets/xx_Images/xx_Images/Charactee New.png" alt="Character" 
          class="w-[140px] sm:w-[370px] absolute z-1 translate-x-[-25vw] translate-y-[24vh] sm:translate-x-[-35vw] sm:translate-y-[61.5vh]" />
      <!--</section>-->
    

    <!-- Gras Bild -->
    <section class="bg-grass-background bg-cover relative z-0 mt-[-37px] sm:mt-[20px]">
 
    <ul class="list-none mx-9 sm:mx-36 my-36 flex flex-col items-center ">
      <li class="w-[65%] sm:w-5/6 flex flex-col items-center justify-center
      bg-[#f6f5f1] mt-0 sm:mt-32 ">

      <div class="flex flex-row items-center justify-center gap-2 m-8">
        <h2 class="font-pixelsplitter text-xl sm:text-3xl">Was ist</h2>
        <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" alt="Logo" class="w-1/3" />
        <h2 class="font-pixelsplitter text-xl sm:text-3xl">?</h2>
      </div>
      <p class="font-vcr text-left px-20 pt-8 text-[14px] sm:text-[20px] sm:px-40 pt-4 pb-8">
        linguExplorer ist eine innovative Lernanwendung, die Schülerinnen und Schüler auf eine unterhaltsame Reise
        durch die englische Sprache mitnimmt. Diese Reise ist speziell darauf ausgelegt, den Herausforderungen der
        Fremdsprachenerlernung entgegenzutreten, die viele Jugendliche in ihrer Schullaufbahn bis zur Matura erleben.
      </p>
      </li>
    <li class="w-[65%] sm:w-5/6 flex flex-col items-center justify-center
    bg-[#f6f5f1] mt-24">

      <h2 class="font-pixelsplitter text-center px-14 pt-14 text-xl sm:text-3xl">Ziele des Spiels:</h2>
      <p class="font-vcr text-left px-20 pt-8 text-[14px] sm:text-[20px] sm:px-40 pt-4 pb-8">
        Das primäre Ziel von linguExplorer ist es, die Motivation der Spieler zu steigern, indem sie auf spielerische
        Weise die englische Sprache erlernen. Das Spiel verbindet Lernen und Spaß, um den Schülern zu helfen, ihre
        Englischkenntnisse langfristig zu verbessern. Durch die interaktiven und herausfordernden Aufgaben soll nicht
        nur das Lernen gefördert, sondern auch ein tieferes Verständnis und ein besseres Gefühl für die Sprache
        vermittelt werden.
      </p>
      <p class="font-vcr text-left px-20 pt-8 text-[14px] sm:text-[20px] sm:px-40 pt-4 pb-8" >
        linguExplorer soll den Nutzern die Möglichkeit geben, durch regelmäßige Interaktion mit der Sprache ihr
        Selbstvertrauen und ihre Fähigkeiten im Umgang mit Englisch zu stärken, und dies auf eine Weise, die im
        traditionellen Unterricht oft fehlt.
      </p>
  </li>
  <li class="w-[65%] sm:w-5/6 flex flex-col items-center justify-center
  bg-[#f6f5f1] mt-24">

      <h2 class="font-pixelsplitter text-center px-14 pt-14 text-xl sm:text-3xl">Gameplay:</h2>
      <p class="font-vcr text-left px-20 pt-8 text-[14px] sm:text-[20px] sm:px-40 pt-4 pb-8">
        linguExplorer ist ein Singleplayer-Lernspiel, das auf der Kombination von Spielspaß und Bildung basiert, ein
        sogenanntes Serious Game. Das Hauptziel des Spiels ist es, Englischkenntnisse durch das Erforschen einer
        offenen Spielwelt zu verbessern. Die Spieler können sich auf einer Map bewegen und verschiedene Orte
        besuchen, die jeweils unterschiedliche Themenbereiche und sprachliche Herausforderungen repräsentieren.
      </p>
      <p class="font-vcr text-left px-20 pt-8 text-[14px] sm:text-[20px] sm:px-40 pt-4 pb-8">
        An diesen Orten stehen dem Spieler eine Vielzahl von Minispielen zur Verfügung, die den Fokus auf spezifische
        sprachliche Fertigkeiten legen.
      </p> 
      <p class="font-vcr text-left px-20 pt-8 text-[14px] sm:text-[20px] sm:px-40 pt-4 pb-8">
        Zusätzlich bietet das Spiel ein Phrasenheft als hilfreiches Werkzeug. Hier
        werden alle während des Spiels erlernten Vokabeln und Phrasen gesammelt. Dieses Phrasenheft steht den
        Spielern jederzeit zur Verfügung und ermöglicht es ihnen, Inhalte nachzuschlagen und ihren Lernfortschritt
        zu verfolgen.
      </p>
  </li>
  </ul>
  </section>

</main>
    
<footer id="footer" class="bg-[#99b305] text-black text-xl">
    <section class="max-w-full text-[14px] sm:text-[20px] mx-10 sm:mx-20 p-4 flex justify-between items-center">
        <nav class="flex flex-row gap-4" aria-label="footer">
          <router-link to="/impressum"  href="#">
            <a href="#" class="hover:opacity-90 font-vcr">Impressum</a>
          </router-link>
          <router-link to="/datenschutz"  href="#">
            <a href="#" class="hover:opacity-90 font-vcr">Datenschutzerklärung</a>
          </router-link>
        </nav>
        <a href="#top" class="hover:opacity-90 font-vcr">nach oben</a>
    </section>
</footer>


</div>
    
</template>

<script>

export default {
  name: 'SpielbeschreibungPage',
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

