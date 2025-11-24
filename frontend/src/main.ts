import './assets/main.css'
import './app.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import './app.css'
import { useAuthStore } from './stores/auth'

// アプリインスタンスを作成
const app = createApp(App)

// プラグイン登録
const pinia = createPinia()
app.use(pinia) // Pinia
app.use(router) // Vue Router

// ストアからログイン状態を復元
const auth = useAuthStore()
auth.restoreFromStorage()

// アプリをマウント
app.mount('#app')
