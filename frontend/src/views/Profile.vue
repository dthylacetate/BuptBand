<template>
  <div :class="['profile-container', `theme-${currentTheme.id}`]">
    <!-- 页面头部 -->
    <div :class="['profile-header', `header-${currentTheme.id}`]">
      <el-page-header @back="router.push('/')" content="乐手档案">
        <template #title>
          <span class="back-link">← 返回广场</span>
        </template>
      </el-page-header>
    </div>

    <!-- 主内容区 -->
    <div :class="['profile-content', `content-${currentTheme.id}`]">
      <!-- 个人信息卡片 -->
      <div :class="['member-card', `card-${currentTheme.id}`]">
        <!-- 卡片边框装饰 -->
        <div v-if="currentTheme.id === 'heavy-metal'" class="card-frame">
          <div class="frame-corner top-left"></div>
          <div class="frame-corner top-right"></div>
          <div class="frame-corner bottom-left"></div>
          <div class="frame-corner bottom-right"></div>
        </div>

        <!-- 卡片头部 -->
        <div :class="['card-header', `card-header-${currentTheme.id}`]">
          <template v-if="!isEdit">
            <span class="header-title">✨ 个人名片</span>
            <el-button
              :class="['edit-btn', `btn-${currentTheme.id}`]"
              @click="isEdit = true"
            >
              <span class="btn-icon">⚡</span>
              <span>编辑资料</span>
            </el-button>
          </template>
          <template v-else>
            <span class="header-title">🔧 编辑模式</span>
          </template>
        </div>

        <!-- 成员信息 -->
        <div :class="['member-intro', `intro-${currentTheme.id}`]">
          <!-- 头像区域 -->
          <div class="avatar-stage">
            <el-upload
              class="avatar-uploader"
              :action="uploadAction"
              :headers="uploadHeaders"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              name="file"
            >
              <div :class="['avatar-frame', `avatar-frame-${currentTheme.id}`]">
                <el-avatar
                  v-if="editForm.avatarUrl"
                  :size="100"
                  :src="serverRoot + editForm.avatarUrl"
                  class="member-avatar"
                />
                <div v-else class="avatar-placeholder">
                  <el-icon :size="40"><Plus /></el-icon>
                  <span>更换头像</span>
                </div>
              </div>
            </el-upload>
            <div :class="['avatar-glow', `glow-${currentTheme.id}`]"></div>
          </div>

          <!-- 基本信息 -->
          <div class="member-basic">
            <h3 :class="['member-name', `name-${currentTheme.id}`]">{{ userInfo.nickname }}</h3>
            <p class="member-email">{{ userInfo.email }}</p>
            <div class="member-badges">
              <div :class="['badge', `badge-${currentTheme.id}`]">
                <span class="badge-icon">🎸</span>
                <span class="badge-text">{{ editForm.instrument || '未知乐器' }}</span>
              </div>
              <div :class="['badge', `badge-${currentTheme.id}`]">
                <span class="badge-icon">⏰</span>
                <span class="badge-text">{{ editForm.playingYears || 0 }}年琴龄</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 分隔线 -->
        <div :class="['divider', `divider-${currentTheme.id}`]">
          <div class="divider-line"></div>
          <div :class="['divider-icon', `icon-${currentTheme.id}`]">{{ getDividerIcon(currentTheme.id) }}</div>
          <div class="divider-line"></div>
        </div>

        <!-- 编辑表单 -->
        <el-form :model="editForm" label-position="top" :class="['profile-form', `form-${currentTheme.id}`]">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="擅长乐器">
                <el-select v-model="editForm.instrument" :disabled="!isEdit" class="theme-select">
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
                <el-input-number
                  v-model="editForm.playingYears"
                  :disabled="!isEdit"
                  class="theme-number"
                  :min="0"
                  :max="50"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="个人简介">
            <el-input
              v-model="editForm.bio"
              type="textarea"
              :rows="3"
              :disabled="!isEdit"
              placeholder="介绍一下你的音乐风格..."
              class="theme-textarea"
            />
          </el-form-item>

          <div v-if="isEdit" class="form-actions">
            <el-button
              :class="['save-btn', `btn-${currentTheme.id}`]"
              @click="handleSave"
            >
              <span class="btn-icon">💾</span>
              <span>保存修改</span>
            </el-button>
            <el-button :class="['cancel-btn', `btn-${currentTheme.id}`]" @click="cancelEdit">取消</el-button>
          </div>
        </el-form>
      </div>

      <!-- 发布管理 -->
      <div :class="['posts-management', `card-${currentTheme.id}`]">
        <div :class="['management-header', `header-${currentTheme.id}`]">
          <span class="management-icon">📋</span>
          <span class="management-title">我的发布管理</span>
        </div>

        <div class="management-content">
          <el-table
            :data="myRecruitments"
            style="width: 100%"
            v-loading="loading"
            empty-text="你还没发过招募贴喵"
            :class="['posts-table', `table-${currentTheme.id}`]"
          >
            <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
            <el-table-column prop="type" label="类型" width="100">
              <template #default="scope">
                <el-tag
                  :type="scope.row.type === 'BAND_SEEKING_MEMBER' ? 'success' : 'warning'"
                  size="small"
                  class="type-tag"
                >
                  {{ scope.row.type === 'BAND_SEEKING_MEMBER' ? '⚡ 招人' : '🤘 求队' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120" fixed="right">
              <template #default="scope">
                <el-popconfirm title="确定要下架这个招募吗？" @confirm="handleDelete(scope.row.id)">
                  <template #reference>
                    <el-button type="danger" link class="delete-btn">
                      <span class="delete-icon">💀</span>
                      <span>下架</span>
                    </el-button>
                  </template>
                </el-popconfirm>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useTheme } from '../composables/useTheme'
import request from '../api/request'

const router = useRouter()
const { currentTheme, loadTheme } = useTheme()
const isEdit = ref(false)
const loading = ref(false)

const userInfo = ref({})
const editForm = ref({})
const myRecruitments = ref([])
let backupData = {}

const apiUrl = import.meta.env.VITE_API_BASE_URL
const serverRoot = apiUrl.replace('/api', '')
const uploadAction = `${apiUrl}/files/upload/avatar`

const uploadHeaders = {
  Authorization: `Bearer ${localStorage.getItem('token')}`
}

const getDividerIcon = (themeId) => {
  const iconMap = {
    'heavy-metal': '☠️',
    'cyberpunk': '💠',
    'retro-wave': '✦'
  }
  return iconMap[themeId] || '☠️'
}

const fetchProfile = async () => {
  const res = await request.get('/users/me')
  userInfo.value = res
  editForm.value = { ...res }
  backupData = { ...res }
}

const fetchMyPosts = async () => {
  loading.value = true
  try {
    const res = await request.get('/recruitments/my')
    myRecruitments.value = res
  } finally {
    loading.value = false
  }
}

const beforeAvatarUpload = (rawFile) => {
  const isImg = rawFile.type === 'image/jpeg' || rawFile.type === 'image/png'
  if (!isImg) {
    ElMessage.error('仅支持 JPG/PNG 格式')
    return false
  }
  if (rawFile.size / 1024 / 1024 > 10) {
    ElMessage.error('图片不能超过 5MB')
    return false
  }
  return true
}

const handleAvatarSuccess = async (path) => {
  editForm.value.avatarUrl = path
  try {
    await request.put('/users/me', { avatarUrl: path })
    userInfo.value.avatarUrl = path
    ElMessage.success('头像已压缩并同步')
  } catch (err) {
    console.error('同步失败', err)
    ElMessage.error('虽然上传成功,但数据库记录失败了')
  }
}

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
  loadTheme()
  fetchProfile()
  fetchMyPosts()
})
</script>

<style scoped>
/* === BASE STYLES === */
.profile-container {
  min-height: 100vh;
  position: relative;
}

.profile-header {
  position: relative;
  z-index: 1;
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.profile-content {
  position: relative;
  z-index: 1;
  max-width: 800px;
  margin: 0 auto;
  padding: 0 20px 40px;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

/* === HEAVY METAL THEME === */
.theme-heavy-metal .member-card {
  position: relative;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 4px solid #ff4500;
  padding: 40px;
  box-shadow: 0 0 40px rgba(255, 69, 0, 0.5), 0 10px 30px rgba(0, 0, 0, 0.8);
}

.theme-heavy-metal .member-name {
  color: #d4d4d4;
}

/* === CYBERPUNK THEME === */
.theme-cyberpunk .member-card {
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border: 2px solid #00ffff;
  padding: 35px;
  box-shadow: 0 0 40px rgba(0, 255, 255, 0.3);
  position: relative;
}

.theme-cyberpunk .member-card::before {
  content: '';
  position: absolute;
  inset: 0;
  border: 2px solid transparent;
  background: linear-gradient(90deg, #00ffff, #ff1493, #00ffff) border-box;
  -webkit-mask: linear-gradient(#fff 0 0) padding-box, linear-gradient(#fff 0 0);
  mask-composite: exclude;
  opacity: 0.3;
  pointer-events: none;
}

.theme-cyberpunk .member-name {
  font-family: 'Courier New', monospace;
  color: #00ffff;
  text-shadow: 0 0 10px rgba(0, 255, 255, 0.5);
}

/* === RETRO WAVE THEME === */
.theme-retro-wave .member-card {
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 100%);
  border: 4px solid transparent;
  border-image: linear-gradient(135deg, #ff1493, #9400d3) 1;
  padding: 35px;
  box-shadow: 0 0 40px rgba(255, 20, 147, 0.4);
}

.theme-retro-wave .member-name {
  color: #ffe0ff;
  text-shadow: 2px 2px 0 #9400d3;
}

/* === SHARED STYLES === */
/* 卡片边框 */
.card-frame {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.frame-corner {
  position: absolute;
  width: 30px;
  height: 30px;
  border: 4px solid #ff4500;
}

.frame-corner.top-left { top: 10px; left: 10px; border-right: none; border-bottom: none; }
.frame-corner.top-right { top: 10px; right: 10px; border-left: none; border-bottom: none; }
.frame-corner.bottom-left { bottom: 10px; left: 10px; border-right: none; border-top: none; }
.frame-corner.bottom-right { bottom: 10px; right: 10px; border-left: none; border-top: none; }

/* 卡片头部 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 3px solid #2d2d2d;
}

.header-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 24px;
  color: #ff4500;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.theme-cyberpunk .header-title {
  font-family: 'Courier New', monospace;
  color: #00ffff;
}

.theme-retro-wave .header-title {
  color: #ff1493;
}

.edit-btn {
  border: none !important;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-heavy-metal {
  background: linear-gradient(135deg, #ff4500 0%, #dc143c 100%) !important;
  color: #ffffff !important;
  font-family: 'Impact', 'Arial Black', sans-serif !important;
  text-transform: uppercase !important;
  letter-spacing: 1px !important;
}

.btn-cyberpunk {
  background: rgba(0, 255, 255, 0.2) !important;
  border: 1px solid #00ffff !important;
  color: #00ffff !important;
  font-family: 'Courier New', monospace !important;
}

.btn-retro-wave {
  background: linear-gradient(135deg, #ff1493, #9400d3) !important;
  color: #ffffff !important;
}

.btn-icon { font-size: 16px; }

/* 成员信息 */
.member-intro {
  display: flex;
  align-items: center;
  gap: 30px;
  margin-bottom: 30px;
}

.avatar-stage {
  position: relative;
  flex-shrink: 0;
}

.avatar-uploader { cursor: pointer; }

.avatar-frame {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 4px solid #ff4500;
  box-shadow: 0 0 20px rgba(255, 69, 0, 0.5);
  overflow: hidden;
  transition: all 0.3s ease;
}

.theme-cyberpunk .avatar-frame {
  border-color: #00ffff;
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.5);
}

.theme-retro-wave .avatar-frame {
  border-color: #ff1493;
  box-shadow: 0 0 20px rgba(255, 20, 147, 0.5);
}

.avatar-frame:hover {
  box-shadow: 0 0 30px rgba(255, 69, 0, 0.8), 0 0 50px rgba(220, 20, 60, 0.5) !important;
}

.member-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #8c8c8c;
  font-size: 12px;
  text-transform: uppercase;
}

.avatar-glow {
  position: absolute;
  top: -10px;
  left: -10px;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  animation: pulse-glow 2s ease-in-out infinite;
}

.theme-heavy-metal .avatar-glow {
  background: radial-gradient(ellipse, rgba(255, 69, 0, 0.3) 0%, transparent 70%);
}

.theme-cyberpunk .avatar-glow {
  background: radial-gradient(ellipse, rgba(0, 255, 255, 0.3) 0%, transparent 70%);
}

.theme-retro-wave .avatar-glow {
  background: radial-gradient(ellipse, rgba(255, 20, 147, 0.3) 0%, transparent 70%);
}

.member-basic {
  flex: 1;
}

.member-name {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 32px;
  font-weight: 900;
  margin: 0 0 10px;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.member-email {
  color: #8c8c8c;
  font-size: 14px;
  margin: 0 0 15px;
}

.member-badges {
  display: flex;
  gap: 15px;
}

.badge {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: rgba(255, 69, 0, 0.1);
  border: 2px solid #ff4500;
  border-radius: 4px;
}

.theme-cyberpunk .badge {
  background: rgba(0, 255, 255, 0.1);
  border-color: #00ffff;
}

.theme-retro-wave .badge {
  background: rgba(255, 20, 147, 0.1);
  border-color: #ff1493;
  border-radius: 20px;
}

.badge-icon { font-size: 18px; }

.badge-text {
  font-family: 'Arial Black', sans-serif;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.theme-heavy-metal .badge-text { color: #ff4500; }
.theme-cyberpunk .badge-text { color: #00ffff; font-family: 'Courier New', monospace; }
.theme-retro-wave .badge-text { color: #ff1493; }

/* 分隔线 */
.divider {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  margin: 30px 0;
}

.divider-line {
  flex: 1;
  height: 2px;
  background: linear-gradient(90deg, transparent 0%, #ff4500 50%, transparent 100%);
}

.theme-cyberpunk .divider-line {
  background: linear-gradient(90deg, transparent 0%, #00ffff 50%, transparent 100%);
}

.theme-retro-wave .divider-line {
  background: linear-gradient(90deg, transparent 0%, #ff1493 50%, transparent 100%);
}

.divider-icon {
  font-size: 24px;
  animation: icon-rotate 10s linear infinite;
}

/* 表单 */
.profile-form :deep(.el-form-item__label) {
  color: #d4d4d4 !important;
  font-family: 'Arial Black', sans-serif !important;
  text-transform: uppercase !important;
}

.theme-select :deep(.el-input__wrapper),
.theme-number :deep(.el-input__wrapper),
.theme-textarea :deep(.el-input__wrapper) {
  background: #0a0a0a !important;
  border: 2px solid #2d2d2d !important;
}

.theme-select :deep(.el-input__wrapper):hover,
.theme-number :deep(.el-input__wrapper):hover,
.theme-textarea :deep(.el-input__wrapper):hover {
  border-color: #ff4500 !important;
}

.theme-select :deep(.el-input__wrapper.is-focus),
.theme-number :deep(.el-input__wrapper.is-focus),
.theme-textarea :deep(.el-input__wrapper.is-focus) {
  border-color: #ff4500 !important;
  box-shadow: 0 0 15px rgba(255, 69, 0, 0.5) !important;
}

.theme-cyberpunk .theme-select :deep(.el-input__wrapper):hover,
.theme-cyberpunk .theme-number :deep(.el-input__wrapper):hover,
.theme-cyberpunk .theme-textarea :deep(.el-input__wrapper):hover {
  border-color: #00ffff !important;
}

.theme-cyberpunk .theme-select :deep(.el-input__wrapper.is-focus),
.theme-cyberpunk .theme-number :deep(.el-input__wrapper.is-focus),
.theme-cyberpunk .theme-textarea :deep(.el-input__wrapper.is-focus) {
  border-color: #00ffff !important;
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.5) !important;
}

.theme-retro-wave .theme-select :deep(.el-input__wrapper):hover,
.theme-retro-wave .theme-number :deep(.el-input__wrapper):hover,
.theme-retro-wave .theme-textarea :deep(.el-input__wrapper):hover {
  border-color: #ff1493 !important;
}

.theme-retro-wave .theme-select :deep(.el-input__wrapper.is-focus),
.theme-retro-wave .theme-number :deep(.el-input__wrapper.is-focus),
.theme-retro-wave .theme-textarea :deep(.el-input__wrapper.is-focus) {
  border-color: #ff1493 !important;
  box-shadow: 0 0 15px rgba(255, 20, 147, 0.5) !important;
}

.theme-select :deep(.el-input__inner),
.theme-textarea :deep(.el-textarea__inner) {
  color: #d4d4d4 !important;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
  margin-top: 25px;
  padding-top: 20px;
  border-top: 2px solid #2d2d2d;
}

.save-btn {
  border: none !important;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-heavy-metal {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%) !important;
  color: #ffffff !important;
  font-family: 'Impact', 'Arial Black', sans-serif !important;
  text-transform: uppercase !important;
}

.btn-cyberpunk {
  background: rgba(0, 255, 255, 0.2) !important;
  border: 1px solid #00ffff !important;
  color: #00ffff !important;
  font-family: 'Courier New', monospace !important;
}

.btn-retro-wave {
  background: linear-gradient(135deg, #ff1493, #9400d3) !important;
  color: #ffffff !important;
}

.cancel-btn {
  background: #2d2d2d !important;
  border: 2px solid #2d2d2d !important;
  color: #d4d4d4 !important;
  font-family: 'Impact', 'Arial Black', sans-serif !important;
}

/* 发布管理 */
.posts-management {
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 3px solid #2d2d2d;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
  overflow: hidden;
}

.theme-cyberpunk .posts-management {
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border-color: #2a2a4a;
}

.theme-retro-wave .posts-management {
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 100%);
  border-color: #2d2d2d;
}

.management-header {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  border-bottom: 3px solid #2d2d2d;
  background: rgba(255, 69, 0, 0.05);
}

.theme-cyberpunk .management-header {
  background: rgba(0, 255, 255, 0.05);
  border-bottom-color: #2a2a4a;
}

.theme-retro-wave .management-header {
  background: rgba(255, 20, 147, 0.05);
}

.management-icon { font-size: 24px; }

.management-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 20px;
  color: #ff4500;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.theme-cyberpunk .management-title {
  font-family: 'Courier New', monospace;
  color: #00ffff;
}

.theme-retro-wave .management-title {
  color: #ff1493;
}

.management-content { padding: 20px; }

/* 表格样式 */
.posts-table :deep(.el-table__header-wrapper) {
  background: #1a1a1a;
}

.posts-table :deep(.el-table__header th) {
  background: #2d2d2d !important;
  color: #d4d4d4 !important;
  font-family: 'Arial Black', sans-serif !important;
  text-transform: uppercase !important;
  border-bottom: 2px solid #ff4500 !important;
}

.theme-cyberpunk .posts-table :deep(.el-table__header th) {
  border-bottom-color: #00ffff !important;
}

.theme-retro-wave .posts-table :deep(.el-table__header th) {
  border-bottom-color: #ff1493 !important;
}

.posts-table :deep(.el-table__body tr) {
  background: #1a1a1a !important;
  transition: all 0.3s ease;
}

.posts-table :deep(.el-table__body tr:hover) {
  background: rgba(255, 69, 0, 0.1) !important;
}

.theme-cyberpunk .posts-table :deep(.el-table__body tr:hover) {
  background: rgba(0, 255, 255, 0.1) !important;
}

.theme-retro-wave .posts-table :deep(.el-table__body tr:hover) {
  background: rgba(255, 20, 147, 0.1) !important;
}

.posts-table :deep(.el-table__body td) {
  border-bottom: 1px solid #2d2d2d !important;
  color: #d4d4d4 !important;
}

.type-tag {
  font-family: 'Arial Black', sans-serif !important;
  text-transform: uppercase !important;
  border: none !important;
}

.delete-btn {
  color: #dc143c !important;
  font-family: 'Arial Black', sans-serif !important;
  text-transform: uppercase !important;
  display: flex;
  align-items: center;
  gap: 5px;
}

.delete-icon { font-size: 16px; }

/* === ANIMATIONS === */
@keyframes pulse-glow {
  0%, 100% {
    transform: scale(1);
    opacity: 0.5;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
}

@keyframes icon-rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* === RESPONSIVE === */
@media (max-width: 768px) {
  .member-intro {
    flex-direction: column;
    text-align: center;
  }

  .member-badges {
    justify-content: center;
  }

  .card-header {
    flex-direction: column;
    gap: 15px;
  }
}
</style>
