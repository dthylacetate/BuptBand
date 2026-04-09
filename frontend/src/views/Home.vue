<template>
  <div class="home-container">
    <el-menu mode="horizontal" class="nav-menu" :ellipsis="false">
      <div class="logo">🎸 RIGHTSPELL 乐手广场</div>
      <div class="flex-grow" />
      <el-sub-menu index="user">
        <template #title>我的账号</template>
        <el-menu-item index="profile">个人中心</el-menu-item>
        <el-menu-item index="logout" @click="handleLogout">退出登录</el-menu-item>
      </el-sub-menu>
    </el-menu>

    <el-main>
      <div class="search-bar">
        <el-input 
          v-model="searchQuery.instrument" 
          placeholder="搜索乐器 (如: Bass)" 
          style="width: 200px; margin-right: 10px;" 
        />
        <el-input 
          v-model="searchQuery.keyword" 
          placeholder="关键词" 
          style="width: 200px; margin-right: 10px;" 
        />
        <el-button type="primary" @click="fetchRecruitments">筛选招募</el-button>
        <el-button type="success" @click="showAddDialog = true">发布招募</el-button>
      </div>

      <el-row :gutter="20" v-if="recruitments.length > 0">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="item in recruitments" :key="item.id">
          <el-card class="recruitment-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span class="instrument-tag"># {{ item.instrument }}</span>
                <el-tag type="warning" size="small">{{ item.campus }}</el-tag>
              </div>
            </template>
            <p class="description">{{ item.description }}</p>
            <div class="footer">
              <span class="author">👤 {{ item.ownerNickname }}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <el-empty v-else description="广场上空荡荡的，去发个帖子吧？" />
    </el-main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../api/request' // 引入我们封装的拦截器
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const recruitments = ref([])
const searchQuery = ref({
  instrument: '',
  keyword: ''
})

// --- 核心逻辑 1：获取招募列表 ---
const fetchRecruitments = async () => {
  try {
    const token = localStorage.getItem('token')
    const res = await axios.get('http://localhost:8080/api/recruitments', {
      headers: { Authorization: `Bearer ${token}` }, // 必须带上 Token
      params: {
        instrument: searchQuery.value.instrument,
        keyword: searchQuery.value.keyword
      }
    })
    recruitments.value = res.data
  } catch (error) {
    if (error.response?.status === 403) {
      ElMessage.error('登录已过期，请重新登录')
      router.push('/login')
    } else {
      ElMessage.error('加载失败')
    }
  }
}

// --- 核心逻辑 2：退出登录 ---
const handleLogout = () => {
  localStorage.removeItem('token')
  ElMessage.info('已退出登录')
  router.push('/login')
}

// 页面加载时立即执行
onMounted(() => {
  fetchRecruitments()
})
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background-color: #f8f9fa;
}

.nav-menu {
  padding: 0 50px;
  align-items: center;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
  color: #409eff;
}

.flex-grow {
  flex-grow: 1;
}

.search-bar {
  margin-bottom: 30px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
}

.recruitment-card {
  margin-bottom: 20px;
  border-radius: 12px;
  transition: transform 0.3s;
}

.recruitment-card:hover {
  transform: translateY(-5px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.instrument-tag {
  font-weight: bold;
  color: #409eff;
}

.description {
  font-size: 0.95rem;
  color: #606266;
  line-height: 1.6;
  height: 60px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.footer {
  margin-top: 15px;
  font-size: 0.85rem;
  color: #909399;
  border-top: 1px solid #ebeef5;
  padding-top: 10px;
}
</style>