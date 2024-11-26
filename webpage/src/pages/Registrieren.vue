<template>
    <div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-center">
        <header class="bg-[#99b305] text-black sticky top-0 z-10 w-full">
         <section class="w-full py-2 flex justify-between items-center px-4">
            <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" alt="Logo" class="w-1/8 max-w-[200px] ml-4" />
            <div class="flex justify-center items-center space-x-4">
                <p class=" font-size:18px font-vcr">Hast du schon einen Account?</p>
            
                    <button class=" max-w-[150px]">
                        <router-link to="/anmelden" class="nav-link"  href="#">

                    <img src="@/assets/xx_Images/xx_Images/Buttons/button anmelden green.png" alt="Registrieren" class=" hover:opacity-80" />
                        </router-link>
                    </button>
            </div>
            </section>
        </header>

        <main class="max-w-6xl mx-auto mt-[80px]">
            <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="min-w-[320px] xxl:min-w-[380px] absolute z-2 translate-x-[-42vw] translate-y-[-12vh] 
            xxl:translate-x-[-42vw] xxl:translate-y-[-11vh]" />
            <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="min-w-[150px] xxl:min-w-[180px] absolute z-1 translate-x-[-38vw] translate-y-[-3vh]
            xxl:translate-x-[-38vw] xxl:translate-y-[-3vh]" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="min-w-[320px] xxl:min-w-[380px] absolute z-2 translate-x-[-45vw] translate-y-[7vh]
        xxl:translate-x-[-45vw] xxl:translate-y-[5vh]" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="min-w-[320px] xxl:min-w-[380px] absolute z-1 translate-x-[30vw] translate-y-[-8vh]
        xxl:translate-x-[30vw] xxl:translate-y-[-8vh]" />
    
        <!--<div class="anmelde-container">-->
            <section class="flex flex-col justify-center items-center gap-[20px] min-w-[700px]">
        <h1 class="font-pixelsplitter text-[60px] mb-6">Registrieren</h1>

            <form @submit.prevent="submit" class="min-w-[600px] max-w-md flex flex-col  items-center mt-[30px]">
                <div class="input-group flex flex-col mb-4 ">
                    <label for="email" class="">E-Mail</label>
                <input type="email" id="email" v-model="data.email" required class="
                font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2" />
                <p v-if="errorMessage" class="text-red-500 mt-2">
                    {{ errorMessage }}
                </p>
                </div>
                <div class="input-group flex flex-col mb-4 ">
                    <label for="uname" class="">Benutzername</label>
                <input type="uname" id="email" v-model="data.name" required class="
                font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2" />
                    <p v-if="usernameError" class="error-message">Benutzername bereits vergeben.</p>
                </div>
                <div class="input-group flex flex-col mb-4 ">
                    <label for="password" class="">Passwort</label>
                <input type="password" id="password" v-model="data.password" required minlength="8" class="
                 font-vcr bg-white border-[#9cb405] border-[2px]  min-w-[500px] p-2" />
                    <p v-if="passwordError" class="error-message">Passwort entspricht nicht den Anforderungen.</p>
                </div>
                <div class="input-group flex flex-col mb-4 ">
                    <label for="password" class="">Passwort Wiederholen</label>
                <input type="password" id="password" v-model="confirmPassword" minlength="8" required class="
                 font-vcr bg-white border-[#9cb405] border-[2px]  min-w-[500px] p-2 " />
                    <p v-if="errorPass" class="text-red-500 mt-2"> 
                    {{ errorPass }}
                </p>
                </div>
                <div class="inline-flex items-center pt-5">
                        <input type="checkbox"  v-model ="data.checked" id="check-with-link" class=" h-5 w-5 cursor-pointer"/>
                        <label class="cursor-pointer ml-2 font-vcr text-black text-sm" for="check-with-link">
                        <p class="  text-[15px]">Ich akzeptiere die Linguexplorer
                            <router-link to="/datenschutz"  href="#">
                        <a href="#" class=" hover:text-[#99b305]  underline">
                            Datenschutzerklärung
                        </a>
                        </router-link>
                        
                    </p>
                        </label>
                </div>
                <p v-if="!data.checked" class="text-red-500 mt-2">Bitte akzeptieren Sie die Datenschutzerklärung um fortzufahren!</p>


                <!-- Speicher-Button -->
                <button type="submit" class="max-w-[150px] mt-6 w-full" :disabled="!isFormValid">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button registrieren blue.png" alt="Registrieren" class="hover:opacity-80" />
                </button>
            </form>
        </section>
       
    </main>

    <footer class="mt-auto">
        <section class="mx-auto p-4 flex  justify-end items-center">
            <button class="max-w-[200px] p-4" >
                <router-link to="/" class="nav-link"  href="#">
                <img src="@/assets/xx_Images/xx_Images/Buttons/button zrk.png" alt="Zurück" class="hover:opacity-80"/>
                </router-link>
            </button>
        </section>
    </footer>
    </div>
</template>

<script>
import { reactive, ref, computed} from 'vue';
import { useRouter } from 'vue-router';
export default {
    name: 'Registrieren',
    setup() {
         const data = reactive({
            name: '',
            email: '',
            password: ''
         });
         const router = useRouter()
         const errorMessage = ref('');
         const errorPass = ref('');
         const confirmPassword = ref('');

         const isFormValid = computed(() => {
        if (data.password !== confirmPassword.value) {
                errorPass.value = 'Passwörter stimmen nicht überein.';
                return false;
            } else {
                errorPass.value = '';
                return true;

            }
        });

    const submit = async() => {
        try {
   const res = await fetch('http://localhost:8000/api/register', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
   });
   if(!res.ok) {
    const err = await res.json();
    if (err.email.length > 0) {
            if (err.email[0].includes('user')) {
                 errorMessage.value = 'User mit dieser Email existiert bereits';
            }
                
          } else {
            errorMessage.value = 'Ein unbekannter Fehler ist aufgetreten.';
          }
   } else {
    await router.push({ 
    path: '/anmelden', 
    query: { reg: 'true'} 
    });
   }
} catch (error) {
    console.error('Fehler beim Senden der Anfrage:', error);
}
   

}
return{
            data,
            submit,
            errorMessage,
            errorPass,
            isFormValid,
            confirmPassword
         }
    },
    data() {
        return {
            email: '',
            username: '',
            password: '',
            emailError: false,
            usernameError: false,
            passwordError: false,
            confirmPasswordError: false,
            checked: false,
        };
    },
    methods: {
        handleRegister() {
            console.log('Registrieren mit:', this.email, this.username, this.password);
        },
        goBack() {
            this.$router.go(-1);
        },
        goToLogin() {
            this.$router.push('/login');
        }
    }
};
</script>

