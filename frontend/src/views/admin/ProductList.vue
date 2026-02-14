<template>
  <div class="min-h-screen bg-gray-100">
    <div class="max-w-6xl mx-auto px-6 py-8 space-y-6">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-3xl font-bold text-gray-800">商品管理</h1>
          <p class="text-gray-500 text-sm">商品の追加・編集・削除ができます</p>
        </div>
        <router-link
          to="/admin/products/new"
          class="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
        >
          商品追加
        </router-link>
      </div>

      <div class="bg-white rounded-2xl shadow p-6">
        <div v-if="loading" class="text-gray-500">読み込み中...</div>
        <div v-else-if="products.length === 0" class="text-gray-500">商品がありません</div>
        <div v-else class="overflow-auto">
          <table class="min-w-full text-sm">
            <thead class="text-left text-gray-500 border-b">
              <tr>
                <th class="py-2 pr-4">ID</th>
                <th class="py-2 pr-4">商品名</th>
                <th class="py-2 pr-4">価格</th>
                <th class="py-2 pr-4">在庫</th>
                <th class="py-2">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="p in products" :key="p.id" class="border-b last:border-0">
                <td class="py-3 pr-4 text-gray-500">{{ p.id }}</td>
                <td class="py-3 pr-4 font-medium text-gray-800">{{ p.name }}</td>
                <td class="py-3 pr-4">&yen;{{ p.price.toLocaleString() }}</td>
                <td class="py-3 pr-4" :class="p.stock < 5 ? 'text-red-500 font-bold' : ''">
                  {{ p.stock }}
                </td>
                <td class="py-3 flex gap-3">
                  <router-link
                    :to="'/admin/products/' + p.id + '/edit'"
                    class="text-blue-600 hover:underline"
                  >
                    編集
                  </router-link>
                  <button
                    class="text-red-500 hover:underline"
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

      <router-link to="/admin" class="text-blue-600 hover:underline text-sm">
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
