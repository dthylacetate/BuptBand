<template>
  <div class="retro-discussion-card" @click="$emit('click')">
    <div class="wave-decoration">
      <div class="wave wave-1"></div>
      <div class="wave wave-2"></div>
    </div>

    <div class="card-content">
      <div class="section-badge">
        <span class="badge-emoji">🌈</span>
        <span class="badge-text">{{ getSectionText(item.section) }}</span>
      </div>

      <h3 class="retro-title">{{ item.title }}</h3>

      <p class="retro-preview">{{ truncateText(item.content, 100) }}</p>

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

    <div class="stars-decoration">
      <div class="star star-1">✦</div>
      <div class="star star-2">✧</div>
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
.retro-discussion-card {
  position: relative;
  background: linear-gradient(135deg, #1a0a2e 0%, #2a1a3e 100%);
  border: 3px solid transparent;
  border-image: linear-gradient(135deg, #ff1493, #9400d3) 1;
  padding: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  overflow: hidden;
}

.retro-discussion-card:hover {
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

.section-badge {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 4px 10px;
  background: rgba(255, 20, 147, 0.2);
  border: 2px dashed #ff1493;
  border-radius: 20px;
  margin-bottom: 15px;
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
  font-size: 16px;
  font-weight: 900;
  color: #ffe0ff;
  margin: 0 0 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
  text-shadow: 2px 2px 0 #9400d3;
}

.retro-preview {
  font-size: 12px;
  color: #c0a0c0;
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
  color: #fff;
  text-shadow: 0 0 5px #ff1493;
}

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

.star-1 { top: 0; right: 0; }
.star-2 { top: 15px; right: 15px; animation-delay: 0.5s; }

@keyframes wave-move {
  0% { transform: translateX(0); }
  100% { transform: translateX(-50%); }
}

@keyframes star-twinkle {
  0%, 100% { opacity: 0.3; transform: scale(1); }
  50% { opacity: 0.8; transform: scale(1.2); }
}
</style>
