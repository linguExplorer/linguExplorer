<template>
  <div class="page-wrapper">
    <!-- Nav Bar -->
    <nav class="navbar">
      <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark dunkel.png" alt="Logo" class="logo" />
      <button class="nav-button">
        <img src="@/assets/xx_Images/xx_Images/Buttons/button jzt spielen.png" alt="Jetzt spielen" />
      </button>
      <img src="@/assets/xx_Images/xx_Images/menue.png" alt="Menü" class="menu-icon" />
    </nav>

    <!-- Content -->
    <div class="container">

      <div class="welcome-container">
        <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="sun-image-s" />
        <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="left-cloud-s" />
        <div class="welcome-text">
          <h1>Willkommen</h1>
          <h1>zu</h1>
        </div>
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="right-cloud-s" />
      </div>

      <div class="linguexplorer-logo">
        <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark.png" alt="Linguexplorer" class="linguexplorer-image" />
      </div>

      <!-- Button direkt unter dem Linguexplorer-Logo -->
      <div class="container1">
        <!-- Character Image - positioned near the "Jetzt spielen" button -->
        <img src="@/assets/xx_Images/xx_Images/Charactee New.png" alt="Character" class="character-image" />

        <button class="play-button">
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
      <div class="image-between">
        <h2>Was ist</h2>
        <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark hell.png" alt="Logo" class="between-image" />
        <h2>?</h2>
      </div>
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
import '../styles/Spielbeschreibung.css';
export default {
  name: 'SpielbeschreibungPage',
};
</script>

