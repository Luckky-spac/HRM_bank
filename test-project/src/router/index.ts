import { useAuthStore } from '@/stores/auth'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'homeview',
      component: () => import('@/views/HomeView.vue'),
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/account/LoginAccount.vue'),
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/account/RegisterAccount.vue'),
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('@/views/account/ProfileView.vue'),
      meta: { requiresAuth: true },
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: () => import('@/views/dashboard/DashboardLayout.vue'),
      // meta: { requiresAuth: true, requiresRole: 'admin' },
    },
    {
      path: '/account',
      name: 'UserList',
      component: () => import('@/views/dashboard/UserManager.vue'),
      // meta: { requiresAuth: true, requiresRole: ['admin', 'manager'] },
    },
    {
      path: '/employee',
      name: 'employee',
      component: () => import('@/views/dashboard/EmpManager.vue'),
      // meta: { requiresAuth: true, requiresRole: ['admin', 'manger'] },
    },
    {
      path: '/about',
      name: 'about',
      component: () => import('@/views/AboutView.vue'),
    },
    {
      path: '/transaction',
      name: 'transaction',
      component: () => import('@/views/TransactionView.vue'),
      // meta: { requiresAuth: true, requiresRole: 'admin' },
    },
  ],
})

router.beforeEach(async (to, from, next) => {
  const auth = useAuthStore()
  console.log('Going to:', to.name, '| Token:', auth.token)
  if (auth.token && !auth.user) {
    await auth.fetchProfile()
  }
  if (to.meta.requiresAuth && !auth.token) {
    return next({ name: 'login' })
  }
  if (to.meta.requiresRole) {
    const allowedRoles = Array.isArray(to.meta.requiresRole)
      ? to.meta.requiresRole
      : [to.meta.requiresRole]
    if (!allowedRoles.includes(auth.user?.role)) {
      return next({ name: 'homeview' })
    }
  }
  return next()
})

export default router
