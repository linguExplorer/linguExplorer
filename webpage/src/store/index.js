import { createStore } from "vuex";

const store = createStore({
  state() {
    return {
      isLoggedIn: false,
      email: "",
    };
  },
  mutations: {
    login(state) {
      state.isLoggedIn = true;
    },
    logout(state) {
      state.isLoggedIn = false;
    },
    setEmail(state, email) {
      state.email = email;
    },
    clearEmail(state) {
      state.email = "";
    },
  },
  actions: {
    login(context) {
      context.commit("login");
    },
    logout(context) {
      context.commit("logout");
    },
    updateEmail(context, email) {
      context.commit("setEmail", email);
    },
    removeEmail(context) {
      context.commit("clearEmail");
    },
  },
  getters: {
    isAuthenticated(state) {
      return state.isLoggedIn;
    },
    getEmail(state) {
      return state.email;
    },
  },
});

export default store;
