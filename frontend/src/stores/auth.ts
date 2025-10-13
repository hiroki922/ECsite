import { defineStore } from 'pinia'

// ユーザー情報の方を定義
interface User {
  name: string
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    isLoggedIn: false,
    userName: '',
  }),
  actions: {
    setUser(user: User) {
      this.isLoggedIn = true
      this.userName = user.name
    },
    clearUser() {
      this.isLoggedIn = false
      this.userName = ''
    },
  },
})
