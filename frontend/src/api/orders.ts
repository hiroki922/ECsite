import client from '@/lib/apiClient'

export type OrderItem = {
  id: number
  productId: number
  productName: string
  productImageUrl: string
  quantity: number
  price: number
}

export type Order = {
  id: number
  addressName: string
  addressPostalCode: string
  addressPrefecture: string
  addressCity: string
  addressLine: string
  addressPhone: string
  totalAmount: number
  status: string
  createdAt: string
  items: OrderItem[]
}

export type OrderRequest = {
  addressId: number
}

export const createOrder = async (payload: OrderRequest): Promise<Order> => {
  const res = await client.post<Order>('/orders', payload)
  return res.data
}

export const fetchOrders = async (): Promise<Order[]> => {
  const res = await client.get<Order[]>('/orders')
  return res.data
}

export const fetchOrder = async (id: number): Promise<Order> => {
  const res = await client.get<Order>(`/orders/${id}`)
  return res.data
}
