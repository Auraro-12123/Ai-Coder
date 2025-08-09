<template>
  <div id="userLoginPage">
    <h2 class="title">AI 应用生成 - 用户登录</h2>
    <div class="desc">不写一行代码，生成完整应用</div>
    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
        <a-input v-model:value="formState.userAccount" placeholder="请输入账号" />
      </a-form-item>
      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 8, message: '密码不能小于 8 位' },
        ]"
      >
        <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码" />
      </a-form-item>
      <div class="tips">
        没有账号？
        <RouterLink to="/user/register">去注册</RouterLink>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%" :loading="loading">
          {{ loading ? '登录中...' : '登录' }}
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { userLogin } from '@/api/userController'
import { useLoginUserStore } from '@/stores/loginUser'

const router = useRouter()
const loginUserStore = useLoginUserStore()
const loading = ref(false)

const formState = reactive({
  userAccount: '',
  userPassword: '',
})

const handleSubmit = async (values: { userAccount: string; userPassword: string }) => {
  loading.value = true
  try {
    const res = await userLogin({
      userAccount: values.userAccount,
      userPassword: values.userPassword,
    })

    if (res.data.code === 0 && res.data.data) {
      // 登录成功，更新用户信息
      loginUserStore.setLoginUser(res.data.data)
      message.success('登录成功！')
      // 跳转到首页
      router.push('/')
    } else {
      message.error(res.data.message || '登录失败，请检查账号密码')
    }
  } catch (error: unknown) {
    console.error('登录失败:', error)
    message.error('登录失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
#userLoginPage {
  max-width: 400px;
  margin: 100px auto;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  margin-bottom: 8px;
  color: #1677ff;
  font-size: 24px;
}

.desc {
  text-align: center;
  margin-bottom: 32px;
  color: #666;
  font-size: 14px;
}

.tips {
  text-align: center;
  margin-bottom: 16px;
  font-size: 14px;
  color: #666;
}

.tips a {
  color: #1677ff;
  text-decoration: none;
}

.tips a:hover {
  text-decoration: underline;
}
</style>
