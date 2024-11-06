<template>
    <div class="page-wrapper-registrieren registrieren-page">
        <div class="header-registrieren">
            <div class="register-container-registrieren">
                <div class="register-text-registrieren">

                    <p>Hast du schon einen Account?</p>
                </div>
                <button class="register-button-registrieren">
                    <router-link to="/anmelden" class="nav-link"  href="#">

                    <img src="@/assets/xx_Images/xx_Images/Buttons/button anmelden green.png" alt="Anmelden" />
                    </router-link>

                </button>
            </div>
        </div>

            <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="left-cloud-top-registrieren" />
            <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="left-cloud-bottom-registrieren" />
            <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="sun-image-registrieren" />
            <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="right-cloud-middle-registrieren" />

            <div class="text-registrieren">
                <h1>Registrieren</h1>
            </div>

        <div class="content-box-registrieren">
            <form @submit.prevent="submit">
                <div class="input-group-registrieren">
                    <label for="email">E-Mail</label>
                    <input type="email" id="email" v-model="data.email" required />
                    <p v-if="emailError" class="error-message">Benutzer mit dieser E-Mail existiert bereits.</p>
                </div>
                <div class="input-group-registrieren">
                    <label for="username">Benutzername</label>
                    <input type="text" id="username" v-model="data.name" required />
                    <p v-if="usernameError" class="error-message">Benutzername bereits vergeben.</p>
                </div>
                <div class="input-group-registrieren">
                    <label for="password">Passwort (mind. 8 Zeichen)</label>
                    <input type="password" id="password" v-model="data.password" required minlength="8" />
                    <p v-if="passwordError" class="error-message">Passwort entspricht nicht den Anforderungen.</p>
                </div>
                <div class="input-group-registrieren">
                    <label for="confirmPassword">Passwort wiederholen</label>
                    <input type="password" id="confirmPassword" v-model="confirmPassword" required minlength="8" />
                    <p v-if="confirmPasswordError" class="error-message">Passwörter stimmen nicht überein.</p>
                </div>
                
                <!-- Speicher-Button -->
                <button type="submit" class="submit-button-registrieren">

                    <img src="@/assets/xx_Images/xx_Images/Buttons/button registrieren blue.png" alt="Registrieren" />
                </button>
            </form>
        </div>

        <div class="button-container-registrieren">
            <button class="custom-button-registrieren" @click="goBack">
                <img src="@/assets/xx_Images/xx_Images/Buttons/button zrk.png" alt="Zurück" />
            </button>
        </div>
    </div>
</template>

<script>
import '../styles/Registrieren.css';
import { reactive } from 'vue';
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

    const submit = async() => {
   await fetch('http://localhost:8000/api/register', {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
   });

   await router.push('/anmelden')
}
return{
            data,
            submit
         }
    },
    data() {
        return {
            email: '',
            username: '',
            password: '',
            confirmPassword: '',
            emailError: false,
            usernameError: false,
            passwordError: false,
            confirmPasswordError: false,
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

