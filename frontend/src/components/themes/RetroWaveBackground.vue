<template>
  <div class="retro-wave-background">
    <!-- 渐变太阳 -->
    <div class="retro-sun">
      <div class="sun-gradient"></div>
      <div class="sun-stripes"></div>
    </div>

    <!-- 透视网格 -->
    <div class="perspective-grid"></div>

    <!-- 星空 -->
    <div class="starfield"></div>

    <!-- 漂浮文字 -->
    <div class="vapor-text">
      VAPOR WAVE SYNTH RETRO
    </div>

    <!-- 漂浮星星 -->
    <div class="floating-stars">
      ✦✧★✦✧★
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.retro-wave-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
  background: linear-gradient(180deg, #1a0a2e 0%, #2a1a3e 50%, #1a0a2e 100%);
}

.retro-sun {
  position: absolute;
  top: -50px;
  left: 50%;
  transform: translateX(-50%);
  width: 200px;
  height: 200px;
}

.sun-gradient {
  position: absolute;
  width: 100%;
  height: 100%;
  background: linear-gradient(180deg, #ff1493 0%, #ff69b4 30%, #ffa500 60%, transparent 100%);
  border-radius: 50%;
  animation: sun-pulse 6s ease-in-out infinite;
}

.sun-stripes {
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

.perspective-grid {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 50%;
  perspective: 200px;
  overflow: hidden;
}

.perspective-grid::before {
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

.starfield {
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

.vapor-text {
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

.floating-stars {
  position: absolute;
  top: 30%;
  right: 10%;
  font-size: 40px;
  opacity: 0.04;
  animation: stars-drift 50s linear infinite;
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
</style>
