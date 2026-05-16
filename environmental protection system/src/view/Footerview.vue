<template>
  <!-- 页脚组件 -->
  <footer>
    <span>NEU 空气质量监测</span>
    <span class="divider">|</span>
    <span>上海海洋大学 24计科3班 2436217 朱江龙</span>
    <span class="divider">|</span>
    <span>🌿 绿色呼吸 · 清新未来</span>
  </footer>
</template>

<style scoped>
footer {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100vw;
  z-index: 99;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 5px 0;
  font-size: 12px;
  color: #aaa;
  background: rgba(255, 255, 255, 0.55);
  backdrop-filter: blur(4px);
  border-top: 1px solid rgba(0, 0, 0, 0.04);
}

.divider {
  color: #ddd;
}
</style>
