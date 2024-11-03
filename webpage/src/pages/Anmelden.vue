<template>
    <div class="page-wrapper">
        <div class="header">
            <div class="register-container">
                <div class="register-text">
                    <p>Hast du noch keinen Account?</p>
                </div>
                <button class="register-button" @click="goToRegister">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button registrieren green.png" alt="Registrieren" />
                </button>
            </div>
        </div>
        <div class="anmelde-container">
            <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="left-cloud-top" />
            <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="left-cloud-bottom" />
            <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="sun-image" />
            <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="right-cloud-middle" />
            <div class="welcome-text">
                <h1>Anmelden</h1>
            </div>
        </div>

        <div class="content-box">
            <form @submit.prevent="handleLogin">
                <div class="input-group">
                    <label for="email">E-Mail</label>
                    <input type="email" id="email" v-model="username" required />
                </div>
                <div class="input-group">
                    <label for="password">Passwort</label>
                    <input type="password" id="password" v-model="password" required />
                </div>
                <!-- Fehlermeldung -->
                <p v-if="showError" class="error-message">Ungültige E-Mail oder Passwort</p>

                <button type="submit" class="submit-button">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button anmelden blue.png" alt="Anmelden" />
                </button>
                <div class="forgot-password">
                    <p>Passwort vergessen?</p>
                </div>
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
import '../styles/Anmelden.css';
export default {
    name: 'AnmeldePage',
    data() {
        return {
            username: '',
            password: ''
        };
    },
    methods: {
        handleLogin() {
            console.log('Anmelden mit:', this.username, this.password);
        },
        goBack() {
            this.$router.go(-1);
        },
        goToRegister() {
            this.$router.push('/register');
        }
    }
};
</script>
