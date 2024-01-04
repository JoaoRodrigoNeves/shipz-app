import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/dashboard' },
    {
      path: '/',
      component: () => import('../layouts/default.vue'),
      children: [
        {
          path: 'dashboard',
          component: () => import('../pages/dashboard.vue'),
        },
        {
          path: 'products-list',
          component: () => import('../pages/client/products.vue'),
        },
        {
          path: 'product-catalogs',
          component: () => import('../pages/product-catalog/product-catalogs.vue'),
        },
        {
          path: 'product-catalog/:code',
          component: () => import('../pages/product-catalog/product-catalog-details.vue'),
        },
        {
          path: 'products',
          component: () => import('../pages/product/products.vue'),
        },
        {
          path: 'product-packages',
          component: () => import('../pages/product-package/product-packages.vue'),
        },
        {
          path: 'orders',
          component: () => import('../pages/client-orders/client-orders.vue'),
        },
        {
          path: 'order/:code',
          component: () => import('../pages/client-orders/client-orders-details.vue'),
        },
        {
          path: 'account-settings',
          component: () => import('../pages/account-settings.vue'),
        },
        {
          path: 'product-package/:code',
          component: () => import('../pages/product-package/product-package-details.vue'),
        },
        {
          path: 'account-settings',
          component: () => import('../pages/account-settings.vue'),
        }
      ],
    },
    {
      path: '/',
      component: () => import('../layouts/blank.vue'),
      children: [
        {
          path: 'login',
          component: () => import('../pages/login.vue'),
        },
        {
          path: '/:pathMatch(.*)*',
          component: () => import('../pages/[...all].vue'),
        },
      ],
    },
  ],
})

export default router
