<template>
  <div class="user-edit-page">
    <div class="page-header">
      <h2>编辑用户信息</h2>
      <a-button @click="goBack">返回</a-button>
    </div>

    <div class="edit-form-container">
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
              <a-select v-model:value="formState.userRole" placeholder="请选择用户角色">
                <a-select-option value="user">普通用户</a-select-option>
                <a-select-option value="admin">管理员</a-select-option>
              </a-select>
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
            <a-button @click="goBack">取消</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getUserVoById, updateUser } from '@/api/userController'

const route = useRoute()
const router = useRouter()
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
  userRole: [{ required: true, message: '请选择用户角色', trigger: 'change' }],
  userProfile: [{ max: 500, message: '简介不能超过 500 个字符', trigger: 'blur' }],
}

// 获取用户信息
const fetchUserInfo = async (userId: string) => {
  try {
    const res = await getUserVoById({ id: parseInt(userId) })
    if (res.data.code === 0 && res.data.data) {
      const userData = res.data.data
      Object.assign(formState, {
        id: userData.id,
        userAccount: userData.userAccount,
        userName: userData.userName,
        userAvatar: userData.userAvatar,
        userProfile: userData.userProfile,
        userRole: userData.userRole,
      })
    } else {
      message.error('获取用户信息失败：' + res.data.message)
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    message.error('获取用户信息失败，请稍后重试')
  }
}

// 提交表单
const handleSubmit = async (values: {
  userName: string
  userAvatar: string
  userProfile: string
  userRole: string
}) => {
  loading.value = true
  try {
    const res = await updateUser({
      id: parseInt(formState.id),
      userName: values.userName,
      userAvatar: values.userAvatar,
      userProfile: values.userProfile,
      userRole: values.userRole,
    })

    if (res.data.code === 0) {
      message.success('用户信息更新成功')
      goBack()
    } else {
      message.error('更新失败：' + res.data.message)
    }
  } catch (error) {
    console.error('更新用户信息失败:', error)
    message.error('更新失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 返回上一页
const goBack = () => {
  router.go(-1)
}

onMounted(() => {
  const userId = route.params.id as string
  if (userId) {
    fetchUserInfo(userId)
  } else {
    message.error('用户ID不存在')
    goBack()
  }
})
</script>

<style scoped>
.user-edit-page {
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  background: #fff;
  padding: 16px 24px;
  border-radius: 8px;
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.page-header h2 {
  margin: 0;
  color: #1677ff;
  font-size: 20px;
}

.edit-form-container {
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .user-edit-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }
}
</style>
