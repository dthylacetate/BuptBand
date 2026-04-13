<template>
  <div class="heavy-metal-background">
    <!-- 砖墙背景 -->
    <div class="brick-wall">
      <div class="brick-pattern"></div>
    </div>

    <!-- 火焰效果 -->
    <div class="flame-effects">
      <div class="flame-gradient"></div>
      <div class="embers"></div>
    </div>

    <!-- 漂浮文字 -->
    <div class="floating-text">
      RIGHTSPELL METAL CHAOS
    </div>

    <!-- 漂浮图案 -->
    <div class="floating-patterns">
      ☠️💀🔥🤘⚡
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.heavy-metal-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.brick-wall {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.brick-pattern {
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

.flame-effects {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.flame-gradient {
  position: absolute;
  width: 100%;
  height: 100%;
  background: radial-gradient(ellipse at 20% 30%, rgba(255, 69, 0, 0.05) 0%, transparent 50%),
              radial-gradient(ellipse at 80% 70%, rgba(220, 20, 60, 0.05) 0%, transparent 50%);
}

.embers {
  position: absolute;
  width: 100%;
  height: 100%;
  background-image:
    radial-gradient(circle at 30% 40%, rgba(255, 69, 0, 0.03) 0%, transparent 2%),
    radial-gradient(circle at 70% 60%, rgba(220, 20, 60, 0.03) 0%, transparent 2%);
  animation: ember-float 20s linear infinite;
}

.floating-text {
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

.floating-patterns {
  position: absolute;
  top: 20%;
  right: 5%;
  font-size: 60px;
  opacity: 0.02;
  animation: pattern-drift 40s linear infinite;
}

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
</style>
