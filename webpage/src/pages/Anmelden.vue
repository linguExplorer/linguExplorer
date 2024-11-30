<template>
    <div
      v-if="isLoading"
      class="absolute top-0 left-0 flex justify-center items-center h-screen w-full bg-white bg-opacity-50  z-50"
    ></div>
  
    <div
      class="font-vcr m-0 text-black w-full bg-[#f6f5f1] flex flex-col justify-center"
    >
      <header class="bg-[#99b305] text-black sticky top-0 z-10 w-full px-4">
        <section class="w-full py-2 flex justify-between items-center">
          <img
            src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png"
            alt="Logo"
            class="w-1/6"
          />
          <div class="flex justify-center items-center space-x-2">
            <p class="font-size:18px font-vcr">Hast du noch keinen Account?</p>
  
            <button class="max-w-[150px] mr-[10px]">
              <router-link to="/registrieren" class="nav-link" href="#">
                <img
                  src="@/assets/xx_Images/xx_Images/Buttons/button registrieren green.png"
                  alt="Registrieren"
                  class="hover:opacity-80"
                />
              </router-link>
            </button>
          </div>
        </section>
      </header>
  
      <main class="max-w-6xl mx-auto mt-[80px] px-6">
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
          class="flex flex-col justify-center items-center gap-[20px] min-w-[700px] "
        >
          <h1 class="font-pixelsplitter text-[60px] mb-6">Anmelden</h1>
  
          <form
            @submit.prevent="submit"
            class="min-w-[600px] max-w-md flex flex-col items-center mt-[30px]"
          >
            <div class="input-group flex flex-col mb-4">
              <label for="email" class="">E-Mail</label>
              <input
                type="email"
                id="email"
                v-model="data.email"
                required
                class="font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2"
              />
              <p v-if="emailError" class="text-red-500 mt-2">
                {{ emailError }}
              </p>
            </div>
            <div class="input-group flex flex-col mb-4">
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
              <p v-if="passwordError" class="text-red-500 mt-2">
                {{ passwordError }}
              </p>
            </div>
            </div>
  
            <!-- Fehlermeldung -->
            <p v-if="showError" class="mt-2 ">Ungültige E-Mail oder Passwort</p>
  
            <button type="submit" class="max-w-[150px] mt-6 w-full">
              <img
                src="@/assets/xx_Images/xx_Images/Buttons/button anmelden blue.png"
                alt="Anmelden"
                class="w-full hover:opacity-80"
              />
            </button>
            <router-link to="/forgotPassword" class="nav-link" href="#">
              <p class="text-center mt-4 font-vcr">Passwort vergessen?</p>
            </router-link>
          </form>
        </section>
      </main>
  
      <footer class="mt-auto">
        <section class="mx-auto p-4 flex justify-end items-center">
          <button class="max-w-[200px] p-4">
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
  import { reactive, ref } from "vue";
  import { useRouter } from "vue-router";
  export default {
    name: "AnmeldePage",
  
    mounted() {
      const urlParams = new URLSearchParams(window.location.search);
      const success = urlParams.get("success");
      const reg = urlParams.get("reg");
  
  
    },
  
    setup() {
      const data = reactive({
        email: "",
        password: "",
      });
      const router = useRouter();
      const emailError = ref("");
      const passwordError = ref("");
      const isLoading = ref(false);
  
      const submit = async () => {
        emailError.value = "";
        passwordError.value = "";
        isLoading.value = true;
        try {
          const res = await fetch("http://localhost:8000/api/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify(data),
          });
          if (!res.ok) {
          isLoading.value = false;
            const err = await res.json();
            if (err.detail.length > 0) {
              if (err.detail.includes("User")) {
                emailError.value =
                  "Passwort oder E-Mail falsch";
              } else if (err.detail.includes("password")) {
                passwordError.value = "Passwort oder E-Mail falsch!";
              }
            } else {
              passwordError.value = "Passwort oder E-Mail falsch!";
              emailError.value = "Passwort oder E-Mail falsch!";
            }
          } else {
            await router.push("/comingSoon");
          }
        } catch (error) {
          console.error("Fehler beim Senden der Anfrage:", error);
        }
      };
  
      return {
        data,
        submit,
        emailError,
        passwordError,
        isLoading,
      };
    },
    data() {
      return {
        username: "",
        password: "",
        inputType: 'password',

      };
    },
    methods: {
        togglePasswordVisibility() {
        this.inputType = this.inputType === 'password' ? 'text' : 'password';
          },
      handleLogin() {
        console.log("Anmelden mit:", this.username, this.password);
      },
      goBack() {
        this.$router.go(-1);
      },
      goToRegister() {
        this.$router.push("/register");
      },
    },
  };
  </script>
  