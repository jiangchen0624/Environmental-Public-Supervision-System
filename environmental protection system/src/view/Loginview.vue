<template>
  <!-- 登录页面主布局 -->
  <div class="login-page">
    <!-- 顶部横幅 -->
    <div class="top-banner">
      <el-icon :size="28"><Monitor /></el-icon>
      <span>NEU 空气质量监测系统</span>
    </div>

    <!-- 登录表单区域 -->
    <div class="form-container">
      <div class="form-card">
        <!-- 卡片头部 -->
        <div class="card-header">
          <div class="header-icon">
            <el-icon :size="32"><Avatar /></el-icon>
          </div>
          <h2>用户登录</h2>
          <p>登录后开始空气质量监测工作</p>
        </div>

        <!-- 登录表单 -->
        <el-form label-position="top" @submit.prevent="login">
          <el-form-item label="手机号">
            <el-input
              v-model="phone"
              placeholder="请输入手机号"
              maxlength="11"
              :prefix-icon="Phone"
            />
          </el-form-item>

          <el-form-item label="密码">
            <el-input
              v-model="password"
              placeholder="请输入密码"
              type="password"
              show-password
              :prefix-icon="Lock"
              @keyup.enter="login"
            />
          </el-form-item>

          <el-form-item label="登录身份">
            <el-select v-model="loginRole" placeholder="请选择登录身份" style="width: 100%">
              <el-option label="公众监督员 (NEPS端)" value="公众监督员" />
              <el-option label="AQI检测网格员 (NEPG端)" value="AQI检测网格员" />
              <el-option label="系统管理员 (NEPM端)" value="系统管理员" />
              <el-option label="决策者 (NEPV端)" value="决策者" />
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button
              type="success"
              @click="login"
              :loading="loading"
              size="large"
              class="submit-btn"
            >
              <el-icon v-if="!loading"><CircleCheck /></el-icon>
              <span>登 录</span>
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 底部链接 -->
        <div class="form-footer">
          <span>还没有账号？</span>
          <el-link type="success" @click="router.push({ name: 'Regist' })">前往注册</el-link>
          <span class="divider">|</span>
          <el-link type="success" @click="router.push('/')">返回首页</el-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { Monitor, Avatar, Phone, Lock, CircleCheck } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { post } from '@/util/request'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const phone = ref('')
const password = ref('')
const loginRole = ref('公众监督员')
const loading = ref(false)

const login = () => {
  if (loading.value) return
  if (!phone.value) {
    ElMessage.error('请输入手机号')
    return
  }
  if (!password.value) {
    ElMessage.error('请输入密码')
    return
  }

  loading.value = true
  post('/user/login', {
    phone: phone.value,
    password: password.value,
    role: loginRole.value,
  })
    .then((result) => {
      loading.value = false
      if (result.code === 200) {
        // 类型断言以避免 result.data 为 unknown 导致的类型错误
        userStore.setUser(result.data as any)
        ElMessage.success(`登录成功！身份：${loginRole.value} —— ${(result.data as any)?.name || ''}`)
        const redirect = router.currentRoute.value.query.redirect as string
        router.push(redirect || '/')
      } else {
        ElMessage.error(result.msg)
      }
    })
    .catch(() => {
      loading.value = false
      ElMessage.error('网络错误，请检查后端服务是否启动')
    })
}
</script>

<style scoped>
/* ========== 页面整体 ========== */
.login-page {
  min-height: 100vh;
  background: linear-gradient(160deg, #e8f5e9 0%, #c8e6c9 20%, #e0f2e9 40%, #e3f2fd 70%, #e8eaf6 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.login-page::before {
  content: '';
  position: absolute;
  top: -120px;
  right: -80px;
  width: 400px;
  height: 400px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(76, 175, 80, 0.08) 0%, transparent 70%);
  pointer-events: none;
}

.login-page::after {
  content: '';
  position: absolute;
  bottom: -100px;
  left: -100px;
  width: 500px;
  height: 500px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(33, 150, 243, 0.06) 0%, transparent 70%);
  pointer-events: none;
}

/* ========== 顶部横幅 ========== */
.top-banner {
  width: 100%;
  background: linear-gradient(135deg, #2e7d32 0%, #388e3c 40%, #43a047 100%);
  color: #fff;
  text-align: center;
  padding: 14px 0;
  font-size: 18px;
  font-weight: 500;
  letter-spacing: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  box-shadow: 0 2px 12px rgba(46, 125, 50, 0.3);
  position: relative;
  z-index: 1;
}

/* ========== 表单容器 ========== */
.form-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  position: relative;
  z-index: 1;
}

.form-card {
  width: 420px;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.08), 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 36px 40px 28px;
  border: 1px solid rgba(76, 175, 80, 0.12);
}

/* ========== 卡片头部 ========== */
.card-header {
  text-align: center;
  margin-bottom: 28px;
}

.header-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: linear-gradient(135deg, #e3f2fd, #bbdefb);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
  color: #1565c0;
}

.card-header h2 {
  margin: 0 0 6px;
  font-size: 22px;
  color: #1565c0;
  font-weight: 600;
  letter-spacing: 1px;
}

.card-header p {
  margin: 0;
  font-size: 13px;
  color: #64b5f6;
  letter-spacing: 1px;
}

/* ========== 登录按钮 ========== */
.submit-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  letter-spacing: 4px;
}

.submit-btn :deep(span) {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

/* ========== 底部链接 ========== */
.form-footer {
  text-align: center;
  padding-top: 8px;
  font-size: 13px;
  color: #999;
}

.form-footer .divider {
  margin: 0 8px;
  color: #ddd;
}

/* ========== Element Plus 覆盖 ========== */
:deep(.el-input) {
  --el-input-focus-border-color: #64b5f6;
  --el-input-hover-border-color: #90caf9;
}

:deep(.el-link--primary) {
  --el-link-text-color: #1976d2;
  --el-link-hover-text-color: #1565c0;
}
</style>
