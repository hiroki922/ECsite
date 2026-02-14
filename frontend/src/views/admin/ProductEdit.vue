<template>
  <div class="min-h-screen bg-gray-100">
    <div class="max-w-lg mx-auto px-4 py-8">
      <router-link to="/admin/products" class="text-blue-600 hover:underline text-sm mb-4 inline-block">
        &larr; 商品一覧に戻る
      </router-link>

      <h1 class="text-2xl font-bold mb-4">商品編集</h1>

      <div v-if="loading" class="text-gray-500">読み込み中...</div>
      <form v-else @submit.prevent="handleSubmit" class="bg-white rounded-2xl shadow p-6 space-y-4">
        <div>
          <label class="block font-semibold text-sm text-gray-700 mb-1">商品名</label>
          <BaseInput v-model="product.name" required />
        </div>
        <div>
          <label class="block font-semibold text-sm text-gray-700 mb-1">説明</label>
          <textarea v-model="product.description" class="w-full border rounded-lg px-3 py-2"></textarea>
        </div>
        <div>
          <label class="block font-semibold text-sm text-gray-700 mb-1">価格</label>
          <input v-model.number="product.price" type="number" class="w-full border rounded-lg px-3 py-2" required />
        </div>
        <div>
          <label class="block font-semibold text-sm text-gray-700 mb-1">在庫</label>
          <input v-model.number="product.stock" type="number" class="w-full border rounded-lg px-3 py-2" required />
        </div>
        <div>
          <label class="block font-semibold text-sm text-gray-700 mb-1">画像URL</label>
          <BaseInput v-model="product.imageUrl" />
        </div>

        <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 transition">
          更新する
        </button>

        <p v-if="message" class="text-green-600 text-sm text-center">{{ message }}</p>
        <p v-if="error" class="text-red-600 text-sm text-center">{{ error }}</p>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchProduct, updateProduct, type Product } from '@/api/products'
import BaseInput from '@/components/BaseInput.vue'

const route = useRoute()
const router = useRouter()

const product = ref<Product>({ name: '', description: '', price: 0, imageUrl: '', stock: 0 })
const loading = ref(true)
const message = ref('')
const error = ref('')

onMounted(async () => {
  try {
    const id = Number(route.params.id)
    product.value = await fetchProduct(id)
  } catch {
    error.value = '商品が見つかりません'
  } finally {
    loading.value = false
  }
})

const handleSubmit = async () => {
  message.value = ''
  error.value = ''
  try {
    await updateProduct(product.value)
    message.value = '更新しました'
    setTimeout(() => router.push('/admin/products'), 1000)
  } catch {
    error.value = '更新に失敗しました'
  }
}
</script>
