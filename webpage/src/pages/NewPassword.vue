<template>
    <div class="page-wrapper">
        <div class="content-box">
            <!-- Überschrift -->
            <div class="welcome-text">
                <h1>Neues Passwort erstellen</h1>
            </div>

            <!-- Eingabeformular -->
            <form @submit.prevent="handlePasswordSave">
                <div class="input-group">
                    <label for="password" id="passwordLabel">Passwort (mind. 8 Zeichen)</label>
                    <input type="password" id="password" v-model="password" required minlength="8" />
                </div>
                
                <div class="input-group">
                    <label for="confirmPassword" id="confirmPasswordLabel">Passwort wiederholen</label>
                    <input type="password" id="confirmPassword" v-model="confirmPassword" required minlength="8" />
                </div>

                <!-- Speicher-Button -->
                <button type="submit" class="submit-button">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button speichern.png" alt="Speichern" />
                </button>
            </form>
        </div>
    </div>
</template>

<script>
import '../styles/NewPassword.css';
export default {
    name: 'NewPassword',
    data() {
        return {
            password: '',
            confirmPassword: ''
        };
    },
    methods: {
        handlePasswordSave() {
            if (this.password === this.confirmPassword) {
                console.log('Neues Passwort speichern:', this.password);
            } else {
                console.error('Die Passwörter stimmen nicht überein.');
            }
        }
    }
};
</script>

