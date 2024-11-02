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
  /* Navigation */
  .navbar {
    display: flex;
    align-items: center; 
    background-color: #99b305; 
    padding: 10px 0; /* Innenabstand oben/unten */
    width: 100%; /* Breite */
    position: fixed; /* oberen Rand fix */
    top: 0; /* Viewport */
    left: 0; 
    right: 0; 
  }
  
  .logo {
    height: 35px; 
    margin-left: 20px; /* linker Rand */
    margin-right: auto; /* Logo + Button Abstand */
  }
  
  .nav-button {
    background: none; 
    border: none; 
    margin-right: 15px; /* Abstand zum Menü-Icon */
  }
  
  .nav-button img {
    height: 40px; 
  }
  
  .menu-icon {
    height: 25px; 
    margin-right: 20px; 
  }
  
  .content {
    padding: 70px 20%; /* Innenabstand oben/unten links/rechts */
    text-align: left;
  }
  
  @font-face {
    font-family: 'Pixelsplitter';
    src: url('@/assets/fonts/pixelsplitter/PixelSplitter-Bold.ttf') format('truetype');
    font-weight: normal;
    font-style: normal;
  }

  h1 {
    font-family: 'Pixelsplitter', Arial, sans-serif; 
    margin-bottom: 5px;
    font-size: 45px;
    text-align: center;
    color: black;
  }

  h2 {
    margin-top: 20px; 
    font-size: 20px;
  }
  p {
    margin-bottom: 10px; 
  }
  ul {
    padding: 0; 
    margin: 0; 
  }
  .no-bullets {
    list-style-type: none; /* Entfernt punkte bei liste */
  }
  
  .indented-list {
    padding-left: 20px; /* Eingerückte liste */
  }
  
  .underline {
    text-decoration: underline; 
  }
  
  /* Button */
  .button-container {
    position: fixed; 
    bottom: 20px; 
    right: 20px; 
  }
  
  .custom-button {
    background: none; 
    border: none; 
    padding: 0;
  }
  
  .custom-button img {
    width: 150px; 
    height: auto; 
  }
  
  /* Globales CSS */
  body {
    background-color: #f6f5f1; 
    color: black; 
    margin: 0; 
    padding: 0; 
    font-family: Arial, sans-serif; 
  }

  .page-wrapper {
    background-color: #f6f5f1;
    min-height: 100vh;
    color: black;
    margin: 0; 
    padding: 0;
  }
  </style>
  