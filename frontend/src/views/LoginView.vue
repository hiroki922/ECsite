<template>
  <div class="flex justify-center items-center min-h-[80vh] py-12">
    <div class="bg-white p-10 rounded-2xl shadow-sm w-full max-w-md">
      <h1 class="text-3xl font-bold text-center mb-8 text-gray-800 tracking-tight">ログイン</h1>

      <form @submit.prevent="handleLogin" class="space-y-5">
        <div>
          <label class="block text-sm font-medium mb-1.5 text-gray-600">メールアドレス</label>
          <BaseInput v-model="email" type="email" placeholder="example@mail.com" />
        </div>

        <div>
          <label class="block text-sm font-medium mb-1.5 text-gray-600">パスワード</label>
          <BaseInput v-model="password" type="password" placeholder="********" />
        </div>

        <button
          type="submit"
          class="w-full bg-indigo-600 text-white py-2.5 rounded-xl font-semibold hover:bg-indigo-700 hover:shadow-md transition-all duration-200"
        >
          ログイン
        </button>

        <div class="text-sm text-center text-gray-500 pt-2">
          <p>アカウントをお持ちでないですか？</p>
          <router-link to="/register" class="text-indigo-600 hover:text-indigo-800 font-medium transition">
            新規登録はこちら
          </router-link>
        </div>

        <p v-if="errorMessage" class="text-rose-500 text-sm text-center">
          {{ errorMessage }}
        </p>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import client from '@/lib/apiClient'
import BaseInput from '@/components/BaseInput.vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const router = useRouter()

const email = ref('')
const password = ref('')
const errorMessage = ref('')
const auth = useAuthStore()

interface LoginResponse {
  loggedIn: boolean
  user?: { name: string }
  role?: string
  message?: string
}

const handleLogin = async () => {
  errorMessage.value = ''

  try {
    const res = await client.post<LoginResponse>('/login', {
      email: email.value,
      password: password.value,
    })

    const loginData = res.data

    if (loginData.loggedIn && loginData.user) {
      const role = loginData.role?.toUpperCase() === 'ADMIN' ? 'ADMIN' : 'USER'
      auth.setUser({ name: loginData.user.name, role })
      router.push('/')
    } else {
      errorMessage.value = loginData.message || 'メールアドレスまたはパスワードが間違っています'
    }
  } catch (err) {
    const axiosError = err as { response?: { data?: { error?: string } } }
    errorMessage.value =
      axiosError.response?.data?.error || 'メールアドレスまたはパスワードが間違っています'
  }
}
</script>
