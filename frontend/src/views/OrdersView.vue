<template>
  <div class="py-12">
    <div class="max-w-4xl mx-auto px-6 space-y-6">
      <h1 class="text-3xl font-bold text-gray-800 tracking-tight">注文履歴</h1>

      <div v-if="loading" class="text-gray-400">読み込み中...</div>
      <div v-else-if="orders.length === 0" class="bg-white rounded-2xl shadow-sm p-10 text-center">
        <p class="text-gray-400 text-lg">注文履歴がありません</p>
        <router-link to="/products" class="text-indigo-600 hover:text-indigo-800 mt-3 inline-block transition">
          商品一覧を見る
        </router-link>
      </div>
      <div v-else class="space-y-4">
        <router-link
          v-for="order in orders"
          :key="order.id"
          :to="'/orders/' + order.id"
          class="bg-white rounded-2xl shadow-sm p-6 flex items-center justify-between hover:shadow-md hover:-translate-y-0.5 transition-all duration-200 block"
        >
          <div>
            <p class="font-semibold text-gray-800">注文 #{{ order.id }}</p>
            <p class="text-gray-400 text-sm">{{ formatDate(order.createdAt) }}</p>
            <p class="text-gray-400 text-sm">{{ order.items?.length || 0 }} 点の商品</p>
          </div>
          <div class="text-right">
            <span
              class="px-3 py-1 rounded-full text-xs font-semibold"
              :class="statusClass(order.status)"
            >
              {{ statusLabel(order.status) }}
            </span>
            <p class="text-lg font-bold text-gray-800 mt-1">
              &yen;{{ order.totalAmount.toLocaleString() }}
            </p>
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { fetchOrders, type Order } from '@/api/orders'
import { statusLabel, statusClass } from '@/lib/orderStatus'

const orders = ref<Order[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    orders.value = await fetchOrders()
  } finally {
    loading.value = false
  }
})

const formatDate = (dateStr: string) => {
  const d = new Date(dateStr)
  return `${d.getFullYear()}/${String(d.getMonth() + 1).padStart(2, '0')}/${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}
</script>
