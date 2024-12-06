<template>
       <div
      v-if="isLoading"
      class="absolute top-0 left-0 flex justify-center items-center h-screen w-full bg-white bg-opacity-50  z-50"
    ></div>
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
                    <div class="input-group flex items-center mb-4">
                    <input :type="inputType" id="password" v-model="data.password" required minlength="8" class="
                    font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2"/>
                    <button
                  @click="togglePasswordVisibility"
                  type="button"
                  class="focus:outline-none -ml-8"
                >
                  <img
                    src="@/assets/xx_Images/xx_Images/Buttons/show.png"
                    alt=""
                    class="w-4"
                  />
                </button>
              </div>
                </div>
                <div class="input-group flex flex-col mb-4 ">
                    <label for="confirmPassword" id="confirmPasswordLabel">Passwort wiederholen</label>
                    <div class="flex items-center mb-4">
                    <input :type="confirmInputType" id="confirmPassword" v-model="confirmPassword" required minlength="8" class="
                    font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2"/>
                    <button
                  @click="toggleConfirmPasswordVisibility"
                  type="button"
                  class="focus:outline-none -ml-8"
                >
                  <img
                    src="@/assets/xx_Images/xx_Images/Buttons/show.png"
                    alt=""
                    class="w-4"
                  />
                </button>
          </div>
                    <p v-if="errorMessage" class="text-red-500 mt-2">
                    {{ errorMessage }}
                </p>
                </div>

                <!-- Speicher-Button -->
                <button type="submit" class="max-w-[150px] mt-6 w-full" :disabled="!isFormValid" >
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button speichern.png" alt="Speichern" />
                </button>
            

            </form>
            </section>

    </div>
</template>

<script>
import { reactive, ref, computed} from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Toaster, toast } from 'vue-sonner'

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
    const isLoading = ref(false);


    

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
        isLoading.value = true;
        try {


    
     const res = await fetch('http://localhost:8000/password_reset/confirm/', {
               method: 'POST',
               headers: {'Content-Type': 'application/json'},
               credentials: 'include',
               body: JSON.stringify(data)
    });
    if(!res.ok) {
    
    const err = await res.json();
    toast('Link ist ungültig')
    await router.push('/');

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
      confirmPassword,
      isLoading,

      
    }
  },    
    data() {
        return {
            password: '',
            inputType: 'password',
            confirmInputType: 'password',

        };
    },
    methods: {
        handlePasswordSave() {
        
            if (this.data.password === this.confirmPassword) {
                console.log('Neues Passwort speichern:', this.password);
            } else {
                console.error('Die Passwörter stimmen nicht überein.');
            }
        },

        togglePasswordVisibility() {
        this.inputType = this.inputType === 'password' ? 'text' : 'password';
          },
          toggleConfirmPasswordVisibility() {
        this.confirmInputType = this.confirmInputType === 'password' ? 'text' : 'password';
          },
    }
};
</script>

