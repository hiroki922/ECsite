<template>
  <div class="max-w-lg mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">商品登録</h1>

    <form @submit.prevent="submitProduct">
      <div class="mb-3">
        <label class="block font-semibold">商品名</label>
        <BaseInput v-model="product.name" required class="border rounded p-2 w-full" />
      </div>

      <div class="mb-3">
        <label class="block font-semibold">説明</label>
        <textarea v-model="product.description" class="border rounded p-2 w-full"></textarea>
      </div>

      <div class="mb-3">
        <label class="block font-semibold">価格</label>
        <input
          v-model.number="product.price"
          type="number"
          class="border rounded p-2 w-full"
          required
        />
      </div>

      <div class="mb-3">
        <label class="block font-semibold">在庫</label>
        <input
          v-model.number="product.stock"
          type="number"
          class="border rounded p-2 w-full"
          required
        />
      </div>

      <div class="mb-3">
        <label class="block font-semibold">画像URL</label>
        <BaseInput v-model="product.imageUrl" class="border rounded p-2 w-full" />
      </div>

      <button type="submit" class="bg-blue-600 text-white px-4 py-2 rounded">登録する</button>
    </form>

    <p v-if="message" class="text-green-700 mt-4">
      {{ message }}
    </p>

    <p v-if="error" class="text-red-600 mt-4">
      {{ error }}
    </p>
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
    message.value = '商品が登録されました！'
    setTimeout(() => router.push('/admin/products'), 1000)
  } catch (err: any) {
    error.value = err.response?.data?.error || '登録に失敗しました'
  }
}
</script>
