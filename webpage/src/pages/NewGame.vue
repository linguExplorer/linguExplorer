<template>
    <div class="new-game-page">
      <h1>Neues Spiel</h1>
    </div>
  </template>
  
  <script>
  export default {
    name: 'NewGame',
    data() {
      return {
        gameName: '',
      };
    },
  };
  </script>
  
  <style scoped>
  .new-game-page {
    padding: 20px;
  }
  </style>
  