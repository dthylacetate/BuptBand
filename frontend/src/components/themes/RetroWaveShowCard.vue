<template>
  <div class="retro-show-card" @click="$emit('click')">
    <!-- 波浪线 -->
    <div class="wave-decoration">
      <div class="wave wave-1"></div>
      <div class="wave wave-2"></div>
    </div>

    <div class="card-content">
      <div class="date-badge">
        <span class="badge-emoji">🌈</span>
        <span class="badge-text">{{ formatDate(item.showTime) }}</span>
      </div>

      <h3 class="retro-title">{{ item.title }}</h3>

      <div class="retro-info">
        <div class="info-row">
          <span class="info-icon">🏟️</span>
          <span class="info-text">{{ item.venue }}</span>
        </div>
        <div class="info-row">
          <span class="info-icon">⏰</span>
          <span class="info-text">{{ formatTime(item.showTime) }}</span>
        </div>
      </div>

      <p class="retro-description">{{ truncateText(item.content, 80) }}</p>

      <div class="card-footer">
        <div class="publisher-zone" @click.stop="$emit('user-click')">
          <el-avatar :size="28" :src="serverRoot + item.publisherAvatar" icon="User" class="publisher-avatar" />
          <span class="publisher-name">{{ item.publisherNickname || '匿名乐手' }}</span>
        </div>

        <div :class="['retro-like-btn', { 'liked': item.isLiked }]" @click.stop="$emit('like')">
          <span class="like-icon">{{ item.isLiked ? '✨' : '⭐' }}</span>
          <span class="like-count">{{ item.likeCount || 0 }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

defineProps({
  item: { type: Object, required: true },
  serverRoot: { type: String, default: '' }
})

defineEmits(['click', 'like', 'user-click'])

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric' })
}

const formatTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

const truncateText = (text, maxLength) => {
  if (!text) return ''
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text
}
</script>

<style scoped>
.retro-show-card {
  position: relative;
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 100%);
  border: 3px solid transparent;
  border-image: linear-gradient(135deg, #ff1493, #9400d3) 1;
  padding: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.retro-show-card:hover {
  transform: translateY(-5px) scale(1.02);
  box-shadow: 0 0 40px rgba(255, 20, 147, 0.4);
}

.wave-decoration {
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

.wave-1 { bottom: 20px; animation-delay: 0s; opacity: 0.5; }
.wave-2 { bottom: 10px; animation-delay: 1.5s; opacity: 0.3; }

.card-content {
  position: relative;
  z-index: 1;
}

.date-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  background: rgba(255, 20, 147, 0.2);
  border: 2px solid #ff1493;
  border-radius: 20px;
  margin-bottom: 15px;
  box-shadow: 0 0 10px rgba(255, 20, 147, 0.4);
}

.badge-emoji { font-size: 12px; }

.badge-text {
  font-family: 'Arial Black', sans-serif;
  font-size: 11px;
  color: #ff1493;
  font-weight: bold;
}

.retro-title {
  font-family: 'Arial Black', sans-serif;
  font-size: 18px;
  font-weight: 900;
  color: #ffe0ff;
  margin: 0 0 15px;
  text-transform: uppercase;
  letter-spacing: 1px;
  text-shadow: 2px 2px 0 #9400d3;
}

.retro-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 15px;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-icon { font-size: 12px; }

.info-text {
  font-size: 12px;
  color: #c0a0c0;
}

.retro-description {
  font-size: 12px;
  color: #c0a0c0;
  line-height: 1.6;
  margin: 0 0 20px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 15px;
  border-top: 2px solid rgba(255, 20, 147, 0.3);
}

.publisher-zone {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.publisher-zone:hover { transform: scale(1.05); }

.publisher-avatar {
  border: 2px solid #ff1493;
  box-shadow: 0 0 10px rgba(255, 20, 147, 0.4);
}

.publisher-name {
  font-size: 11px;
  color: #c0a0c0;
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
  box-shadow: 0 0 25px rgba(255, 20, 147, 0.7);
}

.like-icon { font-size: 14px; }

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

@keyframes wave-move {
  0% { transform: translateX(0); }
  100% { transform: translateX(-50%); }
}
</style>
