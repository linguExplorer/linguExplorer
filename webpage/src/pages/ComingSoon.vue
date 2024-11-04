<template>
    <div class="page-wrapper-commingSoon">
           
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="left-cloud-bottom-commingSoon" />
        <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="sun-image-commingSoon" />
        <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="left-cloud-top-commingSoon" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="right-cloud-middle-commingSoon" />

            <div class="coming-soon-text">
                <h1>Coming Soon</h1>
            </div>  
    </div>
</template>

<script>
import '../styles/ComingSoon.css';
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
