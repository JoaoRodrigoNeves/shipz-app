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
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: true }
        },
        {
          path: 'products-list',
          component: () => import('../pages/client/products.vue'),
          meta: { manufacterAuth: false, logisticAuth: false, clientAuth: true, requiredAuth: true }
        },
        {
          path: 'product-catalogs',
          component: () => import('../pages/product-catalog/product-catalogs.vue'),
          meta: { manufacterAuth: true, logisticAuth: false, clientAuth: false, requiredAuth: true }
        },
        {
          path: 'product-catalog/:code',
          component: () => import('../pages/product-catalog/product-catalog-details.vue'),
          meta: { manufacterAuth: true, logisticAuth: false, clientAuth: false, requiredAuth: true }
        },
        {
          path: 'products',
          component: () => import('../pages/product/products.vue'),
          meta: { manufacterAuth: true, logisticAuth: false, clientAuth: false, requiredAuth: true }
        },
        {
          path: 'product-packages',
          component: () => import('../pages/product-package/product-packages.vue'),
          meta: { manufacterAuth: true, logisticAuth: false, clientAuth: false, requiredAuth: true }
        },
        {
          path: 'orders',
          component: () => import('../pages/client-orders/client-orders.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: true }
        },
        {
          path: 'order/:code',
          component: () => import('../pages/client-orders/client-orders-details.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: true }
        },
        {
          path: 'account-settings',
          component: () => import('../pages/account-settings.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: true }
        },
        {
          path: 'product-package/:code',
          component: () => import('../pages/product-package/product-package-details.vue'),
          meta: { manufacterAuth: true, logisticAuth: false, clientAuth: false, requiredAuth: true }
        },
        {
          path: 'sensor-observation',
          component: () => import('../pages/sensor-register.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: false }
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
          name: 'noRouteMatch',
          component: () => import('../pages/[...all].vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: false }
        },
      ],
    },
  ],
})

router.beforeEach(async (to, from, next) => {
  if (to.meta.requiredAuth) {
    const user = JSON.parse(sessionStorage.getItem('user_info'))
    const authUser = sessionStorage.getItem('token')
    if (!authUser) {
        next({ path: '/login' })
    }
    else if (to.meta.manufacterAuth) {
      if (user.role === 'ProductManufacter') {
        next()
      } else {
        next({ path: '/dashboard' })
      }
    } else if (to.meta.logisticAuth) {
      if (user.role === 'LogisticOperator') {
        next()
      } else {
        next({ path: '/dashboard' })
      }
    } else if (to.meta.clientAuth) {
      if (user.role === 'FinalCostumer') {
        next()
      } else {
        next({ path: '/dashboard' })
      }
    }
  } else if (to.name == "noRouteMatch"){
    next()
  }else{
    next()
  }
})

export default router
