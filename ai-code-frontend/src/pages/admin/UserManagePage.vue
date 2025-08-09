<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { listUserVOByPage, deleteUser } from '@/api/userController'

// 表格列定义
const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 80,
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    width: 120,
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    width: 120,
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
    key: 'userAvatar',
    width: 80,
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
    width: 200,
    ellipsis: true,
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
    key: 'userRole',
    width: 100,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 160,
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
    fixed: 'right',
  },
]

// 数据
const router = useRouter()

const data = ref<API.UserVO[]>([])
const total = ref(0)
const loading = ref(false)

// 搜索条件
const searchParams = reactive<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 10,
  userName: '',
  userAccount: '',
  userRole: '',
})

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    // 过滤掉空值，避免空字符串影响查询
    const requestParams = {
      pageNum: searchParams.pageNum,
      pageSize: searchParams.pageSize,
      ...(searchParams.userName && { userName: searchParams.userName }),
      ...(searchParams.userAccount && { userAccount: searchParams.userAccount }),
      ...(searchParams.userRole && { userRole: searchParams.userRole }),
    }

    console.log('发送请求参数:', requestParams)
    const res = await listUserVOByPage(requestParams)
    if (res.data.code === 0 && res.data.data) {
      data.value = res.data.data.records ?? []
      total.value = res.data.data.totalRow ?? 0
    } else {
      console.error('API返回错误:', res.data)
      message.error('获取数据失败，' + res.data.message)
    }
  } catch (error) {
    console.error('获取用户数据失败:', error)
    message.error('获取数据失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  searchParams.pageNum = 1
  fetchData()
}

// 重置搜索
const handleReset = () => {
  searchParams.userName = ''
  searchParams.userAccount = ''
  searchParams.userRole = ''
  searchParams.pageNum = 1
  fetchData()
}

// 分页变化
const handlePageChange = (page: number, pageSize: number) => {
  searchParams.pageNum = page
  searchParams.pageSize = pageSize
  fetchData()
}

// 页面大小变化
const handlePageSizeChange = (current: number, size: number) => {
  searchParams.pageNum = 1
  searchParams.pageSize = size
  fetchData()
}

// 编辑用户
const handleEdit = (record: API.UserVO) => {
  router.push(`/admin/user/edit/${record.id}`)
}

// 查看用户
const handleView = (record: API.UserVO) => {
  message.info(`查看用户: ${record.userName}`)
  // TODO: 实现查看功能
}

// 删除用户
const handleDelete = async (record: API.UserVO) => {
  try {
    const res = await deleteUser({ id: record.id })
    if (res.data.code === 0) {
      message.success('删除成功')
      fetchData()
    } else {
      message.error('删除失败，' + res.data.message)
    }
  } catch (error) {
    console.error('删除用户失败:', error)
    message.error('删除失败，请稍后重试')
  }
}

// 格式化日期
const formatDate = (dateString?: string) => {
  if (!dateString) return '-'
  return new Date(dateString).toLocaleString('zh-CN')
}

// 页面加载时请求一次
onMounted(() => {
  fetchData()
})
</script>

<template>
  <div class="user-manage-page">
    <div class="page-header">
      <h2>用户管理</h2>
      <div class="search-area">
        <a-input
          v-model:value="searchParams.userName"
          placeholder="搜索用户名"
          style="width: 200px; margin-right: 8px"
          @press-enter="handleSearch"
        />
        <a-input
          v-model:value="searchParams.userAccount"
          placeholder="搜索账号"
          style="width: 200px; margin-right: 8px"
          @press-enter="handleSearch"
        />
        <a-select
          v-model:value="searchParams.userRole"
          placeholder="选择角色"
          style="width: 120px; margin-right: 8px"
          allow-clear
        >
          <a-select-option value="user">普通用户</a-select-option>
          <a-select-option value="admin">管理员</a-select-option>
        </a-select>
        <a-button type="primary" @click="handleSearch">搜索</a-button>
        <a-button style="margin-left: 8px" @click="handleReset">重置</a-button>
      </div>
    </div>

    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="false"
      :loading="loading"
      row-key="id"
      class="user-table"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'userAvatar'">
          <a-avatar :src="record.userAvatar" :alt="record.userName">
            {{ record.userName?.charAt(0)?.toUpperCase() }}
          </a-avatar>
        </template>
        <template v-else-if="column.key === 'userRole'">
          <a-tag :color="record.userRole === 'admin' ? 'red' : 'blue'">
            {{ record.userRole === 'admin' ? '管理员' : '普通用户' }}
          </a-tag>
        </template>
        <template v-else-if="column.key === 'createTime'">
          {{ formatDate(record.createTime) }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-button type="link" size="small" @click="handleEdit(record)"> 编辑 </a-button>
            <a-button type="link" size="small" @click="handleView(record)"> 查看 </a-button>
            <a-popconfirm title="确定要删除这个用户吗？" @confirm="handleDelete(record)">
              <a-button type="link" size="small" danger> 删除 </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </template>
    </a-table>

    <div class="pagination-wrapper">
      <a-pagination
        v-model:current="searchParams.pageNum"
        v-model:page-size="searchParams.pageSize"
        :total="total"
        :show-size-changer="true"
        :show-quick-jumper="true"
        :show-total="
          (total: number, range: [number, number]) => `第 ${range[0]}-${range[1]} 条/共 ${total} 条`
        "
        @change="handlePageChange"
        @show-size-change="handlePageSizeChange"
      />
    </div>
  </div>
</template>

<style scoped>
.user-manage-page {
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

.search-area {
  display: flex;
  align-items: center;
}

.user-table {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.pagination-wrapper {
  background: #fff;
  padding: 16px 24px;
  border-radius: 8px;
  margin-top: 16px;
  text-align: right;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .search-area {
    flex-wrap: wrap;
    gap: 8px;
  }

  .search-area .ant-input,
  .search-area .ant-select {
    width: 100% !important;
    margin-right: 0 !important;
  }
}
</style>
