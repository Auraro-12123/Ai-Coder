import { createRouter, createWebHistory } from 'vue-router'

import HomePage from '../pages/HomePage.vue'
import UserLoginPage from '../pages/user/UserLoginPage.vue'
import UserRegisterPage from '../pages/user/UserRegisterPage.vue'
import UserProfilePage from '../pages/user/UserProfilePage.vue'
import UserManagePage from '../pages/admin/UserManagePage.vue'
import UserEditPage from '../pages/admin/UserEditPage.vue'
import NoPermissionPage from '../pages/NoPermissionPage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: '主页',
      component: HomePage,
    },
    {
      path: '/user/login',
      name: '用户登录',
      component: UserLoginPage,
    },
    {
      path: '/user/register',
      name: '用户注册',
      component: UserRegisterPage,
    },
    {
      path: '/user/profile',
      name: '个人信息',
      component: UserProfilePage,
    },
    {
      path: '/admin/userManage',
      name: '用户管理',
      component: UserManagePage,
    },
    {
      path: '/admin/user/edit/:id',
      name: '编辑用户',
      component: UserEditPage,
    },
    {
      path: '/no-permission',
      name: '权限不足',
      component: NoPermissionPage,
    },
  ],
})

export default router
