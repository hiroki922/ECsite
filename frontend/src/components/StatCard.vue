<template>
  <div class="bg-white rounded-2xl shadow-sm p-5 flex flex-col gap-1.5">
    <p class="text-sm text-gray-400 font-medium">{{ title }}</p>
    <p class="text-2xl font-bold text-gray-800">{{ value }}</p>
    <p v-if="trend" class="text-sm font-medium" :class="highlight ? 'text-rose-500' : 'text-gray-400'">{{ trend }}</p>
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
