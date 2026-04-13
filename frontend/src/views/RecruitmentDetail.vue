<template>
  <div :class="['recruitment-detail-container', `theme-${currentTheme.id}`]">
    <!-- 页面头部 -->
    <div :class="['detail-header', `header-${currentTheme.id}`]">
      <el-page-header @back="router.back()" content="招募详情">
        <template #title>
          <span class="back-link">← 返回</span>
        </template>
      </el-page-header>
    </div>

    <!-- 主内容区 -->
    <div :class="['detail-content', `content-${currentTheme.id}`]" v-loading="loading">
      <div :class="['detail-card', `card-${currentTheme.id}`]" v-if="post">
        <!-- 类型标签 -->
        <div :class="['type-badge', post.type === 'BAND_SEEKING_MEMBER' ? 'band' : 'member']">
          {{ post.type === 'BAND_SEEKING_MEMBER' ? '⚡ 乐队招人' : '🤘 个人求队' }}
        </div>

        <!-- 标题 -->
        <h1 :class="['detail-title', `title-${currentTheme.id}`]">{{ post.title }}</h1>

        <!-- 点赞按钮 -->
        <div class="like-section">
          <div
            :class="['like-button', `like-btn-${currentTheme.id}`, { 'liked': post.isLiked }]"
            @click="toggleLike(post.id, 'RECRUITMENT')"
          >
            <span class="like-icon">{{ getLikeIcon(currentTheme.id, post.isLiked) }}</span>
            <span class="like-text">{{ post.isLiked ? '已点赞' : '点赞' }}</span>
            <span class="like-count">{{ post.likeCount || 0 }}</span>
          </div>
        </div>

        <!-- 信息网格 -->
        <div :class="['info-grid', `grid-${currentTheme.id}`]">
          <div :class="['info-card', `card-info-${currentTheme.id}`]">
            <div class="info-icon">🎸</div>
            <div class="info-content">
              <div class="info-label">招募位置</div>
              <div class="info-value">{{ post.position }}</div>
            </div>
          </div>

          <div :class="['info-card', `card-info-${currentTheme.id}`]">
            <div class="info-icon">🔥</div>
            <div class="info-content">
              <div class="info-label">音乐风格</div>
              <div class="info-value">{{ post.style }}</div>
            </div>
          </div>

          <div :class="['info-card', `card-info-${currentTheme.id}`]">
            <div class="info-icon">👤</div>
            <div class="info-content">
              <div class="info-label">发布者</div>
              <div class="info-value">{{ post.publisherNickname }}</div>
            </div>
          </div>

          <div :class="['info-card', `card-info-${currentTheme.id}`, 'contact-card']">
            <div class="info-icon">📞</div>
            <div class="info-content">
              <div class="info-label">联系方式</div>
              <div class="info-value contact-value">{{ post.contactInformation }}</div>
            </div>
          </div>
        </div>

        <!-- 详细信息区域 -->
        <div :class="['sections-container', `sections-${currentTheme.id}`]">
          <div :class="['detail-section', `section-${currentTheme.id}`]">
            <h3 :class="['section-title', `section-title-${currentTheme.id}`]">
              <span class="title-icon">⚡</span>
              <span>技术要求/个人说明</span>
            </h3>
            <div :class="['section-content', `section-content-${currentTheme.id}`]">
              {{ post.requirements || '暂无详细要求' }}
            </div>
          </div>

          <div :class="['detail-section', `section-${currentTheme.id}`]">
            <h3 :class="['section-title', `section-title-${currentTheme.id}`]">
              <span class="title-icon">📝</span>
              <span>详细介绍</span>
            </h3>
            <div :class="['section-content', `section-content-${currentTheme.id}`]">
              {{ post.detail }}
            </div>
          </div>
        </div>

        <!-- 装饰角落 -->
        <div v-if="currentTheme.id === 'heavy-metal'" class="corner-decorations">
          <div class="corner-decoration top-left"></div>
          <div class="corner-decoration top-right"></div>
          <div class="corner-decoration bottom-left"></div>
          <div class="corner-decoration bottom-right"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useTheme } from '../composables/useTheme'
import request from '../api/request'

const route = useRoute()
const router = useRouter()
const { currentTheme, loadTheme } = useTheme()
const post = ref(null)
const loading = ref(false)

const getLikeIcon = (themeId, isLiked) => {
  const iconMap = {
    'heavy-metal': isLiked ? '🔥' : '💀',
    'cyberpunk': isLiked ? '💠' : '⚡',
    'retro-wave': isLiked ? '✨' : '⭐'
  }
  return iconMap[themeId] || '🔥'
}

const fetchDetail = async () => {
  loading.value = true
  try {
    console.log('=== 详情页请求调试 ===')
    console.log('1. 准备请求详情ID:', route.params.id)

    const currentToken = localStorage.getItem('token')
    console.log('2. 当前localStorage中的token:', currentToken)
    console.log('3. Token是否为空:', !currentToken)

    const res = await request.get(`/recruitments/${route.params.id}`)
    post.value = res
    console.log('4. 请求成功')
    console.log('====================')
  } finally {
    loading.value = false
  }
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

onMounted(() => {
  loadTheme()
  fetchDetail()
})
</script>

<style scoped>
/* === BASE STYLES === */
.recruitment-detail-container {
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
}

/* === HEAVY METAL THEME === */
.theme-heavy-metal .detail-card {
  position: relative;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 4px solid #ff4500;
  padding: 40px;
  box-shadow: 0 0 50px rgba(255, 69, 0, 0.6), 0 15px 40px rgba(0, 0, 0, 0.9);
  overflow: hidden;
}

.theme-heavy-metal .detail-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 36px;
  font-weight: 900;
  color: #ff4500;
  margin: 0 0 25px;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.theme-heavy-metal .like-btn-heavy-metal.liked {
  background: linear-gradient(135deg, #ff4500, #dc143c);
  box-shadow: 0 0 40px rgba(255, 69, 0, 0.9);
}

.theme-heavy-metal .corner-decorations {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.theme-heavy-metal .corner-decoration {
  position: absolute;
  width: 30px;
  height: 30px;
  border: 4px solid #ff4500;
  pointer-events: none;
}

.theme-heavy-metal .corner-decoration.top-left { top: 15px; left: 15px; border-right: none; border-bottom: none; }
.theme-heavy-metal .corner-decoration.top-right { top: 15px; right: 15px; border-left: none; border-bottom: none; }
.theme-heavy-metal .corner-decoration.bottom-left { bottom: 15px; left: 15px; border-right: none; border-top: none; }
.theme-heavy-metal .corner-decoration.bottom-right { bottom: 15px; right: 15px; border-left: none; border-top: none; }

/* === CYBERPUNK THEME === */
.theme-cyberpunk .detail-card {
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border: 2px solid #00ffff;
  padding: 35px;
  box-shadow: 0 0 50px rgba(0, 255, 255, 0.3);
  position: relative;
}

.theme-cyberpunk .detail-card::before {
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

.theme-cyberpunk .detail-title {
  font-family: 'Courier New', monospace;
  font-size: 32px;
  font-weight: bold;
  color: #00ffff;
  text-transform: uppercase;
  letter-spacing: 3px;
  text-shadow: 0 0 10px rgba(0, 255, 255, 0.8);
}

.theme-cyberpunk .like-btn-cyberpunk.liked {
  background: rgba(0, 255, 255, 0.3);
  box-shadow: 0 0 30px rgba(0, 255, 255, 0.6);
}

/* === RETRO WAVE THEME === */
.theme-retro-wave .detail-card {
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 100%);
  border: 4px solid transparent;
  border-image: linear-gradient(135deg, #ff1493, #9400d3, #00bfff) 1;
  padding: 35px;
  box-shadow: 0 0 50px rgba(255, 20, 147, 0.4);
}

.theme-retro-wave .detail-title {
  font-family: 'Arial Black', sans-serif;
  font-size: 32px;
  font-weight: 900;
  color: #ff1493;
  text-transform: uppercase;
  letter-spacing: 2px;
  text-shadow: 0 0 10px #ff1493, 2px 2px 0 #9400d3;
}

.theme-retro-wave .like-btn-retro-wave.liked {
  background: linear-gradient(135deg, #ff1493, #9400d3);
  box-shadow: 0 0 40px rgba(255, 20, 147, 0.7);
}

/* === SHARED STYLES === */
.type-badge {
  display: inline-block;
  padding: 8px 20px;
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 16px;
  text-transform: uppercase;
  letter-spacing: 2px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.type-badge.band {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  color: #ffffff;
}

.type-badge.member {
  background: linear-gradient(135deg, #e6a23c 0%, #f0c78a 100%);
  color: #ffffff;
}

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
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.4);
}

.like-btn-cyberpunk.liked { color: #fff; }
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
}

.like-btn-retro-wave.liked { color: #fff; }
.like-btn-retro-wave .like-count { color: #ff1493; }
.like-btn-retro-wave.liked .like-count {
  color: #fff;
  text-shadow: 0 0 5px #ff1493;
}

/* 信息网格 */
.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.info-card {
  display: flex;
  align-items: center;
  gap: 15px;
  background: rgba(255, 255, 255, 0.05);
  border: 2px solid #2d2d2d;
  padding: 20px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.info-card:hover {
  border-color: #ff4500;
  box-shadow: 0 0 15px rgba(255, 69, 0, 0.3);
  transform: translateY(-2px);
}

.info-icon { font-size: 28px; }
.info-content { flex: 1; }

.info-label {
  font-size: 12px;
  color: #8c8c8c;
  text-transform: uppercase;
  letter-spacing: 1px;
  margin-bottom: 5px;
}

.info-value {
  font-size: 16px;
  color: #d4d4d4;
  font-weight: bold;
}

.contact-card {
  grid-column: span 2;
}

.contact-value {
  color: #ff4500;
  font-size: 18px;
}

/* 详细信息区域 */
.sections-container {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.detail-section {
  background: rgba(0, 0, 0, 0.3);
  border: 2px solid #2d2d2d;
  padding: 25px;
  border-radius: 8px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 20px;
  color: #ff4500;
  margin: 0 0 15px;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.title-icon {
  font-size: 24px;
  animation: pulse-glow 2s ease-in-out infinite;
}

.section-content {
  font-size: 15px;
  color: #d4d4d4;
  line-height: 1.8;
  padding: 15px;
  background: rgba(255, 69, 0, 0.05);
  border-left: 3px solid #ff4500;
  white-space: pre-wrap;
}

/* 主题特定的样式调整 */
.theme-cyberpunk .info-card:hover { border-color: #00ffff; }
.theme-cyberpunk .section-title { color: #00ffff; }
.theme-cyberpunk .section-content { border-left-color: #00ffff; }

.theme-retro-wave .info-card:hover { border-color: #ff1493; }
.theme-retro-wave .section-title { color: #ff1493; }
.theme-retro-wave .section-content { border-left-color: #ff1493; }

@keyframes like-pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.25); }
  100% { transform: scale(1); }
}

@keyframes pulse-glow {
  0%, 100% { transform: scale(1); filter: drop-shadow(0 0 10px rgba(255, 69, 0, 0.5)); }
  50% { transform: scale(1.1); filter: drop-shadow(0 0 20px rgba(255, 69, 0, 0.8)); }
}

/* 响应式 */
@media (max-width: 768px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
  .contact-card {
    grid-column: span 1;
  }
}
</style>
