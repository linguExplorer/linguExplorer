<template>
    <div class="page-wrapper-newPassword newPassword">
        <!-- Überschrift -->
        <div class="new-password">
                <h1>Neues Passwort erstellen</h1>
            </div>
        <div class="content-box-newPassword">

            <!-- Eingabeformular -->
            <form @submit.prevent="submit">
                <div class="input-group">
                    <label for="password" id="passwordLabel">Passwort (mind. 8 Zeichen)</label>
                    <input type="password" id="password" v-model="data.password" required minlength="8" />
                </div>
                
                <div class="input-group">
                    <label for="confirmPassword" id="confirmPasswordLabel">Passwort wiederholen</label>
                    <input type="password" id="confirmPassword" v-model="confirmPassword" required minlength="8" />
                </div>

                <!-- Speicher-Button -->
                <button type="submit" class="submit-button-newPassword">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button speichern.png" alt="Speichern" />
                </button>
            </form>
        </div>
    </div>
</template>

<script>
import '../styles/NewPassword.css';
import { reactive } from 'vue';
import { useRouter, useRoute } from 'vue-router';
export default {
    name: 'NewPassword',
    setup() {
    const data = reactive({
      password:'',
      token:''
    });

    const router = useRouter();
    const route = useRoute();
    data.token = route.params.ref;

    const  submit = async() => {
      await fetch('http://localhost:8000/password_reset/confirm/', {
               method: 'POST',
               headers: {'Content-Type': 'application/json'},
               credentials: 'include',
               body: JSON.stringify(data)
    });

    await router.push('/anmelden');
    }

    return {
      data,
      submit
    }
  },
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

