import { defineStore } from 'pinia'
import { fetchCart, addToCart, updateCartItem, removeCartItem } from '@/api/cart'
import type { CartItem } from '@/api/cart'

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: [] as CartItem[],
    loading: false,
    error: '',
  }),
  getters: {
    totalAmount: (state) =>
      state.items.reduce((sum, item) => sum + item.productPrice * item.quantity, 0),
    itemCount: (state) =>
      state.items.reduce((sum, item) => sum + item.quantity, 0),
  },
  actions: {
    async load() {
      this.loading = true
      this.error = ''
      try {
        this.items = await fetchCart()
      } catch {
        this.error = 'カートの取得に失敗しました'
      } finally {
        this.loading = false
      }
    },
    async add(productId: number, quantity: number) {
      await addToCart({ productId, quantity })
      await this.load()
    },
    async update(id: number, quantity: number) {
      await updateCartItem(id, quantity)
      await this.load()
    },
    async remove(id: number) {
      await removeCartItem(id)
      await this.load()
    },
  },
})
