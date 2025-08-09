<template>
  <div class="global-header">
    <div class="header-left">
      <img src="@/assets/logo.png" alt="logo" class="logo" />
      <span class="site-title">AI应用生成平台</span>
    </div>
    <a-menu mode="horizontal" :selected-keys="[selectedKey]" class="header-menu">
      <a-menu-item v-for="item in menuItems" :key="item.key">
        <router-link :to="item.path">{{ item.label }}</router-link>
      </a-menu-item>
    </a-menu>
    <div class="header-right">
      <!-- 未登录状态显示登录按钮 -->
      <a-button v-if="!isLoggedIn" type="primary" @click="goToLogin">登录</a-button>

      <!-- 已登录状态显示用户信息 -->
      <div v-else class="user-info">
        <a-avatar :src="loginUser.userAvatar" :alt="loginUser.userName" class="user-avatar">
          {{ loginUser.userName?.charAt(0)?.toUpperCase() }}
        </a-avatar>
        <span class="user-name">{{ loginUser.userName }}</span>
        <a-dropdown>
          <a-button type="text" class="user-dropdown-btn">
            <template #icon>
              <DownOutlined />
            </template>
          </a-button>
          <template #overlay>
            <a-menu>
              <a-menu-item key="profile" @click="goToUserProfile">
                <UserOutlined />
                个人资料
              </a-menu-item>
              <a-menu-item key="settings">
                <SettingOutlined />
                设置
              </a-menu-item>
              <!-- 管理员菜单 -->
              <a-menu-item
                v-if="loginUser.userRole === 'admin'"
                key="userManage"
                @click="goToUserManage"
              >
                <TeamOutlined />
                用户管理
              </a-menu-item>
              <a-menu-divider />
              <a-menu-item key="logout" @click="handleLogout">
                <LogoutOutlined />
                退出登录
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useLoginUserStore } from '@/stores/loginUser'
import {
  DownOutlined,
  UserOutlined,
  SettingOutlined,
  LogoutOutlined,
  TeamOutlined,
} from '@ant-design/icons-vue'

const props = defineProps<{ menuItems: Array<{ key: string; label: string; path: string }> }>()
const route = useRoute()
const router = useRouter()
const loginUserStore = useLoginUserStore()

const selectedKey = computed(() => {
  const found = props.menuItems.find((item) => item.path === route.path)
  return found ? found.key : ''
})

const loginUser = computed(() => loginUserStore.loginUser)
const isLoggedIn = computed(() => {
  return loginUser.value.userName && loginUser.value.userName !== '未登录'
})

const goToLogin = () => {
  router.push('/user/login')
}

const goToUserManage = () => {
  router.push('/admin/userManage')
}

const goToUserProfile = () => {
  router.push('/user/profile')
}

const handleLogout = () => {
  // 这里可以添加退出登录的逻辑
  loginUserStore.setLoginUser({ userName: '未登录' })
  router.push('/user/login')
}

onMounted(() => {
  // 组件挂载时获取用户信息
  loginUserStore.fetchLoginUser()
})
</script>

<style scoped>
.global-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
  padding: 0 24px;
  background: #fff;
  box-shadow: 0 2px 8px #f0f1f2;
}
.header-left {
  display: flex;
  align-items: center;
}
.logo {
  width: 32px;
  height: 32px;
  margin-right: 12px;
}
.site-title {
  font-size: 20px;
  font-weight: bold;
  color: #1677ff;
}
.header-menu {
  flex: 1;
  margin: 0 32px;
  min-width: 200px;
}
.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-avatar {
  width: 32px;
  height: 32px;
  background-color: #1677ff;
  color: #fff;
  font-weight: bold;
}

.user-name {
  font-size: 14px;
  color: #333;
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-dropdown-btn {
  padding: 4px 8px;
  height: auto;
}

@media (max-width: 600px) {
  .global-header {
    flex-direction: column;
    height: auto;
    padding: 8px;
  }
  .header-menu {
    margin: 8px 0;
  }
  .user-name {
    max-width: 80px;
  }
}
</style>
