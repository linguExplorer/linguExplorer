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
    align-items: center;
    justify-content: center;
    color: black;
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

.content-box {
    width: 40%; /*allgem box schmäler*/
    padding: 20px;
    text-align: center;
    background-color: transparent;
}

.welcome-text {
    text-align: center;         
    font-family: 'Pixelsplitter', Arial, sans-serif;
}

.h1 {
    text-align: center; 
    font-size: 32px;
    color: black;
}

.input-group {
    margin-bottom: 15px;
    width: 100%;
    text-align: center;
}

.input-group label {
    font-family: 'VCR OSD Mono', monospace;
    font-size: 16px;
    display: block;
}

input {
    display: block;
    margin: 10px auto;
    padding: 10px;
    width: 100%; /*eigabefelder schmäler*/
    background-color: white;
    border: 2px solid #9cb405;
    color: black;
    font-family: 'VCR OSD Mono', monospace;
    box-sizing: border-box;
}


#passwordLabel {
    text-align: left;
}

#confirmPasswordLabel {
    text-align: left;
}

.submit-button {
    background: none;
    border: none;
    padding: 0;
    cursor: pointer;
    margin-top: 25px;
}

.submit-button img {
    width: 150px;
    height: auto;
}

input:-webkit-autofill {
    -webkit-box-shadow: 0 0 0px 1000px white inset;
    box-shadow: 0 0 0px 1000px white inset;
}
</style>
