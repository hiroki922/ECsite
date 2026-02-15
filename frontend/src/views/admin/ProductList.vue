<template>
  <div class="py-10">
    <div class="max-w-7xl mx-auto px-6 space-y-6">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-3xl font-bold text-gray-800 tracking-tight">商品管理</h1>
          <p class="text-gray-400 text-sm mt-1">商品の追加・編集・削除ができます</p>
        </div>
        <router-link
          to="/admin/products/new"
          class="px-5 py-2.5 bg-emerald-600 text-white font-semibold rounded-xl hover:bg-emerald-700 hover:shadow-md transition-all duration-200"
        >
          商品追加
        </router-link>
      </div>

      <div class="bg-white rounded-2xl shadow-sm p-6">
        <div v-if="loading" class="text-gray-400">読み込み中...</div>
        <div v-else-if="products.length === 0" class="text-gray-400">商品がありません</div>
        <div v-else class="overflow-auto">
          <table class="min-w-full text-sm">
            <thead class="text-left text-gray-400 border-b">
              <tr>
                <th class="py-3 pr-4 font-medium">ID</th>
                <th class="py-3 pr-4 font-medium">商品名</th>
                <th class="py-3 pr-4 font-medium">価格</th>
                <th class="py-3 pr-4 font-medium">在庫</th>
                <th class="py-3 font-medium">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="p in products" :key="p.id" class="border-b last:border-0 hover:bg-gray-50 transition">
                <td class="py-4 pr-4 text-gray-400">{{ p.id }}</td>
                <td class="py-4 pr-4 font-medium text-gray-800">{{ p.name }}</td>
                <td class="py-4 pr-4">&yen;{{ p.price.toLocaleString() }}</td>
                <td class="py-4 pr-4" :class="p.stock < 5 ? 'text-rose-500 font-bold' : ''">
                  {{ p.stock }}
                </td>
                <td class="py-4 flex gap-4">
                  <router-link
                    :to="'/admin/products/' + p.id + '/edit'"
                    class="text-indigo-600 hover:text-indigo-800 font-medium transition"
                  >
                    編集
                  </router-link>
                  <button
                    class="text-rose-500 hover:text-rose-700 font-medium transition"
                    @click="handleDelete(p.id!, p.name)"
                  >
                    削除
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <router-link to="/admin" class="text-indigo-600 hover:text-indigo-800 text-sm transition">
        &larr; ダッシュボードに戻る
      </router-link>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { fetchProducts, deleteProduct, type Product } from '@/api/products'

const products = ref<Product[]>([])
const loading = ref(true)

const load = async () => {
  loading.value = true
  try {
    products.value = await fetchProducts()
  } finally {
    loading.value = false
  }
}

onMounted(load)

const handleDelete = async (id: number, name: string) => {
  if (!confirm(`「${name}」を削除しますか？`)) return
  try {
    await deleteProduct(id)
    await load()
  } catch {
    alert('削除に失敗しました')
  }
}
</script>
