<template>
  <div class="py-12">
    <div class="max-w-4xl mx-auto px-6 space-y-6">
      <h1 class="text-3xl font-bold text-gray-800 tracking-tight">ショッピングカート</h1>

      <div v-if="cart.loading" class="text-gray-400">読み込み中...</div>
      <div v-else-if="cart.error" class="text-rose-500">{{ cart.error }}</div>
      <div v-else-if="cart.items.length === 0" class="bg-white rounded-2xl shadow-sm p-10 text-center">
        <p class="text-gray-400 text-lg">カートに商品がありません</p>
        <router-link to="/products" class="text-indigo-600 hover:text-indigo-800 mt-3 inline-block transition">
          商品一覧を見る
        </router-link>
      </div>
      <template v-else>
        <div class="bg-white rounded-2xl shadow-sm divide-y">
          <div
            v-for="item in cart.items"
            :key="item.id"
            class="p-5 flex items-center gap-5"
          >
            <img
              :src="item.productImageUrl"
              :alt="item.productName"
              class="w-20 h-20 object-cover rounded-xl"
            />
            <div class="flex-1">
              <router-link
                :to="'/products/' + item.productId"
                class="font-semibold text-gray-800 hover:text-indigo-600 transition"
              >
                {{ item.productName }}
              </router-link>
              <p class="text-gray-400 text-sm">
                &yen;{{ item.productPrice.toLocaleString() }} / 個
              </p>
            </div>
            <div class="flex items-center gap-2">
              <button
                class="w-8 h-8 rounded-lg bg-gray-100 hover:bg-gray-200 font-bold transition"
                :disabled="item.quantity <= 1"
                @click="changeQuantity(item, item.quantity - 1)"
              >
                -
              </button>
              <span class="w-10 text-center font-semibold">{{ item.quantity }}</span>
              <button
                class="w-8 h-8 rounded-lg bg-gray-100 hover:bg-gray-200 font-bold transition"
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
              class="text-rose-500 hover:text-rose-700 text-sm transition"
              @click="handleRemove(item.id)"
            >
              削除
            </button>
          </div>
        </div>

        <div class="bg-white rounded-2xl shadow-sm p-6 flex items-center justify-between">
          <p class="text-xl font-bold text-gray-800">
            合計: &yen;{{ cart.totalAmount.toLocaleString() }}
          </p>
          <router-link
            to="/checkout"
            class="px-8 py-3 bg-indigo-600 text-white font-semibold rounded-xl hover:bg-indigo-700 hover:shadow-md transition-all duration-200"
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
