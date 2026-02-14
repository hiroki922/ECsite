import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api', //Spring Boot APIのベースURL
  withCredentials: true, // Cookieを含める
})

export interface Product {
  id?: number
  name: string
  description: string
  price: number
  imageUrl: string
  stock: number
}

// 商品詳細を取得
export async function fetchProduct(id: number): Promise<Product> {
  const response = await api.get<Product>(`/products/${id}`)
  return response.data
}

// 商品一覧を取得
export async function fetchProducts(): Promise<Product[]> {
  const response = await api.get<Product[]>('/products')
  return response.data
}

// 商品を登録（管理者用）
export async function createProduct(product: Product): Promise<Product> {
  const response = await api.post<Product>('/admin/products', product)
  return response.data
}

// 商品を更新（管理者用）
export async function updateProduct(product: Product): Promise<Product> {
  const response = await api.put<Product>(`/admin/products/${product.id}`, product)
  return response.data
}

// 商品を削除（管理者用）
export async function deleteProduct(productId: number): Promise<void> {
  await api.delete(`/admin/products/${productId}`)
}
