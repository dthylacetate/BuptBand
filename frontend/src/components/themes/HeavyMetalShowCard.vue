<template>
  <div class="heavy-metal-show-card" @click="$emit('click')">
    <!-- 火焰边框 -->
    <div class="flame-border">
      <div class="flame-top"></div>
      <div class="flame-bottom"></div>
    </div>

    <!-- 卡片内容 -->
    <div class="card-content">
      <!-- 日期标签 -->
      <div class="date-badge">
        <span class="date-icon">📅</span>
        <span class="date-text">{{ formatDate(item.showTime) }}</span>
      </div>

      <!-- 标题 -->
      <h3 class="metal-title">{{ item.title }}</h3>

      <!-- 信息区域 -->
      <div class="metal-info">
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
      <p class="metal-description">{{ truncateText(item.content, 80) }}</p>

      <!-- 底部区域 -->
      <div class="card-footer">
        <!-- 发布者信息 -->
        <div class="publisher-zone" @click.stop="$emit('user-click')">
          <el-avatar
            :size="28"
            :src="serverRoot + item.publisherAvatar"
            icon="User"
            class="publisher-avatar"
          />
          <span class="publisher-name">{{ item.publisherNickname || '匿名乐手' }}</span>
        </div>

        <!-- 点赞按钮 -->
        <div
          :class="['metal-like-btn', { 'liked': item.isLiked }]"
          @click.stop="$emit('like')"
        >
          <span class="like-icon">{{ item.isLiked ? '🔥' : '💀' }}</span>
          <span class="like-count">{{ item.likeCount || 0 }}</span>
        </div>
      </div>
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
.heavy-metal-show-card {
  position: relative;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 3px solid #2d2d2d;
  padding: 25px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow:
    0 25px 50px -12px rgba(0, 0, 0, 0.9),
    0 0 0 1px rgba(255, 69, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.05);
  overflow: hidden;
}

.heavy-metal-show-card:hover {
  transform: translateY(-10px) scale(1.02) rotate(1deg);
  box-shadow:
    0 35px 60px -15px rgba(0, 0, 0, 0.95),
    0 0 60px rgba(255, 69, 0, 0.7),
    0 0 0 2px rgba(255, 69, 0, 0.3);
  border-color: #ff4500;
}

/* 火焰边框 */
.flame-border {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.flame-top,
.flame-bottom {
  position: absolute;
  left: 0;
  width: 100%;
  height: 5px;
  background: linear-gradient(90deg, transparent 0%, #ff4500 50%, transparent 100%);
}

.flame-top {
  top: 0;
  animation: flame-flicker 3s ease-in-out infinite;
}

.flame-bottom {
  bottom: 0;
  animation: flame-flicker 3s ease-in-out infinite reverse;
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
  background: linear-gradient(135deg, #dc143c, #ff4500);
  border-radius: 20px;
  margin-bottom: 15px;
  box-shadow: 0 0 15px rgba(255, 69, 0, 0.4);
}

.date-icon {
  font-size: 14px;
}

.date-text {
  font-family: 'Arial Black', sans-serif;
  font-size: 12px;
  color: #ffffff;
  font-weight: bold;
}

.metal-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 20px;
  font-weight: 900;
  color: #d4d4d4;
  margin: 0 0 15px;
  text-transform: uppercase;
  letter-spacing: 1px;
  line-height: 1.3;
}

.metal-info {
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

.info-icon {
  font-size: 14px;
}

.info-text {
  font-size: 13px;
  color: #8c8c8c;
}

.metal-description {
  font-size: 13px;
  color: #8c8c8c;
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
  border-top: 2px solid #2d2d2d;
}

.publisher-zone {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.publisher-zone:hover {
  transform: scale(1.05);
}

.publisher-avatar {
  border: 2px solid #ff4500;
}

.publisher-name {
  font-size: 12px;
  color: #8c8c8c;
  font-family: 'Arial Black', sans-serif;
}

.metal-like-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: rgba(255, 69, 0, 0.1);
  border: 2px solid #ff4500;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.metal-like-btn:hover {
  background: rgba(255, 69, 0, 0.2);
  box-shadow: 0 0 15px rgba(255, 69, 0, 0.5);
  transform: scale(1.05);
}

.metal-like-btn.liked {
  background: linear-gradient(135deg, #ff4500 0%, #dc143c 100%);
  border-color: #ff4500;
  box-shadow: 0 0 20px rgba(255, 69, 0, 0.8);
  animation: metal-pulse 0.5s ease;
}

.like-icon {
  font-size: 16px;
}

.like-count {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 14px;
  font-weight: bold;
  color: #ff4500;
  min-width: 20px;
  text-align: center;
}

.metal-like-btn.liked .like-count {
  color: #ffffff;
}

@keyframes flame-flicker {
  0%, 100% { opacity: 0.6; }
  50% { opacity: 1; }
}

@keyframes metal-pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.2); }
  100% { transform: scale(1); }
}
</style>
