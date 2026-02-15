<template>
  <input
    v-bind="$attrs"
    :value="modelValue"
    @input="onInput"
    :type="type"
    :class="[baseClass, $attrs.class]"
    style="color: #111 !important"
  />
</template>

<script setup lang="ts">
const props = defineProps<{
  modelValue: string
  type?: string
}>()

const emits = defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()

const onInput = (event: Event) => {
  const target = event.target as HTMLInputElement
  emits('update:modelValue', target.value)
}

const type = props.type || 'text'

const baseClass =
  'w-full px-3 py-2.5 border border-gray-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-indigo-300 focus:border-transparent text-gray-800 placeholder-gray-400 transition'
</script>

<style scoped>
input::placeholder {
  color: rgba(0, 0, 0, 0.35);
}
</style>
