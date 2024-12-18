<template>
  <!-- Rechts oben Button -->
  <div class="absolute top-4 right-4">
      <button class="hover-button">
        <router-link to="/afterlogin"  href="#">
        <img
          src="@/assets/xx_Images/xx_Images/Buttons/red_X.png"
          alt="Nach oben"
          class="lg:max-h-[55px] md:max-h-[49px] sm:max-h-[40px] max-h-[40px]"
        />
        </router-link>

      </button>
    </div>
    
  <div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-between">
    <!-- Header -->
    <header class="flex justify-center lg:mt-30 md:mt-20 sm:mt-10 mt-10 lg:px-40 md:px-20 sm:px-4 px-4">
      <h1 class="font-pixelsplitter lg:text-[40px] md:text-[30px] sm:text-[20px] text-[20px] mb-6 text-left">Accountverwaltung</h1>
    </header>

    <!-- Hauptbereich -->
    <main class="flex flex-col justify-start flex-grow px-10"> 
      <!-- Benutzername ändern -->
       <!--w: breite der box-->
      <div class="flex flex-col w-full max-w-[700px] bg-[#d5d6d8] lg:p-8 md:p-6 sm:p-4 p-4 shadow-lg mt-6 sm:ml-60 mx-auto">
        <label for="username" class="font-pixelsplitter lg:text-[26px] md:text-[20px] sm:text-[15px] text-[15px] lg:mb-6 md:mb-5 sm:mb-4 mb-4">BENUTZERNAME ÄNDERN</label>
        <input
          type="text"
          id="username"
          v-model="username"
          class="p-2 bg-white border-[#9cb405] border-2 w-[90%] sm:w-full mb-4 text-sm sm:text-base"
          placeholder="maxmust123"
        />
        <div class="flex justify-end">
          <button @click="changeUser" class="hover-button">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/aenderungenSpeichern.png"
              alt="Benutzername speichern"
              class="lg:h-[50px] md:h-[45px] sm:h-[40px] h-[40px] w-auto hover:opacity-80"
            />
          </button>
        </div>
      </div>


      <!-- Passwort ändern -->
      <div class="flex flex-col w-full max-w-[700px] bg-[#d5d6d8] lg:p-8 md:p-6 sm:p-4 p-4 shadow-lg mt-6 sm:ml-60 mx-auto">
        <h2 class="font-pixelsplitter lg:text-[26px] md:text-[20px] sm:text-[15px] text-[15px] lg:mb-6 md:mb-5 sm:mb-4 mb-4">PASSWORT ÄNDERN</h2>

        <div class="input-group flex flex-col mb-4">
          <label for="password" class="lg:text-[16px] md:text-[15px] sm:text-[14px] text-[14px]">altes Passwort</label>
          <input
            type="password"
            id="password"
            v-model="password"
            class="p-2 bg-white border-[#9cb405] border-2 w-[90%] sm:w-full mb-4 text-sm sm:text-base"
            required
          />
          <p v-if="passwordError" class="text-red-500 mt-2">
            {{ passwordError }}
          </p>
        </div>

        <div class="input-group flex flex-col mb-4">
          <label for="newPassword" class="lg:text-[16px] md:text-[15px] sm:text-[14px] text-[14px]"
            >Neues Passwort (mind. 8 Zeichen)</label
          >
          <input
            type="password"
            id="newPassword"
            v-model="newPassword"
            class="p-2 bg-white border-[#9cb405] border-2 w-[90%] sm:w-full mb-4 text-sm sm:text-base"
            required
          />
        </div>

        <div class="input-group flex flex-col mb-4">
          <label for="confirmPassword" class="lg:text-[16px] md:text-[15px] sm:text-[14px] text-[14px]"
            >Passwort wiederholen</label
          >
          <input
            type="password"
            id="confirmPassword"
            v-model="confirmPassword"
            class="p-2 bg-white border-[#9cb405] border-2 w-[90%] sm:w-full mb-4 text-sm sm:text-base"
            required
          />
        </div>
        <div class="flex justify-end">
          <button @click="editPassword" class="hover-button">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/aenderungenSpeichern.png"
              alt="Passwort speichern"
              class="lg:h-[50px] md:h-[45px] sm:h-[40px] h-[40px] w-auto hover:opacity-80"
            />
          </button>
        </div>
      </div>

      <!-- E-Mail ändern -->
      <div class="flex flex-col w-full max-w-[700px] bg-[#d5d6d8] p-4 sm:p-8 shadow-lg mt-6 sm:ml-60 mx-auto">
        <label
          for="email"
          class="font-pixelsplitter lg:text-[26px] md:text-[20px] sm:text-[15px] text-[15px] lg:mb-6 md:mb-5 sm:mb-4 mb-4">E-MAIL ÄNDERN
        </label>
        <input
          type="email"
          id="email"
          v-model="email"
          class="p-2 bg-white border-[#9cb405] border-2 w-[90%] sm:w-full mb-4 text-sm sm:text-base"
          placeholder="max.mustermann@gmail.com"
        />
        <div class="flex justify-end">
          <button @click="editEmail" class="hover-button">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/aenderungenSpeichernVerifizieren.png"
              alt="E-Mail speichern"
              class="lg:h-[50px] md:h-[45px] sm:h-[40px] h-[40px] w-auto hover:opacity-80"
            />
          </button>
        </div>
      </div>
    </main>
  </div>
  <footer class="mt-auto w-full flex justify-between items-center px-4 sm:px-12 py-6 sm:py-12 bg-[#f6f5f1]">
    <!-- Linker Button -->
    <div class="flex justify-start">
      <button class="hover-button max-w-[100px] sm:max-w-[150px]" @click="goBack">
        <router-link to="/" href="#">
          <img src="@/assets/xx_Images/xx_Images/Buttons/abmelden.png" alt="Zurück" />
        </router-link>
      </button>
    </div>

    <!-- Rechter Button -->
    <div class="flex justify-end">
      <button class="hover-button max-w-[100px] sm:max-w-[150px]" @click="goBack">
        <router-link to="/" href="#">
          <img src="@/assets/xx_Images/xx_Images/Buttons/Loeschen.png" alt="Zurück" />
        </router-link>
      </button>
    </div>
  </footer>
</template>


<script>

import { onMounted , ref, reactive} from 'vue';

export default {
  name: "AccInfoSeite",

  setup() {
    const username = ref(""); 
    const email = ref("max.mustermann@gmail.com");
    const UserError = ref("");


 

    onMounted(async () => {
      
        const response = await fetch('http://localhost:8000/api/user', {
          headers: {'Content-Type': 'application/json'},
          credentials: 'include'
        });

      const content = await response.json();
      username.value = content.name;
      email.value = content.email;
    
  });

  const changeUser = async () => {
    try {
          const res = await fetch("http://localhost:8000/api/update-username", {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify({ name: username.value }),
          });
          if (!res.ok) {
            const err = await res.json();
            if (err.error) {
            UserError.value = err.error;             
            }
          } 

        } catch (error) {
          console.error("Fehler beim Senden der Anfrage:", error);
        }
      };

 
  return {

    username,
    email,
    changeUser,
    UserError
  }
  },

  data() {
    return {
      password: "",
      newPassword: "",
      confirmPassword: "",
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
