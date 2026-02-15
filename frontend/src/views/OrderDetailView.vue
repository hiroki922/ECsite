<template>
  <div class="py-12">
    <div class="max-w-4xl mx-auto px-6 space-y-6">
      <router-link to="/orders" class="text-indigo-600 hover:text-indigo-800 text-sm transition">
        &larr; 注文履歴に戻る
      </router-link>

      <div v-if="loading" class="text-gray-400">読み込み中...</div>
      <div v-else-if="error" class="text-rose-500">{{ error }}</div>
      <template v-else-if="order">
        <div class="flex items-center justify-between">
          <h1 class="text-3xl font-bold text-gray-800 tracking-tight">注文 #{{ order.id }}</h1>
          <span
            class="px-4 py-1 rounded-full text-sm font-semibold"
            :class="statusClass(order.status)"
          >
            {{ statusLabel(order.status) }}
          </span>
        </div>

        <p class="text-gray-400 text-sm">注文日時: {{ formatDate(order.createdAt) }}</p>

        <section class="bg-white rounded-2xl shadow-sm p-6">
          <h2 class="text-lg font-semibold text-gray-800 mb-3">配送先</h2>
          <p class="text-gray-700">{{ order.addressName }}</p>
          <p class="text-gray-500 text-sm">
            〒{{ order.addressPostalCode }} {{ order.addressPrefecture }} {{ order.addressCity }}
          </p>
          <p class="text-gray-500 text-sm">{{ order.addressLine }}</p>
          <p class="text-gray-400 text-sm">TEL: {{ order.addressPhone }}</p>
        </section>

        <section class="bg-white rounded-2xl shadow-sm p-6">
          <h2 class="text-lg font-semibold text-gray-800 mb-4">注文商品</h2>
          <div class="divide-y">
            <div v-for="item in order.items" :key="item.id" class="py-4 flex items-center gap-4">
              <img :src="item.productImageUrl" class="w-16 h-16 object-cover rounded-xl" />
              <div class="flex-1">
                <p class="font-semibold text-gray-800">{{ item.productName }}</p>
                <p class="text-gray-400 text-sm">&yen;{{ item.price.toLocaleString() }} x {{ item.quantity }}</p>
              </div>
              <p class="font-bold">&yen;{{ (item.price * item.quantity).toLocaleString() }}</p>
            </div>
          </div>
          <div class="mt-4 pt-4 border-t flex justify-between">
            <p class="text-xl font-bold">合計</p>
            <p class="text-xl font-bold text-indigo-600">&yen;{{ order.totalAmount.toLocaleString() }}</p>
          </div>
        </section>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { fetchOrder, type Order } from '@/api/orders'
import { statusLabel, statusClass } from '@/lib/orderStatus'

const route = useRoute()
const order = ref<Order | null>(null)
const loading = ref(true)
const error = ref('')

onMounted(async () => {
  try {
    const id = Number(route.params.id)
    order.value = await fetchOrder(id)
  } catch {
    error.value = '注文が見つかりません'
  } finally {
    loading.value = false
  }
})

const formatDate = (dateStr: string) => {
  const d = new Date(dateStr)
  return `${d.getFullYear()}/${String(d.getMonth() + 1).padStart(2, '0')}/${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}
</script>
