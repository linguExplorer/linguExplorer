<template>
    <div class="bg-[#f6f5f1] w-full justify-center min-h-screen text-black justify-center">
  
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
  
      <!-- Main Content -->
      <div class="flex flex-col justify-center items-center px-10 sm:px-6 text-center mt-24">
        <!-- Überschrift -->
        <section class="w-full max-w-[500px] flex flex-col justify-center items-center gap-6">
            <h1 class="font-pixelsplitter lg:text-[40px] md:text-[35px] sm:text-[30px] text-[25px] mb-4 whitespace-nowrap">
                Passwort vergessen?
            </h1>
          <p class="text-[14px] font-vcr sm:text-[18px]">
            Gib hier die E-Mail-Adresse ein, um dein<br> Passwort zurückzusetzen
          </p>
  
          <!-- Eingabeformular -->
          <form @submit.prevent="submit" class="w-full flex flex-col justify-center items-center gap-4">
            <div class="w-full">
              <label for="email" class="font-vcr block text-left mb-2 text-[14px] sm:text-[18px]">E-Mail:</label>
              <input 
                type="email" 
                id="email" 
                v-model="data.email" 
                required 
                class="font-vcr bg-white border-[#9cb405] border-[2px] p-2 text-sm w-full"
              />
            </div>
  
            <!-- Passwort-Button -->
            <button 
              type="submit" 
              class="hover-button w-full flex justify-center items-center mt-5">
              <img 
                src="@/assets/xx_Images/xx_Images/Buttons/button neues passwort.png" 
                alt="Neues Passwort" 
                class="max-w-[120px] sm:max-w-[200px] w-full"
              />
            </button>
          </form>
        </section>
      </div>
  
    </div>
  </template>
  
  <script>
  import { reactive } from 'vue';
  import { useRouter } from 'vue-router';
  
  export default {
    name: 'ForgotPassword',
    
    setup() {
      const data = reactive({
        email: ''
      });
      const router = useRouter();
  
      const submit = async () => {
        await fetch('http://localhost:8000/password_reset/', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          credentials: 'include',
          body: JSON.stringify(data)
        });
  
        await router.push({ 
          path: '/anmelden', 
          query: { success: 'true' } 
        });
      };
  
      return {
        data,
        submit
      };
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
  