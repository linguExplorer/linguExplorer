<template>
  <div
    v-if="isLoading"
    class="absolute top-0 left-0 flex justify-center items-center h-screen w-full bg-white bg-opacity-50 z-50"
  ></div>

  <div
    class="font-vcr m-0 text-black w-full h-screen bg-[#f6f5f1] flex flex-col justify-center"
  >
    <!-- Header -->
    <header class="bg-[#99b305] text-black sticky top-0 z-10 w-full px-4">
      <section class="w-full py-2 flex justify-between items-center">
        <img
          src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png"
          alt="Logo"
          class="w-3/6 max-w-[180px] sm:max-w-[200px] mr-9"
        />
        <div class="flex justify-center items-center sm:gap-4">
          <p class="text-[8px] sm:text-[18px] font-vcr mr-1 sm:mr-4">
            Hast du schon einen Account?
          </p>
          <button class="hover-button max-w-[100px] sm:max-w-[150px]">
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

    <main
      class="w-full mx-auto mt-[40px] sm:mt-[80px] px-4 sm:px-6 bg-[#f6f5f1] relative"
    >
      <!-- Background Images -->
      <img
        src="@/assets/xx_Images/xx_Images/cloud.png"
        alt="Wolke"
        class="w-[160px] sm:min-w-[320px] absolute z-2 translate-x-[-42vw] translate-y-[-10vh] sm:translate-x-[-42vw] sm:translate-y-[-1vh]"
      />
      <img
        src="@/assets/xx_Images/xx_Images/sun.png"
        alt="Sonne"
        class="w-[70px] sm:min-w-[150px] absolute z-1 translate-x-[-38vw] translate-y-[-4vh] sm:translate-x-[-38vw] sm:translate-y-[8vh]"
      />
      <img
        src="@/assets/xx_Images/xx_Images/Cloud2.png"
        alt="Wolke"
        class="w-[160px] sm:min-w-[320px] absolute z-2 translate-x-[-50vw] translate-y-[-3vh] sm:translate-x-[-45vw] sm:translate-y-[18vh]"
      />
      <img
        src="@/assets/xx_Images/xx_Images/Cloud2.png"
        alt="Wolke"
        class="w-[140px] sm:min-w-[320px] absolute z-1 translate-x-[40vw] translate-y-[-3vh] sm:translate-x-[30vw] sm:translate-y-[6vh]"
      />

      <!--<div class="anmelde-container">-->
      <section
        class="hover-button bg-[#f6f5f1] flex flex-col justify-center items-center gap-4 sm:gap-0 px-1 sm:px-0"
      >
        <h1
          class="font-pixelsplitter text-[30px] sm:text-[60px] mb-5 mt-10 sm:mb-0 sm:mt-5"
        >
          Registrieren
        </h1>

        <form
          @submit.prevent="handleRegister"
          class="w-[240px] sm:w-[500px] flex flex-col justify-center items-center gap-4"
        >
          <div class="w-full">
            <label
              for="email"
              class="font-vcr block text-left mb-2 text-[14px] sm:text-[16px]"
              >E-Mail</label
            >
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
          <div class="w-full">
            <label
              for="uname"
              class="font-vcr block text-left mb-2 text-[14px] sm:text-[16px]"
              >Benutzername</label
            >
            <input
              type="text"
              id="uname"
              v-model="data.name"
              required
              class="font-vcr bg-white border-[#9cb405] border-[2px] p-2 text-sm w-full"
            />

            <p v-if="usernameError" class="error-message">
              Benutzername bereits vergeben.
            </p>
          </div>

          <div class="input-group flex flex-col sm:mb-0 w-full">
            <label
              for="password"
              class="font-vcr block text-left mb-2 text-[14px] sm:text-[16px]"
              >Passwort</label
            >
            <div class="input-group flex items-center">
              <input
                :type="inputType"
                id="password"
                v-model="data.password"
                required
                class="font-vcr bg-white border-[#9cb405] border-[2px] p-2 text-sm w-full"
              />
              <button
                @click="togglePasswordVisibility"
                type="button"
                class="focus:outline-none -ml-8"
              >
                <img
                  src="@/assets/xx_Images/xx_Images/Buttons/show.png"
                  alt=""
                  class="w-4 mb-2 mt-2"
                />
              </button>
            </div>
            <p v-if="passwordError" class="error-message">
              Passwort entspricht nicht den Anforderungen.
            </p>
          </div>

          <div class="input-group flex flex-col mb-0 sm:mb-4 w-full">
            <label
              for="confpassword"
              class="font-vcr block text-left mb-2 text-[14px] sm:text-[16px]"
            >
              Passwort Wiederholen
            </label>
            <div class="flex items-center mb-4">
                <input
                  :type="confirmInputType"
                  id="confpassword"
                  v-model="confirmPassword"
                  required
                  class="font-vcr bg-white border-[#9cb405] border-[2px] p-2 text-sm w-full"
                />
                <button
                  @click="toggleConfirmPasswordVisibility"
                  type="button"
                  class="focus:outline-none -ml-8"
                >
                  <img
                    src="@/assets/xx_Images/xx_Images/Buttons/show.png"
                    alt=""
                    class="w-4 mb-0 mt-2"
                  />
                </button>
            </div>
            <p v-if="errorPass" class="text-red-500 mt-2">
              {{ errorPass }}
            </p>
          </div>

          <div class="inline-flex items-center pt-5">
            <input
              type="checkbox"
              v-model="data.checked"
              id="check-with-link"
              class="h-5 w-5 cursor-pointer"
            />
            <label
              for="check-with-link"
              class="cursor-pointer ml-2 font-vcr text-black text-[12px] sm:text-[14px]"
            >
              Ich akzeptiere die linguExplorer
              <router-link
                to="/datenschutz"
                target="_blank"
                class="underline hover:text-green-500"
              >
                Datenschutzerklärung
              </router-link>
            </label>
          </div>
          <p
            v-if="!data.checked"
            class="text-red-500 text-center mt-0 text-[13px] sm:text-[15px]"
          >
            Bitte akzeptieren Sie die Datenschutzerklärung um fortzufahren!
          </p>

          <button
            type="submit"
            class="hover-button w-full max-w-[120px] sm:max-w-[150px] mt-0"
            :disabled="!isFormValid"
            @click="submit"
          >
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/button registrieren blue.png"
              alt="Registrieren"
              class="w-full hover:opacity-80"
            />
          </button>
        </form>
      </section>
    </main>

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
import { reactive, ref, computed } from "vue";
import { useRouter } from "vue-router";
import { Toaster, toast } from "vue-sonner";
import { useStore } from "vuex";
import ToastComponent_reg from "@/components/ToastComponent_reg.vue";
import { shallowRef } from 'vue';

export default {
  name: "Registrieren",
  setup() {
    const data = reactive({
      name: "",
      email: "",
      password: "",
    });
    const router = useRouter();
    const errorMessage = ref("");
    const errorPass = ref("");
    const confirmPassword = ref("");
    const isLoading = ref(false);
    const store = useStore();

    const isFormValid = computed(() => {
      if (data.password !== confirmPassword.value) {
        errorPass.value = "Passwörter stimmen nicht überein.";
        return false;
      } else {
        errorPass.value = "";
        return true;
      }
    });

    const submit = async () => {
      isLoading.value = true;

      try {
        const res = await fetch("https://da.linguexplorer.com/api/register", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(data),
        });
        if (!res.ok) {
          isLoading.value = false;
          const err = await res.json();
          if (err.email.length > 0) {
            if (err.email[0].includes("user")) {
              errorMessage.value = "User mit dieser Email existiert bereits";
            }
          } else {
            errorMessage.value = "Ein unbekannter Fehler ist aufgetreten.";
          }
        } else {
          toast.custom(shallowRef(ToastComponent_reg), { duration: 3000,
    
    message: 'Dies ist eine benutzerdefinierte Toast-Nachricht!'
  });
          await store.dispatch("updateEmail", data.email);
          await router.push({
            path: "/eMailVerif",
          });
        }
      } catch (error) {
        console.error("Fehler beim Senden der Anfrage:", error);
      }
    };
    return {
      data,
      submit,
      errorMessage,
      errorPass,
      isFormValid,
      confirmPassword,
      isLoading,
    };
  },
  data() {
    return {
      username: "",
      password: "",
      emailError: false,
      usernameError: false,
      passwordError: false,
      confirmPasswordError: false,
      checked: false,
      inputType: "password",
      confirmInputType: "password",
    };
  },
  methods: {
    togglePasswordVisibility() {
      this.inputType = this.inputType === "password" ? "text" : "password";
    },
    toggleConfirmPasswordVisibility() {
      this.confirmInputType =
        this.confirmInputType === "password" ? "text" : "password";
    },

    handleRegister() {
      console.log(
        "Registrieren mit:",
        this.email,
        this.username,
        this.password
      );
    },
    goBack() {
      this.$router.go(-1);
    },
    goToLogin() {
      this.$router.push("/login");
    },
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
