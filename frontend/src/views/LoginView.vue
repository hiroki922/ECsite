<template>
  <div class="flex justify-center items-center min-h-screen bg-gray-100">
    <div class="bg-white p-10 rounded-2xl shadow-lg w-96">
      <h1 class="text-3xl font-bold text-center mb-6 text-gray-700">ログイン</h1>

      <form @submit.prevent="handleLogin">
        <!-- メールアドレス -->
        <div class="mb-4">
          <BaseInput v-model="email" type="email" placeholder="メールアドレス" />
        </div>

        <!-- パスワード -->
        <div class="mb-4">
          <BaseInput v-model="password" type="password" placeholder="パスワード" />
        </div>

        <!-- ログインボタン -->
        <div class="mt-6">
          <button
            type="submit"
            class="w-full bg-blue-500 text-white py-2 rounded-lg hover:bg-blue-600 transition"
          >
            ログイン
          </button>
        </div>

        <!-- 登録フォームへの案内 -->
        <div class="text-sm text-center text-gray-600 mt-4">
          <p>アカウントをお持ちでないですか？</p>
          <p>
            <a href="/register" class="text-blue-500 hover:underline">登録はこちら</a>
          </p>
        </div>
        <!-- エラーメッセージ -->
        <p v-if="errorMessage" class="text-red-500 text-sm mt-4 text-center">
          {{ errorMessage }}
        </p>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import BaseInput from '@/components/BaseInput.vue'
import { useAuthStore } from '@/stores/auth'
import router from '@/router'

// フォーム入力
const email = ref('')
const password = ref('')
const errorMessage = ref('')
const auth = useAuthStore()

// バックエンドレスポンス型
interface User {
  name: string
  email: string
}

// サーバーから返ってくるデータの型
interface LoginResponse {
  loggedIn: boolean
  user?: User
  message?: string
}

const handleLogin = async () => {
  errorMessage.value = '' // エラーメッセージをリセット

  try {
    // Sping Bootのバックエンドにログインリクエストを送信
    // ここではaxiosを使ってPOSTリクエストを送信します
    const res = await axios.post<LoginResponse>(
      'http://localhost:8080/api/login',
      {
        email: email.value,
        password: password.value,
      },
      { withCredentials: true }
    ) // withCredentialsをtrueに設定してクッキーを送信

    const loginData = res.data

    // ログイン成功時の処理
    if (loginData.loggedIn && loginData.user) {
      // ユーザー情報をPiniaストアに保存
      auth.setUser(loginData.user)
      router.push('/')
    } else {
      errorMessage.value = loginData.message || 'メールアドレスまたはパスワードが間違っています'
    }
  } catch (err) {
    // エラーハンドリング
    const axiosError = err as { response?: { data?: { error?: string } } }
    errorMessage.value =
      axiosError.response?.data?.error || 'メールアドレスまたはパスワードが間違っています'
  }
}
</script>
