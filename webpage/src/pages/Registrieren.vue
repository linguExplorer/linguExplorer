<template>
  <div v-if="isLoading" 
  class="absolute top-0 left-0 flex justify-center items-center h-screen w-full bg-white bg-opacity-50 z-50"></div>
  
  <div class="font-vcr m-0 text-black w-full h-screen bg-[#f6f5f1] flex flex-col justify-center">

    <!-- Header -->
    <header class="bg-[#99b305] text-black sticky top-0 z-10 w-full px-4">
      <section class="w-full py-2 flex justify-between items-center">
        <img
          src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png"
          alt="Logo"
          class="w-3/6 max-w-[180px] sm:max-w-[200px] mr-9"
        />
        <div class="flex justify-center items-center sm:gap-4">
          <p class="lg:text-[18px] md:text-[14px] sm:text-[8px] text-[8px] font-vcr mr-1 sm:mr-4">Hast du schon einen Account?</p>
          <button class="hover-button lg:max-w-[130px] md:max-w-[120px] sm:max-w-[100px] max-w-[90px]">
            <router-link to="/anmelden" class="nav-link">
              <img
                src="@/assets/xx_Images/xx_Images/Buttons/button anmelden green.png"
                alt="Registrieren"
                class="hover:opacity-80"
              />
            </router-link>
          </button>
        </div>
      </section>
    </header>

    <main class="w-full mx-auto mt-[40px] sm:mt-[80px] px-4 sm:px-6 bg-[#f6f5f1]">
      <!-- Background Images -->
      <img
          src="@/assets/xx_Images/xx_Images/cloud.png"
          alt="Wolke"
          class="lg:min-w-[320px] md:min-w-[240px] sm:min-w-[160px] w-[160px] absolute z-2 translate-x-[-42vw] translate-y-[-10vh] sm:translate-y-[-12vh]"
        />
        <img
          src="@/assets/xx_Images/xx_Images/sun.png"
          alt="Sonne"
          class="lg:min-w-[150px] md:min-w-[100px] sm:min-w-[70px] w-[70px] absolute z-1 translate-x-[-38vw] lg:translate-y-[-3vh] md:translate-y-[-3vh] sm:translate-y-[-5vh] translate-y-[-5vh]"
        />
        <img
          src="@/assets/xx_Images/xx_Images/Cloud2.png"
          alt="Wolke"
          class="lg:min-w-[320px] md:min-w-[240px] sm:min-w-[160px] w-[160px] absolute z-2 translate-x-[-45vw] lg:translate-y-[7vh] md:translate-y-[3vh] sm:translate-y-[-2vh] translate-y-[-2vh]"
        />
        <img
          src="@/assets/xx_Images/xx_Images/Cloud2.png"
          alt="Wolke"
          class="lg:min-w-[320px] md:min-w-[240px] sm:min-w-[160px] w-[160px] absolute z-1 translate-x-[40vw] translate-y-[-3vh] sm:translate-x-[30vw] sm:translate-y-[-8vh]"
        />

      <!-- Registration Form -->
      <section class="hover-button bg-[#f6f5f1] flex flex-col justify-center items-center gap-4 sm:gap-0 px-1 sm:px-0">

        <h1 class="font-pixelsplitter lg:text-[60px] md:text-[50px] sm:text-[30px] text-[30px] mb-5 mt-10 sm:mb-0 sm:mt-5">Registrieren</h1>

        <form 
        @submit.prevent="handleRegister" 
        class="w-[240px] sm:w-[500px] flex flex-col justify-center items-center gap-4">

          <!-- Email Input -->
          <div class="w-full">
            <label for="email" 
            class="font-vcr block text-left mb-2 lg:text-[16px] md:text-[15px] sm:text-[14px] text-[14px]">E-Mail</label>
            <input
              type="email"
              id="email"
              v-model="data.email"
              required
              class="font-vcr bg-white border-[#9cb405] border-[2px] p-2 text-sm w-full"
            />
            <p v-if="errorMessage" class="text-red-500 mt-2">{{ errorMessage }}</p>
          </div>

          <!-- Username Input -->
          <div class="w-full">
            <label for="uname" class="font-vcr block text-left mb-2 lg:text-[16px] md:text-[15px] sm:text-[14px] text-[14px]">Benutzername</label>
            <input
              type="text"
              id="uname"
              v-model="data.name"
              required
              class="font-vcr bg-white border-[#9cb405] border-[2px] p-2 text-sm w-full"
            />
            <p v-if="usernameError" class="text-red-500 mt-2">{{ usernameError }}</p>
          </div>

          <!-- Password Input -->
          <div class="input-group flex flex-col sm:mb-0 w-full">
            <label for="password" 
            class="font-vcr block text-left mb-2 lg:text-[16px] md:text-[15px] sm:text-[14px] text-[14px]">Passwort</label>
            <div class="input-group flex items-center">
              <input
                :type="inputType"
                id="password"
                v-model="data.password"
                required
                class="font-vcr bg-white border-[#9cb405] border-[2px] p-2 text-sm w-full"
              />
              <button @click="togglePasswordVisibility" type="button" class="focus:outline-none -ml-8">
                <img src="@/assets/xx_Images/xx_Images/Buttons/show.png" alt="" class="w-4 mb-2 mt-2" />
              </button>
            </div>
            <p v-if="passwordError" class="text-red-500 mt-2">{{ passwordError }}</p>
          </div>

          <!-- Confirm Password Input -->
          <div class="input-group flex flex-col mb-0 sm:mb-4 w-full">
            <label for="confpassword" 
            class="font-vcr block text-left mb-2 lg:text-[16px] md:text-[15px] sm:text-[14px] text-[14px]">Passwort Wiederholen</label>
            <div class="input-group flex items-center mb-0">
              <input
                :type="confirmInputType"
                id="confpassword"
                v-model="confirmPassword"
                required
                class="font-vcr bg-white border-[#9cb405] border-[2px] p-2 text-sm w-full"
              />
              <button @click="toggleConfirmPasswordVisibility" type="button" class="focus:outline-none -ml-8">
                <img src="@/assets/xx_Images/xx_Images/Buttons/show.png" alt="" class="w-4 mb-0 mt-2" />
              </button>
            </div>
            <p v-if="errorPass" class="text-red-500 mt-2">{{ errorPass }}</p>
          </div>

          <!-- Privacy Policy Checkbox -->
          <div class="inline-flex items-center pt-0">
            <input type="checkbox" v-model="data.checked" id="check-with-link" class="h-5 w-5 cursor-pointer" />
            <label for="check-with-link" class="cursor-pointer ml-2 font-vcr text-black text-[12px] sm:text-[14px]">
              Ich akzeptiere die linguExplorer
              <router-link to="/datenschutz" class="hover:text-[#99b305] underline">
                Datenschutzerklärung
              </router-link>
            </label>
          </div>
          <p v-if="!data.checked" class="text-red-500 text-center mt-0 text-[13px] sm:text-[15px]">Bitte akzeptieren Sie die Datenschutzerklärung um fortzufahren!</p>

          <!-- Submit Button -->
          <button type="submit" class="hover-button w-full lg:max-w-[150px] md:max-w-[145px] sm:max-w-[120px] max-w-[120px] mt-0" :disabled="!isFormValid">
            <img src="@/assets/xx_Images/xx_Images/Buttons/button registrieren blue.png" alt="Registrieren" class="w-full hover:opacity-80" />
          </button>
        </form>
      </section>
    </main>

    <!-- Footer -->
    <footer class="mt-auto bg-[#f6f5f1]">
        <section class="p-4 flex justify-end items-center">
          <button class="hover-button max-w-[170px] sm:max-w-[200px] p-4">
            <router-link to="/" class="nav-link" href="#">
              <img
                src="@/assets/xx_Images/xx_Images/Buttons/button zrk.png"
                alt="Zurück"
                class="hover:opacity-80"
              />
            </router-link>
          </button>
        </section>
      </footer>
  </div>
</template>

<script>
import { reactive, ref, computed } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: 'Registrieren',
  setup() {
    const data = reactive({
      name: '',
      email: '',
      password: '',
      checked: false
    });
    const confirmPassword = ref('');
    const errorMessage = ref('');
    const passwordError = ref('');
    const errorPass = ref('');
    const usernameError = ref('');
    const isLoading = ref(false);

    const isFormValid = computed(() => {
      if (data.password !== confirmPassword.value) {
        errorPass.value = 'Passwörter stimmen nicht überein.';
        return false;
      } else {
        errorPass.value = '';
        return true;
      }
    });

    const submit = async () => {
      isLoading.value = true;

      try {
        const res = await fetch('http://localhost:8000/api/register', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(data)
        });
        if (!res.ok) {
          isLoading.value = false;
          const err = await res.json();
          if (err.email && err.email[0].includes('user')) {
            errorMessage.value = 'User mit dieser Email existiert bereits';
          } else {
            errorMessage.value = 'Ein unbekannter Fehler ist aufgetreten.';
          }
        } else {
          await router.push('/eMailVerif');
        }
      } catch (error) {
        console.error('Fehler beim Senden der Anfrage:', error);
      }
    };

    const togglePasswordVisibility = () => {
      inputType.value = inputType.value === 'password' ? 'text' : 'password';
    };

    const toggleConfirmPasswordVisibility = () => {
      confirmInputType.value = confirmInputType.value === 'password' ? 'text' : 'password';
    };

    const router = useRouter();
    
    return {
      data,
      confirmPassword,
      isFormValid,
      submit,
      errorMessage,
      passwordError,
      errorPass,
      usernameError,
      isLoading,
      togglePasswordVisibility,
      toggleConfirmPasswordVisibility
    };
  }
};
</script>

<style scoped>
.hover-button img {
  transition: opacity 0.2s ease-in-out, transform 0.2s ease-in-out;
}
.hover-button img:hover {
  opacity: 0.8;
  transform: scale(1.05);
}
</style>