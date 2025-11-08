<template>
  <div class="p-6">
    <h2 class="text-2xl font-bold mb-4">商品一覧</h2>
    <div v-if="products.length === 0">商品がありません。</div>
    <div class="grid grid-cols-3 gap-4">
      <div v-for="p in products" :key="p.id" class="border p-4 rounded">
        <img :src="p.imageUrl" alt="" class="w-full h-40 object-cover mb-2" />
        <h3 class="font-bold">{{ p.name }}</h3>
        <p>{{ p.description }}</p>
        <p class="text-blue-600 font-bold">¥{{ p.price.toLocaleString() }} 円</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { fetchProducts } from '@/api/products'

const products = ref<any[]>([])

onMounted(async () => {
  products.value = await fetchProducts()
})
</script>
