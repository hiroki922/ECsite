<!-- src/views/admin/AdminView.vue -->
<template>
  <div class="min-h-screen bg-gray-100">
    <div class="max-w-6xl mx-auto px-6 py-8 space-y-8">
      <header class="flex items-center justify-between">
        <div>
          <h1 class="text-3xl font-bold text-gray-800">管理ダッシュボード</h1>
          <p class="text-gray-500 text-sm">売上・在庫・注文の状況を確認できます</p>
        </div>
        <div class="flex gap-3">
          <button class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">
            商品追加
          </button>
          <button class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
            注文CSV出力
          </button>
        </div>
      </header>

      <section class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <StatCard title="今日の売上" value="¥128,400" trend="+12% 前日比" />
        <StatCard title="新規注文" value="23件" trend="+5件" />
        <StatCard title="在庫アラート" value="4商品" trend="要補充" highlight />
        <StatCard title="新規ユーザー" value="12人" trend="+3人" />
      </section>

      <section class="bg-white rounded-2xl shadow p-6 space-y-4">
        <div class="flex items-center justify-between">
          <h2 class="text-xl font-bold text-gray-800">商品管理</h2>
          <div class="flex gap-3">
            <input v-model="search" placeholder="商品名で検索" class="border rounded px-3 py-1" />
            <select v-model="status" class="border rounded px-3 py-1">
              <option value="">すべて</option>
              <option value="published">公開中</option>
              <option value="draft">下書き</option>
              <option value="archived">非公開</option>
            </select>
          </div>
        </div>

        <div class="overflow-auto">
          <table class="min-w-full text-sm">
            <thead class="text-left text-gray-500 border-b">
              <tr>
                <th class="py-2 pr-4">商品</th>
                <th class="py-2 pr-4">価格</th>
                <th class="py-2 pr-4">在庫</th>
                <th class="py-2 pr-4">状態</th>
                <th class="py-2">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="p in filteredProducts" :key="p.id" class="border-b last:border-0">
                <td class="py-3 pr-4 font-medium text-gray-800">{{ p.name }}</td>
                <td class="py-3 pr-4">¥{{ p.price.toLocaleString() }}</td>
                <td class="py-3 pr-4" :class="p.stock < 5 ? 'text-red-500' : ''">{{ p.stock }}</td>
                <td class="py-3 pr-4">
                  <span class="px-2 py-1 rounded text-xs" :class="statusClass(p.status)">{{
                    statusLabel(p.status)
                  }}</span>
                </td>
                <td class="py-3 flex gap-2">
                  <button class="text-blue-600 hover:underline">編集</button>
                  <button class="text-gray-500 hover:underline">非公開</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <section class="bg-white rounded-2xl shadow p-6">
        <h2 class="text-xl font-bold text-gray-800 mb-3">最近の注文</h2>
        <ul class="divide-y">
          <li v-for="o in orders" :key="o.id" class="py-3 flex justify-between">
            <div>
              <p class="font-medium text-gray-800">{{ o.customer }} さん</p>
              <p class="text-gray-500 text-sm">注文ID: {{ o.id }} / {{ o.items }}点</p>
            </div>
            <div class="text-right">
              <p class="font-semibold">¥{{ o.total.toLocaleString() }}</p>
              <p class="text-sm text-gray-500">{{ o.date }}</p>
            </div>
          </li>
        </ul>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import StatCard from '@/components/StatCard.vue'

const search = ref('')
const status = ref('')

const products = ref([
  { id: 1, name: 'スニーカー', price: 9800, stock: 3, status: 'published' },
  { id: 2, name: 'Tシャツ', price: 3200, stock: 18, status: 'draft' },
  { id: 3, name: 'バックパック', price: 12800, stock: 0, status: 'archived' },
])

const orders = ref([
  { id: 'A-1023', customer: '佐藤', items: 2, total: 15800, date: '2024/03/01' },
  { id: 'A-1022', customer: '田中', items: 1, total: 6800, date: '2024/02/29' },
])

const filteredProducts = computed(() =>
  products.value.filter(
    (p) =>
      (!status.value || p.status === status.value) &&
      p.name.toLowerCase().includes(search.value.toLowerCase())
  )
)

function statusLabel(s: string) {
  return { published: '公開中', draft: '下書き', archived: '非公開' }[s] || '不明'
}
function statusClass(s: string) {
  return (
    {
      published: 'bg-green-100 text-green-700',
      draft: 'bg-yellow-100 text-yellow-700',
      archived: 'bg-gray-200 text-gray-600',
    }[s] || 'bg-gray-200 text-gray-600'
  )
}
</script>
