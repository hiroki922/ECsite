<template>
  <div class="bg-gray-100 min-h-screen py-10">
    <div class="max-w-4xl mx-auto px-4 space-y-6">
      <h1 class="text-3xl font-bold text-gray-800">ご注文の確認</h1>

      <!-- 注文商品 -->
      <section class="bg-white rounded-2xl shadow p-6">
        <h2 class="text-xl font-semibold text-gray-800 mb-4">注文内容</h2>
        <div class="divide-y">
          <div v-for="item in cart.items" :key="item.id" class="py-3 flex items-center gap-4">
            <img :src="item.productImageUrl" class="w-16 h-16 object-cover rounded" />
            <div class="flex-1">
              <p class="font-semibold text-gray-800">{{ item.productName }}</p>
              <p class="text-gray-500 text-sm">&yen;{{ item.productPrice.toLocaleString() }} x {{ item.quantity }}</p>
            </div>
            <p class="font-bold">&yen;{{ (item.productPrice * item.quantity).toLocaleString() }}</p>
          </div>
        </div>
        <div class="mt-4 pt-4 border-t flex justify-between">
          <p class="text-lg font-bold">合計</p>
          <p class="text-lg font-bold text-blue-600">&yen;{{ cart.totalAmount.toLocaleString() }}</p>
        </div>
      </section>

      <!-- 配送先選択 -->
      <section class="bg-white rounded-2xl shadow p-6">
        <h2 class="text-xl font-semibold text-gray-800 mb-4">配送先を選択</h2>
        <div v-if="addresses.length === 0" class="text-gray-500">
          配送先が登録されていません。
          <router-link to="/addresses" class="text-blue-600 hover:underline">配送先を登録する</router-link>
        </div>
        <div v-else class="space-y-3">
          <label
            v-for="addr in addresses"
            :key="addr.id"
            class="block border rounded-xl p-4 cursor-pointer transition"
            :class="selectedAddressId === addr.id ? 'border-blue-500 bg-blue-50' : 'border-gray-200 hover:border-gray-400'"
          >
            <input
              type="radio"
              :value="addr.id"
              v-model="selectedAddressId"
              class="mr-3"
            />
            <span class="font-semibold">{{ addr.name }}</span>
            <span v-if="addr.isDefault" class="ml-2 text-xs bg-blue-100 text-blue-700 px-2 py-0.5 rounded-full">デフォルト</span>
            <p class="text-gray-600 text-sm mt-1 ml-6">
              〒{{ addr.postalCode }} {{ addr.prefecture }} {{ addr.city }} {{ addr.addressLine }}
            </p>
            <p class="text-gray-500 text-sm ml-6">TEL: {{ addr.phone }}</p>
          </label>
        </div>
      </section>

      <!-- 注文確定 -->
      <div class="flex items-center justify-between">
        <router-link to="/cart" class="text-blue-600 hover:underline">&larr; カートに戻る</router-link>
        <button
          :disabled="!selectedAddressId || cart.items.length === 0 || submitting"
          class="px-8 py-3 font-semibold text-white rounded-lg transition"
          :class="selectedAddressId && !submitting ? 'bg-blue-600 hover:bg-blue-700' : 'bg-gray-400 cursor-not-allowed'"
          @click="handleOrder"
        >
          {{ submitting ? '処理中...' : '注文を確定する' }}
        </button>
      </div>

      <p v-if="errorMessage" class="text-red-600 text-center">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { fetchAddresses, type Address } from '@/api/addresses'
import { createOrder } from '@/api/orders'

const router = useRouter()
const cart = useCartStore()
const addresses = ref<Address[]>([])
const selectedAddressId = ref<number | null>(null)
const submitting = ref(false)
const errorMessage = ref('')

onMounted(async () => {
  await cart.load()
  try {
    addresses.value = await fetchAddresses()
    const defaultAddr = addresses.value.find((a) => a.isDefault)
    if (defaultAddr) {
      selectedAddressId.value = defaultAddr.id
    }
  } catch {
    errorMessage.value = '配送先の取得に失敗しました'
  }
})

const handleOrder = async () => {
  if (!selectedAddressId.value) return
  submitting.value = true
  errorMessage.value = ''
  try {
    const order = await createOrder({ addressId: selectedAddressId.value })
    await cart.load()
    router.push(`/orders/${order.id}`)
  } catch (err: any) {
    errorMessage.value = err.response?.data?.error || '注文の作成に失敗しました'
  } finally {
    submitting.value = false
  }
}
</script>
