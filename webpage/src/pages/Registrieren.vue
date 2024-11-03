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

<style scoped>
@font-face {
    font-family: 'VCR OSD Mono';
    src: url('@/assets/fonts/vcr_osd_mono/VCR_OSD_MONO_1.001.ttf') format('truetype');
    font-weight: normal;
    font-style: normal;
}

.page-wrapper {
    background-color: #f6f5f1; 
    min-height: 100vh; 
    width: 100vw; 
    display: flex; 
    flex-direction: column; 
    justify-content: space-between; 
    color: black; 
    margin: 0; 
    padding: 0; 
    box-sizing: border-box; 
}

.header {
    display: flex; 
    justify-content: flex-end; 
    align-items: center; 
    z-index: 10; 
    padding: 20px; 
    width: 100%; 
}

.register-container {
    display: flex;
    align-items: center; 
}

.register-text {
    font-family: 'VCR OSD Mono', monospace; 
    font-size: 18px; 
    margin-right: 10px;
}

.register-button {
    background: none; 
    border: none; 
    padding: 0; 
    width: 150px; 
    height: auto; 
    margin-right: 20px; 
}

.register-button img {
    width: 110px; 
    height: auto; 
}

.welcome-container {
    display: flex;
    align-items: flex-end; 
    justify-content: center; 
    margin-bottom: 20px; 
    position: relative; 
}

/* Wolkenstile */
.sun-image {
    width: 100px; 
    height: auto;
    position: absolute; 
    left: 10%; 
    bottom: 50%; 
    z-index: 1; 
}

/* Obere Wolke links */
.left-cloud-top {
    width: 230px; 
    height: auto;
    position: absolute; 
    left: 5%; 
    bottom: 70%; 
    z-index: 0; 
}

/* Untere Wolke links */
.left-cloud-bottom {
    width: 230px; 
    height: auto;
    position: absolute; 
    left: 3%; 
    bottom: 0%; 
    z-index: 2; 
}

/* Rechte Wolke */
.right-cloud {
    width: 230px; 
    height: auto;
    position: absolute; 
    right: 10%; 
    bottom: 20%; 
}

/* Welcome Text */
.welcome-text {
    display: flex;
    flex-direction: column; 
    align-items: center; 
}

h1 {
    font-family: 'Pixelsplitter', Arial, sans-serif;
    font-size: 45px;
    color: black;
    margin: 0 10px 10px; 
    text-align: center;
}

/* Content Box Styles */
.content-box {
    width: 30%; 
    margin: 0 auto; 
    padding: 20px;
    box-shadow: none; 
    text-align: center; 
    position: relative;
    z-index: 2;
    background-color: transparent; 
}

input {
    display: block;
    margin: 10px auto;
    padding: 10px;
    width: 100%;
    background-color: white; 
    border: 2px solid #9cb405; 
    color: black; 
    font-family: 'VCR OSD Mono', monospace; 
    box-sizing: border-box; 
}

input:-webkit-autofill {
    -webkit-box-shadow: 0 0 0px 1000px white inset;
    box-shadow: 0 0 0px 1000px white inset;
}

.input-group {
    text-align: left; 
    margin-bottom: 15px; 
}

.input-group label {
    font-family: 'VCR OSD Mono', monospace; 
    font-size: 16px; 
    display: block; 
}

.submit-button {
    background: none; 
    border: none; 
    padding: 0; 
    cursor: pointer; 
    margin-top: 25px; 
    margin-bottom: 15px; 
}

.submit-button img {
    width: 150px; 
    height: auto; 
}

.button-container {
    display: flex; 
    justify-content: flex-end; 
    padding: 20px; 
}

.custom-button {
    background: none; 
    border: none; 
    padding: 0; 
}

.custom-button img {
    width: 150px; 
    height: auto; 
}

label {
    display: block;
    margin-bottom: 5px;
}

/* Fehlermeldung */
.error-message {
    color: red;
    font-family: 'VCR OSD Mono', monospace; 
    font-size: 16px;
    margin-top: 5px;
}
</style>
