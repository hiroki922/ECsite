<template>
  <div class="py-12">
    <div class="max-w-4xl mx-auto px-6 space-y-6">
      <div class="flex items-center justify-between">
        <div>
          <h1 class="text-3xl font-bold text-gray-800 tracking-tight">配送先設定</h1>
          <p class="text-gray-400 text-sm mt-1">配送先を登録・編集できます</p>
        </div>
        <span class="text-sm text-gray-400">登録件数: {{ addresses.length }} 件</span>
      </div>

      <p v-if="apiError" class="text-rose-500 text-sm">{{ apiError }}</p>

      <section class="bg-white rounded-2xl shadow-sm p-6">
        <h2 class="text-xl font-semibold text-gray-800 mb-4">新規追加</h2>
        <form class="grid grid-cols-1 md:grid-cols-2 gap-4" @submit.prevent="handleSubmit">
          <div>
            <label class="block text-sm font-medium text-gray-600 mb-1.5">氏名 *</label>
            <BaseInput v-model="form.name" placeholder="山田 太郎" required />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-600 mb-1.5">電話番号 *</label>
            <BaseInput v-model="form.phone" type="tel" placeholder="09012345678" required />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-600 mb-1.5">郵便番号 *</label>
            <BaseInput v-model="form.postalCode" placeholder="123-4567" required />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-600 mb-1.5">都道府県 *</label>
            <BaseInput v-model="form.prefecture" placeholder="東京都" required />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-600 mb-1.5">市区町村 *</label>
            <BaseInput v-model="form.city" placeholder="渋谷区" required />
          </div>
          <div>
            <label class="block text-sm font-medium text-gray-600 mb-1.5">番地・建物名 *</label>
            <BaseInput v-model="form.addressLine" placeholder="1-2-3 サンプルマンション101" required />
          </div>

          <div class="md:col-span-2 flex items-center gap-3">
            <input id="isDefault" v-model="form.isDefault" type="checkbox" class="h-4 w-4 rounded border-gray-300 text-indigo-600" />
            <label for="isDefault" class="text-sm text-gray-600">デフォルト配送先に設定する</label>
          </div>

          <div class="md:col-span-2 flex flex-wrap items-center gap-3">
            <button
              type="submit"
              class="px-6 py-2.5 bg-indigo-600 text-white font-semibold rounded-xl hover:bg-indigo-700 hover:shadow-md transition-all duration-200"
            >
              追加する
            </button>
            <p v-if="errors.length" class="text-sm text-rose-500">{{ errors.join(' / ') }}</p>
          </div>
        </form>
      </section>

      <section class="bg-white rounded-2xl shadow-sm p-6 space-y-4">
        <div class="flex items-center justify-between">
          <h2 class="text-xl font-semibold text-gray-800">登録済み配送先</h2>
          <span class="text-sm text-gray-400">デフォルトは1件のみ設定できます</span>
        </div>
        <div v-if="addresses.length === 0" class="text-gray-400">まだ配送先がありません。</div>
        <div v-else class="grid grid-cols-1 gap-3">
          <div
            v-for="address in addresses"
            :key="address.id"
            class="border border-gray-200 rounded-2xl p-5 flex flex-col md:flex-row md:items-center md:justify-between gap-3 hover:border-gray-300 transition"
          >
            <div class="space-y-1">
              <div class="flex items-center gap-2">
                <p class="text-lg font-semibold text-gray-800">{{ address.name }}</p>
                <span
                  v-if="address.isDefault"
                  class="px-2 py-0.5 text-xs rounded-full bg-indigo-100 text-indigo-700"
                >
                  デフォルト
                </span>
              </div>
              <p class="text-gray-500 text-sm">
                〒{{ address.postalCode }} {{ address.prefecture }} {{ address.city }}
              </p>
              <p class="text-gray-500 text-sm">{{ address.addressLine }}</p>
              <p class="text-gray-400 text-sm">TEL: {{ address.phone }}</p>
            </div>
            <div class="flex items-center gap-4">
              <button
                class="text-indigo-600 hover:text-indigo-800 text-sm font-medium transition"
                :disabled="address.isDefault"
                @click="setDefault(address.id)"
              >
                デフォルトにする
              </button>
              <button
                class="text-rose-500 hover:text-rose-700 text-sm font-medium transition"
                @click="removeAddress(address.id)"
              >
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
import BaseInput from '@/components/BaseInput.vue'
import type { Address, AddressPayload } from '@/api/addresses'
import { createAddress, deleteAddress, fetchAddresses, setDefaultAddress } from '@/api/addresses'

const addresses = ref<Address[]>([])

const form = reactive<AddressPayload>({
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
  if (
    !form.name ||
    !form.postalCode ||
    !form.prefecture ||
    !form.city ||
    !form.addressLine ||
    !form.phone
  ) {
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
