<template>
  <div class="bg-gray-100 min-h-screen py-10">
    <div class="max-w-4xl mx-auto px-4 space-y-6">
      <h1 class="text-3xl font-bold text-gray-800">ショッピングカート</h1>

      <div v-if="cart.loading" class="text-gray-500">読み込み中...</div>
      <div v-else-if="cart.error" class="text-red-600">{{ cart.error }}</div>
      <div v-else-if="cart.items.length === 0" class="bg-white rounded-2xl shadow p-8 text-center">
        <p class="text-gray-500 text-lg">カートに商品がありません</p>
        <router-link to="/products" class="text-blue-600 hover:underline mt-2 inline-block">
          商品一覧を見る
        </router-link>
      </div>
      <template v-else>
        <div class="bg-white rounded-2xl shadow divide-y">
          <div
            v-for="item in cart.items"
            :key="item.id"
            class="p-4 flex items-center gap-4"
          >
            <img
              :src="item.productImageUrl"
              :alt="item.productName"
              class="w-20 h-20 object-cover rounded"
            />
            <div class="flex-1">
              <router-link
                :to="'/products/' + item.productId"
                class="font-semibold text-gray-800 hover:text-blue-600"
              >
                {{ item.productName }}
              </router-link>
              <p class="text-gray-500 text-sm">
                &yen;{{ item.productPrice.toLocaleString() }} / 個
              </p>
            </div>
            <div class="flex items-center gap-2">
              <button
                class="w-8 h-8 rounded bg-gray-200 hover:bg-gray-300 font-bold"
                :disabled="item.quantity <= 1"
                @click="changeQuantity(item, item.quantity - 1)"
              >
                -
              </button>
              <span class="w-10 text-center font-semibold">{{ item.quantity }}</span>
              <button
                class="w-8 h-8 rounded bg-gray-200 hover:bg-gray-300 font-bold"
                :disabled="item.quantity >= item.stock"
                @click="changeQuantity(item, item.quantity + 1)"
              >
                +
              </button>
            </div>
            <p class="w-28 text-right font-bold text-gray-800">
              &yen;{{ (item.productPrice * item.quantity).toLocaleString() }}
            </p>
            <button
              class="text-red-500 hover:text-red-700 text-sm"
              @click="handleRemove(item.id)"
            >
              削除
            </button>
          </div>
        </div>

        <div class="bg-white rounded-2xl shadow p-6 flex items-center justify-between">
          <p class="text-xl font-bold text-gray-800">
            合計: &yen;{{ cart.totalAmount.toLocaleString() }}
          </p>
          <router-link
            to="/checkout"
            class="px-6 py-3 bg-blue-600 text-white font-semibold rounded-lg hover:bg-blue-700 transition"
          >
            レジに進む
          </router-link>
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useCartStore } from '@/stores/cart'
import type { CartItem } from '@/api/cart'

const cart = useCartStore()

onMounted(() => {
  cart.load()
})

const changeQuantity = async (item: CartItem, newQty: number) => {
  if (newQty < 1 || newQty > item.stock) return
  await cart.update(item.id, newQty)
}

const handleRemove = async (id: number) => {
  await cart.remove(id)
}
</script>
