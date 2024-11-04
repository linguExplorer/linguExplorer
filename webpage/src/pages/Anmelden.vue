<template>
    <div class="page-wrapper-anmelden anmelden-page">
        <div class="header-anmelden">
            <div class="container-anmelden">
                <div class="text-anmelden">
                    <p>Hast du noch keinen Account?</p>
                </div>
                <button class="button-anmelden" @click="goToRegister">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button registrieren green.png" alt="Registrieren" />
                </button>
            </div>
        </div>

        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="left-cloud-bottom-anmelden" />
        <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="sun-image-anmelden" />
        <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="left-cloud-top-anmelden" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="right-cloud-middle-anmelden" />

        <!--<div class="anmelde-container">-->
            <div class="ueberschrift-anmelden">
                <h1>Anmelden</h1>
            </div>
        <!--</div>-->

        <div class="content-box-anmelden">
            <form @submit.prevent="handleLogin">
                <div class="input-group-anmelden">
                    <label for="email">E-Mail</label>
                    <input type="email" id="email" v-model="username" required />
                </div>
                <div class="input-group-anmelden">
                    <label for="password">Passwort</label>
                    <input type="password" id="password" v-model="password" required />
                </div>
                <!-- Fehlermeldung -->
                <p v-if="showError" class="error-message-anmelden">Ungültige E-Mail oder Passwort</p>

                <button type="submit" class="submit-button-anmelden">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button anmelden blue.png" alt="Anmelden" />
                </button>
                <div class="forgot-password-anmelden">
                    <p>Passwort vergessen?</p>
                </div>
            </form>
        </div>

        <div class="button-container-anmelden">
            <button class="back-button-anmelden" @click="goBack">
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
