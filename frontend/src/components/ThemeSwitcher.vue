<template>
  <div class="theme-switcher">
    <el-dropdown trigger="click" @command="handleThemeChange">
      <div class="theme-trigger">
        <span class="theme-icon">{{ currentTheme.icon }}</span>
        <span class="theme-name">{{ currentTheme.name }}</span>
      </div>
      <template #dropdown>
        <el-dropdown-menu class="theme-dropdown">
          <div
            v-for="(theme, key) in themes"
            :key="key"
            :class="['theme-option', { 'active': currentTheme.id === theme.id }]"
            @click="handleThemeChange(theme)"
          >
            <span class="theme-option-icon">{{ theme.icon }}</span>
            <span class="theme-option-name">{{ theme.name }}</span>
            <div class="theme-preview">
              <div
                v-for="(color, colorKey) in themePreviewColors(theme)"
                :key="colorKey"
                class="color-dot"
                :style="{ backgroundColor: color }"
              />
            </div>
          </div>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useTheme } from '../composables/useTheme'

const { currentTheme, themes, setTheme } = useTheme()

const themePreviewColors = (theme) => ({
  primary: theme.colors.primary,
  secondary: theme.colors.secondary,
  accent: theme.colors.accent
})

const handleThemeChange = (theme) => {
  setTheme(theme)
}
</script>

<style scoped>
.theme-switcher {
  display: inline-block;
  margin-right: 20px;
}

.theme-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.05);
  border: 2px solid #2d2d2d;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.theme-trigger:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: #ff4500;
  box-shadow: 0 0 15px rgba(255, 69, 0, 0.3);
}

.theme-icon {
  font-size: 18px;
}

.theme-name {
  font-family: 'Arial Black', sans-serif;
  font-size: 14px;
  color: #d4d4d4;
}

.theme-dropdown {
  background: #1a1a1a !important;
  border: 2px solid #2d2d2d !important;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.8) !important;
  padding: 10px !important;
  min-width: 250px !important;
}

.theme-option {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 15px !important;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 5px;
}

.theme-option:hover {
  background: rgba(255, 255, 255, 0.05) !important;
}

.theme-option.active {
  background: rgba(255, 69, 0, 0.1) !important;
  border-left: 3px solid #ff4500;
}

.theme-option-icon {
  font-size: 20px;
}

.theme-option-name {
  flex: 1;
  font-family: 'Arial Black', sans-serif;
  font-size: 14px;
  color: #d4d4d4;
}

.theme-preview {
  display: flex;
  gap: 5px;
}

.color-dot {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  border: 2px solid rgba(255, 255, 255, 0.2);
  transition: transform 0.2s ease;
}

.color-dot:hover {
  transform: scale(1.3);
}

/* 重写 Element Plus 下拉样式 */
:deep(.el-dropdown-menu__item) {
  padding: 0 !important;
  line-height: normal !important;
}

:deep(.el-dropdown-menu__item:focus),
:deep(.el-dropdown-menu__item:not(.is-disabled):hover) {
  background: transparent !important;
  color: inherit !important;
}
</style>
