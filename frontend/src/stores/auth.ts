import { defineStore } from 'pinia'

const STORAGE_KEY = 'authState'

// 保存済みの状態を読み込む
function loadSavedAuth() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

// ユーザー情報の方を定義
interface User {
  name: string
  role: 'ADMIN' | 'USER'
}

export const useAuthStore = defineStore('auth', {
  state: () => {
    const saved = loadSavedAuth()
    const role = saved?.role === 'ADMIN' || saved?.role === 'USER' ? saved.role : ''
    return {
      isLoggedIn: saved?.isLoggedIn ?? false,
      userName: saved?.userName ?? '',
      role: role as 'ADMIN' | 'USER' | '',
    }
  },
  actions: {
    persist() {
      localStorage.setItem(
        STORAGE_KEY,
        JSON.stringify({
          isLoggedIn: this.isLoggedIn,
          userName: this.userName,
          role: this.role,
        })
      )
    },
    setUser(user: User) {
      this.isLoggedIn = true
      this.userName = user.name
      this.role = user.role
      this.persist()
    },
    clearUser() {
      this.isLoggedIn = false
      this.userName = ''
      this.role = ''
      this.persist()
    },
    restoreFromStorage() {
      const saved = loadSavedAuth()
      if (saved?.isLoggedIn && (saved.role === 'ADMIN' || saved.role === 'USER')) {
        this.isLoggedIn = true
        this.userName = saved.userName ?? ''
        this.role = saved.role
      }
    },
  },
})
