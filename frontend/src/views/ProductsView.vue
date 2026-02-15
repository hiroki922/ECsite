<template>
  <div class="bg-gray-100 min-h-screen py-10">
    <div class="max-w-6xl mx-auto px-6">
      <h2 class="text-3xl font-bold text-gray-800 mb-6">商品一覧</h2>

      <div v-if="loading" class="text-gray-500">読み込み中...</div>
      <div v-else-if="error" class="text-red-500">{{ error }}</div>
      <div v-else-if="products.length === 0" class="text-gray-500">商品がありません。</div>
      <div v-else class="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
        <router-link
          v-for="p in products"
          :key="p.id"
          :to="'/products/' + p.id"
          class="bg-white rounded-xl shadow hover:shadow-lg transition overflow-hidden block"
        >
          <img :src="p.imageUrl" :alt="p.name" class="w-full h-48 object-cover" />
          <div class="p-4">
            <h3 class="font-bold text-gray-800">{{ p.name }}</h3>
            <p class="text-gray-500 text-sm mt-1 line-clamp-2">{{ p.description }}</p>
            <p class="text-blue-600 font-bold mt-2">&yen;{{ p.price.toLocaleString() }}</p>
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { fetchProducts, type Product } from '@/api/products'

const products = ref<Product[]>([])
const loading = ref(true)
const error = ref('')

onMounted(async () => {
  try {
    products.value = await fetchProducts()
  } catch {
    error.value = '商品の取得に失敗しました'
  } finally {
    loading.value = false
  }
})
</script>
