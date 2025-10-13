<template>
  <HeaderComponent />
  <!-- ヘッダーを別コンポーネント化 -->
  <main class="p-6">
    <router-view />
  </main>
</template>

<script setup lang="ts">
import HeaderComponent from './components/HeaderComponent.vue'
</script>
