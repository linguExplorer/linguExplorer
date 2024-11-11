<template>
    <div class="font-vcr m-0 p-3 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-center">
        <header class="top-0">
            <section class="mx-auto p-4 flex  justify-end items-center gap-8">
                    
                    <p class=" font-size:18px font-vcr">Hast du schon einen Account?</p>
            
                    <button class=" max-w-[150px]">
                        <router-link to="/anmelden" class="nav-link"  href="#">

                    <img src="@/assets/xx_Images/xx_Images/Buttons/button anmelden green.png" alt="Registrieren" class=" hover:opacity-80" />
                        </router-link>
                </button>
            </section>
        </header>

        <main class="max-w-6xl mx-auto mt-[80px]">
            <img src="@/assets/xx_Images/xx_Images/cloud.png" alt="Wolke" class="min-w-[320px] xxl:min-w-[380px] absolute z-2 translate-x-[-42vw] translate-y-[-12vh] 
            xxl:translate-x-[-42vw] xxl:translate-y-[-11vh]" />
            <img src="@/assets/xx_Images/xx_Images/sun.png" alt="Sonne" class="min-w-[150px] xxl:min-w-[180px] absolute z-1 translate-x-[-38vw] translate-y-[-3vh]
            xxl:translate-x-[-38vw] xxl:translate-y-[-3vh]" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="min-w-[320px] xxl:min-w-[380px] absolute z-2 translate-x-[-45vw] translate-y-[7vh]
        xxl:translate-x-[-45vw] xxl:translate-y-[5vh]" />
        <img src="@/assets/xx_Images/xx_Images/Cloud2.png" alt="Wolke" class="min-w-[320px] xxl:min-w-[380px] absolute z-1 translate-x-[30vw] translate-y-[-8vh]
        xxl:translate-x-[30vw] xxl:translate-y-[-8vh]" />
    
        <!--<div class="anmelde-container">-->
            <section class="flex flex-col justify-center items-center gap-[20px] min-w-[700px]">
        <h1 class="font-pixelsplitter text-[60px] mb-6">Registrieren</h1>

            <form @submit.prevent="handleRegister" class="min-w-[600px] max-w-md flex flex-col  items-center mt-[30px]">
                <div class="input-group flex flex-col mb-4 ">
                    <label for="email" class="">E-Mail</label>
                <input type="email" id="email" v-model="username" required class="
                font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2" />
                </div>
                <div class="input-group flex flex-col mb-4 ">
                    <label for="uname" class="">Benutzername</label>
                <input type="uname" id="email" v-model="username" required class="
                font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2" />
                    <p v-if="usernameError" class="error-message">Benutzername bereits vergeben.</p>
                </div>
                <div class="input-group flex flex-col mb-4 ">
                    <label for="password" class="">Passwort</label>
                <input type="password" id="password" v-model="password" required class="
                 font-vcr bg-white border-[#9cb405] border-[2px]  min-w-[500px] p-2" />
                    <p v-if="passwordError" class="error-message">Passwort entspricht nicht den Anforderungen.</p>
                </div>
                <div class="input-group flex flex-col mb-4 ">
                    <label for="password" class="">Passwort Wiederholen</label>
                <input type="password" id="password" v-model="password" required class="
                 font-vcr bg-white border-[#9cb405] border-[2px]  min-w-[500px] p-2 " />
                    <p v-if="confirmPasswordError" class="error-message">Passwörter stimmen nicht überein.</p>
                </div>
                
                <!-- Speicher-Button -->
                <button type="submit" class="max-w-[150px] mt-6 w-full">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button registrieren blue.png" alt="Registrieren" class="hover:opacity-80" />
                </button>
            </form>
        </section>
       
    </main>

    <footer class="mt-auto">
        <section class="mx-auto p-4 flex  justify-end items-center">
            <button class="max-w-[200px] p-4" >
                <router-link to="/" class="nav-link"  href="#">
                <img src="@/assets/xx_Images/xx_Images/Buttons/button zrk.png" alt="Zurück" class="hover:opacity-80"/>
                </router-link>
            </button>
        </section>
    </footer>
    </div>
</template>

<script>
export default {
    name: 'Registrieren',
    data() {
        return {
            email: '',
            username: '',
            password: '',
            confirmPassword: '',
            emailError: false,
            usernameError: false,
            passwordError: false,
            confirmPasswordError: false,
        };
    },
    methods: {
        handleRegister() {
            console.log('Registrieren mit:', this.email, this.username, this.password);
        },
        goBack() {
            this.$router.go(-1);
        },
        goToLogin() {
            this.$router.push('/login');
        }
    }
};
</script>

