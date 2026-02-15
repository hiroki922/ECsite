<template>
  <div class="bg-gray-100 min-h-screen">
    <section class="bg-blue-600 text-white py-20 text-center">
      <h1 class="text-4xl font-bold mb-4">ECサイトへようこそ</h1>
      <p class="text-lg mb-8">お気に入りの商品を見つけましょう</p>
      <router-link
        to="/products"
        class="inline-block bg-white text-blue-600 font-semibold px-8 py-3 rounded-lg hover:bg-gray-100 transition"
      >
        商品一覧を見る
      </router-link>
    </section>

    <section class="max-w-6xl mx-auto px-6 py-12">
      <h2 class="text-2xl font-bold text-gray-800 mb-6">おすすめ商品</h2>
      <div v-if="products.length === 0" class="text-gray-500">読み込み中...</div>
      <div v-else class="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-6">
        <router-link
          v-for="p in products.slice(0, 8)"
          :key="p.id"
          :to="'/products/' + p.id"
          class="bg-white rounded-xl shadow hover:shadow-lg transition overflow-hidden block"
        >
          <img :src="p.imageUrl" :alt="p.name" class="w-full h-48 object-cover" />
          <div class="p-4">
            <h3 class="font-bold text-gray-800">{{ p.name }}</h3>
            <p class="text-blue-600 font-bold mt-1">&yen;{{ p.price.toLocaleString() }}</p>
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

onMounted(async () => {
  try {
    products.value = await fetchProducts()
  } catch {
    // トップページなのでエラーは静かに処理
  }
})
</script>
