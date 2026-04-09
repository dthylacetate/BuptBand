<template>
  <div class="profile-page">
    <el-page-header 
      @back="router.push('/')" 
      content="乐手个人档案" 
      class="page-header"
    >
      <template #title>
        <span>返回广场</span>
      </template>
    </el-page-header>

    <el-card class="profile-card" shadow="always">
      <template #header>
        <div class="card-header">
          <span class="title-text">✨ 个人名片</span>
          <el-button v-if="!isEdit" type="primary" size="small" @click="enterEditMode">
            编辑资料
          </el-button>
        </div>
      </template>

      <div class="user-intro">
        <el-avatar :size="80" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
        <div class="user-basic">
          <h2>{{ userInfo.nickname }}</h2>
          <p>{{ userInfo.email }}</p>
        </div>
      </div>

      <el-divider />

      <el-form :model="editForm" label-position="top">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="擅长乐器">
              <el-select 
                v-model="editForm.instrument" 
                :disabled="!isEdit" 
                placeholder="选择乐器" 
                style="width: 100%"
              >
                <el-option label="Bass" value="Bass" />
                <el-option label="Guitar" value="Guitar" />
                <el-option label="Vocal" value="Vocal" />
                <el-option label="Drum" value="Drum" />
                <el-option label="Keyboard" value="Keyboard" />
                <el-option label="其他 (请在简介说明)" value="Other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="琴龄 (年)">
              <el-input-number 
                v-model="editForm.playingYears" 
                :disabled="!isEdit" 
                :min="0" :max="60" 
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="常驻校区">
          <el-radio-group v-model="editForm.campus" :disabled="!isEdit">
            <el-radio label="沙河">沙河校区</el-radio>
            <el-radio label="西土城">西土城校区</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="个人简介">
          <el-input 
            v-model="editForm.bio" 
            type="textarea" 
            :rows="4" 
            :disabled="!isEdit"
            placeholder="介绍一下你的技术栈、喜欢的乐队或风格..." 
          />
        </el-form-item>

        <div v-if="isEdit" class="form-actions">
          <el-button type="success" @click="handleSave">保存名片</el-button>
          <el-button @click="cancelEdit">取消</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request' // 引入封装好的拦截器

const router = useRouter()
const isEdit = ref(false)

// 用户展示数据
const userInfo = ref({
  nickname: '',
  email: '',
  instrument: '',
  campus: '',
  bio: '',
  playingYears: 0
})

// 表单编辑数据
const editForm = ref({ ...userInfo.value })

// 备份数据（用于取消修改时回滚）
let backupData = {}

/**
 * 核心逻辑：从后端获取当前用户信息
 */
const fetchProfile = async () => {
  try {
    // 自动带 Token，地址由环境变量决定
    const res = await request.get('/users/me')
    userInfo.value = res
    editForm.value = { ...res }
    backupData = { ...res }
  } catch (err) {
    console.error('档案加载失败', err)
  }
}

/**
 * 开启编辑模式
 */
const enterEditMode = () => {
  isEdit.value = true
}

/**
 * 提交修改到后端
 */
const handleSave = async () => {
  try {
    // 对应后端的 PUT /api/users/me
    const updatedUser = await request.put('/users/me', editForm.value)
    
    ElMessage.success('档案已更新！')
    userInfo.value = updatedUser
    backupData = { ...updatedUser }
    isEdit.value = false
  } catch (err) {
    // 报错弹窗已由拦截器托管
  }
}

/**
 * 取消编辑并恢复原始数据
 */
const cancelEdit = () => {
  editForm.value = { ...backupData }
  isEdit.value = false
}

// 初始化加载
onMounted(() => {
  fetchProfile()
})
</script>

<style scoped>
.profile-page {
  max-width: 600px;
  margin: 30px auto;
  padding: 0 20px;
}

.page-header {
  margin-bottom: 20px;
}

.profile-card {
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 12px rgba(0,0,0,0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-text {
  font-weight: bold;
  font-size: 1.1rem;
}

.user-intro {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 10px 0;
}

.user-basic h2 {
  margin: 0;
  color: #303133;
}

.user-basic p {
  margin: 5px 0 0;
  color: #909399;
  font-size: 0.9rem;
}

.form-actions {
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 响应式调整 */
@media (max-width: 480px) {
  .profile-page {
    margin: 10px auto;
  }
}
</style>