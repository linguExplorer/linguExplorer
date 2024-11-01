<template>
    <div>
      <!-- Navigation Bar -->
      <nav class="navbar">
        <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark hell.png" alt="Logo" class="logo" />
        <button class="nav-button">
          <img src="@/assets/xx_Images/xx_Images/Buttons/button jzt spielen.png" alt="Jetzt spielen" />
        </button>
        <img src="@/assets/xx_Images/xx_Images/menue.png" alt="Menü" class="menu-icon" />
      </nav>
  
      <h1>Datenschutzerklärung</h1>
  
      <div class="content">
        <h2>Allgemeine Hinweise:</h2>
        <p>
          Der Schutz Ihrer persönlichen Daten ist uns sehr wichtig. Diese Datenschutzerklärung informiert Sie darüber, welche Daten wir erheben, wie wir diese verarbeiten und zu welchem Zweck sie genutzt werden. Wir behandeln Ihre personenbezogenen Daten vertraulich und entsprechend der gesetzlichen Datenschutzvorschriften sowie dieser Datenschutzerklärung.
        </p>
        <h2>Verantwortlich:</h2>
        <ul class="no-bullets">
          <li>Alexander Nems</li>
          <li>Thuy Tien Luong</li>
          <li>Benjamin Bician</li>
          <li>Helena Stindl</li>
          <li>Britta Reinwart</li>
        </ul>
        <p class="underline">team@linguexplorer.com</p>
        <p>Rennweg 89b, 1030 Wien</p>
        <h2>Erhebung und Verarbeitung personenbezogener Daten:</h2>
        <p>
          Wir erheben, verarbeiten und nutzen personenbezogene Daten (z. B. Name, E-Mail-Adresse), soweit dies für die Bereitstellung unserer Webapplikation und der damit verbundenen Services notwendig ist. Dies erfolgt in Übereinstimmung mit den geltenden Datenschutzvorschriften.
        </p>
        <h2>Folgende Daten können erhoben werden:</h2>
        <ul class="indented-list">
          <li>Name und E-Mail-Adresse</li>
          <li>Nutzungsdaten</li>
          <li>Technische Daten</li>
        </ul>
        <h2>Zweck der Datenverarbeitung:</h2>
        <p>
          Die erhobenen Daten dienen zur Bereitstellung unseres Lernspiels "Linguexplorer".
        </p>
        <h2>Speicherung der Daten:</h2>
        <p>
          Ihre Daten werden nur so lange gespeichert, wie es für die Erfüllung des jeweiligen Zwecks erforderlich ist.
        </p>
        <h2>Weitergabe der Daten:</h2>
        <p>
          Personenbezogene Daten werden nicht ohne Ihre ausdrückliche Zustimmung an Dritte weitergegeben, es sei denn, dies ist gesetzlich vorgeschrieben oder zur Vertragserfüllung notwendig.
        </p>
        <h2>Rechte der betroffenen Personen:</h2>
        <ul class="indented-list">
          <li>Auskunft über Ihre bei uns gespeicherten Daten zu erhalten.</li>
          <li>Berichtigung unrichtiger Daten zu verlangen.</li>
          <li>Löschung Ihrer Daten zu fordern.</li>
          <li>Der Verarbeitung Ihrer Daten zu widersprechen.</li>
          <li>Eine Beschwerde bei der zuständigen Datenschutzbehörde einzureichen.</li>
        </ul>
        <h2>Änderungen der Datenschutzerklärung:</h2>
        <p>
          Diese Datenschutzerklärung wird regelmäßig überprüft und aktualisiert, um den rechtlichen Anforderungen zu entsprechen oder um Änderungen in unseren Services abzubilden.
        </p>
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
  export default {
    name: 'DatenschutzPage',
  };
  </script>
  
  <style scoped>
  /* Navigation Bar Styles */
  .navbar {
    display: flex;
    align-items: center; /* Vertikal zentriert */
    background-color: #99b305; /* Hintergrundfarbe der Navigationsleiste */
    padding: 10px 0; /* Innenabstand oben/unten reduziert */
    width: 100%; /* Breite der Navigationsleiste auf 100% */
    position: fixed; /* Fest positioniert am oberen Rand */
    top: 0; /* Am oberen Rand des Viewports */
    left: 0; /* Links am Rand des Viewports */
    right: 0; /* Rechts am Rand des Viewports */
  }
  
  .logo {
    height: 35px; /* Höhe des Logos */
    margin-left: 20px; /* Abstand zum linken Rand */
    margin-right: auto; /* Platz zwischen Logo und Button */
  }
  
  .nav-button {
    background: none; /* Entfernt die Standard-Hintergrundfarbe */
    border: none; /* Entfernt den Standardrahmen */
    margin-right: 15px; /* Abstand zum Menü-Icon */
  }
  
  .nav-button img {
    height: 40px; /* Höhe des Buttons */
  }
  
  .menu-icon {
    height: 25px; /* Höhe des Menü-Icons */
    margin-right: 20px; /* Abstand zum rechten Rand */
  }
  
  .content {
    padding: 70px 20%; /* Innenabstand oben/unten 70px, links/rechts 20% */
    text-align: left; /* Text linksbündig */
  }
  
  /* Spezifische Stile für die Seite */
  h1 {
    margin-bottom: 15px; /* Abstand unter dem Titel */
    font-size: 22px; /* Größe des Haupttitels */
    color: black; /* Haupttitelfarbe */
  }
  h2 {
    margin-top: 20px; /* Abstand über den Untertiteln */
    font-size: 20px; /* Größe der Untertitel */
  }
  p {
    margin-bottom: 10px; /* Abstand unter den Absätzen */
  }
  ul {
    padding: 0; 
    margin: 0; 
  }
  .no-bullets {
    list-style-type: none; /* Entfernt die Aufzählungszeichen */
  }
  
  /* Eingerückte Listen */
  .indented-list {
    padding-left: 20px; /* Eingerückte Aufzählungslisten */
  }
  
  /* Unterstrichene E-Mail */
  .underline {
    text-decoration: underline; /* Unterstreicht den Text */
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
    width: 150px; /* Setze die Breite des Buttons (kann nach Bedarf angepasst werden) */
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
  