<template>
  <div class="min-h-screen bg-gray-100">
    <div class="max-w-6xl mx-auto px-6 py-8 space-y-8">
      <header class="flex items-center justify-between">
        <div>
          <h1 class="text-3xl font-bold text-gray-800">管理ダッシュボード</h1>
          <p class="text-gray-500 text-sm">商品・注文・ユーザーの状況を確認できます</p>
        </div>
        <div class="flex gap-3">
          <router-link
            to="/admin/products/new"
            class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
          >
            商品追加
          </router-link>
        </div>
      </header>

      <!-- 統計カード -->
      <section class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <StatCard title="商品数" :value="`${stats.productCount}件`" />
        <StatCard title="ユーザー数" :value="`${stats.userCount}人`" />
        <StatCard title="未処理注文" :value="`${stats.pendingOrderCount}件`" :highlight="stats.pendingOrderCount > 0" trend="要対応" />
        <StatCard title="総注文数" :value="`${stats.totalOrderCount}件`" />
      </section>

      <!-- ナビゲーション -->
      <section class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <router-link
          to="/admin/products"
          class="bg-white rounded-2xl shadow p-6 hover:shadow-lg transition"
        >
          <h3 class="text-lg font-bold text-gray-800">商品管理</h3>
          <p class="text-gray-500 text-sm mt-1">商品の追加・編集・削除</p>
        </router-link>
        <router-link
          to="/admin/users"
          class="bg-white rounded-2xl shadow p-6 hover:shadow-lg transition"
        >
          <h3 class="text-lg font-bold text-gray-800">ユーザー管理</h3>
          <p class="text-gray-500 text-sm mt-1">ユーザー一覧・ロール変更</p>
        </router-link>
        <router-link
          to="/admin/orders"
          class="bg-white rounded-2xl shadow p-6 hover:shadow-lg transition"
        >
          <h3 class="text-lg font-bold text-gray-800">注文管理</h3>
          <p class="text-gray-500 text-sm mt-1">注文一覧・ステータス管理</p>
        </router-link>
      </section>

      <!-- 最近の注文 -->
      <section class="bg-white rounded-2xl shadow p-6">
        <h2 class="text-xl font-bold text-gray-800 mb-3">最近の注文</h2>
        <div v-if="recentOrders.length === 0" class="text-gray-500">注文がありません</div>
        <ul v-else class="divide-y">
          <li v-for="o in recentOrders" :key="o.id" class="py-3 flex justify-between">
            <div>
              <p class="font-medium text-gray-800">{{ o.addressName }} さん</p>
              <p class="text-gray-500 text-sm">
                注文 #{{ o.id }} / {{ o.items?.length || 0 }}点
              </p>
            </div>
            <div class="text-right">
              <p class="font-semibold">&yen;{{ o.totalAmount.toLocaleString() }}</p>
              <span
                class="px-2 py-0.5 rounded-full text-xs font-semibold"
                :class="statusClass(o.status)"
              >
                {{ statusLabel(o.status) }}
              </span>
            </div>
          </li>
        </ul>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import StatCard from '@/components/StatCard.vue'
import { fetchDashboardStats, fetchRecentOrders, type DashboardStats, type AdminOrder } from '@/api/admin'

const stats = ref<DashboardStats>({ productCount: 0, userCount: 0, pendingOrderCount: 0, totalOrderCount: 0 })
const recentOrders = ref<AdminOrder[]>([])

onMounted(async () => {
  try {
    const [s, o] = await Promise.all([fetchDashboardStats(), fetchRecentOrders()])
    stats.value = s
    recentOrders.value = o
  } catch (err) {
    console.error('ダッシュボードデータの取得に失敗しました', err)
  }
})

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
