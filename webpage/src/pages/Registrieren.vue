<template>
        <div
      v-if="isLoading"
      class="absolute top-0 left-0 flex justify-center items-center h-screen w-full bg-white bg-opacity-50  z-50"
    ></div>
    <div
      class="font-vcr m-0 p-3 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-center"
    >
      <header class="bg-[#99b305] top-0">
        <section class="mx-auto p-4 flex justify-end items-center gap-8">
          <p class="font-size:18px font-vcr">Hast du schon einen Account?</p>
  
          <button class="hover-button max-w-[150px]">
            <router-link to="/anmelden" class="nav-link" href="#">
              <img
                src="@/assets/xx_Images/xx_Images/Buttons/button anmelden green.png"
                alt="Registrieren"
                class="hover:opacity-80"
              />
            </router-link>
          </button>
        </section>
      </header>
  
      <main class="max-w-6xl mx-auto mt-[80px]">
        <img
          src="@/assets/xx_Images/xx_Images/cloud.png"
          alt="Wolke"
          class="min-w-[320px] xxl:min-w-[380px] absolute z-2 translate-x-[-42vw] translate-y-[-12vh] xxl:translate-x-[-42vw] xxl:translate-y-[-11vh]"
        />
        <img
          src="@/assets/xx_Images/xx_Images/sun.png"
          alt="Sonne"
          class="min-w-[150px] xxl:min-w-[180px] absolute z-1 translate-x-[-38vw] translate-y-[-3vh] xxl:translate-x-[-38vw] xxl:translate-y-[-3vh]"
        />
        <img
          src="@/assets/xx_Images/xx_Images/Cloud2.png"
          alt="Wolke"
          class="min-w-[320px] xxl:min-w-[380px] absolute z-2 translate-x-[-45vw] translate-y-[7vh] xxl:translate-x-[-45vw] xxl:translate-y-[5vh]"
        />
        <img
          src="@/assets/xx_Images/xx_Images/Cloud2.png"
          alt="Wolke"
          class="min-w-[320px] xxl:min-w-[380px] absolute z-1 translate-x-[30vw] translate-y-[-8vh] xxl:translate-x-[30vw] xxl:translate-y-[-8vh]"
        />
  
        <!--<div class="anmelde-container">-->
        <section
          class="flex flex-col justify-center items-center gap-[20px] min-w-[700px]"
        >
          <h1 class="font-pixelsplitter text-[60px] mb-6">Registrieren</h1>
  
          <form
            @submit.prevent="handleRegister"
            class="min-w-[600px] max-w-md flex flex-col items-center mt-[30px]"
          >
            <div class="input-group flex flex-col mb-4 ml-4">
              <label for="email" class="">E-Mail</label>
              <input
                type="email"
                id="email"
                v-model="data.email"
                required
                class="font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2"
              />
              <p v-if="errorMessage" class="text-red-500 mt-2">
                      {{ errorMessage }}
                  </p>
            </div>
            <div class="input-group flex flex-col mb-4 ml-4">
              <label for="uname" class="">Benutzername</label>
              <input
                type="text"
                id="uname"
                v-model="data.name"
                required
                class="font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2"
              />
              <p v-if="usernameError" class="error-message">
                Benutzername bereits vergeben.
              </p>
            </div>
            <div class="flex flex-col">
              <label for="password" class="">Passwort</label>
              <div class="input-group flex items-center mb-4">
                <input
                  :type="inputType"
                  id="password"
                  v-model="data.password"
                  required
                  class="font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2"
                />
                <button
                  @click="togglePasswordVisibility"
                  type="button"
                  class="focus:outline-none -ml-8"
                >
                  <img
                    src="@/assets/xx_Images/xx_Images/Buttons/show.png"
                    alt=""
                    class="w-4"
                  />
                </button>
                <p v-if="passwordError" class="error-message">
                  Passwort entspricht nicht den Anforderungen.
                </p>
       
              </div>
            </div>
            <div class="flex flex-col">
              <label for="confpassword" class="">Passwort Wiederholen</label>
              <div class="flex items-center mb-4">
              <input
                :type="confirmInputType"
                id="confpassword"
                v-model="confirmPassword"
                required
                class="font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2"
              />
              <button
                  @click="toggleConfirmPasswordVisibility"
                  type="button"
                  class="focus:outline-none -ml-8"
                >
                  <img
                    src="@/assets/xx_Images/xx_Images/Buttons/show.png"
                    alt=""
                    class="w-4"
                  />
                </button>
               
          </div>
          <p v-if="errorPass" class="text-red-500 mt-2"> 
                      {{ errorPass }}
                  </p>
            </div>
            <div class="inline-flex items-center pt-5">
                          <input type="checkbox"  v-model ="data.checked" id="check-with-link" class=" h-5 w-5 cursor-pointer"/>
                          <label class="cursor-pointer ml-2 font-vcr text-black text-sm" for="check-with-link">
                          <p class="  text-[15px]">Ich akzeptiere die linguExplorer
                              <router-link to="/datenschutz"  href="#">
                          <a href="#" class=" hover:text-[#99b305]  underline">
                              Datenschutzerklärung
                          </a>
                          </router-link>
                          
                      </p>
                          </label>
                  </div>
                  <p v-if="!data.checked" class="text-red-500 mt-2">Bitte akzeptieren Sie die Datenschutzerklärung um fortzufahren!</p>
  
  
            <button type="submit" class="hover-button max-w-[150px] mt-6 w-full" :disabled="!isFormValid" @click="submit">
              <img
                src="@/assets/xx_Images/xx_Images/Buttons/button registrieren blue.png"
                alt="Registrieren"
                class="hover:opacity-80"
              />
            </button>
          </form>
        </section>
      </main>
  
      <footer class="mt-auto">
        <section class="mx-auto p-4 flex justify-end items-center">
          <button class="hover-button max-w-[200px] p-4">
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
  import { reactive, ref, computed} from 'vue';
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
           const errorMessage = ref('');
           const errorPass = ref('');
           const confirmPassword = ref('');
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
  
      const submit = async() => {
        isLoading.value = true;

          try {
     const res = await fetch('http://localhost:8000/api/register', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
     });
     if(!res.ok) {
        isLoading.value = false;
      const err = await res.json();
      if (err.email.length > 0) {
              if (err.email[0].includes('user')) {
                   errorMessage.value = 'User mit dieser Email existiert bereits';
              }
                  
            } else {
              errorMessage.value = 'Ein unbekannter Fehler ist aufgetreten.';
            }
     } else {
      await router.push({ 
      path: '/eMailVerif' 
      });
     }
  } catch (error) {
      console.error('Fehler beim Senden der Anfrage:', error);
  }
     
  
  }
  return{
              data,
              submit,
              errorMessage,
              errorPass,
              isFormValid,
              confirmPassword,
              isLoading,

           }
      },
      data() {
          return {
  
              username: '',
              password: '',
              emailError: false,
              usernameError: false,
              passwordError: false,
              confirmPasswordError: false,
              checked: false,
              inputType: 'password',
              confirmInputType: 'password',

          };
      },
      methods: {
          togglePasswordVisibility() {
        this.inputType = this.inputType === 'password' ? 'text' : 'password';
          },
          toggleConfirmPasswordVisibility() {
        this.confirmInputType = this.confirmInputType === 'password' ? 'text' : 'password';
          },
  
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
.hover-button img {
  transition: opacity 0.2s ease-in-out, transform 0.2s ease-in-out;
}
.hover-button img:hover {
  opacity: 0.8;
  transform: scale(1.05);
}

</style>