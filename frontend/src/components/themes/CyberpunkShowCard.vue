<template>
  <div class="cyberpunk-show-card" @click="$emit('click')">
    <!-- 扫描线效果 -->
    <div class="scanlines"></div>
    <!-- 霓虹边框 -->
    <div class="neon-border"></div>

    <div class="card-content">
      <!-- 日期标签 -->
      <div class="date-badge">
        <span class="badge-icon">⚡</span>
        <span class="badge-text">{{ formatDate(item.showTime) }}</span>
      </div>

      <!-- 标题 -->
      <h3 class="cyber-title">{{ item.title }}</h3>

      <!-- 信息区域 -->
      <div class="cyber-info">
        <div class="info-row">
          <span class="info-icon">🏟️</span>
          <span class="info-text">{{ item.venue }}</span>
        </div>
        <div class="info-row">
          <span class="info-icon">⏰</span>
          <span class="info-text">{{ formatTime(item.showTime) }}</span>
        </div>
      </div>

      <!-- 描述 -->
      <p class="cyber-description">{{ truncateText(item.content, 80) }}</p>

      <!-- 底部区域 -->
      <div class="card-footer">
        <div class="publisher-zone" @click.stop="$emit('user-click')">
          <el-avatar :size="28" :src="serverRoot + item.publisherAvatar" icon="User" class="publisher-avatar" />
          <span class="publisher-name">{{ item.publisherNickname || '匿名乐手' }}</span>
        </div>

        <div :class="['cyber-like-btn', { 'liked': item.isLiked }]" @click.stop="$emit('like')">
          <span class="like-icon">{{ item.isLiked ? '💠' : '⚡' }}</span>
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
.cyberpunk-show-card {
  position: relative;
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border: 2px solid #2a2a4a;
  padding: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.cyberpunk-show-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 0 40px rgba(0, 255, 255, 0.4);
  border-color: #00ffff;
}

.scanlines {
  position: absolute;
  inset: 0;
  background: repeating-linear-gradient(0deg, rgba(0, 255, 255, 0.03) 0px, transparent 1px, transparent 2px);
  pointer-events: none;
}

.neon-border {
  position: absolute;
  inset: 0;
  border: 2px solid transparent;
  background: linear-gradient(90deg, #00ffff, #ff1493, #00ffff) border-box;
  -webkit-mask: linear-gradient(#fff 0 0) padding-box, linear-gradient(#fff 0 0);
  mask-composite: exclude;
  opacity: 0.3;
}

.card-content {
  position: relative;
  z-index: 1;
}

.date-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 12px;
  background: rgba(0, 255, 255, 0.1);
  border: 1px solid #00ffff;
  border-radius: 2px;
  margin-bottom: 15px;
  box-shadow: 0 0 10px rgba(0, 255, 255, 0.3);
}

.badge-icon { font-size: 12px; }
.badge-text {
  font-family: 'Courier New', monospace;
  font-size: 11px;
  color: #00ffff;
  font-weight: bold;
}

.cyber-title {
  font-family: 'Courier New', monospace;
  font-size: 18px;
  font-weight: bold;
  color: #e0e0ff;
  margin: 0 0 15px;
  text-transform: uppercase;
  letter-spacing: 1px;
  text-shadow: 0 0 5px rgba(0, 255, 255, 0.5);
}

.cyber-info {
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
  color: #a0a0c0;
  font-family: 'Courier New', monospace;
}

.cyber-description {
  font-size: 12px;
  color: #a0a0c0;
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
  border-top: 1px solid #2a2a4a;
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
  border: 1px solid #00ffff;
  box-shadow: 0 0 5px rgba(0, 255, 255, 0.3);
}

.publisher-name {
  font-size: 11px;
  color: #a0a0c0;
  font-family: 'Courier New', monospace;
}

.cyber-like-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: rgba(0, 255, 255, 0.1);
  border: 1px solid #00ffff;
  border-radius: 2px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cyber-like-btn:hover {
  background: rgba(0, 255, 255, 0.2);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.4);
  transform: scale(1.05);
}

.cyber-like-btn.liked {
  background: rgba(0, 255, 255, 0.3);
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.6);
}

.like-icon { font-size: 14px; }

.like-count {
  font-family: 'Courier New', monospace;
  font-size: 12px;
  font-weight: bold;
  color: #00ffff;
  min-width: 20px;
  text-align: center;
}

.cyber-like-btn.liked .like-count {
  color: #ffffff;
  text-shadow: 0 0 5px #00ffff;
}
</style>
