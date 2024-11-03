<template>
    <div class="page-wrapper">
        <div class="header">
            <div class="register-container">
                <div class="register-text">
                    <p>Hast du schon einen Account?</p>
                </div>
                <button class="register-button" @click="goToLogin">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button anmelden green.png" alt="Anmelden" />
                </button>
            </div>
        </div>

        <div class="welcome-container">
            <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="left-cloud-top" />
            <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="left-cloud-bottom" />
            <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="sun-image" />
            <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="right-cloud" />
            <div class="welcome-text">
                <h1>Registrieren</h1>
            </div>
        </div>

        <div class="content-box">
            <form @submit.prevent="handleRegister">
                <div class="input-group">
                    <label for="email">E-Mail</label>
                    <input type="email" id="email" v-model="email" required />
                    <p v-if="emailError" class="error-message">Benutzer mit dieser E-Mail existiert bereits.</p>
                </div>
                <div class="input-group">
                    <label for="username">Benutzername</label>
                    <input type="text" id="username" v-model="username" required />
                    <p v-if="usernameError" class="error-message">Benutzername bereits vergeben.</p>
                </div>
                <div class="input-group">
                    <label for="password">Passwort (mind. 8 Zeichen)</label>
                    <input type="password" id="password" v-model="password" required minlength="8" />
                    <p v-if="passwordError" class="error-message">Passwort entspricht nicht den Anforderungen.</p>
                </div>
                <div class="input-group">
                    <label for="confirmPassword">Passwort wiederholen</label>
                    <input type="password" id="confirmPassword" v-model="confirmPassword" required minlength="8" />
                    <p v-if="confirmPasswordError" class="error-message">Passwörter stimmen nicht überein.</p>
                </div>
                
                <!-- Speicher-Button -->
                <button type="submit" class="submit-button">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button registrieren blue.png" alt="Registrieren" />
                </button>
            </form>
        </div>

        <div class="button-container">
            <button class="custom-button" @click="goBack">
                <img src="@/assets/xx_Images/xx_Images/Buttons/button zrk.png" alt="Zurück" />
            </button>
        </div>
    </div>
</template>

<script>
import '../styles/Registrieren.css';
export default {
    name: 'Registrieren',
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
