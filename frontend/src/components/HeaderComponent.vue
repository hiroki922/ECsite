<template>
  <header class="bg-gradient-to-r from-indigo-700 to-purple-700 text-white shadow-lg">
    <div class="max-w-7xl mx-auto px-6 py-4 flex justify-between items-center">
      <router-link :to="homePath" class="text-2xl font-bold tracking-tight hover:opacity-90 transition">
        ECサイト
      </router-link>
      <nav class="flex items-center gap-5">
        <router-link :to="homePath" class="text-white/80 hover:text-white transition text-sm font-medium">
          Home
        </router-link>

        <!-- 未ログイン -->
        <template v-if="!auth.isLoggedIn">
          <router-link to="/products" class="text-white/80 hover:text-white transition text-sm font-medium">
            商品一覧
          </router-link>
          <router-link
            to="/login"
            class="bg-white/20 backdrop-blur text-white font-semibold px-4 py-1.5 rounded-xl hover:bg-white/30 transition text-sm"
          >
            ログイン
          </router-link>
          <router-link
            to="/register"
            class="bg-white text-indigo-700 font-semibold px-4 py-1.5 rounded-xl hover:bg-gray-100 transition text-sm"
          >
            新規登録
          </router-link>
        </template>

        <!-- 一般ユーザー -->
        <template v-else-if="auth.role === 'USER'">
          <router-link to="/products" class="text-white/80 hover:text-white transition text-sm font-medium">
            商品一覧
          </router-link>
          <router-link to="/cart" class="relative text-white/80 hover:text-white transition text-sm font-medium">
            カート
            <span
              v-if="cartStore.itemCount > 0"
              class="absolute -top-2 -right-4 bg-rose-500 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center font-bold"
            >
              {{ cartStore.itemCount }}
            </span>
          </router-link>
          <router-link to="/orders" class="text-white/80 hover:text-white transition text-sm font-medium">
            注文履歴
          </router-link>
          <router-link to="/addresses" class="text-white/80 hover:text-white transition text-sm font-medium">
            配送先
          </router-link>
          <span class="text-white/60 text-sm">{{ auth.userName }} さん</span>
          <button
            @click="logout"
            class="bg-white/20 backdrop-blur text-white font-semibold px-4 py-1.5 rounded-xl hover:bg-white/30 transition text-sm"
          >
            ログアウト
          </button>
        </template>

        <!-- 管理者 -->
        <template v-else-if="auth.role === 'ADMIN'">
          <router-link
            to="/admin"
            class="bg-white text-indigo-700 font-semibold px-4 py-1.5 rounded-xl hover:bg-gray-100 transition text-sm"
          >
            管理者ページ
          </router-link>
          <span class="text-white/60 text-sm">{{ auth.userName }} さん</span>
          <button
            @click="logout"
            class="bg-white/20 backdrop-blur text-white font-semibold px-4 py-1.5 rounded-xl hover:bg-white/30 transition text-sm"
          >
            ログアウト
          </button>
        </template>
      </nav>
    </div>
  </header>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { useCartStore } from '@/stores/cart'
import { useRouter } from 'vue-router'
import client from '@/lib/apiClient'
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
    await client.post('/logout')
  } finally {
    auth.clearUser()
    router.push('/login')
  }
}
</script>
