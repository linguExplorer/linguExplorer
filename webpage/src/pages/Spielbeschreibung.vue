<template>
  <div class="page-wrapper">
    <!-- Nav Bar -->
    <nav class="navbar">
      <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark hell.png" alt="Logo" class="logo" />
      <button class="nav-button">
        <img src="@/assets/xx_Images/xx_Images/Buttons/button jzt spielen.png" alt="Jetzt spielen" />
      </button>
      <img src="@/assets/xx_Images/xx_Images/menue.png" alt="Menü" class="menu-icon" />
    </nav>

    <!-- Content -->
    <div class="container">
      <h1>Wilkommen</h1>
      <h1>zu</h1>
      <h2>Geschichte:</h2>
      <p>
        Linguexplorer ist eine innovative Lernanwendung, die Schülerinnen und Schüler auf eine unterhaltsame Reise
        durch die englische Sprache mitnimmt. Diese Reise ist speziell darauf ausgelegt, den Herausforderungen der
        Fremdsprachenerlernung entgegenzutreten, die viele Jugendliche in ihrer Schullaufbahn bis zur Matura erleben.
      </p>
      <h2>Gameplay:</h2>
      <p>
        Linguexplorer ist ein Singleplayer-Lernspiel, das auf der Kombination von Spielspaß und Bildung basiert, ein
        sogenanntes Serious Game. Das Hauptziel des Spiels ist es, Englischkenntnisse durch das Erforschen einer
        offenen Spielwelt zu verbessern. Die Spieler können sich auf einer Map bewegen und verschiedene Orte
        besuchen, die jeweils unterschiedliche Themenbereiche und sprachliche Herausforderungen repräsentieren.
      </p>
      <p>
        An diesen Orten stehen dem Spieler eine Vielzahl von Minispielen zur Verfügung, die den Fokus auf spezifische
        sprachliche Fertigkeiten legen. Zusätzlich bietet das Spiel ein Phrasenheft als hilfreiches Werkzeug. Hier
        werden alle während des Spiels erlernten Vokabeln und Phrasen gesammelt. Dieses Phrasenheft steht den
        Spielern jederzeit zur Verfügung und ermöglicht es ihnen, Inhalte nachzuschlagen und ihren Lernfortschritt
        zu verfolgen.
      </p>
      <h2>Ziele des Spiels:</h2>
      <p>
        Das primäre Ziel von Linguexplorer ist es, die Motivation der Spieler zu steigern, indem sie auf spielerische
        Weise die englische Sprache erlernen. Das Spiel verbindet Lernen und Spaß, um den Schülern zu helfen, ihre
        Englischkenntnisse langfristig zu verbessern. Durch die interaktiven und herausfordernden Aufgaben soll nicht
        nur das Lernen gefördert, sondern auch ein tieferes Verständnis und ein besseres Gefühl für die Sprache
        vermittelt werden.
      </p>
      <p>
        Linguexplorer soll den Nutzern die Möglichkeit geben, durch regelmäßige Interaktion mit der Sprache ihr
        Selbstvertrauen und ihre Fähigkeiten im Umgang mit Englisch zu stärken, und dies auf eine Weise, die im
        traditionellen Unterricht oft fehlt.
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SpielbeschreibungPage',
};
</script>

<style scoped>
/* Nav Bar */
.navbar {
  display: flex;
  align-items: center;
  background-color: #99b305;
  padding: 10px 0;
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1; /* Ensures nav bar is above content */
}

.logo {
  height: 35px;
  margin-left: 20px;
  margin-right: auto;
}

.nav-button {
  background: none;
  border: none;
  margin-right: 15px;
}

.nav-button img {
  height: 40px;
}

.menu-icon {
  height: 25px;
  margin-right: 20px;
}

/* Content Container */
.container {
  padding: 60px 20px 20px; 
}

/* Headings */
h1 {
  font-family: 'Pixelsplitter', Arial, sans-serif;
  font-size: 45px;
  color: black;
  margin-bottom: 20px;
  text-align: center;
}

h2 {
  margin-top: 20px;
  font-size: 24px;
  color: #333;
}

p {
  margin-bottom: 15px;
  color: #555;
  line-height: 1.6;
}

/* Global Styles */
body {
  background-color: #f6f5f1;
  color: #333;
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
}

@font-face {
  font-family: 'Pixelsplitter';
  src: url('@/assets/fonts/pixelsplitter/PixelSplitter-Bold.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

.page-wrapper {
    background-color: #f6f5f1;
    min-height: 100vh;
    color: black;
    margin: 0; 
    padding: 0;
  }

</style>




