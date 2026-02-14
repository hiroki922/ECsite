<template>
  <div class="bg-gray-100 min-h-screen py-10">
    <div class="max-w-4xl mx-auto px-4 space-y-6">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-3xl font-bold text-gray-800">配送先設定</h1>
          <p class="text-gray-500">配送先を登録・編集できます</p>
        </div>
        <span class="text-sm text-gray-500">登録件数: {{ addresses.length }} 件</span>
      </div>
      <p v-if="apiError" class="text-red-600 text-sm">{{ apiError }}</p>

      <section class="bg-white rounded-2xl shadow p-6">
        <h2 class="text-xl font-semibold text-gray-800 mb-4">新規追加</h2>
        <form class="grid grid-cols-1 md:grid-cols-2 gap-4" @submit.prevent="handleSubmit">
          <div>
            <label class="block text-sm text-gray-600 mb-1">氏名 *</label>
            <input v-model="form.name" type="text" :class="inputClass" placeholder="山田 太郎" required />
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">電話番号 *</label>
            <input
              v-model="form.phone"
              type="tel"
              :class="inputClass"
              placeholder="09012345678"
              required
            />
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">郵便番号 *</label>
            <input
              v-model="form.postalCode"
              type="text"
              :class="inputClass"
              placeholder="123-4567"
              required
            />
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">都道府県 *</label>
            <input v-model="form.prefecture" type="text" :class="inputClass" placeholder="東京都" required />
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">市区町村 *</label>
            <input v-model="form.city" type="text" :class="inputClass" placeholder="渋谷区" required />
          </div>
          <div>
            <label class="block text-sm text-gray-600 mb-1">番地・建物名 *</label>
            <input
              v-model="form.addressLine"
              type="text"
              :class="inputClass"
              placeholder="1-2-3 サンプルマンション101"
              required
            />
          </div>
          <div class="md:col-span-2 flex items-center gap-3">
            <input id="isDefault" v-model="form.isDefault" type="checkbox" class="h-4 w-4" />
            <label for="isDefault" class="text-sm text-gray-700">デフォルト配送先に設定する</label>
          </div>
          <div class="md:col-span-2 flex flex-wrap items-center gap-3">
            <button
              type="submit"
              class="px-5 py-2 bg-blue-600 text-white font-semibold rounded hover:bg-blue-700 transition"
            >
              追加する
            </button>
            <p v-if="errors.length" class="text-sm text-red-600">
              {{ errors.join(' / ') }}
            </p>
          </div>
        </form>
      </section>

      <section class="bg-white rounded-2xl shadow p-6 space-y-4">
        <div class="flex items-center justify-between">
          <h2 class="text-xl font-semibold text-gray-800">登録済み配送先</h2>
          <span class="text-sm text-gray-500">デフォルトは1件のみ設定できます</span>
        </div>
        <div v-if="addresses.length === 0" class="text-gray-500">まだ配送先がありません。</div>
        <div v-else class="grid grid-cols-1 gap-3">
          <div
            v-for="address in addresses"
            :key="address.id"
            class="border rounded-xl p-4 flex flex-col md:flex-row md:items-center md:justify-between gap-3"
          >
            <div class="space-y-1">
              <div class="flex items-center gap-2">
                <p class="text-lg font-semibold text-gray-800">{{ address.name }}</p>
                <span
                  v-if="address.isDefault"
                  class="px-2 py-0.5 text-xs rounded-full bg-blue-100 text-blue-700"
                >
                  デフォルト
                </span>
              </div>
              <p class="text-gray-700 text-sm">
                〒{{ address.postalCode }} {{ address.prefecture }} {{ address.city }}
              </p>
              <p class="text-gray-700 text-sm">{{ address.addressLine }}</p>
              <p class="text-gray-500 text-sm">TEL: {{ address.phone }}</p>
            </div>
            <div class="flex items-center gap-3">
              <button
                class="text-blue-600 hover:underline text-sm"
                :disabled="address.isDefault"
                @click="setDefault(address.id)"
              >
                デフォルトにする
              </button>
              <button class="text-red-500 hover:underline text-sm" @click="removeAddress(address.id)">
                削除
              </button>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import type { Address } from '@/api/addresses'
import { createAddress, deleteAddress, fetchAddresses, setDefaultAddress } from '@/api/addresses'

type Address = {
  id: number
  name: string
  postalCode: string
  prefecture: string
  city: string
  addressLine: string
  phone: string
  isDefault: boolean
}

const addresses = ref<Address[]>([])

const form = reactive<Omit<Address, 'id'>>({
  name: '',
  postalCode: '',
  prefecture: '',
  city: '',
  addressLine: '',
  phone: '',
  isDefault: false,
})

const errors = ref<string[]>([])
const apiError = ref('')
const inputClass =
  'w-full border border-gray-300 rounded-lg px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200'

const load = async () => {
  apiError.value = ''
  try {
    addresses.value = await fetchAddresses()
  } catch (err) {
    apiError.value = '配送先の取得に失敗しました'
    console.error(err)
  }
}

onMounted(load)

const handleSubmit = async () => {
  errors.value = []
  apiError.value = ''
  if (!form.name || !form.postalCode || !form.prefecture || !form.city || !form.addressLine || !form.phone) {
    errors.value.push('必須項目を入力してください')
  }
  if (form.postalCode && !/^[0-9]{3}-?[0-9]{4}$/.test(form.postalCode)) {
    errors.value.push('郵便番号は 123-4567 の形式で入力してください')
  }
  if (form.phone && !/^[0-9]{10,11}$/.test(form.phone)) {
    errors.value.push('電話番号はハイフンなしで10〜11桁で入力してください')
  }
  if (errors.value.length) return

  try {
    await createAddress(form)
    await load()
    resetForm()
  } catch (err) {
    apiError.value = '配送先の登録に失敗しました'
    console.error(err)
  }
}

const setDefault = async (id: number) => {
  apiError.value = ''
  try {
    await setDefaultAddress(id)
    await load()
  } catch (err) {
    apiError.value = 'デフォルト設定に失敗しました'
    console.error(err)
  }
}

const removeAddress = async (id: number) => {
  apiError.value = ''
  try {
    await deleteAddress(id)
    await load()
  } catch (err) {
    apiError.value = '削除に失敗しました'
    console.error(err)
  }
}

const resetForm = () => {
  form.name = ''
  form.postalCode = ''
  form.prefecture = ''
  form.city = ''
  form.addressLine = ''
  form.phone = ''
  form.isDefault = false
}
</script>
