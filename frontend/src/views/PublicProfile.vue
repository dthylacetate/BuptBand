<template>
  <div :class="['public-profile-container', `theme-${currentTheme.id}`]">
    <!-- 页面头部 -->
    <div :class="['profile-header', `header-${currentTheme.id}`]">
      <el-page-header @back="router.back()" :content="`${nickname} 的名片`">
        <template #title>
          <span class="back-link">← 返回</span>
        </template>
      </el-page-header>
    </div>

    <!-- 主内容区 -->
    <div :class="['profile-content', `content-${currentTheme.id}`]">
      <!-- 乐手卡片 -->
      <div :class="['musician-card', `card-${currentTheme.id}`]" v-if="user">
        <!-- 装饰角落 -->
        <div v-if="currentTheme.id === 'heavy-metal'" class="corner-decorations">
          <div class="corner-decoration top-left"></div>
          <div class="corner-decoration top-right"></div>
          <div class="corner-decoration bottom-left"></div>
          <div class="corner-decoration bottom-right"></div>
        </div>

        <!-- 乐手头部信息 -->
        <div :class="['musician-header', `header-${currentTheme.id}`]">
          <div class="avatar-stage">
            <el-avatar
              :size="100"
              :src="serverRoot + user.avatarUrl"
              icon="User"
              :class="['user-avatar', `avatar-${currentTheme.id}`]"
            />
            <div :class="['avatar-glow', `glow-${currentTheme.id}`]"></div>
          </div>
          <div class="musician-info">
            <h2 :class="['musician-name', `name-${currentTheme.id}`]">{{ user.nickname }}</h2>
            <div class="musician-badges">
              <div :class="['badge', `badge-${currentTheme.id}`]">
                <span class="badge-icon">🎸</span>
                <span class="badge-text">{{ user.instrument || '神秘乐手' }}</span>
              </div>
              <div :class="['badge', `badge-${currentTheme.id}`]">
                <span class="badge-icon">⏰</span>
                <span class="badge-text">琴龄: {{ user.playingYears || 0 }}年</span>
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

        <!-- 关于我 -->
        <div class="bio-section">
          <h3 :class="['section-title', `title-${currentTheme.id}`]">
            <span class="title-icon">📝</span>
            <span>关于我</span>
          </h3>
          <div :class="['bio-content', `bio-${currentTheme.id}`]">
            {{ user.bio || '这个乐手很懒，什么都没写~' }}
          </div>
        </div>
      </div>

      <!-- 发布列表 -->
      <div :class="['posts-section', `card-${currentTheme.id}`]">
        <div :class="['posts-header', `header-${currentTheme.id}`]">
          <span class="posts-icon">🎸</span>
          <span class="posts-title">他的发布</span>
        </div>

        <div v-if="userPosts.length > 0" class="posts-grid">
          <div
            v-for="item in userPosts"
            :key="item.id"
            :class="['post-card', `post-${currentTheme.id}`]"
            @click="router.push(`/recruitments/${item.id}`)"
          >
            <h4 class="post-title">{{ item.title }}</h4>
            <div :class="['post-badge', item.type === 'BAND_SEEKING_MEMBER' ? 'band-badge' : 'member-badge']">
              {{ item.position }}
            </div>
            <p class="post-detail">{{ item.detail }}</p>
            <div class="post-footer">
              <span class="footer-text">点击查看详情 →</span>
            </div>
          </div>
        </div>

        <div v-else :class="['empty-posts', `empty-${currentTheme.id}`]">
          <div class="empty-icon">🎸</div>
          <p>这个乐手还没有发布过招募信息</p>
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
const nickname = route.params.nickname
const user = ref(null)
const userPosts = ref([])

const serverRoot = import.meta.env.VITE_API_BASE_URL.replace('/api', '')

const getDividerIcon = (themeId) => {
  const iconMap = {
    'heavy-metal': '☠️',
    'cyberpunk': '💠',
    'retro-wave': '✦'
  }
  return iconMap[themeId] || '☠️'
}

const loadData = async () => {
  try {
    user.value = await request.get(`/users/public/${nickname}`)
    userPosts.value = await request.get(`/recruitments/user/${nickname}`)
  } catch (err) {
    console.error(err)
  }
}

onMounted(() => {
  loadTheme()
  loadData()
})
</script>

<style scoped>
/* === BASE STYLES === */
.public-profile-container {
  min-height: 100vh;
  position: relative;
}

.profile-header {
  position: relative;
  z-index: 1;
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.profile-content {
  position: relative;
  z-index: 1;
  max-width: 900px;
  margin: 0 auto;
  padding: 0 20px 60px;
  display: flex;
  flex-direction: column;
  gap: 40px;
}

/* === HEAVY METAL THEME === */
.theme-heavy-metal .musician-card {
  position: relative;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 4px solid #ff4500;
  padding: 40px;
  box-shadow: 0 0 50px rgba(255, 69, 0, 0.6), 0 15px 40px rgba(0, 0, 0, 0.9);
}

.theme-heavy-metal .musician-name {
  color: #d4d4d4;
}

.theme-heavy-metal .section-title {
  color: #ff4500;
}

/* === CYBERPUNK THEME === */
.theme-cyberpunk .musician-card {
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border: 2px solid #00ffff;
  padding: 35px;
  box-shadow: 0 0 50px rgba(0, 255, 255, 0.3);
  position: relative;
}

.theme-cyberpunk .musician-card::before {
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

.theme-cyberpunk .musician-name {
  font-family: 'Courier New', monospace;
  color: #00ffff;
  text-shadow: 0 0 10px rgba(0, 255, 255, 0.5);
}

.theme-cyberpunk .section-title {
  font-family: 'Courier New', monospace;
  color: #00ffff;
}

/* === RETRO WAVE THEME === */
.theme-retro-wave .musician-card {
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 100%);
  border: 4px solid transparent;
  border-image: linear-gradient(135deg, #ff1493, #9400d3) 1;
  padding: 35px;
  box-shadow: 0 0 50px rgba(255, 20, 147, 0.4);
}

.theme-retro-wave .musician-name {
  color: #ffe0ff;
  text-shadow: 2px 2px 0 #9400d3;
}

.theme-retro-wave .section-title {
  color: #ff1493;
}

/* === SHARED STYLES === */
/* 装饰角落 */
.corner-decorations {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.corner-decoration {
  position: absolute;
  width: 30px;
  height: 30px;
  border: 4px solid #ff4500;
  pointer-events: none;
}

.corner-decoration.top-left { top: 15px; left: 15px; border-right: none; border-bottom: none; }
.corner-decoration.top-right { top: 15px; right: 15px; border-left: none; border-bottom: none; }
.corner-decoration.bottom-left { bottom: 15px; left: 15px; border-right: none; border-top: none; }
.corner-decoration.bottom-right { bottom: 15px; right: 15px; border-left: none; border-top: none; }

/* 乐手头部 */
.musician-header {
  display: flex;
  align-items: center;
  gap: 30px;
  margin-bottom: 30px;
}

.avatar-stage {
  position: relative;
  flex-shrink: 0;
}

.user-avatar {
  border: 4px solid #ff4500;
  box-shadow: 0 0 20px rgba(255, 69, 0, 0.5);
}

.theme-cyberpunk .user-avatar {
  border-color: #00ffff;
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.5);
}

.theme-retro-wave .user-avatar {
  border-color: #ff1493;
  box-shadow: 0 0 20px rgba(255, 20, 147, 0.5);
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

.musician-info {
  flex: 1;
}

.musician-name {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 36px;
  font-weight: 900;
  margin: 0 0 15px;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.musician-badges {
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

/* 关于我 */
.bio-section {
  margin-top: 20px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 20px;
  margin: 0 0 15px;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.title-icon {
  font-size: 24px;
  animation: pulse-glow 2s ease-in-out infinite;
}

.bio-content {
  font-size: 15px;
  line-height: 1.8;
  padding: 20px;
  border-left: 4px solid #ff4500;
}

.theme-heavy-metal .bio-content {
  color: #d4d4d4;
  background: rgba(255, 69, 0, 0.05);
}

.theme-cyberpunk .bio-content {
  color: #d4d4d4;
  border-left-color: #00ffff;
  background: rgba(0, 255, 255, 0.05);
}

.theme-retro-wave .bio-content {
  color: #d4d4d4;
  border-left-color: #ff1493;
  background: rgba(255, 20, 147, 0.05);
}

/* 发布区 */
.posts-section {
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 3px solid #2d2d2d;
  padding: 30px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.6);
}

.theme-cyberpunk .posts-section {
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border-color: #2a2a4a;
}

.theme-retro-wave .posts-section {
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 100%);
  border-color: #2d2d2d;
}

.posts-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 25px;
  padding-bottom: 20px;
  border-bottom: 3px solid #ff4500;
}

.theme-cyberpunk .posts-header {
  border-bottom-color: #00ffff;
}

.theme-retro-wave .posts-header {
  border-bottom-color: #ff1493;
}

.posts-icon {
  font-size: 28px;
  animation: pulse-glow 2s ease-in-out infinite;
}

.posts-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 24px;
  color: #ff4500;
  text-transform: uppercase;
  letter-spacing: 2px;
}

.theme-cyberpunk .posts-title {
  font-family: 'Courier New', monospace;
  color: #00ffff;
}

.theme-retro-wave .posts-title {
  color: #ff1493;
}

/* 发布网格 */
.posts-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.post-card {
  position: relative;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 100%);
  border: 2px solid #2d2d2d;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.post-card:hover {
  border-color: #ff4500;
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.8), 0 0 30px rgba(255, 69, 0, 0.3);
}

.theme-cyberpunk .post-card:hover {
  border-color: #00ffff;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.8), 0 0 30px rgba(0, 255, 255, 0.3);
}

.theme-retro-wave .post-card:hover {
  border-color: #ff1493;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.8), 0 0 30px rgba(255, 20, 147, 0.3);
}

.post-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 16px;
  font-weight: 900;
  color: #d4d4d4;
  margin: 0 0 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
  line-height: 1.3;
}

.post-badge {
  padding: 4px 10px;
  font-family: 'Arial Black', sans-serif;
  font-size: 10px;
  text-transform: uppercase;
  border-radius: 4px;
  display: inline-block;
  margin-bottom: 12px;
}

.band-badge {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  color: #ffffff;
}

.member-badge {
  background: linear-gradient(135deg, #e6a23c 0%, #f0c78a 100%);
  color: #ffffff;
}

.post-detail {
  font-size: 13px;
  color: #8c8c8c;
  line-height: 1.6;
  margin: 0 0 15px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.post-footer {
  padding-top: 12px;
  border-top: 2px solid #2d2d2d;
}

.footer-text {
  font-size: 12px;
  color: #ff4500;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.theme-cyberpunk .footer-text { color: #00ffff; }
.theme-retro-wave .footer-text { color: #ff1493; }

/* 空状态 */
.empty-posts {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 60px;
  margin-bottom: 15px;
  animation: pulse-glow 2s ease-in-out infinite;
}

.empty-posts p {
  font-size: 16px;
  color: #8c8c8c;
  margin: 0;
}

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
  .musician-header {
    flex-direction: column;
    text-align: center;
  }

  .musician-badges {
    justify-content: center;
  }

  .posts-grid {
    grid-template-columns: 1fr;
  }

  .musician-name {
    font-size: 24px;
  }
}
</style>
