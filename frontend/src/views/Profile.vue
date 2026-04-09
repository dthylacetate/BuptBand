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
        <el-upload
          class="avatar-uploader"
          :action="uploadAction"
          :headers="uploadHeaders"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          name="file"
        >
          <el-avatar 
            v-if="editForm.avatarUrl" 
            :size="80" 
            :src="serverRoot + editForm.avatarUrl" 
            class="avatar-img"
          />
          <div v-else class="avatar-default">
            <el-icon><Plus /></el-icon>
            <span>更换头像</span>
          </div>
        </el-upload>

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
                <el-option label="Bass" value="Bass" />
                <el-option label="Guitar" value="Guitar" />
                <el-option label="Vocal" value="Vocal" />
                <el-option label="Drum" value="Drum" />
                <el-option label="Keyboard" value="Keyboard" />
                <el-option label="其他" value="Other" />
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
          <el-input v-model="editForm.bio" type="textarea" :rows="3" :disabled="!isEdit" placeholder="介绍一下你的音乐风格..." />
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
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '../api/request'

const router = useRouter()
const isEdit = ref(false)
const loading = ref(false)

const userInfo = ref({})
const editForm = ref({})
const myRecruitments = ref([])
let backupData = {}

// ⚡️ 环境变量处理
const apiUrl = import.meta.env.VITE_API_BASE_URL; // http://localhost:8080/api
// 提取后端根路径 (去掉 /api)，用于拼接静态资源 /uploads
const serverRoot = apiUrl.replace('/api', ''); 
// 拼接上传接口地址
const uploadAction = `${apiUrl}/files/upload/avatar`;

// 上传请求头
const uploadHeaders = {
  Authorization: `Bearer ${localStorage.getItem('token')}`
}

// 1. 获取个人资料
const fetchProfile = async () => {
  const res = await request.get('/users/me')
  userInfo.value = res
  editForm.value = { ...res }
  backupData = { ...res }
}

// 2. 获取“我的发布”
const fetchMyPosts = async () => {
  loading.value = true
  try {
    const res = await request.get('/recruitments/my')
    myRecruitments.value = res
  } finally {
    loading.value = false
  }
}

// 3. 上传前校验
const beforeAvatarUpload = (rawFile) => {
  const isImg = rawFile.type === 'image/jpeg' || rawFile.type === 'image/png';
  if (!isImg) {
    ElMessage.error('仅支持 JPG/PNG 格式');
    return false;
  }
  if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('图片不能超过 5MB喵');
    return false;
  }
  return true;
}

// 4. 上传成功
const handleAvatarSuccess = async (path) => {
  editForm.value.avatarUrl = path;
  try {
    // 立即更新数据库中的头像字段
    await request.put('/users/me', { avatarUrl: path });
    userInfo.value.avatarUrl = path;
    ElMessage.success('头像已压缩并同步');
  } catch (err) {
    console.error("同步失败", err);
    ElMessage.error('虽然上传成功，但数据库记录失败了');
  }
}

// 删除帖子
const handleDelete = async (id) => {
  try {
    await request.delete(`/recruitments/${id}`)
    ElMessage.success('招募帖已成功下架')
    fetchMyPosts()
  } catch (err) {
    console.error(err)
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
  fetchMyPosts()
})
</script>

<style scoped>
.profile-page { max-width: 700px; margin: 30px auto; padding: 0 20px; }
.page-header { margin-bottom: 20px; }
.profile-card, .posts-card { border-radius: 12px; border: none; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.title-text { font-weight: bold; }

.user-intro { display: flex; align-items: center; gap: 20px; margin-bottom: 10px; }

/* 头像上传框美化 */
.avatar-uploader {
  border: 2px dashed #dcdfe6;
  border-radius: 50%;
  cursor: pointer;
  width: 84px;
  height: 84px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.3s;
  overflow: hidden;
}
.avatar-uploader:hover {
  border-color: #409eff;
  background-color: #f5f7fa;
}
.avatar-default {
  color: #8c939d;
  font-size: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.avatar-img { display: block; object-fit: cover; }

.user-basic h3 { margin: 0; font-size: 1.3rem; color: #303133; }
.user-basic p { margin: 5px 0 0; color: #909399; font-size: 0.85rem; }
.form-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 15px; }
</style>