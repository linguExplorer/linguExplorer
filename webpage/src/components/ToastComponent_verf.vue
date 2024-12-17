<template>

  
    <div class="  flex flex-row items-center justify-center bg-white border-[#9cc2d3] border-4 pr-10 max-h-[60px] ml-[100px]">
      <img src="@/assets/xx_Images/check.png"  class="max-h-[60px]" />

      
        <p class="font-vcr text-black text-sm  py-8">{{ message }}</p>
      
    </div>


</template>

  
<script>
export default {
  props: {
    message: {
      default: "E-Mail erfolgreich verifiziert!"
    }
  }
}
</script>