<template>
        <header class="bg-[#99b305] text-black sticky top-0 z-10 w-full">
            <section class="w-full py-2 flex justify-between items-center px-4">
            <img 
            src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" 
            alt="Logo" 
            class="w-1/8 max-w-[150px] ml-4 sm:w-1/3 sm:max-w-[200px]" />
            </section>
        </header>

    <div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-center">

        <!-- Überschrift -->
        <section class="flex flex-col justify-center items-center gap-[20px] min-w-[700px] sm:min-w-[350px] sm:px-4">
            <h1 class="font-pixelsplitter text-[28px] mb-6 sm:text-[40px]">Passwort vergessen?</h1>

            <p class="text-[14px] font-vcr text-center sm:text-[18px]">Gib hier die E-Mail-Adresse ein, um dein <br> Passwort zurückzusetzen</p>
            
            <!-- Eingabeformular -->
            <form @submit.prevent="submit" class="min-w-[600px] max-w-md flex flex-col  justify-center items-center mt-[30px]">
                <div class="input-group flex flex-col mb-4 ">
                    <label for="email">E-Mail:</label>
                    <input type="email" id="email" v-model="data.email" required 
                    class="font-vcr bg-white border-[#9cb405] border-[2px] p-1 sm:p-2 sm:text-sm min-w-[200px]"/>
                </div>
                
                <!-- Passwort-Button -->
                <button type="submit" class="w-1/3 mt-6 text-centersm:w-full">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button neues passwort.png" 
                    alt="Neues Passwort" 
                    class="max-w-[120px] sm:max-w-[200px] w-full"
                    />
                </button>
            </form>
        </section>
    
    </div>
</template>

<script>
import { reactive } from 'vue';
import { useRouter} from 'vue-router';
export default {
    name: 'ForgotPassword',
    
    setup() {
    const data = reactive({
      email:''
    });
    const router = useRouter()

    const  submit = async() => {
      await fetch('http://localhost:8000/password_reset/', {
               method: 'POST',
               headers: {'Content-Type': 'application/json'},
               credentials: 'include',
               body: JSON.stringify(data)
    });

    await router.push({ 
    path: '/anmelden', 
    query: { success: 'true'} 
    });
    
    }

    return {
      data,
      submit
    }
},
    data() {
        return {
            email: ''
        };
    },
    methods: {
        handlePasswordReset() {
            console.log('Passwort zurücksetzen für:', this.email);
        }
    }
};
</script>
