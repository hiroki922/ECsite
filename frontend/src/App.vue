<template>
  <header class="p-4 bg-gray-800 text-white">
    <h1 class="text-xl font-bold">ECサイト</h1>
    <nav class="mt-2 space-x-4">
      <router-link to="/">Home</router-link>
      <router-link to="/login">Login</router-link>
    </nav>
  </header>

  <main class="p-6">
    <!-- ルートごとにコンテンツがここに表示される -->
    <router-view />
  </main>
</template>
