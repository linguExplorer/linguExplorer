<template>
  <div class="min-h-screen bg-[#f6f5f1]">

    <header class="bg-[#99b305] text-black sticky top-0 z-10">
      <section class="max-w-full mx-auto px-6 py-2 flex justify-between items-center ">
      <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" alt="Logo" class="w-1/6" />
      <div class="flex justify-center items-center space-x-4">
      <button class="nav-button-spielbeschreibung">
        <img src="@/assets/xx_Images/xx_Images/Buttons/button jzt spielen.png" alt="Jetzt spielen" class="max-h-[55px] px-2" />
      </button>
      <button id="hamburger-button" class="text-4xl focus:outline-none cursor-pointer items-center">
                &#9776;
        </button>
      </div>
      </section>
    </header>
    <main class="max-w-6xl mx-auto mt-[80px]">

      <h1 class="font-pixelsplitter text-[50px]">Impressum</h1>

      <div class="content-datenschutz  mx-[250px] mt-10">
        <p class="text-center">Informationspflicht laut Offenlegungspflicht laut §25 Mediengesetz</p>
        <h2 class="font-bold mt-6">Websitebetreiber-Team:</h2>
        <ul>
          <li>Alexander Nems</li>   
          <li>Thuy Tien Luong</li>
          <li>Benjamin Bician</li>
          <li>Helena Stindl</li>
          <li>Britta Reinwart</li>
        </ul>
        <h2 class="font-bold mt-6">Inhalt:</h2>
        <p>
          Diese Website dient zur Bereitstellung von Informationen über das Projekt "Linguexplorer", das im Rahmen einer Diplomarbeit an der HTL-Rennweg im Schuljahr 2024/25 entwickelt wurde. Linguexplorer ist eine Singleplayer-Webapplikation, die als Lernspiel konzipiert wurde und Schülerinnen und Schülern hilft, auf spielerische Weise die englische Sprache zu erlernen.
        </p>
        <h2 class="font-bold mt-6">Adresse:</h2>
        <p>Rennweg 89b, 1030 Wien</p>
        <h2 class="font-bold mt-6">Kontaktdaten:</h2>
        <p class="underline">team@linguexplorer.com</p>
        <h2>Urheberrecht:</h2>
        <p>
          Die Inhalte dieser Website sind urheberrechtlich geschützt. Jegliche Verwertung, die nicht vom Urheberrecht gedeckt ist, bedarf der schriftlichen Zustimmung des Betreibers.
        </p>
        <h2>Haftung für Links:</h2>
        <p>
          Trotz sorgfältiger inhaltlicher Kontrolle übernehmen wir keine Haftung für die Inhalte externer Links. Für den Inhalt der verlinkten Seiten sind ausschließlich deren Betreiber verantwortlich. Sollten Sie rechtswidrige Inhalte auf verlinkten Seiten bemerken, informieren Sie uns bitte, damit wir die Links umgehend entfernen können.
        </p>
      </div>

    </main>
    
    <footer class="mt-auto">
        <section class="mx-auto p-4 flex  justify-end items-center">
        
            <button class="max-w-[200px] p-4" @click="goBack">
              <router-link to="/"  href="#">

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
  
