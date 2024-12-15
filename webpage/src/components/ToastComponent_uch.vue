<template>

  
    <div class="  flex flex-row items-center justify-center bg-white border-[#9cc2d3] border-4 max-h-[60px] ml-[20px]">
      <img src="@/assets/xx_Images/error.png"  class="max-h-[60px]" />

      
        <p class="font-vcr text-black text-sm ">Benutzername erfolgreich geändert!</p>
      
    </div>


</template>

  
<script>
export default {
  props: {
    message: {
      default: "Benutzername erfolgreich geändert!"
    }
  }
}
</script>