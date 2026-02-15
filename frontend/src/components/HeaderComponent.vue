<template>
  <header class="p-4 bg-gray-800 text-white flex justify-between items-center">
    <router-link :to="homePath" class="text-xl font-bold">
      ECサイト
    </router-link>
    <nav class="space-x-4 flex items-center">
      <router-link :to="homePath">Home</router-link>

      <!-- 未ログイン -->
      <template v-if="!auth.isLoggedIn">
        <router-link to="/products">商品一覧</router-link>
        <router-link
          to="/login"
          class="bg-blue-500 text-white font-semibold px-3 py-1 rounded hover:bg-blue-600"
        >
          ログイン
        </router-link>
        <router-link
          to="/register"
          class="bg-green-500 text-white font-semibold px-3 py-1 rounded hover:bg-green-600"
        >
          新規登録
        </router-link>
      </template>

      <!-- 一般ユーザー -->
      <template v-else-if="auth.isLoggedIn && auth.role === 'USER'">
        <router-link to="/products">商品一覧</router-link>
        <router-link to="/cart" class="relative">
          カート
          <span
            v-if="cartStore.itemCount > 0"
            class="absolute -top-2 -right-3 bg-red-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center"
          >
            {{ cartStore.itemCount }}
          </span>
        </router-link>
        <router-link to="/orders">注文履歴</router-link>
        <router-link to="/addresses">配送先設定</router-link>
        <span class="text-gray-300 text-sm">ようこそ、{{ auth.userName }} さん</span>
        <button
          @click="logout"
          class="bg-red-500 text-white font-semibold px-3 py-1 rounded hover:bg-red-600"
        >
          ログアウト
        </button>
      </template>

      <!-- 管理者 -->
      <template v-else-if="auth.isLoggedIn && auth.role === 'ADMIN'">
        <router-link
          to="/admin"
          class="bg-green-500 text-white font-semibold px-3 py-1 rounded hover:bg-green-600"
        >
          管理者ページ
        </router-link>
        <span class="text-gray-300 text-sm">ようこそ、{{ auth.userName }} さん</span>
        <button
          @click="logout"
          class="bg-red-500 text-white font-semibold px-3 py-1 rounded hover:bg-red-600"
        >
          ログアウト
        </button>
      </template>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { useCartStore } from '@/stores/cart'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { computed, onMounted } from 'vue'

const router = useRouter()

const auth = useAuthStore()
const cartStore = useCartStore()
const homePath = computed(() => (auth.isLoggedIn && auth.role === 'ADMIN' ? '/admin' : '/'))

onMounted(() => {
  if (auth.isLoggedIn && auth.role === 'USER') {
    cartStore.load()
  }
})

async function logout(): Promise<void> {
  try {
    await axios.post('http://localhost:8080/api/logout', {}, { withCredentials: true })
  } finally {
    auth.clearUser()
    router.push('/login')
  }
}
</script>
