<template>
  <div class="py-10">
    <div class="max-w-7xl mx-auto px-6 space-y-6">
      <div>
        <h1 class="text-3xl font-bold text-gray-800 tracking-tight">ユーザー管理</h1>
        <p class="text-gray-400 text-sm mt-1">ユーザーのロール変更ができます</p>
      </div>

      <div class="bg-white rounded-2xl shadow-sm p-6">
        <div v-if="loading" class="text-gray-400">読み込み中...</div>
        <div v-else class="overflow-auto">
          <table class="min-w-full text-sm">
            <thead class="text-left text-gray-400 border-b">
              <tr>
                <th class="py-3 pr-4 font-medium">ID</th>
                <th class="py-3 pr-4 font-medium">名前</th>
                <th class="py-3 pr-4 font-medium">メール</th>
                <th class="py-3 pr-4 font-medium">ロール</th>
                <th class="py-3 font-medium">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="u in users" :key="u.id" class="border-b last:border-0 hover:bg-gray-50 transition">
                <td class="py-4 pr-4 text-gray-400">{{ u.id }}</td>
                <td class="py-4 pr-4 font-medium text-gray-800">{{ u.name }}</td>
                <td class="py-4 pr-4 text-gray-500">{{ u.email }}</td>
                <td class="py-4 pr-4">
                  <span
                    class="px-2.5 py-1 rounded-full text-xs font-semibold"
                    :class="u.role === 'ADMIN' ? 'bg-purple-100 text-purple-700' : 'bg-gray-100 text-gray-600'"
                  >
                    {{ u.role === 'ADMIN' ? '管理者' : '一般' }}
                  </span>
                </td>
                <td class="py-4">
                  <button
                    class="text-indigo-600 hover:text-indigo-800 text-sm font-medium transition"
                    @click="toggleRole(u)"
                  >
                    {{ u.role === 'ADMIN' ? '一般ユーザーにする' : '管理者にする' }}
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
import { fetchUsers, updateUserRole, type AdminUser } from '@/api/admin'

const users = ref<AdminUser[]>([])
const loading = ref(true)

onMounted(async () => {
  try {
    users.value = await fetchUsers()
  } finally {
    loading.value = false
  }
})

const toggleRole = async (user: AdminUser) => {
  const newRole = user.role === 'ADMIN' ? 'USER' : 'ADMIN'
  if (!confirm(`${user.name} を ${newRole === 'ADMIN' ? '管理者' : '一般ユーザー'} に変更しますか？`)) return
  try {
    const updated = await updateUserRole(user.id, newRole)
    user.role = updated.role
  } catch {
    alert('ロール変更に失敗しました')
  }
}
</script>
