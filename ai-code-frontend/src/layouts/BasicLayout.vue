<template>
  <a-layout style="min-height: 100vh">
    <a-layout-header style="padding: 0">
      <GlobalHeader :menu-items="menuItems" />
    </a-layout-header>
    <a-layout-content style="margin: 24px 16px 0">
      <router-view />
    </a-layout-content>
    <a-layout-footer style="padding: 0">
      <GlobalFooter />
    </a-layout-footer>
  </a-layout>
</template>

<script setup lang="ts">
import 'ant-design-vue/dist/reset.css'
import { computed } from 'vue'
import { useLoginUserStore } from '@/stores/loginUser'

import GlobalHeader from '../components/GlobalHeader.vue'
import GlobalFooter from '../components/GlobalFooter.vue'

const loginUserStore = useLoginUserStore()

// 基础菜单项
const baseMenuItems = [
  { key: 'home', label: '首页', path: '/' },
  { key: 'about', label: '关于', path: '/about' },
]

// 根据用户角色动态生成菜单
const menuItems = computed(() => {
  const items = [...baseMenuItems]

  // 如果是管理员，添加用户管理菜单
  if (loginUserStore.loginUser.userRole === 'admin') {
    items.push({ key: 'userManage', label: '用户管理', path: '/admin/userManage' })
  }

  return items
})
</script>
