import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProductsView from '../views/ProductsView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import AddressView from '../views/AddressView.vue'
import { useAuthStore } from '../stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/products', name: 'products', component: ProductsView },
    { path: '/products/:id', name: 'product-detail', component: () => import('../views/ProductDetailView.vue') },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/register', name: 'register', component: RegisterView },
    { path: '/cart', name: 'cart', meta: { requiresAuth: true }, component: () => import('../views/CartView.vue') },
    { path: '/checkout', name: 'checkout', meta: { requiresAuth: true }, component: () => import('../views/CheckoutView.vue') },
    { path: '/orders', name: 'orders', meta: { requiresAuth: true }, component: () => import('../views/OrdersView.vue') },
    { path: '/orders/:id', name: 'order-detail', meta: { requiresAuth: true }, component: () => import('../views/OrderDetailView.vue') },
    { path: '/addresses', name: 'addresses', meta: { requiresAuth: true }, component: AddressView },
    { path: '/admin', name: 'admin', meta: { requiresAuth: true, requiresAdmin: true }, component: () => import('../views/admin/AdminView.vue') },
    { path: '/admin/products', name: 'admin-products', meta: { requiresAuth: true, requiresAdmin: true }, component: () => import('../views/admin/ProductList.vue') },
    { path: '/admin/products/new', name: 'admin-product-new', meta: { requiresAuth: true, requiresAdmin: true }, component: () => import('../views/admin/ProductNew.vue') },
    { path: '/admin/products/:id/edit', name: 'admin-product-edit', meta: { requiresAuth: true, requiresAdmin: true }, component: () => import('../views/admin/ProductEdit.vue') },
    { path: '/admin/users', name: 'admin-users', meta: { requiresAuth: true, requiresAdmin: true }, component: () => import('../views/admin/UserManagement.vue') },
    { path: '/admin/orders', name: 'admin-orders', meta: { requiresAuth: true, requiresAdmin: true }, component: () => import('../views/admin/AdminOrderList.vue') },
  ],
})

router.beforeEach((to) => {
  const auth = useAuthStore()

  if (to.meta.requiresAuth && !auth.isLoggedIn) {
    return { name: 'login' }
  }
  if (to.meta.requiresAdmin && auth.role !== 'ADMIN') {
    return { name: 'home' }
  }
})

export default router
