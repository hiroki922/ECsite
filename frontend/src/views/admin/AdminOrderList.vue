<template>
  <div class="py-10">
    <div class="max-w-7xl mx-auto px-6">
      <div class="flex items-center justify-between mb-6">
        <div>
          <h1 class="text-3xl font-bold text-gray-800 tracking-tight">注文管理</h1>
          <p class="text-gray-400 text-sm mt-1">注文のステータスを管理できます</p>
        </div>
        <router-link to="/admin" class="text-indigo-600 hover:text-indigo-800 text-sm transition">
          &larr; ダッシュボードに戻る
        </router-link>
      </div>

      <div v-if="loading" class="text-center py-12 text-gray-400">読み込み中...</div>
      <div v-else-if="orders.length === 0" class="text-center py-12 text-gray-400">注文がありません</div>

      <div v-else class="space-y-4">
        <div v-for="order in orders" :key="order.id" class="bg-white rounded-2xl shadow-sm p-6">
          <div class="flex justify-between items-start mb-4">
            <div>
              <p class="font-bold text-gray-800">注文 #{{ order.id }}</p>
              <p class="text-sm text-gray-500">{{ order.addressName }} さん</p>
              <p class="text-sm text-gray-400">{{ formatDate(order.createdAt) }}</p>
            </div>
            <div class="text-right">
              <p class="font-semibold text-lg">&yen;{{ order.totalAmount.toLocaleString() }}</p>
              <select
                :value="order.status"
                class="mt-1 border border-gray-200 rounded-xl px-3 py-1.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-300"
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
            <li v-for="item in order.items" :key="item.id" class="py-3 flex justify-between text-gray-600">
              <span>{{ item.productName }} x {{ item.quantity }}</span>
              <span>&yen;{{ (item.price * item.quantity).toLocaleString() }}</span>
            </li>
          </ul>
        </div>
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
  return `${d.getFullYear()}/${String(d.getMonth() + 1).padStart(2, '0')}/${String(d.getDate()).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}
</script>
