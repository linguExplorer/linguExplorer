<template>
  <div class="min-h-screen bg-[#f6f5f1]">

    <header class="bg-[#99b305] text-black sticky top-0 z-10">
      <section class="max-w-full mx-auto px-6 py-2 flex justify-between items-center ">
      <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" alt="Logo" class="w-1/6" />
      <div class="flex justify-center items-center space-x-4">
      <button class="nav-button-spielbeschreibung">
        <router-link to="/anmelden" class="nav-link"  href="#">
        <img src="@/assets/xx_Images/xx_Images/Buttons/button jzt spielen.png" alt="Jetzt spielen" class="max-h-[55px] px-2 hover:opacity-80" />
        </router-link>
      </button>
      <!--<button id="hamburger-button" class="text-4xl focus:outline-none cursor-pointer items-center">
                &#9776;
        </button>-->
      </div>
      </section>
    </header>
    <!-- Nav Bar -->


    <!-- Content -->
    <main class="max-w-full mx-auto">
      <section id="top" class="flex flex-col w-full justify-center items-center  bg-cloud-background bg-cover">

    
          <h1 class="font-pixelsplitter text-[68px] m-0 leading-none xxl:text-[80px] mt-20">Willkommen</h1>
              <h1 class="font-pixelsplitter text-[68px] m-0 xxl:text-[80px]  ">zu</h1>
      
        <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_scaled.png" alt="Linguexplorer" class="w-5/12 mb-10" />

      <!-- Button direkt unter dem Linguexplorer-Logo -->
        <!-- Character Image - neben dem "Jetzt spielen" button -->

          <button class="play-button">
            <router-link to="/anmelden" class="nav-link"  href="#">
            <img src="@/assets/xx_Images/xx_Images/Buttons/button play now.png" alt="Jetzt spielen" class="max-h-[110px] min-h-[80px] hover:opacity-80" />
            </router-link>
          </button>
          <img src="@/assets/xx_Images/xx_Images/Charactee New.png" alt="Character" class="w-[300px] lg:w-[380px] absolute z-1
          translate-x-[-35vw] translate-y-[35vh] xxl:translate-x-[-35vw] xxl:translate-y-[28.5vh]" />
    </section>
    

    <section class="bg-grass-background bg-cover relative z-0 mt-[20px]">
      <!-- Gras Bild unter dem Button -->
 
    <ul class="list-none mx-36 my-36 flex flex-col items-center ">
      <li class="w-4/6 sm:w-5/6 flex flex-col items-center justify-center
      bg-[#f6f5f1] mt-32">

      <div class="flex flex-row items-center justify-center gap-2 m-8">

        <h2 class="font-pixelsplitter text-3xl">Was ist</h2>
        <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" alt="Logo" class="w-1/3" />
        <h2 class="font-pixelsplitter text-3xl">?</h2>
      </div>
      <p class="text-[20px] font-vcr text-left px-40 pb-20">
        linguExplorer ist eine innovative Lernanwendung, die Schülerinnen und Schüler auf eine unterhaltsame Reise
        durch die englische Sprache mitnimmt. Diese Reise ist speziell darauf ausgelegt, den Herausforderungen der
        Fremdsprachenerlernung entgegenzutreten, die viele Jugendliche in ihrer Schullaufbahn bis zur Matura erleben.
      </p>
      </li>
    <li class="w-4/6 sm:w-5/6 flex flex-col items-center justify-center
    bg-[#f6f5f1] mt-24">

      <h2 class="font-pixelsplitter text-3xl text-center px-14 pt-14">Ziele des Spiels:</h2>
      <p class="text-[20px] font-vcr text-left px-40 pt-8">
        Das primäre Ziel von linguExplorer ist es, die Motivation der Spieler zu steigern, indem sie auf spielerische
        Weise die englische Sprache erlernen. Das Spiel verbindet Lernen und Spaß, um den Schülern zu helfen, ihre
        Englischkenntnisse langfristig zu verbessern. Durch die interaktiven und herausfordernden Aufgaben soll nicht
        nur das Lernen gefördert, sondern auch ein tieferes Verständnis und ein besseres Gefühl für die Sprache
        vermittelt werden.
      </p>
      <p class="text-[20px] font-vcr text-left px-40 pb-20" >
        linguExplorer soll den Nutzern die Möglichkeit geben, durch regelmäßige Interaktion mit der Sprache ihr
        Selbstvertrauen und ihre Fähigkeiten im Umgang mit Englisch zu stärken, und dies auf eine Weise, die im
        traditionellen Unterricht oft fehlt.
      </p>
  </li>
  <li class="w-4/6 sm:w-5/6 flex flex-col items-center justify-center
  bg-[#f6f5f1] mt-24">

      <h2 class="font-pixelsplitter text-3xl text-center px-14 pt-14">Gameplay:</h2>
      <p class="text-[20px] font-vcr text-left px-40 pt-8 ">
        linguExplorer ist ein Singleplayer-Lernspiel, das auf der Kombination von Spielspaß und Bildung basiert, ein
        sogenanntes Serious Game. Das Hauptziel des Spiels ist es, Englischkenntnisse durch das Erforschen einer
        offenen Spielwelt zu verbessern. Die Spieler können sich auf einer Map bewegen und verschiedene Orte
        besuchen, die jeweils unterschiedliche Themenbereiche und sprachliche Herausforderungen repräsentieren.
      </p>
      <p class="text-[20px] font-vcr text-left px-40 pt-8 pb-8 ">
        An diesen Orten stehen dem Spieler eine Vielzahl von Minispielen zur Verfügung, die den Fokus auf spezifische
        sprachliche Fertigkeiten legen.
      </p> 
      <p class="text-[20px] font-vcr text-left px-40 pb-20 ">
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
    <section class="max-w-full mx-20 p-4 flex justify-between items-center">
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

