<template>
  <div class="max-w-6xl mx-auto px-6 py-8">
    <div class="flex items-center justify-between mb-6">
      <h1 class="text-2xl font-bold text-gray-800">注文管理</h1>
      <router-link to="/admin" class="text-blue-600 hover:underline text-sm">ダッシュボードに戻る</router-link>
    </div>

    <div v-if="loading" class="text-center py-12 text-gray-500">読み込み中...</div>

    <div v-else-if="orders.length === 0" class="text-center py-12 text-gray-500">注文がありません</div>

    <div v-else class="space-y-4">
      <div v-for="order in orders" :key="order.id" class="bg-white rounded-xl shadow p-6">
        <div class="flex justify-between items-start mb-3">
          <div>
            <p class="font-bold text-gray-800">注文 #{{ order.id }}</p>
            <p class="text-sm text-gray-500">{{ order.addressName }} さん</p>
            <p class="text-sm text-gray-400">{{ formatDate(order.createdAt) }}</p>
          </div>
          <div class="text-right">
            <p class="font-semibold text-lg">&yen;{{ order.totalAmount.toLocaleString() }}</p>
            <select
              :value="order.status"
              class="mt-1 border rounded px-2 py-1 text-sm"
              @change="onStatusChange(order.id, ($event.target as HTMLSelectElement).value)"
            >
              <option value="PENDING">処理中</option>
              <option value="PAID">支払済</option>
              <option value="SHIPPED">発送済</option>
              <option value="DELIVERED">配達完了</option>
              <option value="CANCELLED">キャンセル</option>
            </select>
          </div>
        </div>
        <ul class="divide-y text-sm">
          <li v-for="item in order.items" :key="item.id" class="py-2 flex justify-between">
            <span>{{ item.productName }} x {{ item.quantity }}</span>
            <span>&yen;{{ (item.price * item.quantity).toLocaleString() }}</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { fetchAllOrders, updateOrderStatus, type AdminOrder } from '@/api/admin'

const orders = ref<AdminOrder[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    orders.value = await fetchAllOrders()
  } finally {
    loading.value = false
  }
})

const onStatusChange = async (id: number, status: string) => {
  const updated = await updateOrderStatus(id, status)
  const idx = orders.value.findIndex((o) => o.id === id)
  if (idx !== -1) orders.value[idx] = updated
}

const formatDate = (dateStr: string) => {
  const d = new Date(dateStr)
  return `${d.getFullYear()}/${d.getMonth() + 1}/${d.getDate()} ${d.getHours()}:${String(d.getMinutes()).padStart(2, '0')}`
}
</script>
