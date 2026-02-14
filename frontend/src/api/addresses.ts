import axios from 'axios'

export type AddressPayload = {
  name: string
  postalCode: string
  prefecture: string
  city: string
  addressLine: string
  phone: string
  isDefault: boolean
}

export type Address = AddressPayload & { id: number }

const client = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
})

export const fetchAddresses = async () => {
  const res = await client.get<Address[]>('/addresses')
  return res.data
}

export const createAddress = async (payload: AddressPayload) => {
  const res = await client.post<Address>('/addresses', payload)
  return res.data
}

export const updateAddress = async (id: number, payload: AddressPayload) => {
  const res = await client.put<Address>(`/addresses/${id}`, payload)
  return res.data
}

export const deleteAddress = async (id: number) => {
  await client.delete(`/addresses/${id}`)
}

export const setDefaultAddress = async (id: number) => {
  const res = await client.post<Address>(`/addresses/${id}/default`)
  return res.data
}
