<template>
  <div
    v-if="isLoading"
    class="absolute top-0 left-0 flex justify-center items-center h-screen w-full bg-white bg-opacity-50 z-50"
  ></div>
  <div
    class="bg-[#f6f5f1] w-full justify-center min-h-screen text-black justify-center"
  >
    <!-- Header -->
    <header class="bg-[#99b305] text-black sticky top-0 z-10">
      <section class="w-full py-2 flex justify-between items-center px-4">
        <img
          src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png"
          alt="Logo"
          class="w-3/6 max-w-[180px] sm:max-w-[200px] mr-9"
        />
      </section>
    </header>

    <div
      class="flex flex-col justify-center items-center px-4 sm:px-6 text-center lg:mt-40 md:mt-40 lg:mt-24 mt-24"
    >
      <!-- Überschrift -->
      <section
        class="w-full max-w-[500px] flex flex-col justify-center items-center gap-6"
      >
        <h1
          class="font-pixelsplitter lg:text-[40px] md:text-[35px] sm:text-[30px] text-[25px] mb-4 sm:whitespace-nowrap"
        >
          Neues Passwort erstellen
        </h1>

        <!-- Eingabeformular -->
        <form
          @submit.prevent="submit"
          class="lg:w-[500px] md:w-[400px] sm:w-[300px] w-[300px] flex flex-col justify-center items-center gap-4"
        >
          <div class="w-full px-8 sm:px-4">
            <label
              for="password"
              id="passwordLabel"
              class="font-vcr block text-left mb-0 lg:text-[16px] md:text-[15px] sm:text-[14px] text-[14px]"
              >Passwort (mind. 8 Zeichen)
            </label>
            <input
              :type="inputType"
              id="password"
              v-model="data.password"
              required
              minlength="8"
              class="font-vcr bg-white border-[#9cb405] border-[2px] lg:p-2 md:p-1.5 sm:p-1 p-1 text-sm w-full"
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
          </div>

          <div class="w-full px-8 sm:px-4">
            <label
              for="confirmPassword"
              id="confirmPasswordLabel"
              class="font-vcr block text-left mb-0 lg:text-[16px] md:text-[15px] sm:text-[14px] text-[14px]"
              >Passwort wiederholen
            </label>
            <input
              :type="confirmInputType"
              id="confirmPassword"
              v-model="confirmPassword"
              required
              minlength="8"
              class="font-vcr bg-white border-[#9cb405] border-[2px] lg:p-2 md:p-1.5 sm:p-1 p-1 text-sm w-full"
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
            <p v-if="errorMessage" class="text-red-500 mt-2 font-vcr">
              {{ errorMessage }}
            </p>
          </div>

          <!-- Speicher-Button -->
          <button
            type="submit"
            class="hover-button w-full flex justify-center items-center mt-5"
            :disabled="!isFormValid"
          >
            <img
              src="@/assets/xx_Images/xx_Images/Buttons/button speichern.png"
              alt="Speichern"
              class="lg:max-w-[150px] md:max-w-[130px] sm:max-w-[120px] max-w-[100px] w-full"
            />
          </button>
        </form>
      </section>
    </div>
  </div>
</template>

<script>
import { reactive, ref, computed } from "vue";
import { useRouter, useRoute } from "vue-router";
import { Toaster, toast } from "vue-sonner";

export default {
  name: "NewPassword",
  setup() {
    const data = reactive({
      password: "",
      token: "",
    });

    const confirmPassword = ref("");
    const router = useRouter();
    const route = useRoute();
    data.token = route.params.ref;
    const errorMessage = ref("");
    const isLoading = ref(false);

    const isFormValid = computed(() => {
      if (data.password !== confirmPassword.value) {
        errorMessage.value = "Passwörter stimmen nicht überein.";
        return false;
      } else {
        errorMessage.value = "";
        return true;
      }
    });

    const submit = async () => {
      isLoading.value = true;
      try {
        const res = await fetch(
          "https://da.linguexplorer.com/password_reset/confirm/",
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify(data),
          }
        );
        if (!res.ok) {
          const err = await res.json();
          toast("Link ist ungültig");
          await router.push("/");

          if (err.password.length > 0) {
            errorMessage.value =
              "Das Passwort muss Buchstaben und Zeichen enthalten!";
          } else {
            errorMessage.value = "Ein unbekannter Fehler ist aufgetreten.";
          }
        } else {
          await router.push("/anmelden");
        }
      } catch (error) {
        console.error("Fehler beim Senden der Anfrage:", error);
      }
    };
    return {
      data,
      submit,
      errorMessage,
      isFormValid,
      confirmPassword,
      isLoading,
    };
  },
  data() {
    return {
      password: "",
      inputType: "password",
      confirmInputType: "password",
    };
  },
  methods: {
    handlePasswordSave() {
      if (this.data.password === this.confirmPassword) {
        console.log("Neues Passwort speichern:", this.password);
      } else {
        console.error("Die Passwörter stimmen nicht überein.");
      }
    },

    togglePasswordVisibility() {
      this.inputType = this.inputType === "password" ? "text" : "password";
    },
    toggleConfirmPasswordVisibility() {
      this.confirmInputType =
        this.confirmInputType === "password" ? "text" : "password";
    },
  },
};
</script>
