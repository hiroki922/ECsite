<template>
  <header class="p-4 bg-gray-800 text-white flex justify-between items-center">
    <h1 class="text-xl font-bold">ECサイト</h1>
    <nav class="space-x-4 flex items-center">
      <router-link to="/">Home</router-link>
      <router-link to="/products">Products</router-link>
      <router-link to="/cart">Cart</router-link>
      <router-link to="/orders">Orders</router-link>

      <template v-if="auth.isLoggedIn">
        <span class="text-gray-300 text-sm">ようこそ、{{ auth.userName }} さん</span>
        <button @click="logout" class="bg-red-500 px-3 py-1 rounded hover:bg-red-600">
          Logout
        </button>
      </template>

      <template v-else>
        <router-link to="/login" class="bg-blue-500 px-3 py-1 rounded hover:bg-blue-600">
          Login
        </router-link>
      </template>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import router from '@/router'
import axios from 'axios'

const auth = useAuthStore()

async function logout(): Promise<void> {
  try {
    await axios.post('http://localhost:8080/api/logout', {}, { withCredentials: true })
  } finally {
    auth.clearUser()
    router.push('/login')
  }
}
</script>
