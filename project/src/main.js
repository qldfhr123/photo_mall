import { createApp } from 'vue'
import store from "@/script/store"
import router from "@/script/router"
import App from './App.vue'


createApp(App).use(store).use(router).mount('#app')



