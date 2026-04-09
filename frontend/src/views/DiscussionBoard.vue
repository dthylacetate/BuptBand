<template>
  <div class="board-container">
    <el-page-header @back="router.push('/')" content="乐手讨论区" class="page-header">
      <template #title>回广场</template>
    </el-page-header>

    <el-tabs v-model="activeSection" class="board-tabs" @tab-change="fetchPosts">
      <el-tab-pane label="🎸 技术/乐理" name="TECH_THEORY" />
      <el-tab-pane label="🍻 闲聊树洞" name="CHIT_CHAT" />
    </el-tabs>

    <div v-loading="loading" class="post-list">
      <el-card 
        v-for="post in posts" 
        :key="post.id" 
        class="post-card" 
        shadow="hover"
        @click="goToDetail(post.id)"
      >
        <div class="post-header">
          <el-avatar :size="32" :src="serverRoot + post.publisherAvatar" icon="User" />
          <div class="post-meta">
            <span class="nickname">{{ post.publisherNickname }}</span>
            <span class="time">{{ formatDate(post.createdAt) }}</span>
          </div>
        </div>
        <h3 class="post-title">{{ post.title }}</h3>
        <p class="post-summary">{{ truncateContent(post.content) }}</p>
      </el-card>

      <el-empty v-if="posts.length === 0" description="这里还没有乐手发言，快来抢沙发！" />
    </div>

    <el-button type="primary" class="fab-button" circle @click="showAddDialog = true">
      <el-icon :size="24"><Plus /></el-icon>
    </el-button>

    <el-dialog v-model="showAddDialog" title="发表新动态" width="500px">
      <el-form :model="addForm" label-position="top">
        <el-form-item label="标题" required>
          <el-input v-model="addForm.title" placeholder="起个吸引人的标题喵" />
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input 
            v-model="addForm.content" 
            type="textarea" 
            :rows="5" 
            placeholder="分享你的乐理见解或琴房趣事..." 
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="submitPost">立即发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '../api/request'

const router = useRouter()
const loading = ref(false)
const posts = ref([])
const activeSection = ref('TECH_THEORY')
const showAddDialog = ref(false)

const apiUrl = import.meta.env.VITE_API_BASE_URL || ''
const serverRoot = apiUrl.replace('/api', '')

const addForm = ref({ title: '', content: '', section: '' })

// 1. 获取帖子列表
const fetchPosts = async () => {
  loading.value = true
  try {
    const res = await request.get('/discussions', {
      params: { section: activeSection.value }
    })
    posts.value = res
  } finally {
    loading.value = false
  }
}

// 2. 提交新帖
const submitPost = async () => {
  if (!addForm.value.title || !addForm.value.content) {
    return ElMessage.warning('标题和内容都要写喵')
  }
  addForm.value.section = activeSection.value
  await request.post('/discussions', addForm.value)
  ElMessage.success('发布成功！')
  showAddDialog.value = false
  addForm.value = { title: '', content: '', section: '' }
  fetchPosts()
}

// 3. 跳转详情（准备下一站开发）
const goToDetail = (id) => {
  router.push(`/discussions/${id}`)
}

// 工具函数：格式化时间
const formatDate = (dateStr) => {
  return new Date(dateStr).toLocaleString('zh-CN', { 
    month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' 
  })
}

// 工具函数：内容截取
const truncateContent = (content) => {
  return content.length > 80 ? content.substring(0, 80) + '...' : content
}

onMounted(fetchPosts)
</script>

<style scoped>
.board-container { max-width: 800px; margin: 20px auto; padding: 0 20px; position: relative; min-height: 80vh; }
.page-header { margin-bottom: 20px; }
.board-tabs { margin-bottom: 20px; }
.post-card { margin-bottom: 15px; cursor: pointer; border-radius: 12px; border: none; }
.post-header { display: flex; align-items: center; gap: 10px; margin-bottom: 12px; }
.post-meta { display: flex; flex-direction: column; }
.nickname { font-size: 14px; font-weight: bold; color: #303133; }
.time { font-size: 12px; color: #909399; }
.post-title { margin: 0 0 10px; font-size: 18px; }
.post-summary { font-size: 14px; color: #606266; line-height: 1.6; }

/* ⚡️ 悬浮按钮样式 */
.fab-button {
  position: fixed;
  right: 50px;
  bottom: 50px;
  width: 60px;
  height: 60px;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
  z-index: 99;
}
</style>