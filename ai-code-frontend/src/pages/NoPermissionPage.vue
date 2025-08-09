<template>
  <div class="no-permission-page">
    <div class="error-container">
      <a-result status="403" title="403" sub-title="抱歉，您没有权限访问此页面">
        <template #extra>
          <a-button type="primary" @click="goHome"> 返回首页 </a-button>
          <a-button @click="goBack"> 返回上一页 </a-button>
        </template>
      </a-result>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}

const goBack = () => {
  router.go(-1)
}
</script>

<style scoped>
.no-permission-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #f5f5f5;
}

.error-container {
  background: #fff;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
</style>
