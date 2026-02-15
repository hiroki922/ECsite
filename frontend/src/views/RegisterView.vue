<template>
  <div class="flex justify-center items-center min-h-[80vh] py-12">
    <div class="bg-white p-10 rounded-2xl shadow-sm w-full max-w-md">
      <h1 class="text-3xl font-bold text-center mb-8 text-gray-800 tracking-tight">新規登録</h1>

      <template v-if="!isSuccess">
        <form @submit.prevent="handleRegister" class="space-y-5">
          <div>
            <label class="block text-sm font-medium mb-1.5 text-gray-600">名前</label>
            <BaseInput v-model="name" type="text" placeholder="山田 太郎" />
          </div>

          <div>
            <label class="block text-sm font-medium mb-1.5 text-gray-600">メールアドレス</label>
            <BaseInput v-model="email" type="email" placeholder="example@mail.com" />
          </div>

          <div>
            <label class="block text-sm font-medium mb-1.5 text-gray-600">パスワード</label>
            <BaseInput v-model="password" type="password" placeholder="********" />
          </div>

          <div>
            <label class="block text-sm font-medium mb-1.5 text-gray-600">パスワード確認</label>
            <BaseInput v-model="confirmPassword" type="password" placeholder="********" />
          </div>

          <button
            type="submit"
            class="w-full bg-emerald-600 text-white py-2.5 rounded-xl font-semibold hover:bg-emerald-700 hover:shadow-md transition-all duration-200"
          >
            登録する
          </button>

          <p v-if="errorMessage" class="text-rose-500 text-sm text-center">
            {{ errorMessage }}
          </p>
        </form>
      </template>

      <template v-else>
        <div class="text-center space-y-5">
          <p class="text-2xl font-semibold text-emerald-600">登録が完了しました</p>
          <p class="text-gray-500">ログインしてお買い物を始めましょう。</p>
          <button
            @click="goLogin"
            class="w-full bg-indigo-600 text-white py-2.5 rounded-xl font-semibold hover:bg-indigo-700 hover:shadow-md transition-all duration-200"
          >
            ログイン画面へ
          </button>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import BaseInput from '@/components/BaseInput.vue'
import client from '@/lib/apiClient'
import { useRouter } from 'vue-router'

const router = useRouter()

const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const errorMessage = ref('')
const isSuccess = ref(false)

interface RegisterResponse {
  id: number
  name: string
}

const handleRegister = async () => {
  if (!name.value || !email.value || !password.value || !confirmPassword.value) {
    errorMessage.value = '全ての項目を入力してください'
    return
  }
  if (password.value !== confirmPassword.value) {
    errorMessage.value = 'パスワードが一致しません'
    return
  }
  try {
    await client.post<RegisterResponse>('/register', {
      name: name.value,
      email: email.value,
      password: password.value,
    })
    errorMessage.value = ''
    isSuccess.value = true
  } catch (err) {
    const axiosError = err as { response?: { data?: { error?: string } }; message: string }
    errorMessage.value = '登録に失敗しました: ' + (axiosError.response?.data?.error || axiosError.message)
  }
}

const goLogin = () => {
  router.push('/login')
}
</script>
