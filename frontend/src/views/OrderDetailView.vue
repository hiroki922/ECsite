<template>
  <div class="bg-gray-100 min-h-screen py-10">
    <div class="max-w-4xl mx-auto px-4 space-y-6">
      <router-link to="/orders" class="text-blue-600 hover:underline text-sm">
        &larr; 注文履歴に戻る
      </router-link>

      <div v-if="loading" class="text-gray-500">読み込み中...</div>
      <div v-else-if="error" class="text-red-600">{{ error }}</div>
      <template v-else-if="order">
        <div class="flex items-center justify-between">
          <h1 class="text-3xl font-bold text-gray-800">注文 #{{ order.id }}</h1>
          <span
            class="px-4 py-1 rounded-full text-sm font-semibold"
            :class="statusClass(order.status)"
          >
            {{ statusLabel(order.status) }}
          </span>
        </div>

        <p class="text-gray-500 text-sm">注文日時: {{ formatDate(order.createdAt) }}</p>

        <!-- 配送先 -->
        <section class="bg-white rounded-2xl shadow p-6">
          <h2 class="text-lg font-semibold text-gray-800 mb-2">配送先</h2>
          <p class="text-gray-700">{{ order.addressName }}</p>
          <p class="text-gray-600 text-sm">
            〒{{ order.addressPostalCode }} {{ order.addressPrefecture }} {{ order.addressCity }}
          </p>
          <p class="text-gray-600 text-sm">{{ order.addressLine }}</p>
          <p class="text-gray-500 text-sm">TEL: {{ order.addressPhone }}</p>
        </section>

        <!-- 注文商品 -->
        <section class="bg-white rounded-2xl shadow p-6">
          <h2 class="text-lg font-semibold text-gray-800 mb-4">注文商品</h2>
          <div class="divide-y">
            <div v-for="item in order.items" :key="item.id" class="py-3 flex items-center gap-4">
              <img :src="item.productImageUrl" class="w-16 h-16 object-cover rounded" />
              <div class="flex-1">
                <p class="font-semibold text-gray-800">{{ item.productName }}</p>
                <p class="text-gray-500 text-sm">&yen;{{ item.price.toLocaleString() }} x {{ item.quantity }}</p>
              </div>
              <p class="font-bold">&yen;{{ (item.price * item.quantity).toLocaleString() }}</p>
            </div>
          </div>
          <div class="mt-4 pt-4 border-t flex justify-between">
            <p class="text-xl font-bold">合計</p>
            <p class="text-xl font-bold text-blue-600">&yen;{{ order.totalAmount.toLocaleString() }}</p>
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

const statusLabel = (s: string) =>
  ({ PENDING: '処理中', PAID: '支払済', SHIPPED: '発送済', DELIVERED: '配達完了', CANCELLED: 'キャンセル' }[s] || s)

const statusClass = (s: string) =>
  ({
    PENDING: 'bg-yellow-100 text-yellow-700',
    PAID: 'bg-blue-100 text-blue-700',
    SHIPPED: 'bg-purple-100 text-purple-700',
    DELIVERED: 'bg-green-100 text-green-700',
    CANCELLED: 'bg-red-100 text-red-700',
  }[s] || 'bg-gray-100 text-gray-700')
</script>
