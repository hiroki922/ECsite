import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ProductsView from '../views/ProductsView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import AddressView from '../views/AddressView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/products', name: 'products', component: ProductsView },
    { path: '/products/:id', name: 'product-detail', component: () => import('../views/ProductDetailView.vue') },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/register', name: 'register', component: RegisterView },
    { path: '/cart', name: 'cart', component: () => import('../views/CartView.vue') },
    { path: '/checkout', name: 'checkout', component: () => import('../views/CheckoutView.vue') },
    { path: '/orders', name: 'orders', component: () => import('../views/OrdersView.vue') },
    { path: '/orders/:id', name: 'order-detail', component: () => import('../views/OrderDetailView.vue') },
    { path: '/addresses', name: 'addresses', component: AddressView },
    { path: '/admin', name: 'admin', component: () => import('../views/admin/AdminView.vue') },
    { path: '/admin/products', name: 'admin-products', component: () => import('../views/admin/ProductList.vue') },
    { path: '/admin/products/new', name: 'admin-product-new', component: () => import('../views/admin/ProductNew.vue') },
    { path: '/admin/products/:id/edit', name: 'admin-product-edit', component: () => import('../views/admin/ProductEdit.vue') },
    { path: '/admin/users', name: 'admin-users', component: () => import('../views/admin/UserMnagement.vue') },
  ],
})

export default router
