/**
 * 路由配置文件
 * 四端：NEPS监督员 | NEPG网格员 | NEPM管理员 | NEPV决策者
 */
import { createRouter, createWebHistory } from 'vue-router'
import Homeview from '@/view/HomeView.vue'
import RegistView from '@/view/RegistView.vue'
import Loginview from '@/view/LoginView.vue'
import ReportView from '@/view/ReportView.vue'
import AdminView from '@/view/AdminView.vue'
import InspectorView from '@/view/InspectorView.vue'
import DecisionView from '@/view/DecisionView.vue'
import ProfileView from '@/view/ProfileView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'Home', component: Homeview },
    { path: '/regist', name: 'Regist', component: RegistView },
    { path: '/login', name: 'Login', component: Loginview },
    {
      path: '/report',
      name: 'Report',
      component: ReportView,
      meta: { requiresAuth: true, role: '公众监督员' },
    },
    {
      path: '/admin',
      name: 'Admin',
      component: AdminView,
      meta: { requiresAuth: true, roles: ['系统管理员', '决策者'] },
    },
    {
      path: '/inspector',
      name: 'Inspector',
      component: InspectorView,
      meta: { requiresAuth: true, role: 'AQI检测网格员' },
    },
    {
      path: '/decision',
      name: 'Decision',
      component: DecisionView,
      meta: { requiresAuth: true, role: '决策者' },
    },
    {
      path: '/profile',
      name: 'Profile',
      component: ProfileView,
      meta: { requiresAuth: true },
    },
  ],
})

router.beforeEach((to, _from) => {
  // 从 sessionStorage 读取登录态
  const token = sessionStorage.getItem('token')
  const userStr = sessionStorage.getItem('user')
  const user: { role: string } | null = userStr ? JSON.parse(userStr) as { role: string } : null

  if (to.meta.requiresAuth) {
    if (!token || !user) {
      return { name: 'Login', query: { redirect: to.fullPath } }
    }
    if (to.meta.roles && !to.meta.roles.includes(user.role)) {
      return { name: 'Home' }
    }
    if (to.meta.role && user.role !== to.meta.role) {
      return { name: 'Home' }
    }
  }
})

export default router
