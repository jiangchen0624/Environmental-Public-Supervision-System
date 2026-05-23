import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface UserInfo {
  id: number
  phone: string
  name: string
  province: string
  city: string
  role: string
}

export const useUserStore = defineStore('user', () => {
  // 从 sessionStorage 恢复登录状态（关闭浏览器自动清除）
  const saved = sessionStorage.getItem('user')
  const initial: UserInfo | null = saved ? JSON.parse(saved) : null

  const user = ref<UserInfo | null>(initial)
  const token = ref<string>(sessionStorage.getItem('token') || '')

  const isLoggedIn = computed(() => !!user.value && !!token.value)
  const userName = computed(() => user.value?.name || '')
  const userRole = computed(() => user.value?.role || '')

  /** 登录：保存用户信息到 state + sessionStorage */
  function setUser(data: UserInfo) {
    user.value = data
    token.value = 'logged-in'
    sessionStorage.setItem('user', JSON.stringify(data))
    sessionStorage.setItem('token', 'logged-in')
  }

  /** 退出登录：清除 state + sessionStorage */
  function logout() {
    user.value = null
    token.value = ''
    sessionStorage.removeItem('user')
    sessionStorage.removeItem('token')
  }

  return { user, token, isLoggedIn, userName, userRole, setUser, logout }
})
