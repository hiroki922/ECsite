<template>
  <div class="flex flex-col min-h-screen">
    <HeaderComponent />
    <main class="flex-1">
      <router-view />
    </main>
    <footer class="bg-gray-800 text-gray-400 text-center text-sm py-6">
      <p>&copy; 2026 ECサイト. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup lang="ts">
import HeaderComponent from './components/HeaderComponent.vue'
</script>
