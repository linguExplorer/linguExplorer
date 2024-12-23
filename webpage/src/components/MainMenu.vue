<template>
    <div class="main-menu">
      <header>
        <img src="@/assets/logo.png" alt="Spiel Logo" class="logo" />
      </header>
  
      <div class="buttons">
        <button @click="startNewGame">Neues Spiel</button>
        <button @click="loadGame">Spielstand laden</button>
        <button @click="openSettings">Optionen</button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "MainMenu",
    methods: {
      startNewGame() {
        // Logik für neues Spiel
        this.$router.push('/game');  // zur Spiel-Seite wechseln
      },
      loadGame() {
        // Logik Laden eines Spielstands
        const savedGame = this.loadSavedGame();
        if (savedGame) {
          this.$router.push('/game');  // Spiel laden
        } else {
          alert('Kein Spielstand vorhanden');
        }
      },
      loadSavedGame() {
        //  gespeicherten Daten laden
        //  wie?
        return null; // Kein gespeichertes Spiel
      },
      openSettings() {
        this.$router.push('/settings');  // zu den Einstellungen wechseln
      },
    },
  };
  </script>
  