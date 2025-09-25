<template>
  <div class="flex justify-center items-center min-h-screen bg-gray-100">
    <div class="bg-white p-10 rounded-2xl shadow-lg w-96">
      <h1 class="text-3xl font-bold text-center mb-6 text-gray-700">
        ログイン
      </h1>

      <form @submit.prevent="handleLogin">
        <!-- メールアドレス -->
        <div class="mb-4">
          <BaseInput
            v-model="email"
            type="email"
            placeholder="メールアドレス"
          />
        </div>

        <!-- パスワード -->
        <div class="mb-4">
          <BaseInput
            v-model="password"
            type="password"
            placeholder="パスワード"
          />
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
          <p >
            アカウントをお持ちでないですか？
          </p>
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
import { ref } from "vue";
import BaseInput from "@/components/BaseInput.vue";

const email = ref("");
const password = ref("");
const errorMessage = ref("");

const handleLogin = () => {
  if (email.value === "test@example.com" && password.value === "password") {
    alert("ログイン成功 🎉");
  } else {
    errorMessage.value = "メールアドレスまたはパスワードが間違っています";
  }
};
</script>
