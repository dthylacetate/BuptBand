<template>
  <div class="metal-login-page">
    <!-- 🔥 BACKGROUND EFFECTS 🔥 -->
    <div class="bg-effects">
      <div class="flame-overlay"></div>
      <div class="noise-overlay"></div>
    </div>

    <!-- 🎸 CONCERT TICKET LOGIN CARD 🎸 -->
    <div class="ticket-container">
      <div class="ticket-main">
        <!-- TORN EDGES -->
        <div class="torn-edge-left"></div>
        <div class="torn-edge-right"></div>

        <!-- TICKET HEADER -->
        <div class="ticket-header">
          <div class="skull-icon">☠️</div>
          <h1 class="flame-title">RIGHTSPELL</h1>
          <p class="subtitle">🎸 乐手登录 🎸</p>
          <div class="barcode">
            <div class="barcode-line"></div>
            <div class="barcode-line"></div>
            <div class="barcode-line"></div>
            <div class="barcode-line"></div>
            <div class="barcode-line"></div>
          </div>
        </div>

        <!-- LOGIN FORM -->
        <el-form label-position="top" class="metal-form">
          <el-form-item label="昵称">
            <el-input
              v-model="loginForm.nickname"
              placeholder="请输入昵称"
              class="metal-input"
            />
          </el-form-item>

          <el-form-item label="密码">
            <el-input
              v-model="loginForm.password"
              type="password"
              show-password
              placeholder="请输入密码"
              class="metal-input"
            />
          </el-form-item>

          <el-button
            type="primary"
            @click="handleLogin"
            class="metal-login-button"
            size="large"
          >
            🔥 登录 🔥
          </el-button>
        </el-form>

        <!-- TICKET FOOTER -->
        <div class="ticket-footer">
          <el-link type="info" @click="$router.push('/register')" class="register-link">
            没有账号？去注册 →
          </el-link>
        </div>

        <!-- DECORATIVE CHAINS -->
        <div class="chain-decoration">
          <div class="chain-link"></div>
          <div class="chain-link"></div>
          <div class="chain-link"></div>
        </div>
      </div>

      <!-- GLOW EFFECTS -->
      <div class="glow-spot"></div>
      <div class="glow-spot glow-spot-2"></div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const router = useRouter()
const loginForm = ref({
  nickname: '',
  password: ''
})

const handleLogin = async () => {
  try {
    const res = await request.post('/users/login', loginForm.value)

    console.log('=== 登录调试信息 ===')
    console.log('1. 登录成功，返回的token:', res)
    console.log('2. Token类型:', typeof res)
    console.log('3. Token长度:', res?.length)

    localStorage.setItem('token', res)

    const savedToken = localStorage.getItem('token')
    console.log('4. 保存后的token:', savedToken)
    console.log('5. 保存后token类型:', typeof savedToken)
    console.log('==================')

    ElMessage.success('欢迎回来！')
    router.push('/')
  } catch (err) {
    console.error('登录失败:', err)
    // 报错已被拦截器处理
  }
}
</script>

<style scoped>
/* 🔥 MAIN PAGE CONTAINER 🔥 */
.metal-login-page {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 50%, #0a0a0a 100%);
  position: relative;
  overflow: hidden;
}

/* BACKGROUND EFFECTS */
.bg-effects {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.flame-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(ellipse at center, rgba(255, 69, 0, 0.1) 0%, transparent 70%);
  animation: flicker 3s ease-in-out infinite;
}

.noise-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 400 400' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
  opacity: 0.05;
}

/* 🎸 CONCERT TICKET CONTAINER 🎸 */
.ticket-container {
  position: relative;
  z-index: 1;
  perspective: 1000px;
}

.ticket-main {
  position: relative;
  width: 420px;
  background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 50%, #1a1a1a 100%);
  border: 3px solid #ff4500;
  border-radius: 0;
  padding: 40px 30px;
  box-shadow:
    0 0 30px rgba(255, 69, 0, 0.3),
    0 0 60px rgba(220, 20, 60, 0.2),
    0 10px 40px rgba(0, 0, 0, 0.8),
    inset 0 1px 0 rgba(255, 255, 255, 0.1);
  transform: rotate(-2deg);
  transition: all 0.3s ease;
  overflow: hidden;
}

.ticket-main:hover {
  transform: rotate(0deg) scale(1.02);
  box-shadow:
    0 0 40px rgba(255, 69, 0, 0.5),
    0 0 80px rgba(220, 20, 60, 0.3),
    0 15px 50px rgba(0, 0, 0, 0.9),
    inset 0 1px 0 rgba(255, 255, 255, 0.15);
}

/* TORN EDGES */
.torn-edge-left,
.torn-edge-right {
  position: absolute;
  top: 0;
  bottom: 0;
  width: 20px;
  background: #0a0a0a;
  z-index: 2;
}

.torn-edge-left {
  left: -10px;
  clip-path: polygon(
    0% 0%,
    100% 5%,
    80% 15%,
    100% 25%,
    70% 35%,
    100% 45%,
    80% 55%,
    100% 65%,
    70% 75%,
    100% 85%,
    80% 95%,
    100% 100%,
    0% 95%,
    20% 85%,
    0% 75%,
    30% 65%,
    0% 55%,
    20% 45%,
    0% 35%,
    30% 25%,
    0% 15%,
    20% 5%
  );
}

.torn-edge-right {
  right: -10px;
  clip-path: polygon(
    100% 0%,
    0% 5%,
    20% 15%,
    0% 25%,
    30% 35%,
    0% 45%,
    20% 55%,
    0% 65%,
    30% 75%,
    0% 85%,
    20% 95%,
    0% 100%,
    100% 95%,
    80% 85%,
    100% 75%,
    70% 65%,
    100% 55%,
    80% 45%,
    100% 35%,
    70% 25%,
    100% 15%,
    80% 5%
  );
}

/* TICKET HEADER */
.ticket-header {
  text-align: center;
  margin-bottom: 30px;
  position: relative;
}

.skull-icon {
  font-size: 48px;
  margin-bottom: 10px;
  animation: pulse-glow 2s ease-in-out infinite;
}

.flame-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 42px;
  font-weight: 900;
  letter-spacing: 2px;
  background: linear-gradient(45deg, #ff4500 0%, #dc143c 50%, #ffa500 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 0 30px rgba(255, 69, 0, 0.5);
  margin: 0;
  text-transform: uppercase;
  animation: flicker 3s ease-in-out infinite;
}

.subtitle {
  font-family: 'Arial Black', sans-serif;
  font-size: 16px;
  color: #8c8c8c;
  margin: 10px 0 20px;
  text-transform: uppercase;
  letter-spacing: 3px;
}

.barcode {
  display: flex;
  justify-content: center;
  gap: 4px;
  margin-top: 15px;
}

.barcode-line {
  width: 3px;
  height: 30px;
  background: linear-gradient(180deg, #ff4500 0%, #dc143c 100%);
  animation: barcode-flicker 2s ease-in-out infinite;
}

.barcode-line:nth-child(2) { animation-delay: 0.2s; }
.barcode-line:nth-child(3) { animation-delay: 0.4s; }
.barcode-line:nth-child(4) { animation-delay: 0.6s; }
.barcode-line:nth-child(5) { animation-delay: 0.8s; }

@keyframes barcode-flicker {
  0%, 100% { opacity: 1; height: 30px; }
  50% { opacity: 0.7; height: 25px; }
}

/* METAL FORM STYLES */
.metal-form {
  margin: 30px 0;
}

.metal-form :deep(.el-form-item__label) {
  color: #d4d4d4 !important;
  font-family: 'Arial Black', sans-serif;
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.metal-input :deep(.el-input__wrapper) {
  background: #0a0a0a !important;
  border: 2px solid #2d2d2d !important;
  box-shadow: none !important;
  transition: all 0.3s ease !important;
}

.metal-input :deep(.el-input__wrapper):hover {
  border-color: #ff4500 !important;
}

.metal-input :deep(.el-input__wrapper.is-focus) {
  border-color: #ff4500 !important;
  box-shadow: 0 0 15px rgba(255, 69, 0, 0.5), 0 0 30px rgba(220, 20, 60, 0.3) !important;
}

.metal-input :deep(.el-input__inner) {
  color: #d4d4d4 !important;
  font-family: 'Arial', sans-serif;
  font-size: 16px;
}

.metal-input :deep(.el-input__inner)::placeholder {
  color: #8c8c8c !important;
  opacity: 0.5;
}

/* METAL LOGIN BUTTON */
.metal-login-button {
  width: 100%;
  margin-top: 20px;
  height: 50px !important;
  background: linear-gradient(135deg, #ff4500 0%, #dc143c 50%, #ff4500 100%) !important;
  border: none !important;
  color: #ffffff !important;
  font-family: 'Impact', 'Arial Black', sans-serif !important;
  font-size: 20px !important;
  font-weight: 900 !important;
  text-transform: uppercase !important;
  letter-spacing: 2px !important;
  box-shadow:
    0 4px 6px rgba(0, 0, 0, 0.5),
    0 0 20px rgba(255, 69, 0, 0.5),
    inset 0 2px 4px rgba(255, 255, 255, 0.2) !important;
  transition: all 0.3s ease !important;
  position: relative;
  overflow: hidden;
}

.metal-login-button::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
    45deg,
    transparent 30%,
    rgba(255, 255, 255, 0.3) 50%,
    transparent 70%
  );
  transform: rotate(45deg);
  animation: button-shine 3s ease-in-out infinite;
}

@keyframes button-shine {
  0% { transform: translateX(-100%) rotate(45deg); }
  100% { transform: translateX(100%) rotate(45deg); }
}

.metal-login-button:hover {
  background: linear-gradient(135deg, #ffa500 0%, #ff4500 50%, #ffa500 100%) !important;
  transform: scale(1.05);
  box-shadow:
    0 6px 12px rgba(0, 0, 0, 0.7),
    0 0 40px rgba(255, 69, 0, 0.8),
    0 0 60px rgba(220, 20, 60, 0.5),
    inset 0 2px 4px rgba(255, 255, 255, 0.3) !important;
}

.metal-login-button:active {
  transform: scale(0.98);
}

/* TICKET FOOTER */
.ticket-footer {
  text-align: center;
  margin-top: 25px;
  padding-top: 20px;
  border-top: 2px solid #2d2d2d;
}

.register-link {
  color: #8c8c8c !important;
  font-family: 'Arial Black', sans-serif;
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 1px;
  transition: all 0.3s ease;
}

.register-link:hover {
  color: #ff4500 !important;
  text-shadow: 0 0 10px rgba(255, 69, 0, 0.5);
}

/* CHAIN DECORATION */
.chain-decoration {
  position: absolute;
  bottom: -30px;
  right: -30px;
  display: flex;
  gap: 5px;
  opacity: 0.3;
  transform: rotate(45deg);
}

.chain-link {
  width: 15px;
  height: 15px;
  border: 3px solid #ff4500;
  border-radius: 50%;
}

/* GLOW SPOTS */
.glow-spot {
  position: absolute;
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(255, 69, 0, 0.15) 0%, transparent 70%);
  border-radius: 50%;
  pointer-events: none;
  z-index: 0;
}

.glow-spot {
  top: -100px;
  left: -100px;
}

.glow-spot-2 {
  bottom: -100px;
  right: -100px;
  background: radial-gradient(circle, rgba(220, 20, 60, 0.1) 0%, transparent 70%);
}

/* ANIMATIONS */
@keyframes flicker {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.95; }
}

@keyframes pulse-glow {
  0%, 100% {
    transform: scale(1);
    filter: drop-shadow(0 0 10px rgba(255, 69, 0, 0.5));
  }
  50% {
    transform: scale(1.05);
    filter: drop-shadow(0 0 20px rgba(255, 69, 0, 0.8));
  }
}

/* RESPONSIVE */
@media (max-width: 480px) {
  .ticket-main {
    width: 90%;
    padding: 30px 20px;
  }

  .flame-title {
    font-size: 32px;
  }

  .skull-icon {
    font-size: 36px;
  }
}
</style>
