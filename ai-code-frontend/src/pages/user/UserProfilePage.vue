<template>
  <div class="user-profile-page">
    <div class="page-header">
      <h2>个人信息设置</h2>
    </div>

    <div class="profile-form-container">
      <a-form
        :model="formState"
        :rules="rules"
        layout="vertical"
        @finish="handleSubmit"
        ref="formRef"
      >
        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="用户ID" name="id">
              <a-input v-model:value="formState.id" disabled />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="账号" name="userAccount">
              <a-input v-model:value="formState.userAccount" disabled />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="用户名" name="userName">
              <a-input v-model:value="formState.userName" placeholder="请输入用户名" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="用户角色" name="userRole">
              <a-input v-model:value="formState.userRole" disabled />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="头像URL" name="userAvatar">
          <a-input v-model:value="formState.userAvatar" placeholder="请输入头像URL" />
        </a-form-item>

        <a-form-item label="用户简介" name="userProfile">
          <a-textarea
            v-model:value="formState.userProfile"
            placeholder="请输入用户简介"
            :rows="4"
          />
        </a-form-item>

        <a-form-item>
          <a-space>
            <a-button type="primary" html-type="submit" :loading="loading">
              {{ loading ? '保存中...' : '保存' }}
            </a-button>
            <a-button @click="resetForm">重置</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { updateUser } from '@/api/userController'
import { useLoginUserStore } from '@/stores/loginUser'

const loginUserStore = useLoginUserStore()
const formRef = ref()
const loading = ref(false)

const formState = reactive({
  id: '',
  userAccount: '',
  userName: '',
  userAvatar: '',
  userProfile: '',
  userRole: '',
})

const rules = {
  userName: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' },
  ],
  userProfile: [{ max: 500, message: '简介不能超过 500 个字符', trigger: 'blur' }],
}

// 初始化表单数据
const initFormData = () => {
  const userData = loginUserStore.loginUser
  Object.assign(formState, {
    id: userData.id,
    userAccount: userData.userAccount,
    userName: userData.userName,
    userAvatar: userData.userAvatar,
    userProfile: userData.userProfile,
    userRole: userData.userRole,
  })
}

// 提交表单
const handleSubmit = async (values: {
  userName: string
  userAvatar: string
  userProfile: string
}) => {
  loading.value = true
  try {
    const res = await updateUser({
      id: parseInt(formState.id),
      userName: values.userName,
      userAvatar: values.userAvatar,
      userProfile: values.userProfile,
      userRole: formState.userRole, // 保持原有角色
    })

    if (res.data.code === 0) {
      message.success('个人信息更新成功')
      // 更新本地用户信息
      loginUserStore.setLoginUser({
        ...loginUserStore.loginUser,
        userName: values.userName,
        userAvatar: values.userAvatar,
        userProfile: values.userProfile,
      })
    } else {
      message.error('更新失败：' + res.data.message)
    }
  } catch (error) {
    console.error('更新个人信息失败:', error)
    message.error('更新失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  initFormData()
  formRef.value?.resetFields()
  message.info('表单已重置')
}

onMounted(() => {
  initFormData()
})
</script>

<style scoped>
.user-profile-page {
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background: #fff;
  padding: 16px 24px;
  border-radius: 8px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.page-header h2 {
  margin: 0;
  color: #1677ff;
  font-size: 20px;
}

.profile-form-container {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .user-profile-page {
    padding: 16px;
  }
}
</style>
