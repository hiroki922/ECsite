import axios from "axios";

// CSRF トークンを HTML の meta から取得
const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content') || '';
const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content') || 'X-CSRF-TOKEN';

// Axios のデフォルト設定
const api = axios.create({
  baseURL: '/api',  // 共通の API ベース URL
  headers: {
    [csrfHeader]: csrfToken,
    'Content-Type': 'application/json',
  },
});

export default api;