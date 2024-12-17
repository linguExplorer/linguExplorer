<template>
  <div class="min-h-screen w-full bg-[#f6f5f1]">

    <!-- Header -->
    <header class="bg-[#99b305] text-black sticky top-0 z-10">
      <section class="max-w-full mx-auto py-2 flex justify-between items-center lg:px-6 md:px-6 sm:px-6 px-4">
        <!-- Logo -->
        <img 
          src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" 
          alt="Logo" 
          class="lg:max-h-[50px] md:max-h-[45px] sm:max-h-[35px] max-h-[35px]" 
        />
        <div class="flex justify-center items-center space-x-2 sm:space-x-4">
          <!-- Button -->
          <button class="nav-button-spielbeschreibung hover-button">
            <img 
              src="@/assets/xx_Images/xx_Images/Buttons/button jzt spielen.png" 
              alt="Jetzt spielen" 
              class="lg:max-h-[50px] md:max-h-[45px] sm:max-h-[35px] max-h-[35px] px-2" 
            />
          </button>
        </div>
      </section>
    </header>

    <!-- <main class="max-w-[95%] sm:max-w-6xl md:max-w-[80%] lg:max-w-6xl mx-auto mt-[40px] sm:mt-[80px] px-10 sm:px-1">-->
      
    <!-- Main Content -->
    <main class="max-w-[95%] sm:max-w-6xl mx-auto mt-[40px] sm:mt-[80px] px-10 sm:px-1">
      <!-- Title -->
      <h1 class="font-pixelsplitter text-[20px] sm:text-[50px] text-center">Impressum</h1>

      <!-- Content -->
      <div class="content-datenschutz mt-6 sm:mt-10 text-sm sm:text-base leading-relaxed mx-auto sm:mx-[150px] lg:mx-[250px]">
        <p class="mb-6">
          Informationspflicht laut Offenlegungspflicht laut §25 Mediengesetz
        </p>
        
        <!-- Website Team -->
        <h2 class="font-bold mt-6">Websitebetreiber-Team:</h2>
        <p>Alexander Nems</p>
        <p>Thuy Tien Luong</p>
        <p>Benjamin Bician</p>
        <p>Helena Stindl</p>
        <p>Britta Reinwart</p>

        <!-- Content -->
        <h2 class="font-bold mt-6">Inhalt:</h2>
        <p>
          Diese Website dient zur Bereitstellung von Informationen über das Projekt "Linguexplorer", das im Rahmen einer Diplomarbeit an der HTL-Rennweg im Schuljahr 2024/25 entwickelt wurde. Linguexplorer ist eine Singleplayer-Webapplikation, die als Lernspiel konzipiert wurde und Schülerinnen und Schülern hilft, auf spielerische Weise die englische Sprache zu erlernen.
        </p>

        <!-- Address -->
        <h2 class="font-bold mt-6">Adresse:</h2>
        <p>Rennweg 89b, 1030 Wien</p>

        <!-- Contact -->
        <h2 class="font-bold mt-6">Kontaktdaten:</h2>
        <p class="underline">team@linguexplorer.com</p>

        <!-- Copyright -->
        <h2 class="font-bold mt-6">Urheberrecht:</h2>
        <p>
          Die Inhalte dieser Website sind urheberrechtlich geschützt. Jegliche Verwertung, die nicht vom Urheberrecht gedeckt ist, bedarf der schriftlichen Zustimmung des Betreibers.
        </p>

        <!-- Liability for Links -->
        <h2 class="font-bold mt-6">Haftung für Links:</h2>
        <p>
          Trotz sorgfältiger inhaltlicher Kontrolle übernehmen wir keine Haftung für die Inhalte externer Links. Für den Inhalt der verlinkten Seiten sind ausschließlich deren Betreiber verantwortlich. Sollten Sie rechtswidrige Inhalte auf verlinkten Seiten bemerken, informieren Sie uns bitte, damit wir die Links umgehend entfernen können.
        </p>
      </div>
    </main>

    <!-- Footer -->
    <footer class="mt-auto">
      <section class="mx-auto p-4 flex justify-end items-center">
        <button class="max-w-[100px] sm:max-w-[150px] lg:max-w-[200px] hover-button">
          <router-link to="/" href="#">
            <img src="@/assets/xx_Images/xx_Images/Buttons/button zrk.png" alt="Zurück" />
          </router-link>
        </button>
      </section>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'ImpressumPage',
};
</script>

<style scoped>
/* Hover animation */
.hover-button img {
  transition: transform 0.2s ease-in-out;
}

.hover-button img:hover {
  transform: scale(1.05);
}
</style>
