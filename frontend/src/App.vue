<template>
  <div class="global-background-container" :class="`theme-${currentTheme.id}`">
    <!-- 🎨 THEME-BASED BACKGROUND LAYERS 🎨 -->

    <!-- 重金属主题背景 -->
    <div v-if="currentTheme.id === 'heavy-metal'" class="theme-background-layers heavy-metal-background">
      <div class="brick-wall">
        <div class="brick-pattern"></div>
      </div>
      <div class="flame-effects">
        <div class="flame-gradient"></div>
        <div class="embers"></div>
      </div>
      <div class="floating-text">RIGHTSPELL METAL CHAOS</div>
      <div class="floating-patterns">☠️💀🔥🤘⚡</div>
    </div>

    <!-- 赛博朋克主题背景 -->
    <div v-else-if="currentTheme.id === 'cyberpunk'" class="theme-background-layers cyberpunk-background">
      <div class="cyber-grid"></div>
      <div class="scanlines"></div>
      <div class="neon-glows"></div>
      <div class="glitch-text">CYBER PUNK NEON LASER</div>
      <div class="floating-symbols">💠⚡🌃⚡💠</div>
    </div>

    <!-- 复古霓虹主题背景 -->
    <div v-else-if="currentTheme.id === 'retro-wave'" class="theme-background-layers retro-wave-background">
      <div class="retro-sun">
        <div class="sun-gradient"></div>
        <div class="sun-stripes"></div>
      </div>
      <div class="perspective-grid"></div>
      <div class="starfield"></div>
      <div class="vapor-text">VAPOR WAVE SYNTH RETRO</div>
      <div class="floating-stars">✦✧★✦✧★</div>
    </div>

    <!-- 主内容 -->
    <div class="content-layer">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useTheme } from './composables/useTheme'

const { currentTheme, loadTheme } = useTheme()

// 初始化时加载主题
onMounted(() => {
  loadTheme()
})
</script>

<style>
/* === GLOBAL THEME VARIABLES === */
:root {
  --theme-primary: #ff4500;
  --theme-secondary: #dc143c;
  --theme-accent: #ffa500;
  --theme-bg: #0a0a0a;
  --theme-surface: #1a1a1a;
  --theme-text: #d4d4d4;
  --theme-border: #2d2d2d;
}

/* === GLOBAL BACKGROUND CONTAINER === */
.global-background-container {
  position: relative;
  min-height: 100vh;
  width: 100%;
  overflow-x: hidden;
  transition: background 0.5s ease;
}

.content-layer {
  position: relative;
  z-index: 10;
  min-height: 100vh;
}

/* === THEME BACKGROUND LAYERS === */
.theme-background-layers {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

/* === HEAVY METAL THEME STYLES === */
.heavy-metal-background .brick-wall {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.heavy-metal-background .brick-pattern {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image:
    linear-gradient(335deg, rgba(255,255,255,0.02) 23px, transparent 23px),
    linear-gradient(155deg, rgba(255,255,255,0.02) 23px, transparent 23px),
    linear-gradient(335deg, rgba(255,255,255,0.02) 23px, transparent 23px),
    linear-gradient(155deg, rgba(255,255,255,0.02) 23px, transparent 23px);
  background-size: 58px 58px;
}

.heavy-metal-background .flame-effects {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.heavy-metal-background .flame-gradient {
  position: absolute;
  width: 100%;
  height: 100%;
  background: radial-gradient(ellipse at 20% 30%, rgba(255, 69, 0, 0.05) 0%, transparent 50%),
              radial-gradient(ellipse at 80% 70%, rgba(220, 20, 60, 0.05) 0%, transparent 50%);
}

.heavy-metal-background .embers {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image:
    radial-gradient(circle at 30% 40%, rgba(255, 69, 0, 0.03) 0%, transparent 2%),
    radial-gradient(circle at 70% 60%, rgba(220, 20, 60, 0.03) 0%, transparent 2%);
  animation: ember-float 20s linear infinite;
}

.heavy-metal-background .floating-text {
  position: absolute;
  top: 10%;
  left: -100px;
  width: 120%;
  height: 200px;
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 120px;
  font-weight: 900;
  color: rgba(255, 69, 0, 0.03);
  text-transform: uppercase;
  letter-spacing: 20px;
  transform: rotate(-5deg);
  animation: text-float 30s ease-in-out infinite;
  white-space: nowrap;
}

.heavy-metal-background .floating-patterns {
  position: absolute;
  top: 20%;
  right: 5%;
  font-size: 60px;
  opacity: 0.02;
  animation: pattern-drift 40s linear infinite;
}

/* === CYBERPUNK THEME STYLES === */
.cyberpunk-background .cyber-grid {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image:
    linear-gradient(rgba(0, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 30px 30px;
  animation: grid-glitch 10s linear infinite;
}

.cyberpunk-background .scanlines {
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
  animation: scan-move 8s linear infinite;
}

.cyberpunk-background .neon-glows {
  position: absolute;
  width: 100%;
  height: 100%;
  background:
    radial-gradient(circle at 20% 30%, rgba(0, 255, 255, 0.05) 0%, transparent 50%),
    radial-gradient(circle at 80% 70%, rgba(255, 20, 147, 0.05) 0%, transparent 50%);
  animation: glow-pulse 4s ease-in-out infinite;
}

.cyberpunk-background .glitch-text {
  position: absolute;
  top: 15%;
  left: -50px;
  width: 110%;
  font-family: 'Courier New', monospace;
  font-size: 100px;
  font-weight: bold;
  color: rgba(0, 255, 255, 0.04);
  text-transform: uppercase;
  letter-spacing: 10px;
  animation: glitch-anim 5s ease-in-out infinite;
  white-space: nowrap;
}

.cyberpunk-background .floating-symbols {
  position: absolute;
  bottom: 20%;
  right: 5%;
  font-size: 50px;
  opacity: 0.03;
  animation: symbol-drift 30s linear infinite;
}

/* === RETRO WAVE THEME STYLES === */
.retro-wave-background {
  background: linear-gradient(180deg, #1a0a2e 0%, #2a1a3e 50%, #1a0a2e 100%);
}

.retro-wave-background .retro-sun {
  position: absolute;
  top: -50px;
  left: 50%;
  transform: translateX(-50%);
  width: 200px;
  height: 200px;
}

.retro-wave-background .sun-gradient {
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(180deg, #ff1493 0%, #ff69b4 30%, #ffa500 60%, transparent 100%);
  border-radius: 50%;
  animation: sun-pulse 6s ease-in-out infinite;
}

.retro-wave-background .sun-stripes {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 60%;
  background: repeating-linear-gradient(
    0deg,
    transparent,
    transparent 8px,
    rgba(0, 0, 0, 0.3) 8px,
    rgba(0, 0, 0, 0.3) 16px
  );
}

.retro-wave-background .perspective-grid {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 50%;
  perspective: 200px;
  overflow: hidden;
}

.retro-wave-background .perspective-grid::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: -50%;
  width: 200%;
  height: 100%;
  background-image:
    linear-gradient(rgba(255, 20, 147, 0.2) 1px, transparent 1px),
    linear-gradient(90deg, rgba(148, 0, 211, 0.2) 1px, transparent 1px);
  background-size: 40px 40px;
  transform: rotateX(60deg);
  transform-origin: center bottom;
  animation: grid-scroll 20s linear infinite;
}

.retro-wave-background .starfield {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image:
    radial-gradient(2px 2px at 20px 30px, #fff, transparent),
    radial-gradient(2px 2px at 40px 70px, rgba(255, 255, 255, 0.8), transparent),
    radial-gradient(1px 1px at 90px 40px, #fff, transparent),
    radial-gradient(2px 2px at 160px 120px, rgba(255, 255, 255, 0.9), transparent);
  background-size: 200px 200px;
  animation: star-twinkle 4s ease-in-out infinite;
}

.retro-wave-background .vapor-text {
  position: absolute;
  top: 25%;
  left: 0;
  width: 100%;
  font-family: 'Arial Black', sans-serif;
  font-size: 80px;
  font-weight: 900;
  color: rgba(255, 20, 147, 0.06);
  text-transform: uppercase;
  letter-spacing: 15px;
  text-align: center;
  animation: vapor-shift 20s ease-in-out infinite;
}

.retro-wave-background .floating-stars {
  position: absolute;
  top: 30%;
  right: 10%;
  font-size: 40px;
  opacity: 0.04;
  animation: stars-drift 50s linear infinite;
}

/* === ANIMATIONS === */
@keyframes ember-float {
  0% { transform: translateY(0); }
  100% { transform: translateY(-100px); }
}

@keyframes text-float {
  0%, 100% { transform: rotate(-5deg) translateY(0); }
  50% { transform: rotate(-3deg) translateY(-50px); }
}

@keyframes pattern-drift {
  0% { transform: translateX(-100px); }
  100% { transform: translateX(100px); }
}

@keyframes grid-glitch {
  0%, 90%, 100% { transform: translate(0); }
  92% { transform: translate(2px, 1px); }
  94% { transform: translate(-2px, -1px); }
  96% { transform: translate(1px, 2px); }
  98% { transform: translate(-1px, -2px); }
}

@keyframes scan-move {
  0% { transform: translateY(0); }
  100% { transform: translateY(50px); }
}

@keyframes glow-pulse {
  0%, 100% { opacity: 0.5; }
  50% { opacity: 1; }
}

@keyframes glitch-anim {
  0%, 90%, 100% { transform: translate(0); }
  92% { transform: translate(-2px, 1px); }
  94% { transform: translate(2px, -1px); }
  96% { transform: translate(-1px, 2px); }
  98% { transform: translate(1px, -2px); }
}

@keyframes symbol-drift {
  0% { transform: translateX(-100px); }
  100% { transform: translateX(100px); }
}

@keyframes sun-pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.1); }
}

@keyframes grid-scroll {
  0% { transform: rotateX(60deg) translateY(0); }
  100% { transform: rotateX(60deg) translateY(40px); }
}

@keyframes star-twinkle {
  0%, 100% { opacity: 0.6; }
  50% { opacity: 1; }
}

@keyframes vapor-shift {
  0%, 100% { transform: translateY(0) scale(1); filter: hue-rotate(0deg); }
  50% { transform: translateY(-30px) scale(1.05); filter: hue-rotate(30deg); }
}

@keyframes stars-drift {
  0% { transform: translateX(100px); }
  100% { transform: translateX(-100px); }
}

/* === BODY BASE STYLES === */
body {
  margin: 0;
  font-family: 'Arial Black', 'Impact', sans-serif;
  background: var(--theme-bg);
  color: var(--theme-text);
  overflow-x: hidden;
}

/* 全局链接样式 */
a {
  color: var(--theme-primary);
  text-decoration: none;
  transition: all 0.3s ease;
}

a:hover {
  color: var(--theme-secondary);
  text-shadow: 0 0 10px var(--theme-primary);
}
</style>
