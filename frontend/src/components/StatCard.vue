<template>
  <div class="bg-white rounded-2xl shadow p-4 flex flex-col gap-1">
    <p class="text-sm text-gray-500">{{ title }}</p>
    <p class="text-2xl font-bold text-gray-800">{{ value }}</p>
    <p class="text-sm" :class="highlight ? 'text-red-600' : 'text-gray-500'">{{ trend }}</p>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  title: string
  value: string
  trend?: string
  highlight?: boolean
}>()
</script>
