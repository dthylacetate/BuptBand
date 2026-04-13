<template>
  <div class="cyberpunk-background">
    <!-- 网格背景 -->
    <div class="cyber-grid"></div>

    <!-- 扫描线 -->
    <div class="scanlines"></div>

    <!-- 霓虹光晕 -->
    <div class="neon-glows"></div>

    <!-- 故障文字 -->
    <div class="glitch-text">
      CYBER PUNK NEON LASER
    </div>

    <!-- 漂浮符号 -->
    <div class="floating-symbols">
      💠⚡🌃⚡💠
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.cyberpunk-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.cyber-grid {
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
  animation: scan-move 8s linear infinite;
}

.neon-glows {
  position: absolute;
  width: 100%;
  height: 100%;
  background:
    radial-gradient(circle at 20% 30%, rgba(0, 255, 255, 0.05) 0%, transparent 50%),
    radial-gradient(circle at 80% 70%, rgba(255, 20, 147, 0.05) 0%, transparent 50%);
  animation: glow-pulse 4s ease-in-out infinite;
}

.glitch-text {
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

.floating-symbols {
  position: absolute;
  bottom: 20%;
  right: 5%;
  font-size: 50px;
  opacity: 0.03;
  animation: symbol-drift 30s linear infinite;
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
</style>
