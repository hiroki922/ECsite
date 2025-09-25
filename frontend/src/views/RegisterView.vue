<template>
  <div class="flex justify-center items-center min-h-screen bg-gray-100">
    <div class="bg-white p-10 rounded-2xl shadow-lg w-96">
      <!-- タイトル -->
      <h1 class="text-3xl font-bold text-center mb-6 text-gray-700">新規登録</h1>

      <form @submit.prevent="handleRegister">
        <!-- 名前 -->
        <div class="mb-4">
          <label class="block text-sm font-medium mb-1 text-gray-700">名前</label>
          <BaseInput
            v-model="name"
            type="text"
            placeholder="山田 太郎"
          />
        </div>

        <!-- メールアドレス -->
        <div class="mb-4">
          <label class="block text-sm font-medium mb-1 text-gray-700">メールアドレス</label>
          <BaseInput
            v-model="email"
            type="email"
            placeholder="example@mail.com"
          />
        </div>

        <!-- パスワード -->
        <div class="mb-4">
          <label class="block text-sm font-medium mb-1 text-gray-700">パスワード</label>
          <BaseInput
            v-model="password"
            type="password"
            placeholder="********"
          />
        </div>

        <!-- パスワード確認 -->
        <div class="mb-6">
          <label class="block text-sm font-medium mb-1 text-gray-700">パスワード確認</label>
          <BaseInput
            v-model="confirmPassword"
            type="password"
            placeholder="********"
          />
        </div>

        <!-- 登録ボタン -->
        <div>
          <button
            type="submit"
            class="w-full bg-green-500 text-white py-2 rounded-lg hover:bg-green-600 transition"
          >
            登録する
          </button>
        </div>
      </form>

      <p v-if="errorMessage" class="text-red-500 text-sm mt-4 text-center">
        {{ errorMessage }}
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import BaseInput from '@/components/BaseInput.vue'
import axios from 'axios'
const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const errorMessage = ref('')
const handleRegister = async () => {
  if (!name.value || !email.value || !password.value || !confirmPassword.value) {
    errorMessage.value = '全ての項目を入力してください'
    return
  }
  if (password.value !== confirmPassword.value) {
    errorMessage.value = 'パスワードが一致しません'
    return
  }
  // サーバーから返ってくるデータの型
  interface RegisterResponse {
    id: number
    name: string
  }
  try {
    const res = await axios.post<RegisterResponse>('http://localhost:8080/api/register', {
      name: name.value,
      email: email.value,
      password: password.value,
      confirmPassword: confirmPassword.value
    })
    errorMessage.value = `✅ 登録成功！ID: ${res.data.id}, 名前: ${res.data.name}`
  } catch (err) {
    const axiosError = err as { response?: { data?: { message?: string } }; message: string }
    errorMessage.value = "❌ 登録失敗: " + (axiosError.response?.data?.message || axiosError.message)
  }
}
</script>