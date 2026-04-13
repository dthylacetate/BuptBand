<template>
  <div :class="['show-detail-container', `theme-${currentTheme.id}`]">
    <!-- 页面头部 -->
    <div :class="['detail-header', `header-${currentTheme.id}`]">
      <el-page-header @back="router.back()" content="演出详情">
        <template #title>
          <span class="back-link">← 返回</span>
        </template>
      </el-page-header>
    </div>

    <!-- 主内容区 -->
    <div :class="['detail-content', `content-${currentTheme.id}`]" v-loading="loading">
      <div class="detail-layout" v-if="show">
        <!-- 主信息卡片 -->
        <div :class="['main-card', `card-${currentTheme.id}`]">
          <!-- 状态标签 -->
          <div class="status-badge">即将开演</div>

          <!-- 标题和图标 -->
          <div class="show-header">
            <div :class="['show-icon', `icon-${currentTheme.id}`]">{{ getThemeIcon(currentTheme.id) }}</div>
            <h1 :class="['show-title', `title-${currentTheme.id}`]">{{ show.title }}</h1>
          </div>

          <!-- 点赞按钮 -->
          <div class="like-section">
            <div
              :class="['like-button', `like-btn-${currentTheme.id}`, { 'liked': show.isLiked }]"
              @click="toggleLike(show.id, 'SHOW_ANNOUNCEMENT')"
            >
              <span class="like-icon">{{ getLikeIcon(currentTheme.id, show.isLiked) }}</span>
              <span class="like-text">{{ show.isLiked ? '已点赞' : '点赞' }}</span>
              <span class="like-count">{{ show.likeCount || 0 }}</span>
            </div>
          </div>

          <!-- 信息网格 -->
          <div :class="['info-grid', `grid-${currentTheme.id}`]">
            <div :class="['info-card', `card-info-${currentTheme.id}`]">
              <div class="info-icon">📅</div>
              <div class="info-text">
                <div class="info-label">演出日期</div>
                <div class="info-value">{{ formatDate(show.showTime) }}</div>
              </div>
            </div>

            <div :class="['info-card', `card-info-${currentTheme.id}`]">
              <div class="info-icon">⏰</div>
              <div class="info-text">
                <div class="info-label">入场时间</div>
                <div class="info-value">{{ formatTime(show.showTime) }}</div>
              </div>
            </div>

            <div :class="['info-card', `card-info-${currentTheme.id}`]">
              <div class="info-icon">📍</div>
              <div class="info-text">
                <div class="info-label">演出地点</div>
                <div class="info-value">{{ show.venue }}</div>
              </div>
            </div>
          </div>

          <!-- 分隔线 -->
          <div :class="['divider', `divider-${currentTheme.id}`]">
            <div class="divider-line"></div>
            <div class="divider-text">🎸 演出介绍 🎸</div>
            <div class="divider-line"></div>
          </div>

          <!-- 内容正文 -->
          <div :class="['content-body', `content-body-${currentTheme.id}`]">
            {{ show.content || '暂无详细介绍，现场见分晓！' }}
          </div>
        </div>

        <!-- 侧边栏 -->
        <div class="sidebar">
          <!-- 发布者卡片 -->
          <div :class="['publisher-card', `card-${currentTheme.id}`]">
            <div :class="['card-header', `header-${currentTheme.id}`]">
              <span class="header-icon">👤</span>
              <span class="header-title">发布者 / 主办方</span>
            </div>
            <div class="publisher-info">
              <el-avatar
                :size="70"
                :src="serverRoot + show.publisherAvatar"
                icon="User"
                :class="['publisher-avatar', `avatar-${currentTheme.id}`]"
              />
              <h3 :class="['publisher-name', `name-${currentTheme.id}`]">{{ show.publisherNickname }}</h3>
              <el-button
                :class="['profile-btn', `btn-${currentTheme.id}`]"
                @click="router.push(`/user/${show.publisherNickname}`)"
              >
                <span class="btn-icon">🎸</span>
                <span>查看主页</span>
              </el-button>
            </div>
          </div>

          <!-- 温馨提示 -->
          <div :class="['tips-card', `card-${currentTheme.id}`]">
            <div :class="['tips-header', `header-${currentTheme.id}`]">
              <span class="tips-icon">💡</span>
              <span class="tips-title">温馨提示</span>
            </div>
            <div :class="['tips-content', `tips-${currentTheme.id}`]">
              <p>⚡ 请提前准时入场，以免错过精彩开场。</p>
              <p>⚡ 现场注意财物安全，享受纯粹的现场魅力。</p>
              <p>⚡ 准备好你的喉咙，尽情呐喊吧！</p>
            </div>
          </div>
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
const loading = ref(false)
const show = ref(null)

const apiUrl = import.meta.env.VITE_API_BASE_URL || ''
const serverRoot = apiUrl.replace('/api', '')

const getThemeIcon = (themeId) => {
  const iconMap = {
    'heavy-metal': '🔥',
    'cyberpunk': '💠',
    'retro-wave': '🌆'
  }
  return iconMap[themeId] || '🔥'
}

const getLikeIcon = (themeId, isLiked) => {
  const iconMap = {
    'heavy-metal': isLiked ? '🔥' : '💀',
    'cyberpunk': isLiked ? '💠' : '⚡',
    'retro-wave': isLiked ? '✨' : '⭐'
  }
  return iconMap[themeId] || '🔥'
}

const loadShowDetail = async () => {
  loading.value = true
  try {
    const res = await request.get(`/shows/${route.params.id}`)
    show.value = res
  } finally {
    loading.value = false
  }
}

const formatDate = (dateStr) => {
  const d = new Date(dateStr)
  return d.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}

const formatTime = (dateStr) => {
  const d = new Date(dateStr)
  const weeks = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  const time = d.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit', hour12: false })
  return `${weeks[d.getDay()]} ${time}`
}

const toggleLike = async (id, type) => {
  try {
    if (show.value && show.value.id === id) {
      show.value.isLiked = !show.value.isLiked
      show.value.likeCount = show.value.isLiked ? (show.value.likeCount || 0) + 1 : Math.max((show.value.likeCount || 0) - 1, 0)
    }
    await request.post(`/likes/${type}/${id}`)
  } catch (error) {
    console.error('点赞失败', error)
    if (show.value && show.value.id === id) {
      show.value.isLiked = !show.value.isLiked
      show.value.likeCount = show.value.isLiked ? (show.value.likeCount || 0) + 1 : Math.max((show.value.likeCount || 0) - 1, 0)
    }
  }
}

onMounted(() => {
  loadTheme()
  loadShowDetail()
})
</script>

<style scoped>
/* === BASE STYLES === */
.show-detail-container {
  min-height: 100vh;
  position: relative;
}

.detail-header {
  position: relative;
  z-index: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.detail-content {
  position: relative;
  z-index: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 60px;
}

.detail-layout {
  display: grid;
  grid-template-columns: 1fr 350px;
  gap: 30px;
}

.sidebar {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

/* === HEAVY METAL THEME === */
.theme-heavy-metal .main-card {
  position: relative;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 4px solid #ff4500;
  padding: 40px;
  box-shadow: 0 0 50px rgba(255, 69, 0, 0.6), 0 15px 40px rgba(0, 0, 0, 0.9);
}

.theme-heavy-metal .show-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 36px;
  font-weight: 900;
  color: #ff4500;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.theme-heavy-metal .show-icon {
  font-size: 48px;
  animation: flame-pulse 2s ease-in-out infinite;
}

.theme-heavy-metal .status-badge {
  padding: 10px 20px;
  background: linear-gradient(135deg, #dc143c 0%, #ff4500 100%);
  color: #ffffff;
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 1px;
  border-radius: 4px;
  box-shadow: 0 0 20px rgba(255, 69, 0, 0.5);
  display: inline-block;
  margin-bottom: 20px;
}

/* === CYBERPUNK THEME === */
.theme-cyberpunk .main-card {
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border: 2px solid #00ffff;
  padding: 35px;
  box-shadow: 0 0 50px rgba(0, 255, 255, 0.3);
  position: relative;
}

.theme-cyberpunk .main-card::before {
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

.theme-cyberpunk .show-title {
  font-family: 'Courier New', monospace;
  font-size: 32px;
  font-weight: bold;
  color: #00ffff;
  text-transform: uppercase;
  letter-spacing: 3px;
  text-shadow: 0 0 10px rgba(0, 255, 255, 0.8);
}

.theme-cyberpunk .show-icon {
  font-size: 40px;
  animation: cyber-pulse 2s ease-in-out infinite;
}

.theme-cyberpunk .status-badge {
  padding: 8px 16px;
  background: rgba(0, 255, 255, 0.2);
  border: 1px solid #00ffff;
  color: #00ffff;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 2px;
  display: inline-block;
  margin-bottom: 20px;
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.4);
}

/* === RETRO WAVE THEME === */
.theme-retro-wave .main-card {
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 100%);
  border: 4px solid transparent;
  border-image: linear-gradient(135deg, #ff1493, #9400d3, #00bfff) 1;
  padding: 35px;
  box-shadow: 0 0 50px rgba(255, 20, 147, 0.4);
}

.theme-retro-wave .show-title {
  font-family: 'Arial Black', sans-serif;
  font-size: 32px;
  font-weight: 900;
  color: #ff1493;
  text-transform: uppercase;
  letter-spacing: 2px;
  text-shadow: 0 0 10px #ff1493, 2px 2px 0 #9400d3;
}

.theme-retro-wave .show-icon {
  font-size: 40px;
  animation: retro-glow 2s ease-in-out infinite;
}

.theme-retro-wave .status-badge {
  padding: 8px 16px;
  background: linear-gradient(135deg, #ff1493, #9400d3);
  color: #ffffff;
  font-family: 'Arial Black', sans-serif;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
  border-radius: 20px;
  display: inline-block;
  margin-bottom: 20px;
  box-shadow: 0 0 20px rgba(255, 20, 147, 0.5);
}

/* === SHARED STYLES === */
.show-header {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 30px;
}

.show-icon {
  flex-shrink: 0;
}

.show-title {
  flex: 1;
  margin: 0;
  line-height: 1.2;
}

/* 点赞按钮 */
.like-section {
  display: flex;
  justify-content: center;
  margin: 20px 0 30px;
}

.like-button {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px 30px;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.like-button.liked {
  animation: like-pulse 0.5s ease;
}

.like-icon { font-size: 26px; }
.like-text {
  font-family: 'Arial Black', sans-serif;
  font-size: 16px;
  font-weight: bold;
}
.like-count {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 22px;
  font-weight: bold;
  min-width: 30px;
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
  box-shadow: 0 0 30px rgba(255, 69, 0, 0.7);
  transform: scale(1.05);
}

.like-btn-heavy-metal.liked {
  background: linear-gradient(135deg, #ff4500, #dc143c);
  color: #fff;
  box-shadow: 0 0 50px rgba(255, 69, 0, 1);
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
  box-shadow: 0 0 20px rgba(255, 69, 0, 0.4);
  transform: translateY(-3px);
}

.theme-cyberpunk .info-card:hover { border-color: #00ffff; }
.theme-retro-wave .info-card:hover { border-color: #ff1493; }

.info-icon { font-size: 32px; flex-shrink: 0; }
.info-text { flex: 1; }

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

/* 分隔线 */
.divider {
  display: flex;
  align-items: center;
  gap: 20px;
  margin: 40px 0;
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

.divider-text {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 18px;
  color: #ff4500;
  text-transform: uppercase;
  letter-spacing: 2px;
  white-space: nowrap;
}

.theme-cyberpunk .divider-text {
  font-family: 'Courier New', monospace;
  color: #00ffff;
}

.theme-retro-wave .divider-text {
  color: #ff1493;
}

/* 内容正文 */
.content-body {
  font-size: 16px;
  line-height: 2;
  white-space: pre-wrap;
  min-height: 200px;
  padding: 20px;
  border-left: 4px solid #ff4500;
}

.theme-heavy-metal .content-body {
  color: #d4d4d4;
  background: rgba(255, 69, 0, 0.03);
}

.theme-cyberpunk .content-body {
  color: #d4d4d4;
  border-left-color: #00ffff;
  background: rgba(0, 255, 255, 0.03);
}

.theme-retro-wave .content-body {
  color: #d4d4d4;
  border-left-color: #ff1493;
  background: rgba(255, 20, 147, 0.03);
}

/* 发布者卡片 */
.publisher-card {
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 3px solid #2d2d2d;
  padding: 25px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}

.theme-cyberpunk .publisher-card {
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border-color: #2a2a4a;
}

.theme-retro-wave .publisher-card {
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 100%);
  border-color: #2d2d2d;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #2d2d2d;
}

.theme-cyberpunk .card-header {
  border-bottom-color: #2a2a4a;
}

.header-icon { font-size: 24px; }

.header-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 16px;
  color: #ff4500;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.theme-cyberpunk .header-title {
  font-family: 'Courier New', monospace;
  color: #00ffff;
}

.theme-retro-wave .header-title {
  color: #ff1493;
}

.publisher-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.publisher-avatar {
  border: 3px solid #ff4500;
  box-shadow: 0 0 15px rgba(255, 69, 0, 0.5);
}

.theme-cyberpunk .publisher-avatar {
  border-color: #00ffff;
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.5);
}

.theme-retro-wave .publisher-avatar {
  border-color: #ff1493;
  box-shadow: 0 0 15px rgba(255, 20, 147, 0.5);
}

.publisher-name {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 20px;
  color: #d4d4d4;
  margin: 0;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.profile-btn {
  width: 100%;
  border: none !important;
  color: #ffffff !important;
  font-family: 'Impact', 'Arial Black', sans-serif !important;
  text-transform: uppercase !important;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn-heavy-metal {
  background: linear-gradient(135deg, #ff4500 0%, #dc143c 100%) !important;
}

.btn-cyberpunk {
  background: rgba(0, 255, 255, 0.2) !important;
  border: 1px solid #00ffff !important;
  color: #00ffff !important;
}

.btn-retro-wave {
  background: linear-gradient(135deg, #ff1493, #9400d3) !important;
}

/* 温馨提示卡片 */
.tips-card {
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 3px solid #e6a23c;
  padding: 25px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}

.theme-cyberpunk .tips-card {
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border-color: #e6a23c;
}

.theme-retro-wave .tips-card {
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 100%);
  border-color: #e6a23c;
}

.tips-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
}

.tips-icon { font-size: 24px; }

.tips-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 16px;
  color: #e6a23c;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.tips-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.tips-content p {
  font-size: 14px;
  line-height: 1.6;
  margin: 0;
}

.theme-heavy-metal .tips-content p { color: #d4d4d4; }
.theme-cyberpunk .tips-content p { color: #d4d4d4; }
.theme-retro-wave .tips-content p { color: #d4d4d4; }

/* === ANIMATIONS === */
@keyframes like-pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.25); }
  100% { transform: scale(1); }
}

@keyframes flame-pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.2); }
}

@keyframes cyber-pulse {
  0%, 100% { transform: scale(1); filter: drop-shadow(0 0 10px rgba(0, 255, 255, 0.5)); }
  50% { transform: scale(1.15); filter: drop-shadow(0 0 20px rgba(0, 255, 255, 0.8)); }
}

@keyframes retro-glow {
  0%, 100% { transform: scale(1); filter: drop-shadow(0 0 10px rgba(255, 20, 147, 0.5)); }
  50% { transform: scale(1.15); filter: drop-shadow(0 0 20px rgba(255, 20, 147, 0.8)); }
}

/* === RESPONSIVE === */
@media (max-width: 968px) {
  .detail-layout {
    grid-template-columns: 1fr;
  }

  .sidebar {
    order: 2;
  }

  .show-title {
    font-size: 24px;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
