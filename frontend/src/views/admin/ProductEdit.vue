<template>
  <div class="py-10">
    <div class="max-w-lg mx-auto px-6">
      <router-link to="/admin/products" class="text-indigo-600 hover:text-indigo-800 text-sm mb-6 inline-block transition">
        &larr; 商品一覧に戻る
      </router-link>

      <h1 class="text-2xl font-bold text-gray-800 mb-6 tracking-tight">商品編集</h1>

      <div v-if="loading" class="text-gray-400">読み込み中...</div>
      <form v-else @submit.prevent="handleSubmit" class="bg-white rounded-2xl shadow-sm p-6 space-y-5">
        <div>
          <label class="block font-semibold text-sm text-gray-700 mb-1.5">商品名</label>
          <BaseInput v-model="product.name" required />
        </div>
        <div>
          <label class="block font-semibold text-sm text-gray-700 mb-1.5">説明</label>
          <textarea v-model="product.description" class="w-full border border-gray-200 rounded-xl px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-300"></textarea>
        </div>
        <div>
          <label class="block font-semibold text-sm text-gray-700 mb-1.5">価格</label>
          <input v-model.number="product.price" type="number" class="w-full border border-gray-200 rounded-xl px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-300" required />
        </div>
        <div>
          <label class="block font-semibold text-sm text-gray-700 mb-1.5">在庫</label>
          <input v-model.number="product.stock" type="number" class="w-full border border-gray-200 rounded-xl px-3 py-2 focus:outline-none focus:ring-2 focus:ring-indigo-300" required />
        </div>
        <div>
          <label class="block font-semibold text-sm text-gray-700 mb-1.5">画像URL</label>
          <BaseInput v-model="product.imageUrl" />
        </div>

        <button type="submit" class="w-full bg-indigo-600 text-white py-2.5 rounded-xl font-semibold hover:bg-indigo-700 hover:shadow-md transition-all duration-200">
          更新する
        </button>

        <p v-if="message" class="text-emerald-600 text-sm text-center">{{ message }}</p>
        <p v-if="error" class="text-rose-500 text-sm text-center">{{ error }}</p>
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
