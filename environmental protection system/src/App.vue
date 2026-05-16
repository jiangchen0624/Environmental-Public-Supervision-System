<template>
  <!-- 路由视图占位符，用于渲染当前路由对应的组件 -->
  <RouterView />

  <!-- 页脚组件，始终显示在页面底部 -->
  <FooterView />
</template>

<script setup lang="ts">
// 导入页脚组件
import FooterView from '@/view/Footerview.vue'
</script>

<style scoped lang="css">
/* 根组件样式（当前为空） */
</style>