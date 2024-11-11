<template>
    <div class="font-vcr m-0 p-3 text-black w-full bg-[#f6f5f1] min-h-screen flex flex-col justify-center">
        <!-- Überschrift -->
        <section class="flex flex-col justify-center items-center gap-[20px] min-w-[700px]">
            <h1 class="font-pixelsplitter text-[60px] mb-6">Neues Passwort erstellen</h1>
        

            <!-- Eingabeformular -->
            <form @submit.prevent="handlePasswordSave" class="min-w-[600px] max-w-md flex flex-col  justify-center  items-center mt-[30px]">
                <div class="input-group flex flex-col mb-4 ">
                    <label for="password" id="passwordLabel">Passwort (mind. 8 Zeichen)</label>
                    <input type="password" id="password" v-model="password" required minlength="8" class="
                    font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2"/>
                </div>
                
                <div class="input-group flex flex-col mb-4 ">
                    <label for="confirmPassword" id="confirmPasswordLabel">Passwort wiederholen</label>
                    <input type="password" id="confirmPassword" v-model="confirmPassword" required minlength="8" class="
                    font-vcr bg-white border-[#9cb405] border-[2px] min-w-[500px] p-2"/>
                </div>

                <!-- Speicher-Button -->
                <button type="submit" class="max-w-[150px] mt-6 w-full">
                    <img src="@/assets/xx_Images/xx_Images/Buttons/button speichern.png" alt="Speichern" />
                </button>

            </form>
            </section>

    </div>
</template>

<script>
export default {
    name: 'NewPassword',
    data() {
        return {
            password: '',
            confirmPassword: ''
        };
    },
    methods: {
        handlePasswordSave() {
            if (this.password === this.confirmPassword) {
                console.log('Neues Passwort speichern:', this.password);
            } else {
                console.error('Die Passwörter stimmen nicht überein.');
            }
        }
    }
};
</script>

