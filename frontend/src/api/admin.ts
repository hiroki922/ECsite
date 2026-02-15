import client from '@/lib/apiClient'

export type DashboardStats = {
  productCount: number
  userCount: number
  pendingOrderCount: number
  totalOrderCount: number
}

export type AdminUser = {
  id: number
  name: string
  email: string
  role: string
}

export type AdminOrder = {
  id: number
  addressName: string
  totalAmount: number
  status: string
  createdAt: string
  items: { id: number; productName: string; quantity: number; price: number }[]
}

export const fetchDashboardStats = async (): Promise<DashboardStats> => {
  const res = await client.get<DashboardStats>('/admin/dashboard/stats')
  return res.data
}

export const fetchRecentOrders = async (): Promise<AdminOrder[]> => {
  const res = await client.get<AdminOrder[]>('/admin/dashboard/recent-orders')
  return res.data
}

export const fetchUsers = async (): Promise<AdminUser[]> => {
  const res = await client.get<AdminUser[]>('/admin/users')
  return res.data
}

export const updateUserRole = async (id: number, role: string): Promise<AdminUser> => {
  const res = await client.put<AdminUser>(`/admin/users/${id}/role`, { role })
  return res.data
}

export const fetchAllOrders = async (): Promise<AdminOrder[]> => {
  const res = await client.get<AdminOrder[]>('/admin/orders')
  return res.data
}

export const updateOrderStatus = async (id: number, status: string): Promise<AdminOrder> => {
  const res = await client.put<AdminOrder>(`/admin/orders/${id}/status`, { status })
  return res.data
}
