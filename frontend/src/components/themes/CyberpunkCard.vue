<template>
  <div class="cyberpunk-card" @click="$emit('click')">
    <!-- 扫描线效果 -->
    <div class="scanlines"></div>

    <!-- 霓虹边框 -->
    <div class="neon-border"></div>

    <!-- 故障效果 -->
    <div class="glitch-effect"></div>

    <!-- 卡片内容 -->
    <div class="card-content">
      <!-- 类型标签 -->
      <div :class="['cyber-badge', item.type === 'BAND_SEEKING_MEMBER' ? 'band-badge' : 'member-badge']">
        <span class="badge-icon">⚡</span>
        <span class="badge-text">{{ item.type === 'BAND_SEEKING_MEMBER' ? '乐队招人' : '个人求队' }}</span>
        <span class="badge-glow"></span>
      </div>

      <!-- 标题 -->
      <h3 class="cyber-title">{{ item.title }}</h3>

      <!-- 信息区域 -->
      <div class="cyber-info">
        <div class="info-row">
          <span class="info-icon">🎸</span>
          <span class="info-text">{{ item.position }}</span>
        </div>
        <div class="info-row">
          <span class="info-icon">💠</span>
          <span class="info-text">{{ item.style }}</span>
        </div>
      </div>

      <!-- 描述 -->
      <p class="cyber-description">{{ truncateText(item.detail, 60) }}</p>

      <!-- 底部区域 -->
      <div class="card-footer">
        <!-- 发布者信息 -->
        <div class="publisher-zone" @click.stop="$emit('user-click')">
          <div class="avatar-wrapper">
            <el-avatar
              :size="28"
              :src="serverRoot + item.publisherAvatar"
              icon="User"
              class="publisher-avatar"
            />
            <div class="avatar-ring"></div>
          </div>
          <span class="publisher-name">{{ item.publisherNickname || '匿名乐手' }}</span>
        </div>

        <!-- 点赞按钮 -->
        <div
          :class="['cyber-like-btn', { 'liked': item.isLiked }]"
          @click.stop="$emit('like')"
        >
          <span class="like-icon">{{ item.isLiked ? '💠' : '⚡' }}</span>
          <span class="like-count">{{ item.likeCount || 0 }}</span>
          <div class="btn-glow"></div>
        </div>
      </div>
    </div>

    <!-- 数据矩阵装饰 -->
    <div class="matrix-decoration">
      <div class="matrix-line"></div>
      <div class="matrix-line"></div>
      <div class="matrix-line"></div>
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
.cyberpunk-card {
  position: relative;
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 50%, #0a0a1a 100%);
  border: 2px solid #2a2a4a;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow:
    0 10px 30px rgba(0, 0, 0, 0.8),
    0 0 0 1px rgba(0, 255, 255, 0.1);
  overflow: hidden;
}

.cyberpunk-card:hover {
  transform: translateY(-5px);
  box-shadow:
    0 15px 40px rgba(0, 0, 0, 0.9),
    0 0 30px rgba(0, 255, 255, 0.4),
    0 0 0 2px rgba(0, 255, 255, 0.2);
  border-color: #00ffff;
}

/* 扫描线 */
.scanlines {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: repeating-linear-gradient(
    0deg,
    rgba(0, 255, 255, 0.03) 0px,
    transparent 1px,
    transparent 2px,
    rgba(255, 20, 147, 0.03) 3px
  );
  pointer-events: none;
}

/* 霓虹边框 */
.neon-border {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  border: 2px solid transparent;
  background: linear-gradient(90deg, #00ffff, #ff1493, #00ffff) border-box;
  -webkit-mask: linear-gradient(#fff 0 0) padding-box, linear-gradient(#fff 0 0);
  -webkit-mask-composite: xor;
  mask-composite: exclude;
  pointer-events: none;
  opacity: 0.3;
}

.cyberpunk-card:hover .neon-border {
  opacity: 0.6;
  animation: neon-pulse 1.5s ease-in-out infinite;
}

/* 故障效果 */
.glitch-effect {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  opacity: 0;
}

.cyberpunk-card:hover .glitch-effect {
  animation: glitch-anim 0.3s ease-in-out;
}

.card-content {
  position: relative;
  z-index: 1;
}

.cyber-badge {
  position: relative;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 12px;
  background: rgba(0, 255, 255, 0.1);
  border: 1px solid #00ffff;
  border-radius: 2px;
  margin-bottom: 12px;
  font-family: 'Courier New', monospace;
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 1px;
  overflow: hidden;
}

.band-badge {
  color: #00ffff;
  box-shadow: 0 0 10px rgba(0, 255, 255, 0.3);
}

.member-badge {
  color: #ff1493;
  background: rgba(255, 20, 147, 0.1);
  border-color: #ff1493;
  box-shadow: 0 0 10px rgba(255, 20, 147, 0.3);
}

.badge-icon {
  font-size: 12px;
}

.badge-text {
  font-weight: bold;
}

.badge-glow {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.cyberpunk-card:hover .badge-glow {
  left: 100%;
}

.cyber-title {
  font-family: 'Courier New', monospace;
  font-size: 16px;
  font-weight: bold;
  color: #e0e0ff;
  margin: 0 0 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
  line-height: 1.3;
  text-shadow: 0 0 5px rgba(0, 255, 255, 0.5);
}

.cyber-info {
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
  color: #a0a0c0;
  font-family: 'Courier New', monospace;
}

.cyber-description {
  font-size: 11px;
  color: #a0a0c0;
  line-height: 1.5;
  margin: 0 0 15px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  font-family: 'Arial', sans-serif;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #2a2a4a;
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

.avatar-wrapper {
  position: relative;
}

.publisher-avatar {
  border: 1px solid #00ffff;
  box-shadow: 0 0 5px rgba(0, 255, 255, 0.3);
}

.avatar-ring {
  position: absolute;
  top: -2px;
  left: -2px;
  right: -2px;
  bottom: -2px;
  border: 1px solid #00ffff;
  border-radius: 50%;
  animation: ring-pulse 2s ease-in-out infinite;
}

.publisher-name {
  font-size: 11px;
  color: #a0a0c0;
  font-family: 'Courier New', monospace;
}

.cyber-like-btn {
  position: relative;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  background: rgba(0, 255, 255, 0.1);
  border: 1px solid #00ffff;
  border-radius: 2px;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.cyber-like-btn:hover {
  background: rgba(0, 255, 255, 0.2);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.4);
  transform: scale(1.05);
}

.cyber-like-btn.liked {
  background: rgba(0, 255, 255, 0.3);
  border-color: #00ffff;
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.6);
  animation: cyber-like 0.5s ease;
}

.like-icon {
  font-size: 14px;
}

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

.btn-glow {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 255, 0.3), transparent);
  transition: left 0.5s ease;
}

.cyber-like-btn:hover .btn-glow {
  left: 100%;
}

/* 数据矩阵装饰 */
.matrix-decoration {
  position: absolute;
  bottom: 10px;
  right: 10px;
  display: flex;
  flex-direction: column;
  gap: 2px;
  opacity: 0.1;
}

.matrix-line {
  width: 30px;
  height: 2px;
  background: linear-gradient(90deg, #00ffff, transparent);
  animation: matrix-flicker 3s ease-in-out infinite;
}

.matrix-line:nth-child(2) {
  width: 20px;
  animation-delay: 0.5s;
}

.matrix-line:nth-child(3) {
  width: 25px;
  animation-delay: 1s;
}

/* 动画 */
@keyframes neon-pulse {
  0%, 100% { opacity: 0.3; }
  50% { opacity: 0.6; }
}

@keyframes glitch-anim {
  0% { transform: translate(0); }
  20% { transform: translate(-2px, 1px); }
  40% { transform: translate(2px, -1px); }
  60% { transform: translate(-1px, 2px); }
  80% { transform: translate(1px, -2px); }
  100% { transform: translate(0); }
}

@keyframes ring-pulse {
  0%, 100% { transform: scale(1); opacity: 0.5; }
  50% { transform: scale(1.1); opacity: 0.2; }
}

@keyframes cyber-like {
  0% { transform: scale(1); }
  50% { transform: scale(1.2); }
  100% { transform: scale(1); }
}

@keyframes matrix-flicker {
  0%, 100% { opacity: 0.1; }
  50% { opacity: 0.3; }
}
</style>
