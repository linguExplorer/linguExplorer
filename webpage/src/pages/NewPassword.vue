<template>
    <div class="bg-[#f6f5f1] w-full justify-center min-h-screen text-black justify-center">

        <!-- Header -->
      <header class="bg-[#99b305] text-black sticky top-0 z-10 w-full">
        <section class="w-full py-2 flex justify-between items-center px-4">
          <img 
            src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" 
            alt="Logo" 
            class="w-1/8 max-w-[150px] ml-4 sm:w-1/3 sm:max-w-[200px]" />
        </section>
      </header>
        
      <div class="flex flex-col justify-center items-center px-4 sm:px-6 text-center mt-24">
            <!-- Überschrift -->
            <section class="w-full max-w-[500px] flex flex-col justify-center items-center gap-6">
                <h1 class="font-pixelsplitter text-[25px] mb-4 sm:text-[40px] sm:whitespace-nowrap">
                    Neues Passwort erstellen
                </h1>
            

                <!-- Eingabeformular -->
                <form @submit.prevent="submit" class="w-full flex flex-col justify-center items-center gap-4">

                    <div class="w-full px-8 sm:px-4">
                        <label for="password" 
                        id="passwordLabel" class="font-vcr block text-left mb-2 text-[14px] sm:text-[18px]">Passwort (mind. 8 Zeichen)
                        </label>
                        <input type="password" id="password" v-model="data.password" required minlength="8" 
                        class="font-vcr bg-white border-[#9cb405] border-[2px] p-2 text-sm w-full"/>
                    </div>
                    
                    <div class="w-full px-8 sm:px-4">
                        <label for="confirmPassword" 
                        id="confirmPasswordLabel" class="font-vcr block text-left mb-2 text-[14px] sm:text-[18px]">Passwort wiederholen
                        </label>
                        <input type="password" id="confirmPassword" v-model="confirmPassword" required minlength="8" 
                        class="font-vcr bg-white border-[#9cb405] border-[2px] p-2 text-sm w-full"/>
                        <p v-if="errorMessage" class="text-red-500 mt-2">
                        {{ errorMessage }}
                    </p>
                    </div>

                    <!-- Speicher-Button -->
                    <button type="submit" class="hover-button w-full flex justify-center items-center mt-5" :disabled="!isFormValid" >
                        <img src="@/assets/xx_Images/xx_Images/Buttons/button speichern.png" alt="Speichern"
                        class="max-w-[100px] sm:max-w-[150px] w-full" />
                    </button>

                </form>
                </section>

        </div>
    </div>
</template>

<script>
import { reactive, ref, computed} from 'vue';
import { useRouter, useRoute } from 'vue-router';
export default {
    name: 'NewPassword',
    setup() {
    const data = reactive({
      password:'',
      token:''
    });

    const confirmPassword = ref('');
    const router = useRouter();
    const route = useRoute();
    data.token = route.params.ref;
    const errorMessage = ref('');


    

    const isFormValid = computed(() => {
        if (data.password !== confirmPassword.value) {
                errorMessage.value = 'Passwörter stimmen nicht überein.';
                return false;
            } else {
                errorMessage.value = '';
                return true;

            }
        });

    const  submit = async() => {
        try {


    
     const res = await fetch('http://localhost:8000/password_reset/confirm/', {
               method: 'POST',
               headers: {'Content-Type': 'application/json'},
               credentials: 'include',
               body: JSON.stringify(data)
    });
    if(!res.ok) {
    const err = await res.json();

    if (err.password.length > 0) {
            errorMessage.value = 'Das Passwort muss Buchstaben und Zeichen enthalten!'
          } else {
            errorMessage.value = 'Ein unbekannter Fehler ist aufgetreten.';
          }
   }else {
    await router.push('/anmelden');
   }
    } catch (error) {
    console.error('Fehler beim Senden der Anfrage:', error);
}
} 
    return {
      data,
      submit,
      errorMessage,
      isFormValid,
      confirmPassword
      
    }
  },    
    data() {
        return {
            password: '',

        };
    },
    methods: {
        handlePasswordSave() {
            if (this.data.password === this.confirmPassword) {
                console.log('Neues Passwort speichern:', this.password);
            } else {
                console.error('Die Passwörter stimmen nicht überein.');
            }
        }
    }
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

</style>