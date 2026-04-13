<template>
  <div class="retro-card" @click="$emit('click')">
    <!-- 网格背景 -->
    <div class="retro-grid"></div>

    <!-- 渐变太阳 -->
    <div class="retro-sun">
      <div class="sun-rays"></div>
    </div>

    <!-- 波浪线 -->
    <div class="wave-lines">
      <div class="wave wave-1"></div>
      <div class="wave wave-2"></div>
    </div>

    <!-- 卡片内容 -->
    <div class="card-content">
      <!-- 类型标签 -->
      <div :class="['retro-badge', item.type === 'BAND_SEEKING_MEMBER' ? 'band-badge' : 'member-badge']">
        <span class="badge-pixel">{{ item.type === 'BAND_SEEKING_MEMBER' ? '♪' : '♫' }}</span>
        <span class="badge-text">{{ item.type === 'BAND_SEEKING_MEMBER' ? '乐队招人' : '个人求队' }}</span>
      </div>

      <!-- 标题 -->
      <h3 class="retro-title">{{ item.title }}</h3>

      <!-- 信息区域 -->
      <div class="retro-info">
        <div class="info-row">
          <span class="info-icon">🎸</span>
          <span class="info-text">{{ item.position }}</span>
        </div>
        <div class="info-row">
          <span class="info-icon">🌈</span>
          <span class="info-text">{{ item.style }}</span>
        </div>
      </div>

      <!-- 描述 -->
      <p class="retro-description">{{ truncateText(item.detail, 60) }}</p>

      <!-- 底部区域 -->
      <div class="card-footer">
        <!-- 发布者信息 -->
        <div class="publisher-zone" @click.stop="$emit('user-click')">
          <div class="avatar-frame">
            <el-avatar
              :size="28"
              :src="serverRoot + item.publisherAvatar"
              icon="User"
              class="publisher-avatar"
            />
            <div class="frame-border"></div>
          </div>
          <span class="publisher-name">{{ item.publisherNickname || '匿名乐手' }}</span>
        </div>

        <!-- 点赞按钮 -->
        <div
          :class="['retro-like-btn', { 'liked': item.isLiked }]"
          @click.stop="$emit('like')"
        >
          <span class="like-icon">{{ item.isLiked ? '✨' : '⭐' }}</span>
          <span class="like-count">{{ item.likeCount || 0 }}</span>
        </div>
      </div>
    </div>

    <!-- 星星装饰 -->
    <div class="stars-decoration">
      <div class="star star-1">✦</div>
      <div class="star star-2">✧</div>
      <div class="star star-3">✦</div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

defineProps({
  item: {
    type: Object,
    required: true
  },
  serverRoot: {
    type: String,
    default: ''
  }
})

defineEmits(['click', 'like', 'user-click'])

const truncateText = (text, maxLength) => {
  if (!text) return ''
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text
}
</script>

<style scoped>
.retro-card {
  position: relative;
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 50%, #1a0a2e 100%);
  border: 3px solid transparent;
  border-image: linear-gradient(135deg, #ff1493, #9400d3, #00bfff) 1;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow:
    0 10px 30px rgba(0, 0, 0, 0.8),
    0 0 20px rgba(255, 20, 147, 0.2);
  overflow: hidden;
}

.retro-card:hover {
  transform: translateY(-5px) scale(1.02);
  box-shadow:
    0 15px 40px rgba(0, 0, 0, 0.9),
    0 0 40px rgba(255, 20, 147, 0.4),
    0 0 0 2px rgba(255, 20, 147, 0.2);
}

/* 网格背景 */
.retro-grid {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 50%;
  background-image:
    linear-gradient(rgba(255, 20, 147, 0.1) 1px, transparent 1px),
    linear-gradient(90deg, rgba(148, 0, 211, 0.1) 1px, transparent 1px);
  background-size: 20px 20px;
  transform: perspective(200px) rotateX(60deg);
  transform-origin: center bottom;
  pointer-events: none;
  opacity: 0.5;
}

/* 渐变太阳 */
.retro-sun {
  position: absolute;
  top: -30px;
  right: -30px;
  width: 80px;
  height: 80px;
  pointer-events: none;
}

.sun-rays {
  width: 100%;
  height: 100%;
  background: linear-gradient(180deg, #ff1493 0%, #ff69b4 50%, transparent 100%);
  border-radius: 50%;
  animation: sun-pulse 4s ease-in-out infinite;
}

/* 波浪线 */
.wave-lines {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 30px;
  pointer-events: none;
}

.wave {
  position: absolute;
  width: 200%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 20, 147, 0.3), transparent);
  animation: wave-move 3s linear infinite;
}

.wave-1 {
  bottom: 20px;
  animation-delay: 0s;
  opacity: 0.5;
}

.wave-2 {
  bottom: 10px;
  animation-delay: 1.5s;
  opacity: 0.3;
}

.card-content {
  position: relative;
  z-index: 1;
}

.retro-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  background: rgba(255, 20, 147, 0.2);
  border: 2px solid #ff1493;
  margin-bottom: 12px;
  font-family: 'Arial Black', sans-serif;
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 1px;
  box-shadow: 0 0 10px rgba(255, 20, 147, 0.4);
}

.band-badge {
  color: #ff1493;
}

.member-badge {
  color: #00bfff;
  background: rgba(0, 191, 255, 0.2);
  border-color: #00bfff;
  box-shadow: 0 0 10px rgba(0, 191, 255, 0.4);
}

.badge-pixel {
  font-size: 12px;
}

.badge-text {
  font-weight: bold;
}

.retro-title {
  font-family: 'Arial Black', sans-serif;
  font-size: 16px;
  font-weight: 900;
  color: #ffe0ff;
  margin: 0 0 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
  line-height: 1.3;
  text-shadow:
    2px 2px 0 #9400d3,
    -1px -1px 0 #00bfff;
}

.retro-info {
  display: flex;
  flex-direction: column;
  gap: 6px;
  margin-bottom: 12px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-icon {
  font-size: 12px;
}

.info-text {
  font-size: 12px;
  color: #c0a0c0;
}

.retro-description {
  font-size: 11px;
  color: #c0a0c0;
  line-height: 1.5;
  margin: 0 0 15px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 2px solid rgba(255, 20, 147, 0.3);
}

.publisher-zone {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.publisher-zone:hover {
  transform: scale(1.05);
}

.avatar-frame {
  position: relative;
}

.publisher-avatar {
  border: 2px solid #ff1493;
  box-shadow: 0 0 10px rgba(255, 20, 147, 0.4);
}

.frame-border {
  position: absolute;
  top: -3px;
  left: -3px;
  right: -3px;
  bottom: -3px;
  border: 2px dashed #00bfff;
  border-radius: 50%;
  animation: frame-rotate 10s linear infinite;
}

.publisher-name {
  font-size: 11px;
  color: #c0a0c0;
  font-family: 'Arial', sans-serif;
}

.retro-like-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: linear-gradient(135deg, rgba(255, 20, 147, 0.2), rgba(148, 0, 211, 0.2));
  border: 2px solid #ff1493;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 0 10px rgba(255, 20, 147, 0.3);
}

.retro-like-btn:hover {
  background: linear-gradient(135deg, rgba(255, 20, 147, 0.4), rgba(148, 0, 211, 0.4));
  box-shadow: 0 0 20px rgba(255, 20, 147, 0.5);
  transform: scale(1.05);
}

.retro-like-btn.liked {
  background: linear-gradient(135deg, #ff1493, #9400d3);
  border-color: #ff1493;
  box-shadow: 0 0 25px rgba(255, 20, 147, 0.7);
  animation: retro-like 0.6s ease;
}

.like-icon {
  font-size: 14px;
}

.like-count {
  font-family: 'Arial Black', sans-serif;
  font-size: 12px;
  font-weight: bold;
  color: #ff1493;
  min-width: 20px;
  text-align: center;
}

.retro-like-btn.liked .like-count {
  color: #ffffff;
  text-shadow: 0 0 5px #ff1493;
}

/* 星星装饰 */
.stars-decoration {
  position: absolute;
  top: 10px;
  right: 10px;
  opacity: 0.3;
}

.star {
  position: absolute;
  font-size: 10px;
  color: #ff1493;
  animation: star-twinkle 2s ease-in-out infinite;
}

.star-1 { top: 0; right: 0; animation-delay: 0s; }
.star-2 { top: 15px; right: 20px; animation-delay: 0.5s; color: #00bfff; }
.star-3 { top: 8px; right: 35px; animation-delay: 1s; }

/* 动画 */
@keyframes sun-pulse {
  0%, 100% { transform: scale(1); opacity: 0.8; }
  50% { transform: scale(1.1); opacity: 1; }
}

@keyframes wave-move {
  0% { transform: translateX(0); }
  100% { transform: translateX(-50%); }
}

@keyframes frame-rotate {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes retro-like {
  0% { transform: scale(1); }
  50% { transform: scale(1.3) rotate(10deg); }
  100% { transform: scale(1) rotate(0deg); }
}

@keyframes star-twinkle {
  0%, 100% { opacity: 0.3; transform: scale(1); }
  50% { opacity: 0.8; transform: scale(1.2); }
}
</style>
