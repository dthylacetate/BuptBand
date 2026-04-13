<template>
  <div class="heavy-metal-discussion-card" @click="$emit('click')">
    <!-- 涂鸦边框 -->
    <div class="graffiti-border">
      <div class="spray-top-left"></div>
      <div class="spray-bottom-right"></div>
    </div>

    <div class="card-content">
      <!-- 板块标签 -->
      <div class="section-badge">
        <span class="badge-icon">🎨</span>
        <span class="badge-text">{{ getSectionText(item.section) }}</span>
      </div>

      <!-- 标题 -->
      <h3 class="graffiti-title">{{ item.title }}</h3>

      <!-- 内容预览 -->
      <p class="graffiti-preview">{{ truncateText(item.content, 100) }}</p>

      <!-- 底部信息 -->
      <div class="card-footer">
        <!-- 发布者信息 -->
        <div class="publisher-zone" @click.stop="$emit('user-click')">
          <el-avatar :size="28" :src="serverRoot + item.publisherAvatar" icon="User" class="publisher-avatar" />
          <span class="publisher-name">{{ item.publisherNickname || '匿名乐手' }}</span>
        </div>

        <!-- 点赞和评论 -->
        <div class="interaction-zone">
          <div :class="['metal-like-btn', { 'liked': item.isLiked }]" @click.stop="$emit('like')">
            <span class="like-icon">{{ item.isLiked ? '🔥' : '💀' }}</span>
            <span class="like-count">{{ item.likeCount || 0 }}</span>
          </div>
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

const getSectionText = (section) => {
  const sectionMap = {
    'TECH_THEORY': '技术乐理',
    'CHIT_CHAT': '闲聊树洞'
  }
  return sectionMap[section] || section
}

const truncateText = (text, maxLength) => {
  if (!text) return ''
  return text.length > maxLength ? text.substring(0, maxLength) + '...' : text
}
</script>

<style scoped>
.heavy-metal-discussion-card {
  position: relative;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 3px solid #2d2d2d;
  padding: 25px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.9);
  overflow: hidden;
}

.heavy-metal-discussion-card:hover {
  transform: translateY(-5px) scale(1.02) rotate(-1deg);
  box-shadow: 0 35px 60px -15px rgba(0, 0, 0, 0.95), 0 0 50px rgba(255, 69, 0, 0.5);
  border-color: #ff4500;
}

.graffiti-border {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.spray-top-left,
.spray-bottom-right {
  position: absolute;
  width: 80px;
  height: 80px;
  background: radial-gradient(ellipse, rgba(255, 69, 0, 0.3) 0%, transparent 70%);
  border-radius: 50%;
  filter: blur(8px);
}

.spray-top-left { top: -20px; left: -20px; }
.spray-bottom-right { bottom: -20px; right: -20px; }

.card-content {
  position: relative;
  z-index: 1;
}

.section-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  background: rgba(255, 69, 0, 0.1);
  border: 2px dashed #ff4500;
  border-radius: 4px;
  margin-bottom: 15px;
}

.badge-icon { font-size: 14px; }

.badge-text {
  font-family: 'Arial Black', sans-serif;
  font-size: 12px;
  color: #ff4500;
  text-transform: uppercase;
}

.graffiti-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 18px;
  font-weight: 900;
  color: #d4d4d4;
  margin: 0 0 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
  line-height: 1.3;
}

.graffiti-preview {
  font-size: 13px;
  color: #8c8c8c;
  line-height: 1.6;
  margin: 0 0 15px;
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

.publisher-zone:hover { transform: scale(1.05); }

.publisher-avatar {
  border: 2px solid #ff4500;
}

.publisher-name {
  font-size: 12px;
  color: #8c8c8c;
  font-family: 'Arial Black', sans-serif;
}

.interaction-zone {
  display: flex;
  gap: 10px;
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
  background: linear-gradient(135deg, #ff4500, #dc143c);
  box-shadow: 0 0 20px rgba(255, 69, 0, 0.8);
}

.like-icon { font-size: 16px; }

.like-count {
  font-family: 'Impact', sans-serif;
  font-size: 14px;
  font-weight: bold;
  color: #ff4500;
  min-width: 20px;
  text-align: center;
}

.metal-like-btn.liked .like-count { color: #fff; }
</style>
