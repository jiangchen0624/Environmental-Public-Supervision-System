/**
 * 路由配置文件
 * 四端：NEPS监督员 | NEPG网格员 | NEPM管理员 | NEPV决策者
 */
import { createRouter, createWebHistory } from 'vue-router'
import Homeview from '@/view/Homeview.vue'
import RegistView from '@/view/RegistView.vue'
import Loginview from '@/view/Loginview.vue'
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
    { path: '/report', name: 'Report', component: ReportView },
    { path: '/admin', name: 'Admin', component: AdminView },
    { path: '/inspector', name: 'Inspector', component: InspectorView },
    { path: '/decision', name: 'Decision', component: DecisionView },
    { path: '/profile', name: 'Profile', component: ProfileView },
  ],
})

export default router
