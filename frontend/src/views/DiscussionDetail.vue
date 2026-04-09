<template>
  <div class="detail-container" v-loading="loading">
    <el-page-header @back="router.back()" content="话题详情" class="page-header" />

    <el-card class="main-post-card" v-if="post">
      <div class="post-header">
        <el-avatar :size="45" :src="serverRoot + post.publisherAvatar" icon="User" />
        <div class="header-info">
          <h2 class="post-title">{{ post.title }}</h2>
          <div class="meta">
            <span class="nickname">{{ post.publisherNickname }}</span>
            <el-divider direction="vertical" />
            <span class="time">{{ formatDate(post.createdAt) }}</span>
          </div>
        </div>
      </div>
      
      <div class="post-content">
        {{ post.content }}
      </div>
    </el-card>

    <div class="comments-section">
      <h3 class="section-title">💬 全部评论 ({{ comments.length }})</h3>
      
      <div class="comment-input-box">
        <el-input
          v-model="newComment"
          type="textarea"
          :rows="3"
          placeholder="说点什么吧，自古评论出大神..."
        />
        <div class="input-actions">
          <el-button type="primary" size="small" @click="submitComment">发表评论</el-button>
        </div>
      </div>

      <div v-if="comments.length > 0" class="comment-list">
        <div v-for="c in comments" :key="c.id" class="comment-item">
          <el-avatar :size="30" :src="serverRoot + c.userAvatar" icon="User" />
          <div class="comment-body">
            <div class="comment-meta">
              <span class="c-nickname">{{ c.userNickname }}</span>
              <span class="c-time">{{ formatDate(c.createdAt) }}</span>
            </div>
            <p class="c-content">{{ c.content }}</p>
          </div>
        </div>
      </div>
      <el-empty v-else :image-size="60" description="暂时还没有人评论，快来抢沙发！" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const route = useRoute()
const router = useRouter()
const loading = ref(false)

const postId = route.params.id
const post = ref(null)
const comments = ref([])
const newComment = ref('')

const apiUrl = import.meta.env.VITE_API_BASE_URL || ''
const serverRoot = apiUrl.replace('/api', '')

// 1. 初始化加载数据
const loadData = async () => {
  loading.value = true
  try {
    // 获取帖子正文
    post.value = await request.get(`/discussions/${postId}`)
    // 获取评论列表
    await fetchComments()
  } finally {
    loading.value = false
  }
}

// 2. 刷新评论列表
const fetchComments = async () => {
  comments.value = await request.get(`/discussions/${postId}/comments`)
}

// 3. 提交评论
const submitComment = async () => {
  if (!newComment.value.trim()) return ElMessage.warning('评论内容不能为空喵')
  
  try {
    await request.post(`/discussions/${postId}/comments`, {
      content: newComment.value
    })
    ElMessage.success('回帖成功！')
    newComment.value = ''
    fetchComments() // 重新拉取评论列表
  } catch (err) {
    console.error(err)
  }
}

// 时间格式化工具
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', { 
    month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' 
  })
}

onMounted(loadData)
</script>

<style scoped>
.detail-container { max-width: 800px; margin: 20px auto; padding: 0 20px; }
.page-header { margin-bottom: 25px; }

/* 帖子主体样式 */
.main-post-card { border-radius: 12px; border: none; box-shadow: 0 4px 12px rgba(0,0,0,0.05); }
.post-header { display: flex; gap: 15px; align-items: flex-start; margin-bottom: 20px; }
.header-info { flex-grow: 1; }
.post-title { margin: 0 0 8px; font-size: 1.5rem; color: #303133; }
.meta { font-size: 13px; color: #909399; }
.post-content { line-height: 1.8; color: #444; font-size: 15px; white-space: pre-wrap; padding: 10px 0; }

/* 评论区样式 */
.comments-section { margin-top: 40px; }
.section-title { font-size: 1.2rem; margin-bottom: 20px; border-left: 4px solid #409eff; padding-left: 12px; }

.comment-input-box { margin-bottom: 30px; background: #fff; padding: 15px; border-radius: 8px; }
.input-actions { display: flex; justify-content: flex-end; margin-top: 10px; }

.comment-list { background: #fff; border-radius: 12px; padding: 10px 20px; }
.comment-item { display: flex; gap: 12px; padding: 15px 0; border-bottom: 1px solid #f2f6fc; }
.comment-item:last-child { border-bottom: none; }
.comment-body { flex: 1; }
.comment-meta { display: flex; justify-content: space-between; margin-bottom: 6px; }
.c-nickname { font-size: 13px; font-weight: bold; color: #606266; }
.c-time { font-size: 11px; color: #c0c4cc; }
.c-content { font-size: 14px; color: #303133; margin: 0; line-height: 1.5; }
</style>