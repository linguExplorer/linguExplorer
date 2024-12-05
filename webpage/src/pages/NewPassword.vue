<template>
     <header class="bg-[#99b305] text-black sticky top-0 z-10 w-full">
        <section class="w-full py-2 flex justify-between items-center px-4">
            <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" alt="Logo" class="w-1/8 max-w-[200px] ml-4" />
        </section>
        </header>
        
    <div class="font-vcr m-0 p-3 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-center">
        <!-- Überschrift -->
        <section class="flex flex-col justify-center items-center gap-[-10px] min-w-[500px]">
            <h1 class="font-pixelsplitter text-[40px] mb-9 mt-0">Neues Passwort erstellen</h1>
        

            <!-- Eingabeformular -->
            <form @submit.prevent="submit" class="min-w-[600px] max-w-md flex flex-col  justify-center  items-center mt-[30px]">
                <div class="input-group flex flex-col mb-4 ">
                    <label for="password" id="passwordLabel">Passwort</label>
                    <input type="password" id="password" v-model="data.password" required minlength="8" class="
                    font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2"/>
                </div>
                
                <div class="input-group flex flex-col mb-4 ">
                    <label for="confirmPassword" id="confirmPasswordLabel">Passwort wiederholen</label>
                    <input type="password" id="confirmPassword" v-model="confirmPassword" required minlength="8" class="
                    font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2"/>
                    <p v-if="errorMessage" class="text-red-500 mt-2">
                    {{ errorMessage }}
                </p>
                </div>

                <!-- Speicher-Button -->
                <button type="submit" class="hover-button max-w-[150px] mt-6 w-full" :disabled="!isFormValid" >
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button speichern.png" alt="Speichern" />
                </button>
            

            </form>
            </section>

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