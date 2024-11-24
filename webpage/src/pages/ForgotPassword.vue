<template>
    <div class="page-wrapper-forgotPassword forgotPassword">

        <!-- Überschrift -->
        <div class="forgot-password">
                <h1>Passwort vergessen?</h1>
        </div>

        <div class="content-box-forgotPassword">

            <!--<p class="subtext">Gib hier die E-Mail-Adresse ein, um dein Passwort zurückzusetzen</p>-->
            <p class="subtext">instargam:</p>
            
            <!-- Eingabeformular -->
            <form @submit.prevent="handlePasswordReset">
                <div class="input-group">
                    <label for="email">E-Mail:</label>
                    <input type="email" id="email" v-model="email" required />
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
export default {
    name: 'ForgotPassword',
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
