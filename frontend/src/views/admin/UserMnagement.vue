<template>
  <div class="min-h-screen bg-gray-100">
    <div class="max-w-6xl mx-auto px-6 py-8 space-y-6">
      <div>
        <h1 class="text-3xl font-bold text-gray-800">ユーザー管理</h1>
        <p class="text-gray-500 text-sm">ユーザーのロール変更ができます</p>
      </div>

      <div class="bg-white rounded-2xl shadow p-6">
        <div v-if="loading" class="text-gray-500">読み込み中...</div>
        <div v-else class="overflow-auto">
          <table class="min-w-full text-sm">
            <thead class="text-left text-gray-500 border-b">
              <tr>
                <th class="py-2 pr-4">ID</th>
                <th class="py-2 pr-4">名前</th>
                <th class="py-2 pr-4">メール</th>
                <th class="py-2 pr-4">ロール</th>
                <th class="py-2">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="u in users" :key="u.id" class="border-b last:border-0">
                <td class="py-3 pr-4 text-gray-500">{{ u.id }}</td>
                <td class="py-3 pr-4 font-medium text-gray-800">{{ u.name }}</td>
                <td class="py-3 pr-4 text-gray-600">{{ u.email }}</td>
                <td class="py-3 pr-4">
                  <span
                    class="px-2 py-1 rounded text-xs font-semibold"
                    :class="u.role === 'ADMIN' ? 'bg-purple-100 text-purple-700' : 'bg-gray-100 text-gray-700'"
                  >
                    {{ u.role === 'ADMIN' ? '管理者' : '一般' }}
                  </span>
                </td>
                <td class="py-3">
                  <button
                    class="text-blue-600 hover:underline text-sm"
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

      <router-link to="/admin" class="text-blue-600 hover:underline text-sm">
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
