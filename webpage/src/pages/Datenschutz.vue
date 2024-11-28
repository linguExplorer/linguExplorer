<template>
  <div class="min-h-screen bg-[#f6f5f1]">

    <header class="bg-[#99b305] text-black sticky top-0 z-10">
      <section class="max-w-full mx-auto px-6 py-2 flex justify-between items-center ">
      <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" alt="Logo" class="w-1/6" />
      <div class="flex justify-center items-center space-x-4">
      <button class="nav-button-spielbeschreibung">
        <img src="@/assets/xx_Images/xx_Images/Buttons/button jzt spielen.png" alt="Jetzt spielen" class="max-h-[55px] px-2" />
      </button>
      <!--<button id="hamburger-button" class="text-4xl focus:outline-none cursor-pointer items-center">
                &#9776;
        </button>-->
      </div>
      </section>
    </header>
      
      <main class="max-w-6xl mx-auto mt-[80px]">

      <h1 class="font-pixelsplitter text-[50px]">Datenschutzerklärung</h1>
  
      <div class="content-datenschutz  mx-[200px] mt-10">
        <h2 class="font-bold">Allgemeine Hinweise:</h2>
        <p class="mb-6">
          Der Schutz Ihrer persönlichen Daten ist uns sehr wichtig. Diese Datenschutzerklärung informiert Sie darüber, welche Daten wir erheben, wie wir diese verarbeiten und zu welchem Zweck sie genutzt werden. Wir behandeln Ihre personenbezogenen Daten vertraulich und entsprechend der gesetzlichen Datenschutzvorschriften sowie dieser Datenschutzerklärung.
        </p>
        <h2 class="font-bold">Verantwortlich:</h2>
        <ul class="no-bullets">
          <li>Alexander Nems</li>
          <li>Thuy Tien Luong</li>
          <li>Benjamin Bician</li>
          <li>Helena Stindl</li>
          <li>Britta Reinwart</li>
        </ul>
        <p class="underline mt-6">team@linguexplorer.com</p>
        <p class="mt-6">Rennweg 89b, 1030 Wien</p>
        <h2 class="font-bold mt-6">Erhebung und Verarbeitung personenbezogener Daten:</h2>
        <p>
          Wir erheben, verarbeiten und nutzen personenbezogene Daten (z. B. Name, E-Mail-Adresse), soweit dies für die Bereitstellung unserer Webapplikation und der damit verbundenen Services notwendig ist. Dies erfolgt in Übereinstimmung mit den geltenden Datenschutzvorschriften.
        </p>
        <h2 class="font-bold mt-6">Folgende Daten werden erhoben:</h2>
        <ul class="indented-list list-disc px-6">
          <li>Name und E-Mail-Adresse</li>
          <li>Nutzungsdaten</li>
          <li>Technische Daten</li>
        </ul>
        <h2 class="font-bold mt-6">Verwendung von Cookies:</h2>
        <p>
          Beim Besuch unserer Website wird Ihnen die Verwendung von Cookies angezeigt, und Sie werden gebeten, Ihre Zustimmung zur Nutzung dieser Cookies zu erteilen. 
        </p>
        <h2 class="font-bold mt-6">Zweck der Datenverarbeitung:</h2>
        <p>
          Die erhobenen Daten dienen zur Bereitstellung unseres Lernspiels "linguExplorer" und werden für Bereitstellung, Aufrechterhaltung und Verwaltung Ihres Nutzerprofils benötigt. Im Zuge dessen sind diese Daten verpflichtend um unsere Applikation zu verwenden.
        </p>
        <h2 class="font-bold mt-6">Speicherung der Daten:</h2>
        <p>
          Ihre Daten werden temporär gespeichert. Sobald Sie Ihr Profil löschen, wird Ihr Account und Ihre Daten in einem Zeitraum von 7 Tagen gelöscht.
        </p>
        <h2 class="font-bold mt-6">Weitergabe der Daten:</h2>
        <p>
          Personenbezogene Daten werden nicht ohne Ihre ausdrückliche Zustimmung an Dritte weitergegeben, es sei denn, dies ist gesetzlich vorgeschrieben oder zur Vertragserfüllung notwendig.
        </p>
        <h2 class="font-bold mt-6">Rechte der betroffenen Personen:</h2>
        <p>Sie haben das Recht: </p>
        <ul class="indented-list">
          <li>Auskunft über Ihre bei uns gespeicherten Daten zu erhalten.</li>
          <li>Berichtigung unrichtiger Daten zu verlangen.</li>
          <li>Löschung Ihrer Daten zu fordern.</li>
          <li>Der Verarbeitung Ihrer Daten zu widersprechen.</li>
          <li>Eine Beschwerde bei der zuständigen Datenschutzbehörde einzureichen.</li>
        </ul>
        <h2 class="font-bold mt-6">Änderungen der Datenschutzerklärung:</h2>
        <p>
          Diese Datenschutzerklärung wird regelmäßig überprüft und aktualisiert, um den rechtlichen Anforderungen zu entsprechen oder um Änderungen in unseren Services abzubilden. Falls eine Änderung durchgeführt wurde, wird Ihnen dies auf unserer Seite erkenntlich gemacht.
        </p>
      </div>
      </main>

      <footer class="mt-auto">
        <section class="mx-auto p-4 flex  justify-end items-center">
        
            <button class="max-w-[200px] p-4" @click="goBack">
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
    name: 'DatenschutzPage',
  };
  </script>

  