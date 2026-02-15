<template>
  <div class="py-10">
    <div class="max-w-7xl mx-auto px-6 space-y-8">
      <header class="flex items-center justify-between">
        <div>
          <h1 class="text-3xl font-bold text-gray-800 tracking-tight">管理ダッシュボード</h1>
          <p class="text-gray-400 text-sm mt-1">商品・注文・ユーザーの状況を確認できます</p>
        </div>
        <router-link
          to="/admin/products/new"
          class="px-5 py-2.5 bg-emerald-600 text-white font-semibold rounded-xl hover:bg-emerald-700 hover:shadow-md transition-all duration-200"
        >
          商品追加
        </router-link>
      </header>

      <section class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <StatCard title="商品数" :value="`${stats.productCount}件`" />
        <StatCard title="ユーザー数" :value="`${stats.userCount}人`" />
        <StatCard title="未処理注文" :value="`${stats.pendingOrderCount}件`" :highlight="stats.pendingOrderCount > 0" trend="要対応" />
        <StatCard title="総注文数" :value="`${stats.totalOrderCount}件`" />
      </section>

      <section class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <router-link
          to="/admin/products"
          class="bg-white rounded-2xl shadow-sm p-6 hover:shadow-md hover:-translate-y-0.5 transition-all duration-200"
        >
          <h3 class="text-lg font-bold text-gray-800">商品管理</h3>
          <p class="text-gray-400 text-sm mt-1">商品の追加・編集・削除</p>
        </router-link>
        <router-link
          to="/admin/users"
          class="bg-white rounded-2xl shadow-sm p-6 hover:shadow-md hover:-translate-y-0.5 transition-all duration-200"
        >
          <h3 class="text-lg font-bold text-gray-800">ユーザー管理</h3>
          <p class="text-gray-400 text-sm mt-1">ユーザー一覧・ロール変更</p>
        </router-link>
        <router-link
          to="/admin/orders"
          class="bg-white rounded-2xl shadow-sm p-6 hover:shadow-md hover:-translate-y-0.5 transition-all duration-200"
        >
          <h3 class="text-lg font-bold text-gray-800">注文管理</h3>
          <p class="text-gray-400 text-sm mt-1">注文一覧・ステータス管理</p>
        </router-link>
      </section>

      <section class="bg-white rounded-2xl shadow-sm p-6">
        <h2 class="text-xl font-bold text-gray-800 mb-4">最近の注文</h2>
        <div v-if="recentOrders.length === 0" class="text-gray-400">注文がありません</div>
        <ul v-else class="divide-y">
          <li v-for="o in recentOrders" :key="o.id" class="py-4 flex justify-between">
            <div>
              <p class="font-medium text-gray-800">{{ o.addressName }} さん</p>
              <p class="text-gray-400 text-sm">
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
import { statusLabel, statusClass } from '@/lib/orderStatus'

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
</script>
