<template>
  
    <div class="  flex flex-row items-center justify-start bg-white border-[#9cc2d3] border-4 pr-10">
      <img src="@/assets/xx_Images/error.png"  class="max-h-[70px]" />

      
        <p class="font-vcr text-black text-sm  py-8">{{ message }}</p>
      
    </div>

</template>

  
<script>
export default {
  props: {
    message: {
      default: "Link ist ungültig"
    }
  }
}
</script>