<template>
<div
  v-if="isLoading"
  class="fixed inset-0 flex justify-center items-center bg-white bg-opacity-50 z-50"
></div>
  
<div class="w-full bg-[#f6f5f1]">
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
      <div class="font-vcr m-0 text-black lg:w-[1100px] md:w-[800px] sm:w-[300px] w-[300px] bg-[#f6f5f1] min-h-screen flex flex-col justify-left">
      <!-- Header -->
      <header class="flex lg:mt-30 md:mt-20 sm:mt-10 mt-10 lg:px-40 md:px-20 sm:px-4 px-4">
        <h1 class="font-pixelsplitter lg:text-[40px] md:text-[30px] sm:text-[20px] text-[20px] mb-6 text-left">Accountverwaltung</h1>
      </header>

      <!-- Hauptbereich -->
      <main class="flex flex-col justify-start flex-grow lg:px-40 md:px-20 sm:px-4 px-4"> 
        <!-- Benutzername ändern -->
        <!--w: breite der box-->
        <div class="flex flex-col w-full bg-[#d5d6d8] lg:p-8 md:p-6 sm:p-4 p-4 shadow-lg mt-6">
          <label for="username" class="font-pixelsplitter lg:text-[26px] md:text-[20px] sm:text-[15px] text-[15px] lg:mb-6 md:mb-5 sm:mb-4 mb-4">BENUTZERNAME ÄNDERN</label>
          <input
            type="text"
            id="username"
            v-model="username"
            class="p-2 bg-white border-[#9cb405] border-2 w-[90%] sm:w-full mb-4 text-sm sm:text-base"
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
                class="lg:h-[50px] md:h-[45px] sm:h-[40px] h-[40px] w-auto hover:opacity-80"
              />
            </button>
          </div>
        </div>

      <!-- Passwort ändern -->
      <div class="flex flex-col w-full bg-[#d5d6d8] lg:p-8 md:p-6 sm:p-4 p-4 shadow-lg mt-6">
        <div class="flex flex-col">
          <h2 class="font-pixelsplitter lg:text-[26px] md:text-[20px] sm:text-[15px] text-[15px] lg:mb-6 md:mb-5 sm:mb-4 mb-4">PASSWORT ÄNDERN</h2>
          <p class="lg:text-[18px] md:text-[18px] text-[12px] font-vcr text-start pt-5 pb-10 px-5">
      Lege ein sicheres Passwort zum Schutz deines Accounts fest. 
    </p>
  </div>

  <div class="flex justify-end">
            <button @click="changePW" class="hover-button">
              <img
                src="@/assets/xx_Images/xx_Images/Buttons/aenderungenSpeichern.png"
                alt="Passwort speichern"
                class="lg:h-[50px] md:h-[45px] sm:h-[40px] h-[40px] w-auto hover:opacity-80"
              />
            </button>
          </div>
        </div>


      <!-- E-Mail ändern -->
      <div class="flex flex-col w-full bg-[#d5d6d8] p-4 sm:p-8 shadow-lg mt-6">
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
</div>
  <footer class="mt-auto w-full flex justify-between items-center px-4 sm:px-12 py-6 sm:py-12 bg-[#f6f5f1]">
    <!-- Linker Button -->
    <div class="flex justify-start">
      <button class="hover-button max-w-[100px] sm:max-w-[150px]" @click="logout">
        <router-link to="/" href="#">
          <img src="@/assets/xx_Images/xx_Images/Buttons/abmelden.png" alt="Zurück" />
        </router-link>
      </button>
    </div>


  </footer>
</template>


<script>
import { onMounted , ref, reactive} from 'vue';
import { useRouter } from "vue-router";
import { Toaster, toast } from 'vue-sonner'
import { shallowRef } from 'vue';

import ToastComponent from '@/components/ToastComponent.vue';
import ToastComponent_abg from '@/components/ToastComponent_abg.vue';
import ToastComponent_log from '@/components/ToastComponent_log.vue';
import ToastComponent_pwCh from '@/components/ToastComponent_pwCh.vue';
import ToastComponent_uch from '@/components/ToastComponent_uch.vue';

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
        const response = await fetch('https://da.linguexplorer.com/api/user', {
          headers: {'Content-Type': 'application/json'},
          credentials: 'include'
        });
        if (response.ok) { 
          const content = await response.json();
      username.value = content.name;
      email.value = content.email;

        } else {
          await router.push("/");
          toast.custom(shallowRef(ToastComponent_abg), { duration: 3000,
    
    message: 'Dies ist eine benutzerdefinierte Toast-Nachricht!'
  });
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
          const res = await fetch("https://da.linguexplorer.com/api/update-username", {
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
            toast.custom(shallowRef(ToastComponent_uch), { duration: 3000,
    
    message: 'Dies ist eine benutzerdefinierte Toast-Nachricht!'
  });

          }

        } catch (error) {
          console.error("Fehler beim Senden der Anfrage:", error);
        }
      };

    
      const  changePW = async() => {
        isLoading.value = true;

        try {
       const res = await fetch('https://da.linguexplorer.com/password_reset/', {
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
      const res = await fetch('https://da.linguexplorer.com/api/logout', {
               method: 'POST',
               headers: {'Content-Type': 'application/json'},
               credentials: 'include',
    });
    if (res.ok) {
      const err = await res.json();
      await router.push("/");

      window.location.reload();


   
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
