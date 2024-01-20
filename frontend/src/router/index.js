import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: () => import('../layouts/default.vue'),
      children: [
        {
          path: 'products-list',
          component: () => import('../pages/client/products.vue'),
          meta: { manufacterAuth: false, logisticAuth: false, clientAuth: true, requiredAuth: true },
        },
        {
          path: 'product-package/:code',
          component: () => import('../pages/product-package/product-package-details.vue'),
          meta: { manufacterAuth: true, logisticAuth: false, clientAuth: false, requiredAuth: true },
        },
        {
          path: 'product-catalogs',
          component: () => import('../pages/product-catalog/product-catalogs.vue'),
          meta: { manufacterAuth: true, logisticAuth: false, clientAuth: false, requiredAuth: true },
        },
        {
          path: 'product-catalog/:code',
          component: () => import('../pages/product-catalog/product-catalog-details.vue'),
          meta: { manufacterAuth: true, logisticAuth: false, clientAuth: false, requiredAuth: true },
        },
        {
          path: 'products',
          component: () => import('../pages/product/products.vue'),
          meta: { manufacterAuth: true, logisticAuth: false, clientAuth: false, requiredAuth: true },
        },
        {
          path: 'product/:code',
          component: () => import('../pages/product/product-details.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: true },
        },
        {
          path: 'orders',
          component: () => import('../pages/orders/orders.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: true },
        },
        {
          path: 'order/:code',
          component: () => import('../pages/orders/orders-details.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: true },
        },
        {
          path: 'account-settings',
          component: () => import('../pages/account-settings.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: true },
        },
        {
          path: 'sensor-observation',
          component: () => import('../pages/sensor-register.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: false },
        },
        {
          path: 'transport-packages',
          component: () => import('../pages/transport-package-catalog/transport-package-catalog.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: false, requiredAuth: true },
        },
        {
          path: 'transport-packages/:code',
          component: () => import('../pages/transport-package-catalog/transport-package-catalog-details.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: false, requiredAuth: true },
        },
        {
          path: 'sensors',
          component: () => import('../pages/sensor/sensors.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: false, requiredAuth: true },
        },
        {
          path: 'sensor/:code',
          component: () => import('../pages/sensor/sensor-details.vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: true },
        },
      ],
    },
    {
      path: '/',
      component: () => import('../layouts/blank.vue'),
      children: [
        {
          path: 'login',
          component: () => import('../pages/login.vue'),
          meta: { manufacterAuth: false, logisticAuth: false, clientAuth: false, requiredAuth: false },
        },
        {
          path: '/:pathMatch(.*)*',
          name: 'noRouteMatch',
          component: () => import('../pages/[...all].vue'),
          meta: { manufacterAuth: true, logisticAuth: true, clientAuth: true, requiredAuth: false },
        },
      ],
    },
  ],
})

router.beforeEach(async (to, from, next) => {
  const user = JSON.parse(sessionStorage.getItem('user_info'))
  const authUser = sessionStorage.getItem('token')

  if (to.meta.requiredAuth) {
    if (!authUser) {
      return next({ path: '/login' })
    }

    if (user.role === 'ProductManufacter' && to.meta.manufacterAuth) {
      return next()
    } else if (user.role === 'LogisticOperator' && to.meta.logisticAuth) {
      return next()
    } else if (user.role === 'FinalCostumer' && to.meta.clientAuth) {
      return next()
    } else {
      return next({ path: '/login' })
    }
  } else {
    if (authUser) {
      if (user && user.role === 'ProductManufacter') {
        return next({ path: 'product-catalogs' })
      } else if (user && user.role === 'LogisticOperator') {
        return next({ path: 'orders' })
      } else if (user && user.role === 'FinalCostumer') {
        return next({ path: 'products-list' })
      }
    } else if (to.path == '/') {
      next({ path: 'login' })
    } else {
      next()
    }
  }
}
)

export default router
