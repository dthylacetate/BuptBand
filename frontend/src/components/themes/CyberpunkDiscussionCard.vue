<template>
  <div class="cyberpunk-discussion-card" @click="$emit('click')">
    <div class="scanlines"></div>
    <div class="glitch-overlay"></div>

    <div class="card-content">
      <div class="section-badge">
        <span class="badge-icon">💠</span>
        <span class="badge-text">{{ getSectionText(item.section) }}</span>
      </div>

      <h3 class="cyber-title">{{ item.title }}</h3>

      <p class="cyber-preview">{{ truncateText(item.content, 100) }}</p>

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
.cyberpunk-discussion-card {
  position: relative;
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border: 2px solid #2a2a4a;
  padding: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.cyberpunk-discussion-card:hover {
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

.glitch-overlay {
  position: absolute;
  inset: 0;
  pointer-events: none;
  opacity: 0;
}

.cyberpunk-discussion-card:hover .glitch-overlay {
  animation: glitch-anim 0.3s ease-in-out;
}

.card-content {
  position: relative;
  z-index: 1;
}

.section-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  background: rgba(0, 255, 255, 0.1);
  border: 1px solid #00ffff;
  margin-bottom: 15px;
}

.badge-icon { font-size: 12px; }

.badge-text {
  font-family: 'Courier New', monospace;
  font-size: 11px;
  color: #00ffff;
  font-weight: bold;
  text-transform: uppercase;
}

.cyber-title {
  font-family: 'Courier New', monospace;
  font-size: 16px;
  font-weight: bold;
  color: #e0e0ff;
  margin: 0 0 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
  text-shadow: 0 0 5px rgba(0, 255, 255, 0.5);
}

.cyber-preview {
  font-size: 12px;
  color: #a0a0c0;
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
  color: #fff;
  text-shadow: 0 0 5px #00ffff;
}

@keyframes glitch-anim {
  0% { transform: translate(0); }
  20% { transform: translate(-2px, 1px); }
  40% { transform: translate(2px, -1px); }
  60% { transform: translate(-1px, 2px); }
  80% { transform: translate(1px, -2px); }
  100% { transform: translate(0); }
}
</style>
