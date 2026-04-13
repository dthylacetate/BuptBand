<template>
  <div class="heavy-metal-toolbar">
    <div class="toolbar-decoration">
      <div class="chain-left"></div>
      <div class="chain-right"></div>
    </div>

    <div class="toolbar-inner">
      <div class="action-section">
        <el-button
          :icon="Plus"
          class="metal-add-btn"
          @click="$emit('add-click')"
        >
          {{ buttonText }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Plus } from '@element-plus/icons-vue'

defineProps({
  buttonText: {
    type: String,
    default: '🔥 发布招募 🔥'
  }
})

defineEmits(['add-click'])
</script>

<style scoped>
.heavy-metal-toolbar {
  position: relative;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 3px solid #ff4500;
  padding: 30px;
  margin-bottom: 40px;
  box-shadow: 0 0 30px rgba(255, 69, 0, 0.5), 0 8px 24px rgba(0, 0, 0, 0.8);
  animation: pulse-burn 2s ease-in-out infinite;
}

.toolbar-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
}

.chain-left,
.chain-right {
  position: absolute;
  top: 50%;
  width: 30px;
  height: 60px;
  background: repeating-linear-gradient(
    0deg,
    transparent,
    transparent 4px,
    rgba(255, 69, 0, 0.3) 4px,
    rgba(255, 69, 0, 0.3) 8px
  );
}

.chain-left {
  left: 10px;
  transform: translateY(-50%) rotate(-5deg);
}

.chain-right {
  right: 10px;
  transform: translateY(-50%) rotate(5deg);
}

.toolbar-inner {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
}

.metal-add-btn {
  background: linear-gradient(135deg, #ff4500 0%, #dc143c 100%) !important;
  border: none !important;
  color: #ffffff !important;
  font-family: 'Impact', 'Arial Black', sans-serif !important;
  text-transform: uppercase !important;
  letter-spacing: 1px !important;
}

@keyframes pulse-burn {
  0%, 100% { box-shadow: 0 0 30px rgba(255, 69, 0, 0.5), 0 8px 24px rgba(0, 0, 0, 0.8); }
  50% { box-shadow: 0 0 50px rgba(255, 69, 0, 0.8), 0 8px 24px rgba(0, 0, 0, 0.8); }
}
</style>
