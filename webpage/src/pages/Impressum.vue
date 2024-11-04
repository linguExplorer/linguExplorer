<template>
  <div class="page-wrapper">
    <div>
      <!-- Nav Bar -->
      <nav class="navbar">
        <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark hell.png" alt="Logo" class="logo" />
        <button class="nav-button">
          <img src="@/assets/xx_Images/xx_Images/Buttons/button jzt spielen.png" alt="Jetzt spielen" />
        </button>
        <img src="@/assets/xx_Images/xx_Images/menue.png" alt="Menü" class="menu-icon" />
      </nav>
  
      <h1>Impressum</h1>

      <div class="content">
        <p>Informationspflicht laut Offenlegungspflicht laut §25 Mediengesetz</p>
        <h2>Websitebetreiber-Team:</h2>
        <ul>
          <li>Alexander Nems</li>   
          <li>Thuy Tien Luong</li>
          <li>Benjamin Bician</li>
          <li>Helena Stindl</li>
          <li>Britta Reinwart</li>
        </ul>
        <h2>Inhalt:</h2>
        <p>
          Diese Website dient zur Bereitstellung von Informationen über das Projekt "Linguexplorer", das im Rahmen einer Diplomarbeit an der HTL-Rennweg im Schuljahr 2024/25 entwickelt wurde. Linguexplorer ist eine Singleplayer-Webapplikation, die als Lernspiel konzipiert wurde und Schülerinnen und Schülern hilft, auf spielerische Weise die englische Sprache zu erlernen.
        </p>
        <h2>Adresse:</h2>
        <p>Rennweg 89b, 1030 Wien</p>
        <h2>Kontaktdaten:</h2>
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
      <!-- Button -->
      <div class="button-container">
          <button class="custom-button">
            <img src="@/assets/xx_Images/xx_Images/Buttons/button zrk.png" alt="Button" />
          </button>
        </div>
    </div>
  </div>
  </template>
  
  <script>
  import '../styles/Impressum.css';
  export default {
    name: 'ImpressumPage',
  };
  </script>
  
