<template>
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
      <p>team@linguexplorer.com</p>
      <h2>Urheberrecht:</h2>
      <p>
        Die Inhalte dieser Website sind urheberrechtlich geschützt. Jegliche Verwertung, die nicht vom Urheberrecht gedeckt ist, bedarf der schriftlichen Zustimmung des Betreibers.
      </p>
      <h2>Haftung für Links:</h2>
      <p>
        Trotz sorgfältiger inhaltlicher Kontrolle übernehmen wir keine Haftung für die Inhalte externer Links. Für den Inhalt der verlinkten Seiten sind ausschließlich deren Betreiber verantwortlich. Sollten Sie rechtswidrige Inhalte auf verlinkten Seiten bemerken, informieren Sie uns bitte, damit wir die Links umgehend entfernen können.
      </p>
      <!-- Button -->
        <div class="button-container">
      <button class="custom-button">
        <img src="@/assets/xx_Images/xx_Images/Buttons/button zrk.png" alt="Button" />
      </button>
    </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ImpressumPage',
  };
  </script>
  
  <style scoped>
  .content {
    padding: 20px 20%; /* Innenabstand oben/unten 20px, links/rechts 40px */
    text-align: left; /* Text linksbündig */
  }
  
  /* Spezifische Stile für die Seite */
  h1 {
    margin-bottom: 15px; /* Abstand unter dem Titel */
    font-size: 22px; /* Größe des Haupttitels */
  }
  h2 {
    margin-top: 20px; /* Abstand über den Untertiteln */
    font-size: 20px; /* Größe der Untertitel */
  }
  p {
    margin-bottom: 10px; /* Abstand unter den Absätzen */
  }
  ul {
  list-style-type: none; 
  padding: 0; 
  margin: 0; 
}
  
/* Button */
.button-container {
  position: fixed; /* Positioniert den Button relativ zum Browserfenster */
  bottom: 20px; /* Abstand vom unteren Rand */
  right: 20px; /* Abstand vom rechten Rand */
}

.custom-button {
  background: none; /* Entfernt die Standard-Hintergrundfarbe */
  border: none; /* Entfernt den Standardrahmen */
  padding: 0; /* Entfernt das Padding */
}

.custom-button img {
  width: 180px; /* Setze die Breite des Buttons (kann nach Bedarf angepasst werden) */
  height: auto; /* Höhe automatisch basierend auf dem Bildverhältnis */
}

  /* Globales CSS */
  body {
    background-color: #f6f5f1; 
    color: black; 
    margin: 0; 
    padding: 0; 
    font-family: Arial, sans-serif; 
  }
  </style>
  