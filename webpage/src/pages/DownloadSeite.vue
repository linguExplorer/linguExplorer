<template>
    <div class="font-vcr m-0 p-3 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col">
        <section class="flex flex-col justify-center items-center gap-[20px] min-w-[700px]">
            <h1 class="font-pixelsplitter text-[60px] mb-6 mt-[200px]">Download hier</h1>
        
            <a
      
      class="download-link"
      @click="download"
    >
      Jetzt herunterladen
    </a>
           
            <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="min-w-[320px] absolute z-2 translate-x-[-42vw] translate-y-[-12vh]" />
            <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="min-w-[150px] absolute z-1 translate-x-[-38vw] translate-y-[-3vh]" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="min-w-[320px] absolute z-2 translate-x-[-45vw] translate-y-[7vh]" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="min-w-[320px] absolute z-1 translate-x-[30vw] translate-y-[-8vh]" />
           
        </section>
    
</div>
</template>
<script>
  import { useStore } from "vuex";
  import { reactive, ref } from "vue";
  import { useRoute } from 'vue-router';
  import { shallowRef } from 'vue';
  import { Toaster, toast } from "vue-sonner";

  export default {
  name: "Download",
  setup() {
    const download = async () => {
  try {
    const res = await fetch("https://da.linguexplorer.com/api/download?userid=12345", {
      headers: { "Content-Type": "application/json" },
      credentials: "include",
    });

    if (!res.ok) {
      console.log('Fehler beim Herunterladen der Datei');
      return;
    }

    // Extrahiere den Dateinamen aus dem Content-Disposition-Header
    const contentDisposition = res.headers.get('Content-Disposition');
    const filename = contentDisposition
      ? contentDisposition.split('filename=')[1].replace(/['"]/g, '')
      : 'linguExplorer.exe';

    // Konvertiere den Response in einen Blob
    const blob = await res.blob();

    // Erstelle einen temporären Link zum Herunterladen der Datei
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename; // Setze den Dateinamen
    document.body.appendChild(a);
    a.click(); // Klicke den Link an, um den Download zu starten

    // Aufräumen
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);

    console.log('Datei erfolgreich heruntergeladen');
  } catch (e) {
    console.error("Fehler beim Senden der Anfrage:", e);
  }
};

    return {
    download
  };
 
  }
};
</script>

