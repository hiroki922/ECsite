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
  'w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring focus:ring-blue-300 text-gray-800 placeholder-gray-400'
</script>

<style scoped>
input::placeholder {
  color: rgba(0, 0, 0, 0.4);
}

@media (prefers-color-scheme: dark) {
  input::placeholder {
    color: rgba(255, 255, 255, 0.6);
  }
}
</style>
