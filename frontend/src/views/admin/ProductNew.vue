<template>
  <div class="py-10">
    <div class="max-w-lg mx-auto px-6">
      <router-link to="/admin/products" class="text-indigo-600 hover:text-indigo-800 text-sm mb-6 inline-block transition">
        &larr; 商品一覧に戻る
      </router-link>

      <h1 class="text-2xl font-bold text-gray-800 mb-6 tracking-tight">商品登録</h1>

      <form @submit.prevent="submitProduct" class="bg-white rounded-2xl shadow-sm p-6 space-y-5">
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
          登録する
        </button>

        <p v-if="message" class="text-emerald-600 text-sm text-center">{{ message }}</p>
        <p v-if="error" class="text-rose-500 text-sm text-center">{{ error }}</p>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { createProduct, type Product } from '@/api/products'
import BaseInput from '@/components/BaseInput.vue'

const router = useRouter()

const product = ref<Product>({
  name: '',
  description: '',
  price: 0,
  stock: 0,
  imageUrl: '',
})

const message = ref('')
const error = ref('')

const submitProduct = async () => {
  message.value = ''
  error.value = ''

  try {
    await createProduct(product.value)
    message.value = '商品が登録されました'
    setTimeout(() => router.push('/admin/products'), 1000)
  } catch (err: any) {
    error.value = err.response?.data?.error || '登録に失敗しました'
  }
}
</script>
