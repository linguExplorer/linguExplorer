<template>
    <div class="page-wrapper-forgotPassword forgotPassword">

        <!-- Überschrift -->
        <div class="forgot-password">
                <h1>Passwort vergessen?</h1>
        </div>

        <div class="content-box-forgotPassword">

            <p class="subtext">Gib hier die E-Mail-Adresse ein, um dein Passwort zurückzusetzen</p>
            
            <!-- Eingabeformular -->
            <form @submit.prevent="submit">
                <div class="input-group">
                    <label for="email">E-Mail:</label>
                    <input type="email" id="email" v-model="data.email" required />
                </div>
                
                <!-- Passwort-Button -->
                <button type="submit" class="submit-button-forgotPassword">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button neues passwort.png" alt="Neues Passwort" />
                </button>
            </form>
        </div>
    </div>
</template>

<script>
import '../styles/ForgotPassword.css';
import { useRouter} from 'vue-router';
import { reactive } from 'vue';

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
    await router.push('/awaitP')


    
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
