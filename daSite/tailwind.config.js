/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./*.{html,js}", "./!(build|dist|.*)/**/*.{html,js}"],
  theme: {
    extend: {
      colors: {
        floralwhite: "#fdfaed",
        gray: {
          "100": "#1c2121",
          "200": "rgba(42, 30, 30, 0.31)",
        },
        gainsboro: "#d9d9d9",
        yellowgreen: {
          "100": "#99b305",
          "200": "rgba(153, 179, 5, 0.35)",
        },
        goldenrod: "#edd351",
        lightblue: "#9cc2d3",
        orange: "#ffa21f",
      },
      spacing: {},
      fontFamily: {
        "balsamiq-sans": "'Balsamiq Sans'",
        nunito: "Nunito",
        "fredoka-one": "'Fredoka One'",
      },
      borderRadius: {
        "137xl-5": "156.5px",
        "112xl": "131px",
        "34xl": "53px",
        "17xl": "36px",
      },
    },
    fontSize: {
      "5xl": "1.5rem",
      lgi: "1.188rem",
      "17xl": "2.25rem",
      "3xl": "1.375rem",
      "10xl": "1.813rem",
      "29xl": "3rem",
      "19xl": "2.375rem",
      "13xl": "2rem",
      inherit: "inherit",
    },
    screens: {
      mq1575: {
        raw: "screen and (max-width: 1575px)",
      },
      mq1250: {
        raw: "screen and (max-width: 1250px)",
      },
      mq825: {
        raw: "screen and (max-width: 825px)",
      },
      mq450: {
        raw: "screen and (max-width: 450px)",
      },
    },
  },
  corePlugins: {
    preflight: false,
  },
};
