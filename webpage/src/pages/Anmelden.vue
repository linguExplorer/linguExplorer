<template>
  <div
    v-if="isLoading"
    class="absolute top-0 left-0 flex justify-center items-center h-screen w-full bg-white bg-opacity-50 z-50"></div>

  <div class="font-vcr m-0 text-black w-full h-screen bg-[#f6f5f1] flex flex-col justify-center">

    <header class="bg-[#99b305] text-black sticky top-0 z-10 w-full px-4">
      <section class="w-full py-2 flex justify-between items-center">
        <img
          src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png"
          alt="Logo"
          class="w-3/6 max-w-[180px] sm:max-w-[200px] mr-9"
        />
        <div class="flex justify-center items-center class=gap-1 sm:gap-4">
          <!-- Text -->
          <p class="text-[8px] sm:text-[18px] font-vcr mr-1 sm:mr-4">Hast du noch keinen Account?</p>
          <!-- Button -->
          <button class="hover-button max-w-[100px] sm:max-w-[150px]">
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

    <main class="w-full mx-auto mt-[40px] sm:mt-[80px] px-4 sm:px-6 bg-[#f6f5f1]">
      <img
        src="@/assets/xx_Images/xx_Images/cloud.png"
        alt="Wolke"
        class="w-[160px] sm:min-w-[320px] absolute z-2 translate-x-[-42vw] translate-y-[-10vh] sm:translate-x-[-42vw] sm:translate-y-[-12vh]"
      />
      <img
        src="@/assets/xx_Images/xx_Images/sun.png"
        alt="Sonne"
        class="w-[70px] sm:min-w-[150px] absolute z-1 translate-x-[-38vw] translate-y-[-4vh] sm:translate-x-[-38vw] sm:translate-y-[-3vh]"
      />
      <img
        src="@/assets/xx_Images/xx_Images/Cloud2.png"
        alt="Wolke"
        class="w-[160px] sm:min-w-[320px] absolute z-2 translate-x-[-45vw] translate-y-[-2vh] sm:translate-x-[-45vw] sm:translate-y-[7vh]"
      />
      <img
        src="@/assets/xx_Images/xx_Images/Cloud2.png"
        alt="Wolke"
        class="w-[140px] sm:min-w-[320px] absolute z-1 translate-x-[40vw] translate-y-[-3vh] sm:translate-x-[30vw] sm:translate-y-[-8vh]"
      />

      <section class="hover-button bg-[#f6f5f1] flex flex-col justify-center items-center gap-4 sm:gap-[0px] px-1 sm:px-0">
        
        <h1 class="font-pixelsplitter text-[30px] sm:text-[60px] mb-5 mt-10 sm:mb-10 sm:mt-5">Anmelden</h1>

        <form
          @submit.prevent="submit"
          class="w-[240px] sm:w-[500px] flex flex-col justify-center items-center gap-4">
        
          <div class="w-full">
            <label for="email" 
              class="font-vcr block text-left mb-2 text-[14px] sm:text-[16px]">E-Mail</label>
              <input
                type="email"
                id="email"
                v-model="data.email"
                required
                class="font-vcr bg-white border-[#9cb405] border-[2px] p-2 text-sm w-full"
              />
              <p v-if="emailError" class="text-red-500 mt-2">
                {{ emailError }}
              </p>
          </div>

          <div class="input-group flex flex-col mb-0 sm:mb-4 w-full">
            <label for="password" 
            class="font-vcr block text-left mb-2 text-[14px] sm:text-[16px]">Passwort</label>
            <div class="input-group flex items-center mb-4">
              <input
                :type="inputType"
                id="password"
                v-model="data.password"
                required
                class="font-vcr bg-white border-[#9cb405] border-[2px] p-2 text-sm w-full"
              />
              
              <button
                @click="togglePasswordVisibility"
                class="focus:outline-none -ml-8"
              >
                <img
                  src="@/assets/xx_Images/xx_Images/Buttons/show.png"
                  alt=""
                  class="w-4 mb-2 mt-2"
                />
              </button>

              <p v-if="passwordError" class="text-red-500 mt-2">
                {{ passwordError }}
              </p>

            </div>
          </div>

          <!-- Fehlermeldung -->
          <p v-if="showError" class="mt-2 ">Ungültige E-Mail oder Passwort</p>

          <button type="submit" class="w-full max-w-[120px] sm:max-w-[150px] mt-1">
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/button anmelden blue.png"
              alt="Anmelden"
              class="w-full hover:opacity-80"
            />
          </button>
          <router-link to="/forgotPassword" class="nav-link" href="#">
            <p class="text-center mt-4 font-vcr text-[12px] sm:text-[14px]">Passwort vergessen?</p>
          </router-link>
        </form>
      </section>
    </main>

    <!-- Footer -->
    <footer class="mt-auto">
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
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { Toaster, toast } from 'vue-sonner'
import { shallowRef } from 'vue';
import ToastComponent from "@/components/ToastComponent.vue";
import ToastComponent_uch from "@/components/ToastComponent_uch.vue";
import ToastComponent_log from "@/components/ToastComponent_log.vue";


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
        const res = await fetch("https://da.linguexplorer.com/api/login", {
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
              emailError.value = "Passwort oder E-Mail falsch";
            } else if (err.detail.includes("password")) {
              passwordError.value = "Passwort oder E-Mail falsch!";
            }
          } else {
            passwordError.value = "Passwort oder E-Mail falsch!";
            emailError.value = "Passwort oder E-Mail falsch!";
          }
        } else {
          await router.push("/Afterlogin");
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
      inputType: "password",
    };
  },
  methods: {
    togglePasswordVisibility() {
      this.inputType = this.inputType === "password" ? "text" : "password";
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
    toastTest() {

      toast.custom(shallowRef(ToastComponent_log), { duration: 3000,
    
    message: 'Dies ist eine benutzerdefinierte Toast-Nachricht!'
  });


    }
    
  },
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