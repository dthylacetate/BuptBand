<template>
  <div class="cyberpunk-toolbar">
    <div class="toolbar-grid"></div>

    <div class="toolbar-inner">
      <div class="action-section">
        <button class="cyber-add-btn" @click="$emit('add-click')">
          <span class="btn-text">{{ buttonText }}</span>
          <span class="btn-glow"></span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  buttonText: {
    type: String,
    default: '⚡ 发布招募 ⚡'
  }
})

defineEmits(['add-click'])
</script>

<style scoped>
.cyberpunk-toolbar {
  position: relative;
  background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 100%);
  border: 2px solid #00ffff;
  padding: 25px;
  margin-bottom: 40px;
  box-shadow:
    0 0 30px rgba(0, 255, 255, 0.3),
    0 8px 24px rgba(0, 0, 0, 0.8);
  overflow: hidden;
}

.toolbar-grid {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image:
    linear-gradient(rgba(0, 255, 255, 0.05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 255, 255, 0.05) 1px, transparent 1px);
  background-size: 20px 20px;
  opacity: 0.3;
  pointer-events: none;
}

.toolbar-inner {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
}

.cyber-add-btn {
  position: relative;
  padding: 12px 24px;
  background: rgba(0, 255, 255, 0.1);
  border: 2px solid #00ffff;
  border-radius: 2px;
  color: #00ffff;
  font-family: 'Courier New', monospace;
  font-size: 14px;
  font-weight: bold;
  text-transform: uppercase;
  letter-spacing: 1px;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s ease;
}

.cyber-add-btn:hover {
  background: rgba(0, 255, 255, 0.2);
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.5);
  transform: scale(1.05);
}

.btn-text {
  position: relative;
  z-index: 1;
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

.cyber-add-btn:hover .btn-glow {
  left: 100%;
}
</style>
