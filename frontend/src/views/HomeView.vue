<template>
  <div>
    <section class="bg-gradient-to-br from-indigo-600 via-purple-600 to-indigo-800 text-white py-24 text-center">
      <div class="max-w-3xl mx-auto px-6">
        <h1 class="text-5xl font-bold mb-4 tracking-tight">ECサイトへようこそ</h1>
        <p class="text-xl text-white/80 mb-10">お気に入りの商品を見つけましょう</p>
        <router-link
          to="/products"
          class="inline-block bg-white text-indigo-700 font-semibold px-8 py-3 rounded-xl hover:bg-gray-100 hover:shadow-lg transition-all duration-200"
        >
          商品一覧を見る
        </router-link>
      </div>
    </section>

    <section class="max-w-7xl mx-auto px-6 py-16">
      <h2 class="text-2xl font-bold text-gray-800 mb-8">おすすめ商品</h2>
      <div v-if="loading" class="text-gray-400">読み込み中...</div>
      <div v-else-if="error" class="text-rose-500">{{ error }}</div>
      <div v-else-if="products.length === 0" class="text-gray-400">商品がありません</div>
      <div v-else class="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
        <router-link
          v-for="p in products.slice(0, 8)"
          :key="p.id"
          :to="'/products/' + p.id"
          class="bg-white rounded-2xl shadow-sm hover:shadow-md hover:-translate-y-1 transition-all duration-200 overflow-hidden block"
        >
          <img :src="p.imageUrl" :alt="p.name" class="w-full h-48 object-cover" />
          <div class="p-5">
            <h3 class="font-bold text-gray-800">{{ p.name }}</h3>
            <p class="text-indigo-600 font-bold mt-2">&yen;{{ p.price.toLocaleString() }}</p>
          </div>
        </router-link>
      </div>
    </section>
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
