<template>
<div
  v-if="isLoading"
  class="fixed inset-0 flex justify-center items-center bg-white bg-opacity-50 z-50"
></div>
  
  <div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-between">

    <header class="flex justify-start mt-20 px-40">
      <h1 class="font-pixelsplitter text-[40px] mb-6">Accountverwaltung</h1>
    </header>


    <!-- Rechts oben Button -->
    <div class="absolute top-4 right-4">
      <button class="hover-button">
        <router-link to="/afterlogin"  href="#">
        <img
          src="@/assets/xx_Images/xx_Images/Buttons/red_X.png"
          alt="Nach oben"
          class="max-h-[55px] hover:opacity-80"
        />
        </router-link>

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
        <p v-if="UserError" class="text-red-500 mt-2">
                {{ UserError }}
              </p>
        <div class="flex justify-end">
          <button @click="changeUser" class="hover-button">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/aenderungenSpeichern.png"
              alt="Benutzername speichern"
              class="h-[150px] w-auto hover:opacity-80"
            />
          </button>
        </div>
      </div>

      <!-- Passwort ändern -->
      <div class="flex flex-row justify-between w-[700px] max-w-5xl bg-[#d5d6d8] p-8 shadow-lg mt-6 mr-60">
  <div class="flex flex-col">
    <h2 class="font-pixelsplitter text-[26px] mb-4">PASSWORT ÄNDERN</h2>
    <p class="text-[18px] font-vcr text-start pt-5 pb-10 px-5">
      Lege ein sicheres Passwort zum Schutz deines Accounts fest. 
    </p>
  </div>

  <div class="flex items-center justify-end ">
    <button @click="changePW" class="hover-button">
      <img
        src="@/assets/xx_Images/xx_Images/Buttons/changepw.png"
        alt="Passwort speichern"
        class=" w-[200px] hover:opacity-80"
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
          
          <button  class="hover-button">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/aenderungenSpeichernVerifizieren.png"
              alt="E-Mail speichern"
              class="h-[150px] w-auto hover:opacity-80"
            />
          </button>
        </div>
      </div>
    </main>
    <footer class="mt-auto">
        <section class="mx-auto p-4 flex justify-end items-center">
          <button class="max-w-[200px] p-4" @click="logout">
              <img
                src="@/assets/xx_Images/xx_Images/Buttons/abmelden.png"
                alt="Zurück"
                class="hover:opacity-80"
              />
          </button>
        </section>
      </footer>
  </div>

</template>


<script>
import { onMounted , ref, reactive} from 'vue';
import { useRouter } from "vue-router";
import { Toaster, toast } from 'vue-sonner'
import { shallowRef } from 'vue';

import ToastComponent from '@/components/ToastComponent.vue';

export default {
  name: "AccInfoSeite",
components: {
ToastComponent
},


  setup() {
    const username = ref(""); 
    const email = ref("max.mustermann@gmail.com");
    const UserError = ref("");
    const isLoading = ref(false);



 

    onMounted(async () => {
      try {
        const response = await fetch('http://localhost:8000/api/user', {
          headers: {'Content-Type': 'application/json'},
          credentials: 'include'
        });
        if (response.ok) { 
          const content = await response.json();
      username.value = content.name;
      email.value = content.email;

        } else {
          await router.push("/");
          toast('Sie sind nicht mehr angemeldet!');
        }


      
    } catch (error) {
          console.error("Fehler beim Senden der Anfrage:", error);
    }
  }

);
  const router = useRouter();

  const changeUser = async () => {
    toast.custom(shallowRef(ToastComponent), { duration: 3000,
    
    message: 'Dies ist eine benutzerdefinierte Toast-Nachricht!'
  });
   

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
          } else {
            toast('Benutzername erfolgreich geändert!');

          }

        } catch (error) {
          console.error("Fehler beim Senden der Anfrage:", error);
        }
      };

    
      const  changePW = async() => {
        isLoading.value = true;

        try {
       const res = await fetch('http://localhost:8000/password_reset/', {
               method: 'POST',
               headers: {'Content-Type': 'application/json'},
               credentials: 'include',
               body: JSON.stringify({email: email.value})
    });

if (res.ok) { 
  isLoading.value = false;

  toast(`Ein Link wurde an ${email.value} gesendet`);

} else {
  toast('Irgendwas ist schief gelaufen versuche es nochmal!');

}
         } catch (error) {
          console.error("Fehler beim Senden der Anfrage:", error);
        }
    }
 
    const  logout= async() => {
      try {
      const res = await fetch('http://localhost:8000/api/logout', {
               method: 'POST',
               headers: {'Content-Type': 'application/json'},
               credentials: 'include',
    });
    if (res.ok) {
      const err = await res.json();
      await router.push("/");
      toast('Sie haben sich abgemeldet');

    }
  } catch (error) {
          console.error("Fehler beim Senden der Anfrage:", error);
        }

    };

  return {

    username,
    email,
    changeUser,
    UserError,
    changePW,
    logout,
    isLoading
    
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
