<template>
  <div class="heavy-metal-card" @click="$emit('click')">
    <!-- 撕裂边缘效果 -->
    <div class="torn-edges"></div>

    <!-- 火焰装饰 -->
    <div class="flame-decoration">
      <div class="flame flame-1"></div>
      <div class="flame flame-2"></div>
      <div class="flame flame-3"></div>
    </div>

    <!-- 卡片内容 -->
    <div class="card-content">
      <!-- 类型标签 -->
      <div :class="['metal-badge', item.type === 'BAND_SEEKING_MEMBER' ? 'band-badge' : 'member-badge']">
        {{ item.type === 'BAND_SEEKING_MEMBER' ? '⚡ 乐队招人' : '🤘 个人求队' }}
      </div>

      <!-- 标题 -->
      <h3 class="metal-title">{{ item.title }}</h3>

      <!-- 信息区域 -->
      <div class="metal-info">
        <div class="info-row">
          <span class="info-icon">🎸</span>
          <span class="info-text">{{ item.position }}</span>
        </div>
        <div class="info-row">
          <span class="info-icon">🔥</span>
          <span class="info-text">{{ item.style }}</span>
        </div>
      </div>

      <!-- 描述 -->
      <p class="metal-description">{{ truncateText(item.detail, 60) }}</p>

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

    <!-- 角落装饰 -->
    <div class="corner-decoration top-left"></div>
    <div class="corner-decoration top-right"></div>
    <div class="corner-decoration bottom-left"></div>
    <div class="corner-decoration bottom-right"></div>
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
.heavy-metal-card {
  position: relative;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 3px solid #2d2d2d;
  padding: 20px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow:
    0 25px 50px -12px rgba(0, 0, 0, 0.9),
    0 0 0 1px rgba(255, 69, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.05);
  overflow: hidden;
}

.heavy-metal-card:hover {
  transform: translateY(-8px) scale(1.02) rotate(1deg);
  box-shadow:
    0 35px 60px -15px rgba(0, 0, 0, 0.95),
    0 0 40px rgba(255, 69, 0, 0.4),
    0 0 0 2px rgba(255, 69, 0, 0.2);
  border-color: #ff4500;
}

/* 撕裂边缘 */
.torn-edges {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  background-image:
    url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'%3E%3Cpath d='M0,0 L10,5 L20,0 L30,8 L40,2 L50,10 L60,3 L70,9 L80,4 L90,7 L100,0 L100,100 L90,95 L80,100 L70,92 L60,98 L50,90 L40,97 L30,91 L20,98 L10,93 L0,100 Z' fill='rgba(255,69,0,0.05)'/%3E%3C/svg%3E");
  opacity: 0.3;
}

/* 火焰装饰 */
.flame-decoration {
  position: absolute;
  top: -15px;
  right: -15px;
  width: 80px;
  height: 80px;
  pointer-events: none;
  opacity: 0.4;
}

.flame {
  position: absolute;
  width: 20px;
  height: 25px;
  background: linear-gradient(to top, #ff4500, #dc143c);
  border-radius: 50% 50% 50% 50% / 60% 60% 40% 40%;
  animation: flame-dance 1.5s ease-in-out infinite;
}

.flame-1 { top: 20px; right: 20px; animation-delay: 0s; }
.flame-2 { top: 10px; right: 40px; animation-delay: 0.3s; transform: scale(0.8); }
.flame-3 { top: 35px; right: 45px; animation-delay: 0.6s; transform: scale(0.6); }

.card-content {
  position: relative;
  z-index: 1;
}

.metal-badge {
  display: inline-block;
  padding: 4px 10px;
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
  border-radius: 4px;
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

.metal-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 18px;
  font-weight: 900;
  color: #d4d4d4;
  margin: 0 0 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
  line-height: 1.3;
}

.metal-info {
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
  font-size: 14px;
}

.info-text {
  font-size: 13px;
  color: #8c8c8c;
}

.metal-description {
  font-size: 12px;
  color: #8c8c8c;
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

.corner-decoration {
  position: absolute;
  width: 25px;
  height: 25px;
  border: 3px solid #ff4500;
  pointer-events: none;
}

.corner-decoration.top-left { top: 10px; left: 10px; border-right: none; border-bottom: none; }
.corner-decoration.top-right { top: 10px; right: 10px; border-left: none; border-bottom: none; }
.corner-decoration.bottom-left { bottom: 10px; left: 10px; border-right: none; border-top: none; }
.corner-decoration.bottom-right { bottom: 10px; right: 10px; border-left: none; border-top: none; }

@keyframes flame-dance {
  0%, 100% { transform: translateY(0) rotate(-2deg); }
  50% { transform: translateY(-5px) rotate(2deg); }
}

@keyframes metal-pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.2); }
  100% { transform: scale(1); }
}
</style>
