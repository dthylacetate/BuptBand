<template>
  <div class="profile-page">
    <el-page-header @back="router.push('/')" content="乐手档案" class="page-header">
      <template #title>返回广场</template>
    </el-page-header>

    <el-card class="profile-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span class="title-text">✨ 个人名片</span>
          <el-button v-if="!isEdit" type="primary" size="small" @click="isEdit = true">编辑资料</el-button>
        </div>
      </template>
      <div class="user-intro">
        <el-avatar :size="70" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
        <div class="user-basic">
          <h3>{{ userInfo.nickname }}</h3>
          <p>{{ userInfo.email }}</p>
        </div>
      </div>
      <el-divider />
      <el-form :model="editForm" label-position="top">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="擅长乐器">
              <el-select v-model="editForm.instrument" :disabled="!isEdit" style="width: 100%">
                <el-option label="Bass" value="Bass" /><el-option label="Guitar" value="Guitar" />
                <el-option label="Vocal" value="Vocal" /><el-option label="Drum" value="Drum" />
                <el-option label="Keyboard" value="Keyboard" /><el-option label="其他" value="Other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="琴龄">
              <el-input-number v-model="editForm.playingYears" :disabled="!isEdit" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="个人简介">
          <el-input v-model="editForm.bio" type="textarea" :rows="3" :disabled="!isEdit" />
        </el-form-item>
        <div v-if="isEdit" class="form-actions">
          <el-button type="success" @click="handleSave">保存修改</el-button>
          <el-button @click="cancelEdit">取消</el-button>
        </div>
      </el-form>
    </el-card>

    <el-card class="posts-card" shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span class="title-text">📋 我的发布管理</span>
        </div>
      </template>

      <el-table :data="myRecruitments" style="width: 100%" v-loading="loading" empty-text="你还没发过招募贴喵">
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.type === 'BAND_SEEKING_MEMBER' ? 'success' : 'warning'" size="small">
              {{ scope.row.type === 'BAND_SEEKING_MEMBER' ? '招人' : '求队' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-popconfirm title="确定要下架这个招募吗？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button type="danger" link>下架</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const router = useRouter()
const isEdit = ref(false)
const loading = ref(false)

const userInfo = ref({})
const editForm = ref({})
const myRecruitments = ref([]) // ⚡️ 我的帖子列表
let backupData = {}

// 1. 获取个人资料
const fetchProfile = async () => {
  const res = await request.get('/users/me')
  userInfo.value = res
  editForm.value = { ...res }
  backupData = { ...res }
}

// 2. ⚡️ 获取“我的发布”
const fetchMyPosts = async () => {
  loading.value = true
  try {
    const res = await request.get('/recruitments/my')
    myRecruitments.value = res
  } finally {
    loading.value = false
  }
}

// 3. ⚡️ 删除帖子
const handleDelete = async (id) => {
  try {
    await request.delete(`/recruitments/${id}`)
    ElMessage.success('招募帖已成功下架')
    fetchMyPosts() // 刷新列表
  } catch (err) {
    console.error('下架失败', err)
  }
}

const handleSave = async () => {
  await request.put('/users/me', editForm.value)
  ElMessage.success('档案已更新')
  userInfo.value = { ...editForm.value }
  backupData = { ...editForm.value }
  isEdit.value = false
}

const cancelEdit = () => {
  editForm.value = { ...backupData }
  isEdit.value = false
}

onMounted(() => {
  fetchProfile()
  fetchMyPosts() // ⚡️ 进来就加载列表
})
</script>

<style scoped>
.profile-page { max-width: 700px; margin: 30px auto; padding: 0 20px; }
.page-header { margin-bottom: 20px; }
.profile-card, .posts-card { border-radius: 12px; border: none; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.title-text { font-weight: bold; }
.user-intro { display: flex; align-items: center; gap: 15px; margin-bottom: 10px; }
.user-basic h3 { margin: 0; }
.user-basic p { margin: 5px 0 0; color: #909399; font-size: 0.85rem; }
.form-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 15px; }
</style>