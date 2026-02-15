const labels: Record<string, string> = {
  PENDING: '処理中',
  PAID: '支払済',
  SHIPPED: '発送済',
  DELIVERED: '配達完了',
  CANCELLED: 'キャンセル',
}

const classes: Record<string, string> = {
  PENDING: 'bg-yellow-100 text-yellow-700',
  PAID: 'bg-blue-100 text-blue-700',
  SHIPPED: 'bg-purple-100 text-purple-700',
  DELIVERED: 'bg-green-100 text-green-700',
  CANCELLED: 'bg-red-100 text-red-700',
}

export const statusLabel = (s: string): string => labels[s] || s

export const statusClass = (s: string): string => classes[s] || 'bg-gray-100 text-gray-700'
