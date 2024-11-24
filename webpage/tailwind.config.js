/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      screens: {
        'xxl': '1900px',

      },
      backgroundImage: {
        'grass-background': "url('@/assets/xx_Images/xx_Images/gras_combined_scaled.png')",
        'cloud-background': "url('@/assets/xx_Images/xx_Images/cloud_bg_lg.png')"

      },
      fontFamily: {
        vcr: ['"VCR OSD Mono"', 'monospace'],
        pixelsplitter: ['"Pixelsplitter"', 'sans-serif'], 
      }

    },
  },
  plugins: [],
}

