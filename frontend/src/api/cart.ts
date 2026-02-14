import axios from 'axios'

export type CartItem = {
  id: number
  productId: number
  productName: string
  productPrice: number
  productImageUrl: string
  quantity: number
  stock: number
}

export type CartItemPayload = {
  productId: number
  quantity: number
}

const client = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
})

export const fetchCart = async (): Promise<CartItem[]> => {
  const res = await client.get<CartItem[]>('/cart')
  return res.data
}

export const addToCart = async (payload: CartItemPayload): Promise<CartItem> => {
  const res = await client.post<CartItem>('/cart', payload)
  return res.data
}

export const updateCartItem = async (id: number, quantity: number): Promise<CartItem> => {
  const res = await client.put<CartItem>(`/cart/${id}`, { quantity })
  return res.data
}

export const removeCartItem = async (id: number): Promise<void> => {
  await client.delete(`/cart/${id}`)
}
