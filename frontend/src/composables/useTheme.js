import { ref, watch } from 'vue'

// 主题配置
export const THEMES = {
  HEAVY_METAL: {
    id: 'heavy-metal',
    name: '重金属',
    icon: '🤘',
    description: '火焰与骷髅的重金属现场',
    colors: {
      primary: '#ff4500',
      secondary: '#dc143c',
      accent: '#ffa500',
      bg: '#0a0a0a',
      surface: '#1a1a1a',
      text: '#d4d4d4',
      border: '#2d2d2d'
    },
    fonts: {
      heading: "'Impact', 'Arial Black', sans-serif",
      body: "'Arial Black', 'Impact', sans-serif"
    },
    effects: 'flames, skull, torn-edges'
  },
  CYBERPUNK: {
    id: 'cyberpunk',
    name: '赛博朋克',
    icon: '🌃',
    description: '霓虹灯与激光的未来都市',
    colors: {
      primary: '#00ffff',
      secondary: '#ff1493',
      accent: '#39ff14',
      bg: '#0a0a1a',
      surface: '#1a1a2e',
      text: '#e0e0ff',
      border: '#2a2a4a'
    },
    fonts: {
      heading: "'Orbitron', 'Arial Black', sans-serif",
      body: "'Rajdhani', 'Arial', sans-serif"
    },
    effects: 'neon, laser, grid, glitch'
  },
  RETRO_WAVE: {
    id: 'retro-wave',
    name: '复古霓虹',
    icon: '🌆',
    description: '80年代 synthwave 梦幻风格',
    colors: {
      primary: '#ff1493',
      secondary: '#9400d3',
      accent: '#00bfff',
      bg: '#1a0a2e',
      surface: '#2a1a3e',
      text: '#ffe0ff',
      border: '#3a2a4e'
    },
    fonts: {
      heading: "'Press Start 2P', 'Arial Black', sans-serif",
      body: "'Arial', sans-serif"
    },
    effects: 'vaporwave, neon-grid, sun, gradient'
  }
}

// 当前主题状态
const currentTheme = ref(THEMES.HEAVY_METAL)

// 从 localStorage 加载主题
const loadTheme = () => {
  try {
    const savedTheme = localStorage.getItem('buptBand-theme')
    if (savedTheme && THEMES[savedTheme.toUpperCase()]) {
      currentTheme.value = THEMES[savedTheme.toUpperCase()]
    }
  } catch (error) {
    console.warn('Failed to load theme:', error)
  }
}

// 保存主题到 localStorage
const saveTheme = (themeId) => {
  try {
    localStorage.setItem('buptBand-theme', themeId)
  } catch (error) {
    console.warn('Failed to save theme:', error)
  }
}

// 应用主题到文档
const applyTheme = (theme) => {
  const root = document.documentElement

  // 设置CSS变量
  root.style.setProperty('--theme-primary', theme.colors.primary)
  root.style.setProperty('--theme-secondary', theme.colors.secondary)
  root.style.setProperty('--theme-accent', theme.colors.accent)
  root.style.setProperty('--theme-bg', theme.colors.bg)
  root.style.setProperty('--theme-surface', theme.colors.surface)
  root.style.setProperty('--theme-text', theme.colors.text)
  root.style.setProperty('--theme-border', theme.colors.border)

  // 设置主题ID到body
  document.body.className = `theme-${theme.id}`
}

// 切换主题
const setTheme = (theme) => {
  currentTheme.value = theme
  saveTheme(theme.id)
  applyTheme(theme)
}

// 主题组合式函数
export function useTheme() {
  // 监听主题变化
  watch(currentTheme, (newTheme) => {
    applyTheme(newTheme)
  }, { immediate: true })

  return {
    currentTheme,
    themes: THEMES,
    setTheme,
    loadTheme
  }
}
