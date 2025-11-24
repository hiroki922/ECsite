<template>
  <div class="max-w-lg mx-auto p-4">
    <h1 class="text-2xl font-bold mb-4">商品登録</h1>

    <form @submit.prevent="submitProduct">
      <div class="mb-3">
        <label class="block font-semibold">商品名</label>
        <input v-model="product.name" type="text" class="border rounded p-2 w-full" required />
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
        <input v-model="product.imageUrl" type="text" class="border rounded p-2 w-full" />
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
import axios from 'axios'

const product = ref({
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
    const res = await axios.post('/api/admin/products', product.value, {
      withCredentials: true, // ← セッション送るため必須
    })

    message.value = '商品が登録されました！'
    console.log(res.data)
  } catch (err: any) {
    console.error(err)
    error.value = err.response?.data?.error || '登録に失敗しました'
  }
}
</script>
