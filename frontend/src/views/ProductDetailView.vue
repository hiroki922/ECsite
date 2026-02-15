<template>
  <div class="bg-gray-100 min-h-screen py-10">
    <div class="max-w-4xl mx-auto px-4">
      <router-link to="/products" class="text-blue-600 hover:underline text-sm mb-4 inline-block">
        &larr; 商品一覧に戻る
      </router-link>

      <div v-if="loading" class="text-gray-500">読み込み中...</div>
      <div v-else-if="error" class="text-red-600">{{ error }}</div>
      <div v-else-if="product" class="bg-white rounded-2xl shadow p-6 grid grid-cols-1 md:grid-cols-2 gap-8">
        <div>
          <img
            :src="product.imageUrl"
            :alt="product.name"
            class="w-full rounded-lg object-cover"
          />
        </div>
        <div class="space-y-4">
          <h1 class="text-3xl font-bold text-gray-800">{{ product.name }}</h1>
          <p class="text-gray-600">{{ product.description }}</p>
          <p class="text-3xl font-bold text-blue-600">
            &yen;{{ product.price.toLocaleString() }}
          </p>
          <p :class="product.stock > 0 ? 'text-green-600' : 'text-red-500'" class="text-sm font-semibold">
            {{ product.stock > 0 ? `在庫: ${product.stock}個` : '在庫切れ' }}
          </p>

          <div v-if="product.stock > 0" class="flex items-center gap-3">
            <label class="text-sm text-gray-600">数量:</label>
            <input
              v-model.number="quantity"
              type="number"
              min="1"
              :max="product.stock"
              class="w-20 border rounded px-2 py-1 text-center"
            />
          </div>

          <button
            :disabled="product.stock === 0 || !isLoggedIn"
            class="w-full py-3 rounded-lg font-semibold text-white transition"
            :class="product.stock > 0 && isLoggedIn ? 'bg-blue-600 hover:bg-blue-700' : 'bg-gray-400 cursor-not-allowed'"
            @click="handleAddToCart"
          >
            {{ !isLoggedIn ? 'ログインしてください' : 'カートに追加' }}
          </button>
          <p v-if="addMessage" class="text-green-600 text-sm text-center">{{ addMessage }}</p>
          <p v-if="addError" class="text-red-500 text-sm text-center">{{ addError }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { fetchProduct, type Product } from '@/api/products'
import { useAuthStore } from '@/stores/auth'
import { useCartStore } from '@/stores/cart'

const route = useRoute()
const auth = useAuthStore()
const cartStore = useCartStore()
const isLoggedIn = computed(() => auth.isLoggedIn)

const product = ref<Product | null>(null)
const quantity = ref(1)
const loading = ref(true)
const error = ref('')
const addMessage = ref('')
const addError = ref('')

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

const handleAddToCart = async () => {
  if (!product.value?.id) return
  addMessage.value = ''
  addError.value = ''
  try {
    await cartStore.add(product.value.id, quantity.value)
    addMessage.value = 'カートに追加しました'
  } catch {
    addError.value = 'カートへの追加に失敗しました'
  }
}
</script>
