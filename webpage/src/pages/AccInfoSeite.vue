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
        <div class="flex justify-end">
          <button @click="editUsername" class="hover-button">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/aenderungenSpeichern.png"
              alt="Benutzername speichern"
              class="h-[150px] w-auto hover:opacity-80"
            />
          </button>
        </div>
      </div>

      <!-- Passwort ändern -->
      <div class="flex flex-col w-[700px] max-w-5xl bg-[#d5d6d8] p-8 shadow-lg mt-6 mr-60">
        <h2 class="font-pixelsplitter text-[26px] mb-4">PASSWORT ÄNDERN</h2>

        <div class="input-group flex flex-col mb-4">
          <label for="password" class="text-[16px]">altes Passwort</label>
          <input
            type="password"
            id="password"
            v-model="password"
            class="font-vcr bg-white border-[#9cb405] border-2 min-w-[500px] p-2"
            required
          />
          <p v-if="passwordError" class="text-red-500 mt-2">
            {{ passwordError }}
          </p>
        </div>

        <div class="input-group flex flex-col mb-4">
          <label for="newPassword" class="text-[16px]">neues Passwort (mind. 8 Zeichen)</label>
          <input
            type="password"
            id="newPassword"
            v-model="newPassword"
            class="font-vcr bg-white border-[#9cb405] border-2 min-w-[500px] p-2"
            required
          />
        </div>

        <div class="input-group flex flex-col mb-4">
          <label for="confirmPassword" class="text-[16px]">Passwort wiederholen</label>
          <input
            type="password"
            id="confirmPassword"
            v-model="confirmPassword"
            class="font-vcr bg-white border-[#9cb405] border-2 min-w-[500px] p-2"
            required
          />
        </div>
        <div class="flex justify-end">
          <button @click="editPassword" class="hover-button">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/aenderungenSpeichern.png"
              alt="Passwort speichern"
              class="h-[150px] w-auto hover:opacity-80"
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
        <div class="flex justify-end">
          <button @click="editEmail" class="hover-button">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/aenderungenSpeichernVerifizieren.png"
              alt="E-Mail speichern"
              class="h-[150px] w-auto hover:opacity-80"
            />
          </button>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
export default {
  name: "AccInfoSeite",
  data() {
    return {
      username: "maxmust123",
      password: "",
      newPassword: "",
      confirmPassword: "",
      email: "max.mustermann@gmail.com",
    };
  },
  methods: {
    editUsername() {
      alert("Benutzername geändert!");
    },
    editPassword() {
      alert("Passwort geändert!");
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
