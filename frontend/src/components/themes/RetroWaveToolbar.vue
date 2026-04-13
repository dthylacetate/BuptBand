<template>
  <div class="retro-toolbar">
    <div class="toolbar-gradient"></div>

    <div class="toolbar-inner">
      <div class="action-section">
        <button class="retro-add-btn" @click="$emit('add-click')">
          <span class="btn-stars">✨</span>
          <span class="btn-text">{{ buttonText }}</span>
          <span class="btn-stars">✨</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  buttonText: {
    type: String,
    default: '✨ 发布招募 ✨'
  }
})

defineEmits(['add-click'])
</script>

<style scoped>
.retro-toolbar {
  position: relative;
  background: linear-gradient(135deg, #2a1a3e 0%, #1a0a2e 100%);
  border: 3px solid transparent;
  border-image: linear-gradient(135deg, #ff1493, #9400d3, #00bfff) 1;
  padding: 25px;
  margin-bottom: 40px;
  box-shadow:
    0 0 30px rgba(255, 20, 147, 0.3),
    0 8px 24px rgba(0, 0, 0, 0.8);
  overflow: hidden;
}

.toolbar-gradient {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 20, 147, 0.1), transparent);
  pointer-events: none;
}

.toolbar-inner {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
}

.retro-add-btn {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: linear-gradient(135deg, #ff1493, #9400d3);
  border: 2px solid #ff1493;
  border-radius: 20px;
  color: #ffffff;
  font-family: 'Arial Black', sans-serif;
  font-size: 14px;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 1px;
  cursor: pointer;
  box-shadow: 0 0 15px rgba(255, 20, 147, 0.4);
  transition: all 0.3s ease;
}

.retro-add-btn:hover {
  background: linear-gradient(135deg, #ff1493, #00bfff);
  box-shadow: 0 0 25px rgba(255, 20, 147, 0.6);
  transform: scale(1.05) rotate(-2deg);
}

.btn-stars {
  font-size: 16px;
  animation: star-spin 3s linear infinite;
}

.btn-text {
  position: relative;
  z-index: 1;
}

@keyframes star-spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>
