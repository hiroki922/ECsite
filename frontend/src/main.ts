import './assets/main.css'
import './app.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import './app.css'

// ① アプリインスタンスを作成
const app = createApp(App)

// ② プラグイン登録
app.use(createPinia()) // Pinia
app.use(router) // Vue Router

// ③ アプリをマウント
app.mount('#app')
