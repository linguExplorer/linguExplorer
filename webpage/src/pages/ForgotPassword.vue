<template>
        <header class="bg-[#99b305] text-black sticky top-0 z-10 w-full">
            <section class="w-full py-2 flex justify-between items-center px-4">
            <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" alt="Logo" class="w-1/8 max-w-[200px] ml-4" />
            </section>
        </header>

    <div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-center">

        <!-- Überschrift -->
        <section class="flex flex-col justify-center items-center gap-[20px] min-w-[700px]">
            <h1 class="font-pixelsplitter text-[40px] mb-6">Passwort vergessen?</h1>
        
     
            <!--<p class="subtext">Gib hier die E-Mail-Adresse ein, um dein Passwort zurückzusetzen</p>-->
            <!--<p class="subtext">instargam:</p>-->

            <p class="font-size:18px font-vcr text-center">Gib hier die E-Mail-Adresse ein, um dein <br> Passwort zurückzusetzen</p>
            
            <!-- Eingabeformular -->
            <form @submit.prevent="submit" class="min-w-[600px] max-w-md flex flex-col  justify-center items-center mt-[30px]">
                <div class="input-group flex flex-col mb-4 ">
                    <label for="email">E-Mail:</label>
                    <input type="email" id="email" v-model="data.email" required class="
                    font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2"/>
                </div>
                
                <!-- Passwort-Button -->
                <button type="submit" class="w-1/3 mt-6 ">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button neues passwort.png" alt="Neues Passwort"  />
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
