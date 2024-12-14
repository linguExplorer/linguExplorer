<template>
  <div class="font-vcr m-0 text-black w-full bg-[#f6f5f1] flex flex-col justify-start min-h-screen relative">
    <header class="bg-[#99b305] text-black sticky top-0 z-10 w-full">
        <section class="w-full py-2 flex justify-between items-center px-4">
            <img src="@/assets/xx_Images/xx_Images/wordmark/wordmark_hell_scaled.png" alt="Logo" class="w-1/8 max-w-[200px] ml-4" />
        </section>
    </header>

    <main class="max-w-6xl mx-auto mt-[80px] px-6">
            <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="min-w-[320px] xxl:min-w-[380px] absolute z-2 translate-x-[-42vw] translate-y-[-12vh] 
            xxl:translate-x-[-42vw] xxl:translate-y-[-11vh]" />
            <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="min-w-[150px] xxl:min-w-[180px] absolute z-1 translate-x-[-38vw] translate-y-[-3vh]
            xxl:translate-x-[-38vw] xxl:translate-y-[-3vh]" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="min-w-[320px] xxl:min-w-[380px] absolute z-2 translate-x-[-45vw] translate-y-[7vh]
        xxl:translate-x-[-45vw] xxl:translate-y-[5vh]" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="min-w-[320px] xxl:min-w-[380px] absolute z-1 translate-x-[30vw] translate-y-[-8vh]
        xxl:translate-x-[30vw] xxl:translate-y-[-8vh]" />

    </main>

    <section class="flex flex-col justify-center items-center gap-[20px] min-w-[700px]">
            <h1 class="font-pixelsplitter text-[40px] mb-6">Bestätige deine <br> E-Mail-Adresse</h1>

            <p class="font-[20px] font-vcr text-center" >Um deine Registrierung abzuschließen,
               <br> überprüfe bitte dein E-Mail Postfach.<br> Dort findest du eine E-Mail mit einem <br> Bestätigungslink <br>
              Klicke auf den Link in dieser E-Mail, um <br> deine Registrierung zu bestätigen und <br> loszulegen!  </p>

            <p class="font-[10px] font-vcr text-center" >Keine E-Mail erhalten?</p>
            <button class="max-w-[200px] p-4" @click="resend">
                  <img
                    src="@/assets/xx_Images/xx_Images/Buttons/resend.png"
                    alt=""
                    class="hover:opacity-80  z-10"
                  />
                </button>
         
        </section>

  
  </div>
</template>

  <script>
    import { reactive, ref, computed} from 'vue';
    import { Toaster, toast } from 'vue-sonner'

  import { useStore } from "vuex";

  export default {
    name: 'EMailVerif',
    setup() {
      const data = reactive({
        
              email: '',
            
           });
      const store = useStore();
      
      const resend = async() => {

        try {
        data.email = store.getters.getEmail;
        toast(`Ein Link wurde an ${data.email} gesendet`);

        const res = await fetch('https://da.linguexplorer.com/api/resend', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
     });
     if(res.ok) {

      toast('Email gesendet');

     }


     
    } catch (error) {
      console.error('Fehler beim Senden der Anfrage:', error);
  }
}

      return {
        resend,
        data
   
    };
    }

  };
  </script>
  
  