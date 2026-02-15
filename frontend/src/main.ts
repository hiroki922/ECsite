import './assets/main.css'
import './app.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { useAuthStore } from './stores/auth'

// アプリインスタンスを作成
const app = createApp(App)

// プラグイン登録
const pinia = createPinia()
app.use(pinia) // Pinia
app.use(router) // Vue Router

// ストアからログイン状態を復元し、バックエンドセッションを検証
const auth = useAuthStore()
auth.restoreFromStorage()

if (auth.isLoggedIn) {
  import('axios').then(({ default: axios }) => {
    axios
      .get<{ loggedIn: boolean }>('http://localhost:8080/api/me', { withCredentials: true })
      .then((res) => {
        if (!res.data.loggedIn) {
          auth.clearUser()
        }
      })
      .catch(() => {
        auth.clearUser()
      })
  })
}

// アプリをマウント
app.mount('#app')
