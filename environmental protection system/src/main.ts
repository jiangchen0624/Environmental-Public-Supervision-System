/**
 * 应用入口文件
 * 负责创建 Vue 应用实例并配置所需的插件和依赖
 */

// 导入 Vue 核心库，用于创建应用实例
import { createApp } from 'vue'
// 导入 Pinia 状态管理库
import { createPinia } from 'pinia'

// 导入根组件 App
import App from './App.vue'
// 导入路由配置
import router from './router'

// 导入 Element Plus 组件库核心模块
import ElementPlus from 'element-plus'
// 导入 Element Plus 全局样式文件
import 'element-plus/dist/index.css'

// 导入 Element Plus 所有图标组件（按需使用可单独导入）
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 创建 Vue 应用实例
const app = createApp(App)

// 安装 Pinia 状态管理
app.use(createPinia())
// 安装路由插件
app.use(router)
// 安装 Element Plus 组件库
app.use(ElementPlus)

// 注册所有 Element Plus 图标组件到应用中
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 将应用挂载到页面的 #app 元素上
app.mount('#app')