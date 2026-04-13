<template>
  <div :class="['discussion-detail-container', `theme-${currentTheme.id}`]">
    <!-- 页面头部 -->
    <div :class="['detail-header', `header-${currentTheme.id}`]">
      <el-page-header @back="router.back()" content="话题详情">
        <template #title>
          <span class="back-link">← 返回</span>
        </template>
      </el-page-header>
    </div>

    <!-- 主内容区 -->
    <div :class="['detail-content', `content-${currentTheme.id}`]" v-loading="loading">
      <!-- 主帖子 -->
      <div :class="['main-post-card', `card-${currentTheme.id}`]" v-if="post">
        <!-- 帖子头部 -->
        <div :class="['post-header', `header-${currentTheme.id}`]">
          <el-avatar
            :size="50"
            :src="serverRoot + post.publisherAvatar"
            icon="User"
            :class="['post-avatar', `avatar-${currentTheme.id}`]"
          />
          <div class="author-info">
            <h3 :class="['author-name', `name-${currentTheme.id}`]">{{ post.publisherNickname }}</h3>
            <p class="post-time">{{ formatDate(post.createdAt) }}</p>
          </div>
        </div>

        <!-- 标题 -->
        <h1 :class="['post-title', `title-${currentTheme.id}`]">{{ post.title }}</h1>

        <!-- 点赞按钮 -->
        <div class="like-section">
          <div
            :class="['like-button', `like-btn-${currentTheme.id}`, { 'liked': post.isLiked }]"
            @click="toggleLike(post.id, 'DISCUSSION_POST')"
          >
            <span class="like-icon">{{ getLikeIcon(currentTheme.id, post.isLiked) }}</span>
            <span class="like-text">{{ post.isLiked ? '已点赞' : '点赞' }}</span>
            <span class="like-count">{{ post.likeCount || 0 }}</span>
          </div>
        </div>

        <!-- 内容正文 -->
        <div :class="['post-content', `content-${currentTheme.id}`]">
          {{ post.content }}
        </div>
      </div>

      <!-- 评论区 -->
      <div :class="['comments-section', `card-${currentTheme.id}`]">
        <div :class="['comments-header', `header-${currentTheme.id}`]">
          <span class="comments-icon">💬</span>
          <span class="comments-title">全部评论</span>
          <span class="comments-count">({{ comments.length }})</span>
        </div>

        <!-- 评论输入 -->
        <div :class="['comment-input', `input-${currentTheme.id}`]">
          <el-input
            v-model="newComment"
            type="textarea"
            :rows="3"
            placeholder="说点什么吧，自古评论出大神..."
            class="comment-textarea"
          />
          <div class="input-actions">
            <el-button
              :class="['submit-btn', `btn-${currentTheme.id}`]"
              @click="submitComment"
            >
              <span class="btn-icon">🎨</span>
              <span>发表评论</span>
            </el-button>
          </div>
        </div>

        <!-- 评论列表 -->
        <div v-if="comments.length > 0" class="comment-list">
          <div
            v-for="c in comments"
            :key="c.id"
            :class="['comment-item', `comment-${currentTheme.id}`]"
          >
            <div @click.stop="goToUser(c.userNickname)" class="comment-avatar-wrapper">
              <el-avatar
                :size="35"
                :src="serverRoot + c.userAvatar"
                icon="User"
                :class="['comment-avatar', `avatar-${currentTheme.id}`]"
              />
            </div>
            <div class="comment-body">
              <div class="comment-meta">
                <span :class="['comment-nickname', `nickname-${currentTheme.id}`]">{{ c.userNickname }}</span>
                <span class="comment-time">{{ formatDate(c.createdAt) }}</span>
              </div>
              <p :class="['comment-text', `text-${currentTheme.id}`]">{{ c.content }}</p>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-else :class="['empty-comments', `empty-${currentTheme.id}`]">
          <div class="empty-icon">🎨</div>
          <p>暂时还没有人评论，快来抢沙发！</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useTheme } from '../composables/useTheme'
import request from '../api/request'

const route = useRoute()
const router = useRouter()
const { currentTheme, loadTheme } = useTheme()
const loading = ref(false)

const postId = route.params.id
const post = ref(null)
const comments = ref([])
const newComment = ref('')

const apiUrl = import.meta.env.VITE_API_BASE_URL || ''
const serverRoot = apiUrl.replace('/api', '')

const getLikeIcon = (themeId, isLiked) => {
  const iconMap = {
    'heavy-metal': isLiked ? '🔥' : '💀',
    'cyberpunk': isLiked ? '💠' : '⚡',
    'retro-wave': isLiked ? '✨' : '⭐'
  }
  return iconMap[themeId] || '🔥'
}

const loadData = async () => {
  loading.value = true
  try {
    post.value = await request.get(`/discussions/${postId}`)
    await fetchComments()
  } finally {
    loading.value = false
  }
}

const fetchComments = async () => {
  comments.value = await request.get(`/discussions/${postId}/comments`)
}

const submitComment = async () => {
  if (!newComment.value.trim()) return ElMessage.warning('评论内容不能为空')

  try {
    await request.post(`/discussions/${postId}/comments`, {
      content: newComment.value
    })
    ElMessage.success('回帖成功！')
    newComment.value = ''
    fetchComments()
  } catch (err) {
    console.error(err)
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit'
  })
}

const toggleLike = async (id, type) => {
  try {
    if (post.value && post.value.id === id) {
      post.value.isLiked = !post.value.isLiked
      post.value.likeCount = post.value.isLiked ? (post.value.likeCount || 0) + 1 : Math.max((post.value.likeCount || 0) - 1, 0)
    }
    await request.post(`/likes/${type}/${id}`)
  } catch (error) {
    console.error('点赞失败', error)
    if (post.value && post.value.id === id) {
      post.value.isLiked = !post.value.isLiked
      post.value.likeCount = post.value.isLiked ? (post.value.likeCount || 0) + 1 : Math.max((post.value.likeCount || 0) - 1, 0)
    }
  }
}

const goToUser = (nickname) => {
  if (!nickname) return
  router.push(`/user/${nickname}`)
}

onMounted(() => {
  loadTheme()
  loadData()
})
</script>

<style scoped>
/* === BASE STYLES === */
.discussion-detail-container {
  min-height: 100vh;
  position: relative;
}

.detail-header {
  position: relative;
  z-index: 1;
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.detail-content {
  position: relative;
  z-index: 1;
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px 60px;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

/* === HEAVY METAL THEME === */
.theme-heavy-metal .main-post-card {
  position: relative;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 3px solid #ff4500;
  padding: 30px;
  box-shadow: 0 0 40px rgba(255, 69, 0, 0.5), 0 10px 30px rgba(0, 0, 0, 0.8);
}

.theme-heavy-metal .post-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 32px;
  font-weight: 900;
  color: #d4d4d4;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.theme-heavy-metal .author-name {
  color: #ff4500;
}

/* === CYBERPUNK THEME === */
.theme-cyberpunk .main-post-card {
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border: 2px solid #00ffff;
  padding: 30px;
  box-shadow: 0 0 40px rgba(0, 255, 255, 0.3);
  position: relative;
}

.theme-cyberpunk .main-post-card::before {
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

.theme-cyberpunk .post-title {
  font-family: 'Courier New', monospace;
  font-size: 28px;
  font-weight: bold;
  color: #e0e0ff;
  text-transform: uppercase;
  letter-spacing: 2px;
  text-shadow: 0 0 10px rgba(0, 255, 255, 0.5);
}

.theme-cyberpunk .author-name {
  color: #00ffff;
  font-family: 'Courier New', monospace;
}

/* === RETRO WAVE THEME === */
.theme-retro-wave .main-post-card {
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 100%);
  border: 4px solid transparent;
  border-image: linear-gradient(135deg, #ff1493, #9400d3) 1;
  padding: 30px;
  box-shadow: 0 0 40px rgba(255, 20, 147, 0.4);
}

.theme-retro-wave .post-title {
  font-family: 'Arial Black', sans-serif;
  font-size: 28px;
  font-weight: 900;
  color: #ffe0ff;
  text-transform: uppercase;
  letter-spacing: 2px;
  text-shadow: 2px 2px 0 #9400d3;
}

.theme-retro-wave .author-name {
  color: #ff1493;
}

/* === SHARED STYLES === */
.post-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 2px solid #2d2d2d;
}

.theme-cyberpunk .post-header {
  border-bottom-color: #2a2a4a;
}

.post-avatar {
  border: 3px solid #ff4500;
  box-shadow: 0 0 15px rgba(255, 69, 0, 0.5);
}

.theme-cyberpunk .post-avatar {
  border-color: #00ffff;
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.5);
}

.theme-retro-wave .post-avatar {
  border-color: #ff1493;
  box-shadow: 0 0 15px rgba(255, 20, 147, 0.5);
}

.author-info {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.author-name {
  font-family: 'Arial Black', sans-serif;
  font-size: 18px;
  margin: 0;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.post-time {
  font-size: 12px;
  color: #8c8c8c;
  margin: 0;
}

.post-title {
  margin: 0 0 25px;
  line-height: 1.3;
}

/* 点赞按钮 */
.like-section {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}

.like-button {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 24px;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.like-button.liked {
  animation: like-pulse 0.5s ease;
}

.like-icon { font-size: 24px; }
.like-text {
  font-family: 'Arial Black', sans-serif;
  font-size: 16px;
  font-weight: bold;
}
.like-count {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 20px;
  font-weight: bold;
  min-width: 25px;
  text-align: center;
}

/* 重金属点赞按钮 */
.like-btn-heavy-metal {
  background: rgba(255, 69, 0, 0.1);
  border: 3px solid #ff4500;
  color: #ff4500;
}

.like-btn-heavy-metal:hover {
  background: rgba(255, 69, 0, 0.2);
  box-shadow: 0 0 25px rgba(255, 69, 0, 0.6);
}

.like-btn-heavy-metal.liked {
  background: linear-gradient(135deg, #ff4500, #dc143c);
  color: #fff;
  box-shadow: 0 0 40px rgba(255, 69, 0, 0.9);
}

.like-btn-heavy-metal .like-count { color: #ff4500; }
.like-btn-heavy-metal.liked .like-count { color: #fff; }

/* 赛博朋克点赞按钮 */
.like-btn-cyberpunk {
  background: rgba(0, 255, 255, 0.1);
  border: 1px solid #00ffff;
  color: #00ffff;
}

.like-btn-cyberpunk:hover {
  background: rgba(0, 255, 255, 0.2);
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.5);
}

.like-btn-cyberpunk.liked {
  background: rgba(0, 255, 255, 0.3);
  box-shadow: 0 0 30px rgba(0, 255, 255, 0.7);
  color: #fff;
}

.like-btn-cyberpunk .like-count { color: #00ffff; }
.like-btn-cyberpunk.liked .like-count {
  color: #fff;
  text-shadow: 0 0 5px #00ffff;
}

/* 复古霓虹点赞按钮 */
.like-btn-retro-wave {
  background: linear-gradient(135deg, rgba(255, 20, 147, 0.2), rgba(148, 0, 211, 0.2));
  border: 2px solid #ff1493;
  border-radius: 20px;
  color: #ff1493;
  box-shadow: 0 0 15px rgba(255, 20, 147, 0.3);
}

.like-btn-retro-wave:hover {
  background: linear-gradient(135deg, rgba(255, 20, 147, 0.4), rgba(148, 0, 211, 0.4));
  box-shadow: 0 0 25px rgba(255, 20, 147, 0.5);
}

.like-btn-retro-wave.liked {
  background: linear-gradient(135deg, #ff1493, #9400d3);
  box-shadow: 0 0 40px rgba(255, 20, 147, 0.7);
  color: #fff;
}

.like-btn-retro-wave .like-count { color: #ff1493; }
.like-btn-retro-wave.liked .like-count {
  color: #fff;
  text-shadow: 0 0 5px #ff1493;
}

/* 帖子内容 */
.post-content {
  font-size: 16px;
  line-height: 1.8;
  white-space: pre-wrap;
}

.theme-heavy-metal .post-content {
  color: #d4d4d4;
}

.theme-cyberpunk .post-content {
  color: #d4d4d4;
}

.theme-retro-wave .post-content {
  color: #d4d4d4;
}

/* 评论区 */
.comments-section {
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 3px solid #2d2d2d;
  padding: 30px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}

.theme-cyberpunk .comments-section {
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border-color: #2a2a4a;
}

.theme-retro-wave .comments-section {
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 100%);
  border-color: #2d2d2d;
}

.comments-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 3px solid #ff4500;
}

.theme-cyberpunk .comments-header {
  border-bottom-color: #00ffff;
}

.theme-retro-wave .comments-header {
  border-bottom-color: #ff1493;
}

.comments-icon {
  font-size: 28px;
  animation: pulse-glow 2s ease-in-out infinite;
}

.comments-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 24px;
  color: #ff4500;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.theme-cyberpunk .comments-title {
  font-family: 'Courier New', monospace;
  color: #00ffff;
}

.theme-retro-wave .comments-title {
  color: #ff1493;
}

.comments-count {
  font-size: 18px;
  color: #8c8c8c;
}

/* 评论输入 */
.comment-input {
  background: rgba(255, 69, 0, 0.05);
  border: 2px solid #2d2d2d;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.theme-cyberpunk .comment-input {
  background: rgba(0, 255, 255, 0.05);
  border-color: #2a2a4a;
}

.theme-retro-wave .comment-input {
  background: rgba(255, 20, 147, 0.05);
  border-color: #2d2d2d;
}

.comment-textarea :deep(.el-textarea__inner) {
  background: #0a0a0a !important;
  border: 2px solid #2d2d2d !important;
  color: #d4d4d4 !important;
}

.comment-textarea :deep(.el-textarea__inner):focus {
  border-color: #ff4500 !important;
  box-shadow: 0 0 15px rgba(255, 69, 0, 0.5) !important;
}

.theme-cyberpunk .comment-textarea :deep(.el-textarea__inner):focus {
  border-color: #00ffff !important;
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.5) !important;
}

.theme-retro-wave .comment-textarea :deep(.el-textarea__inner):focus {
  border-color: #ff1493 !important;
  box-shadow: 0 0 15px rgba(255, 20, 147, 0.5) !important;
}

.input-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
}

.submit-btn {
  border: none !important;
  color: #ffffff !important;
  font-family: 'Impact', 'Arial Black', sans-serif !important;
  text-transform: uppercase !important;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-heavy-metal {
  background: linear-gradient(135deg, #ff4500 0%, #dc143c 100%) !important;
}

.btn-cyberpunk {
  background: rgba(0, 255, 255, 0.2) !important;
  border: 1px solid #00ffff !important;
  color: #00ffff !important;
  font-family: 'Courier New', monospace !important;
}

.btn-retro-wave {
  background: linear-gradient(135deg, #ff1493, #9400d3) !important;
}

/* 评论列表 */
.comment-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  gap: 15px;
  padding: 20px;
  background: rgba(0, 0, 0, 0.3);
  border: 2px solid #2d2d2d;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.comment-item:hover {
  border-color: #ff4500;
  box-shadow: 0 0 15px rgba(255, 69, 0, 0.3);
}

.theme-cyberpunk .comment-item:hover {
  border-color: #00ffff;
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.3);
}

.theme-retro-wave .comment-item:hover {
  border-color: #ff1493;
  box-shadow: 0 0 15px rgba(255, 20, 147, 0.3);
}

.comment-avatar-wrapper {
  cursor: pointer;
  transition: transform 0.3s ease;
  flex-shrink: 0;
}

.comment-avatar-wrapper:hover {
  transform: scale(1.1);
}

.comment-avatar {
  border: 2px solid #ff4500;
  flex-shrink: 0;
}

.theme-cyberpunk .comment-avatar {
  border-color: #00ffff;
}

.theme-retro-wave .comment-avatar {
  border-color: #ff1493;
}

.comment-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.comment-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-nickname {
  font-family: 'Arial Black', sans-serif;
  font-size: 14px;
  font-weight: bold;
}

.comment-time {
  font-size: 12px;
  color: #8c8c8c;
}

.comment-text {
  font-size: 14px;
  line-height: 1.6;
  margin: 0;
}

.theme-heavy-metal .comment-text { color: #d4d4d4; }
.theme-cyberpunk .comment-text { color: #d4d4d4; }
.theme-retro-wave .comment-text { color: #d4d4d4; }

/* 空状态 */
.empty-comments {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 60px;
  margin-bottom: 15px;
  animation: pulse-glow 2s ease-in-out infinite;
}

.empty-comments p {
  font-size: 16px;
  color: #8c8c8c;
  margin: 0;
}

/* === ANIMATIONS === */
@keyframes like-pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.25); }
  100% { transform: scale(1); }
}

@keyframes pulse-glow {
  0%, 100% {
    transform: scale(1);
    filter: drop-shadow(0 0 10px rgba(255, 69, 0, 0.5));
  }
  50% {
    transform: scale(1.1);
    filter: drop-shadow(0 0 20px rgba(255, 69, 0, 0.8));
  }
}

/* === RESPONSIVE === */
@media (max-width: 768px) {
  .post-title {
    font-size: 24px;
  }

  .main-post-card,
  .comments-section {
    padding: 20px;
  }
}
</style>
