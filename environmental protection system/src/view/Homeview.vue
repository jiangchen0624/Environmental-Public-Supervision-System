<template>
  <!-- 首页主布局 -->
  <div class="home-page">
    <!-- 顶部横幅 -->
    <div class="top-banner">
      <el-icon :size="28"><Monitor /></el-icon>
      <span>NEU 空气质量监测系统</span>
    </div>

    <!-- 主内容区域 -->
    <div class="main-container">
      <div class="hero-card">
        <!-- 左侧图片 -->
        <div class="hero-left">
          <img :src="logo" alt="logo" />
        </div>
        <!-- 右侧内容 -->
        <div class="hero-right">
          <!-- 未登录 -->
          <template v-if="!userStore.isLoggedIn">
            <div class="hero-icon">
              <el-icon :size="48"><Sunny /></el-icon>
            </div>
            <h1>NEU 空气质量监测</h1>
            <p class="subtitle">实时监测 · 精准分析 · 守护蓝天</p>
            <p class="desc">欢迎使用空气质量监测系统，请登录或注册账号开始使用</p>
            <div class="btn-group">
              <el-button type="success" size="large" @click="toLogin" class="hero-btn">
                <el-icon><Avatar /></el-icon>
                <span>登 录</span>
              </el-button>
              <el-button type="primary" size="large" @click="toRegist" class="hero-btn">
                <el-icon><UserFilled /></el-icon>
                <span>注 册</span>
              </el-button>
            </div>
          </template>
          <!-- 已登录 -->
          <template v-else>
            <div class="hero-icon logged">
              <el-icon :size="48"><Avatar /></el-icon>
            </div>
            <h1>欢迎回来，{{ userStore.userName }}</h1>
            <p class="subtitle">
              <el-tag :type="roleTagType" size="large">{{ userStore.userRole }}</el-tag>
            </p>
            <p class="desc">
              手机号：{{ userStore.user?.phone }}<br/>
              地区：{{ userStore.user?.province }} {{ userStore.user?.city }}
            </p>
            <div class="btn-group">
              <template v-if="userStore.userRole === '决策者'">
                <el-button type="primary" size="large" @click="router.push({name:'Admin'})" class="hero-btn">
                  <el-icon><DataAnalysis /></el-icon>
                  <span>数据分析</span>
                </el-button>
                <el-button type="success" size="large" @click="router.push({name:'Decision'})" class="hero-btn">
                  <el-icon><Monitor /></el-icon>
                  <span>可视化大屏</span>
                </el-button>
              </template>
              <template v-else>
                <el-button type="primary" size="large" @click="goDashboard" class="hero-btn">
                  <el-icon><DataAnalysis /></el-icon>
                  <span>进入系统</span>
                </el-button>
              </template>
              <el-button size="large" @click="router.push({name:'Profile'})" class="hero-btn" plain>
                <el-icon><Setting /></el-icon>
                <span>账号管理</span>
              </el-button>
              <el-button type="danger" size="large" @click="handleLogout" class="hero-btn" plain>
                <el-icon><SwitchButton /></el-icon>
                <span>退出登录</span>
              </el-button>
            </div>
          </template>
        </div>
      </div>
    </div>

    <!-- 底部特性 -->
    <div class="features">
      <div class="feature-item">
        <el-icon :size="24"><DataAnalysis /></el-icon>
        <span>实时监测</span>
      </div>
      <div class="feature-item">
        <el-icon :size="24"><TrendCharts /></el-icon>
        <span>数据分析</span>
      </div>
      <div class="feature-item">
        <el-icon :size="24"><Bell /></el-icon>
        <span>智能预警</span>
      </div>
      <div class="feature-item">
        <el-icon :size="24"><Document /></el-icon>
        <span>报告生成</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Monitor, Sunny, Avatar, UserFilled, DataAnalysis, TrendCharts, Bell, Document, SwitchButton, Setting } from '@element-plus/icons-vue'
import logo from '@/asserts/images/logo.jpg'
import router from '@/router'
import { useUserStore } from '@/stores/user'
import { computed } from 'vue'

const userStore = useUserStore()

const roleTagType = computed(() => {
  const map: Record<string, string> = {
    '公众监督员': 'success',
    'AQI检测网格员': 'warning',
    '系统管理员': 'danger',
    '决策者': 'primary',
  }
  return map[userStore.userRole] || 'info'
})

function toRegist() {
  router.push({ name: 'Regist' })
}

function toLogin() {
  router.push({ name: 'Login' })
}

function goDashboard() {
  const roleMap: Record<string, string> = {
    '公众监督员': 'Report',
    'AQI检测网格员': 'Inspector',
    '系统管理员': 'Admin',
    '决策者': 'Decision',
  }
  router.push({ name: roleMap[userStore.userRole] || 'Report' })
}

function handleLogout() {
  userStore.logout()
  router.push('/')
}
</script>

<style scoped>
/* ========== 页面整体 ========== */
.home-page {
  min-height: 100vh;
  background: linear-gradient(160deg, #e8f5e9 0%, #c8e6c9 20%, #e0f2e9 40%, #e3f2fd 70%, #e8eaf6 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.home-page::before {
  content: '';
  position: absolute;
  top: -150px;
  right: -100px;
  width: 500px;
  height: 500px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(76, 175, 80, 0.07) 0%, transparent 70%);
  pointer-events: none;
}

.home-page::after {
  content: '';
  position: absolute;
  bottom: -80px;
  left: -80px;
  width: 350px;
  height: 350px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(33, 150, 243, 0.05) 0%, transparent 70%);
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

/* ========== 主内容 ========== */
.main-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  position: relative;
  z-index: 1;
}

.hero-card {
  display: flex;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.08), 0 2px 8px rgba(0, 0, 0, 0.04);
  border: 1px solid rgba(76, 175, 80, 0.1);
  overflow: hidden;
  max-width: 960px;
  min-height: 420px;
}

/* ========== 左侧图片 ========== */
.hero-left {
  flex: 1;
  min-width: 400px;
}

.hero-left img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

/* ========== 右侧内容 ========== */
.hero-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 36px;
  text-align: center;
}

.hero-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: linear-gradient(135deg, #fff9c4, #fff176);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
  color: #f9a825;
}

.hero-icon.logged {
  background: linear-gradient(135deg, #c8e6c9, #81c784);
  color: #2e7d32;
}

.hero-right h1 {
  margin: 0 0 6px;
  font-size: 26px;
  font-weight: 700;
  color: #2e7d32;
  letter-spacing: 2px;
}

.subtitle {
  margin: 0 0 12px;
  font-size: 14px;
  color: #66bb6a;
  letter-spacing: 3px;
}

.desc {
  margin: 0 0 28px;
  font-size: 13px;
  color: #999;
  line-height: 1.6;
}

/* ========== 按钮组 ========== */
.btn-group {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: center;
}

.hero-btn {
  min-width: 110px;
  height: 40px;
  font-size: 14px;
  letter-spacing: 2px;
}

.hero-btn :deep(span) {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

/* ========== 底部特性栏 ========== */
.features {
  display: flex;
  gap: 48px;
  padding: 16px 0 24px;
  position: relative;
  z-index: 1;
  flex-wrap: wrap;
  justify-content: center;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #999;
  font-size: 13px;
  letter-spacing: 1px;
}
</style>
