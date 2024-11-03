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

      <div class="welcome-container">
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="left-cloud" />
        <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="sun-image" />
        <div class="welcome-text">
          <h1>Willkommen</h1>
          <h1>zu</h1>
        </div>
        <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="right-cloud" />
      </div>

      <div class="linguexplorer-logo">
        <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark.png" alt="Linguexplorer" class="linguexplorer-image" />
      </div>

      <!-- Button direkt unter dem Linguexplorer-Logo -->
      <div class="linguexplorer-button">
        <button class="nav-button">
          <img src="@/assets/xx_Images/xx_Images/Buttons/button play now.png" alt="Jetzt spielen" class="play-button-image" />
        </button>
      </div>

      <!-- Gras Bild unter dem Button -->
      <!--<div class="grass-image-container">
        <img src="@/assets/xx_Images/xx_Images/gras small patch.png" alt="Gras" class="grass-image" />
      </div> -->

    <div class="grass-background">

    <!-- Boxes for content -->
    <div class="content-box">
      <h2>Was ist</h2>
      <h2>?</h2>
      <p>
        Linguexplorer ist eine innovative Lernanwendung, die Schülerinnen und Schüler auf eine unterhaltsame Reise
        durch die englische Sprache mitnimmt. Diese Reise ist speziell darauf ausgelegt, den Herausforderungen der
        Fremdsprachenerlernung entgegenzutreten, die viele Jugendliche in ihrer Schullaufbahn bis zur Matura erleben.
      </p>
    </div>

    <div class="content-box">
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

    <div class="content-box">
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
    </div>
  </div>

    
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

.container {
  width: 100%; /* Breite auf 100% setzen */
  padding: 30px 0 20px; /* Nur oben und unten Padding */
  box-sizing: border-box; /* Box-Sizing auf Border-Box setzen */
}

.welcome-container {
  display: flex;
  align-items: flex-end; /* Align items to the bottom */
  justify-content: center; 
  margin-bottom: 20px; 
  position: relative; /* Allow absolute positioning of children */
}

/* Sun behind the left cloud */
.sun-image {
  width: 100px; 
  height: auto;
  position: absolute; 
  left: 10%; 
  bottom: 40px; 
  z-index: 0; 
}

/* Left Cloud */
.left-cloud {
  width: 230px; 
  height: auto;
  position: absolute; 
  left: 9%; 
  bottom: 0; 
  z-index: 1; 
}

/* Right Cloud */
.right-cloud {
  width: 230px; 
  height: auto;
  position: absolute; 
  right: 9%; 
  bottom: 0; 
}

/* Welcome Text */
.welcome-text {
  display: flex;
  flex-direction: column; /* Text untereinander anordnen */
  align-items: center; /* Text zentrieren */
}

h1 {
  font-family: 'Pixelsplitter', Arial, sans-serif;
  font-size: 50px;
  color: black;
  margin: 0 10px 10px; 
  text-align: center;
}

/* Headings */
h2 {
  font-family: 'Pixelsplitter', Arial, sans-serif;
  margin-top: 10px; 
  font-size: 24px;
  color: black;
}

p {
  margin-bottom: 15px;
  color: black;
  line-height: 1.6;
  font-family: 'VCR OSD Mono', monospace; 
}

/* Global Styles */

@font-face {
  font-family: 'Pixelsplitter';
  src: url('@/assets/fonts/pixelsplitter/PixelSplitter-Bold.ttf') format('truetype');
  font-weight: normal;
  font-style: normal;
}

@font-face {
  font-family: 'VCR OSD Mono'; 
  src: url('@/assets/fonts/vcr_osd_mono/VCR_OSD_MONO_1.001.ttf') format('truetype'); 
  font-weight: normal;
  font-style: normal;
}

html, body {
  height: 100%; /* Sicherstellen, dass der Body die volle Höhe hat */
  margin: 0; /* Margen auf 0 setzen */
  padding: 0; /* Polsterung auf 0 setzen */
}

body {
  background-color: #f6f5f1; /* Hintergrundfarbe */
  color: #333;
  font-family: Arial, sans-serif;
}

.page-wrapper {
    background-color: #f6f5f1; /* Hintergrundfarbe */
    min-height: 100vh; /* Sicherstellen, dass die Seite mindestens die Höhe des Viewports hat */
    color: black;
    margin: 0; /* Keine Margen */
    padding: 0; /* Keine Polsterung */
    width: 100%; /* Breite auf 100% setzen */
    box-sizing: border-box; /* Box-Sizing auf Border-Box setzen */
}


.linguexplorer-logo {
  text-align: center; 
  margin: 20px 0; 
}

.linguexplorer-image {
  width: 80%; 
  max-width: 500px; 
}

.linguexplorer-button {
  display: flex;
  justify-content: center; 
  margin: 100px 0; 
}

.play-button-image {
  height: 200px; 
  width: auto;
}

/* Spacer for additional spacing */
.spacer {
  height: 60px; /* Adjust height as needed for spacing */
}


/* grass hintergrund*/
.grass-background {
  background-image: url('@/assets/xx_Images/xx_Images/gras full.png'); 
  background-size: cover; 
  background-position: center;
  padding: 60px 0 20px; 
  position: relative; 
  min-height: 100vh; 
}

.grass-image-container {
  text-align: center; 
  margin: 0; 
  padding: 0; 
  position: relative; 
}

.grass-image {
  width: 80%; 
  max-width: 400px; 
  z-index: 1; 
}

/* Content Box Styles */
.content-box {
  background-color: rgba(246, 245, 241, 0.9); 
  padding: 20px;
  margin: 30px auto; 
  max-width: 800px; 
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); 
  text-align: center; 
  position: relative; 
  z-index: 2; 
}

</style>
