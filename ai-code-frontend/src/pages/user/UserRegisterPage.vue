<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { register } from '@/api/userController'

const router = useRouter()
const loading = ref(false)

const formState = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

const validateCheckPassword = async (_rule: unknown, value: string) => {
  if (value === '') {
    throw new Error('请确认密码')
  } else if (value !== formState.userPassword) {
    throw new Error('两次输入的密码不一致')
  }
}

const handleSubmit = async (values: {
  userAccount: string
  userPassword: string
  checkPassword: string
}) => {
  loading.value = true
  try {
    const res = await register({
      userAccount: values.userAccount,
      userPassword: values.userPassword,
      checkPassword: values.checkPassword,
    })

    if (res.data.code === 0) {
      message.success('注册成功！请登录')
      // 跳转到登录页面
      router.push({
        path: '/user/login',
        replace: true,
      })
    } else {
      message.error(res.data.message || '注册失败，请稍后重试')
    }
  } catch (error: unknown) {
    console.error('注册失败:', error)
    message.error('注册失败，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div id="userRegisterPage">
    <h2 class="title">AI 应用生成 - 用户注册</h2>
    <div class="desc">不写一行代码，生成完整应用</div>
    <a-form :model="formState" name="register" autocomplete="off" @finish="handleSubmit">
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
      <a-form-item
        name="checkPassword"
        :rules="[
          { required: true, message: '请确认密码' },
          { validator: validateCheckPassword, trigger: 'blur' },
        ]"
      >
        <a-input-password v-model:value="formState.checkPassword" placeholder="请确认密码" />
      </a-form-item>
      <div class="tips">
        已有账号？
        <RouterLink to="/user/login">去登录</RouterLink>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%" :loading="loading">
          {{ loading ? '注册中...' : '注册' }}
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<style scoped>
#userRegisterPage {
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
