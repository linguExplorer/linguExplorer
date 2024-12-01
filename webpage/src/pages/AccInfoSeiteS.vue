<template>
    <div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-between">
      <!-- Header -->
      <header class="flex justify-start mt-20 px-40">
        <h1 class="font-pixelsplitter text-[40px] mb-6">Accountverwaltung</h1>
      </header>
  
      <!-- Rechts oben Button -->
      <div class="absolute top-4 right-4">
        <button class="hover-button">
          <img
            src="@/assets/xx_Images/xx_Images/Buttons/red_X.png"
            alt="Nach oben"
            class="max-h-[55px] hover:opacity-80"
          />
        </button>
      </div>
  
      <!-- Hauptbereich -->
      <main class="flex flex-col justify-start flex-grow px-20"> 
        <!-- Benutzername ändern -->
        <div class="flex flex-col w-[700px] max-w-5xl bg-[#d5d6d8] p-8 shadow-lg mt-6 mr-60"> 
          <label for="username" class="font-pixelsplitter text-[26px] mb-6">BENUTZERNAME ÄNDERN</label>
          <input
            type="text"
            id="username"
            v-model="username"
            class="p-2 bg-white border-[#9cb405] border-2 w-[450px] mb-4"
            placeholder="maxmust123"
          />
        </div>
  
        <!-- Passwort ändern -->
        <div class="flex flex-col w-[700px] max-w-5xl bg-[#d5d6d8] p-8 shadow-lg mt-6 mr-60">
        <h2 class="font-pixelsplitter text-[26px] mb-4">PASSWORT ÄNDERN</h2>

            <div class="flex items-center space-x-4 mb-4">
                <p class="font-vcr text-[16px] text-[#333]">Lege ein sicheres Passwort zum Schutz deines Accounts fest.</p>
                
                <button @click="editPassword" class="hover-button">
                <img
                    src="@/assets/xx_Images/xx_Images/Buttons/aenderungenSpeichern.png"
                    alt="Passwort speichern"
                    class="h-[50px] w-auto hover:opacity-80"
                />
                </button>
            </div>
        </div>
  
        <!-- E-Mail ändern -->
        <div class="flex flex-col w-[700px] max-w-5xl bg-[#d5d6d8] p-8 shadow-lg mt-6 mr-60"> 
          <label for="email" class="font-pixelsplitter text-[26px] mb-6">E-MAIL ÄNDERN</label>
          <input
            type="email"
            id="email"
            v-model="email"
            class="p-2 bg-white border-[#9cb405] border-2 w-[450px] mb-4"
            placeholder="max.mustermann@gmail.com"
          />
        </div>
      </main>
      <footer class="mt-auto w-full flex justify-between items-center px-12 py-12 bg-[#f6f5f1]">
      <!-- Linker Button -->
      <div class="flex justify-start">
        <button class="max-w-[150px]" @click="goBack">
          <router-link to="/" href="#">
            <img src="@/assets/xx_Images/xx_Images/Buttons/abmelden.png" alt="Zurück" />
          </router-link>
        </button>
      </div>
  
      <!-- Rechter Button -->
      <div class="flex justify-end">
        <button class="max-w-[150px]" @click="goBack">
          <router-link to="/" href="#">
            <img src="@/assets/xx_Images/xx_Images/Buttons/Loeschen.png" alt="Zurück" />
          </router-link>
        </button>
      </div>
    </footer>
  
    </div>
  </template>
  
  <script>
  export default {
    name: "AccInfoSeiteS",
    data() {
      return {
        username: "maxmust123",
        email: "max.mustermann@gmail.com",
      };
    },
    methods: {
      editUsername() {
        alert("Benutzername geändert!");
      },
      editEmail() {
        alert("E-Mail geändert!");
      },
    },
  };
  </script>
  
  <style scoped>
  .hover-button img {
    transition: opacity 0.2s ease-in-out, transform 0.2s ease-in-out;
  }
  .hover-button img:hover {
    opacity: 0.8;
    transform: scale(1.05);
  }
  
  .shadow-lg {
    border-radius: 0;
  }
  </style>
  