<template>
    <div class="font-vcr m-0 p-3 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col">
        <section class="flex flex-col justify-center items-center gap-[20px] min-w-[700px]">
            <h1 class="font-pixelsplitter text-[60px] mb-6 mt-[200px]">Coming Soon</h1>
        
           
            <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="min-w-[320px] absolute z-2 translate-x-[-42vw] translate-y-[-12vh]" />
            <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="min-w-[150px] absolute z-1 translate-x-[-38vw] translate-y-[-3vh]" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="min-w-[320px] absolute z-2 translate-x-[-45vw] translate-y-[7vh]" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="min-w-[320px] absolute z-1 translate-x-[30vw] translate-y-[-8vh]" />
           
        </section>
    
</div>
</template>

<script>
export default {
    name: 'ComingSoon',
    methods: {
        goBack() {
            this.$router.go(-1);
        },
        goToRegister() {
            this.$router.push('/register');
        }
    }
};
</script>
