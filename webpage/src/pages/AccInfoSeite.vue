<template>
<div
  v-if="isLoading"
  class="fixed inset-0 flex justify-center items-center bg-white bg-opacity-50 z-50"
></div>
  
<div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-between">
    <!-- Header -->
    <header class="flex justify-center items-center mt-10 px-4 sm:mt-30 sm:px-40">
      <h1 class="font-pixelsplitter text-[20px] sm:text-[40px] mb-6 text-left">Accountverwaltung</h1>
    </header>

    <!-- Rechts oben Button -->
    <div class="absolute top-4 right-4">
      <button class="hover-button">
        <router-link to="/afterlogin"  href="#">
        <img
          src="@/assets/xx_Images/xx_Images/Buttons/red_X.png"
          alt="Nach oben"
          class="max-h-[40px] sm:max-h-[55px]"
        />
        </router-link>

      </button>
    </div>

    <main class="flex flex-col justify-start flex-grow px-10"> 
      <!-- Benutzername ändern -->
       <!--w: breite der box-->
      <div class="flex flex-col w-full max-w-[700px] bg-[#d5d6d8] p-4 sm:p-8 shadow-lg mt-6 sm:ml-60 mx-auto">
        <label for="username" class="font-pixelsplitter text-[15px] sm:text-[26px] mb-4 sm:mb-6">BENUTZERNAME ÄNDERN</label>
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
              class="h-[40px] sm:h-[50px] w-auto hover:opacity-80"
            />
          </button>
        </div>
      </div>



   
              

      <!-- Passwort ändern -->
      <div class="flex flex-col w-full max-w-[700px] bg-[#d5d6d8] p-4 sm:p-8 shadow-lg mt-6 sm:ml-60 mx-auto">
        <div class="flex flex-col">
          <h2 class="font-pixelsplitter text-[15px] sm:text-[26px] mb-4 sm:mb-6">PASSWORT ÄNDERN</h2>
          <p class="text-[18px] font-vcr text-start pt-5 pb-10 px-5">
      Lege ein sicheres Passwort zum Schutz deines Accounts fest. 
    </p>
  </div>

  <div class="flex items-center justify-end ">
    <button @click="changePW" class="hover-button">
      <img
        src="@/assets/xx_Images/xx_Images/Buttons/changepw.png"
        alt="Passwort speichern"
        class="h-[40px] sm:h-[50px] w-auto hover:opacity-80"
        />
    </button>
  </div>
</div>


      <!-- E-Mail ändern -->
      <div class="flex flex-col w-full max-w-[700px] bg-[#d5d6d8] p-4 sm:p-8 shadow-lg mt-6 sm:ml-60 mx-auto">
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
              class="h-[40px] sm:h-[50px] w-auto hover:opacity-80"
            />
          </button>
        </div>
      </div>
    </main>

    <footer class="mt-auto w-full flex justify-between items-center px-4 sm:px-12 py-6 sm:py-12 bg-[#f6f5f1]">
    <!-- Linker Button -->
    <div class="flex justify-start">
      <button class="hover-button max-w-[100px] sm:max-w-[150px]" @click="logout">
        <router-link to="/" href="#">
          <img src="@/assets/xx_Images/xx_Images/Buttons/abmelden.png" alt="Zurück" />
        </router-link>
      </button>
    </div>

    <!-- Rechter Button -->

  </footer>
  </div>

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
      toast.custom(shallowRef(ToastComponent_log), { duration: 3000,
    
    message: 'Dies ist eine benutzerdefinierte Toast-Nachricht!'
  });
   
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
