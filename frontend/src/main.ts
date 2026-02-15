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
  import('./lib/apiClient').then(({ default: apiClient }) => {
    apiClient
      .get<{ loggedIn: boolean }>('/me')
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
