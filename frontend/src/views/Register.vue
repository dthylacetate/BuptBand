<template>
  <div class="register-page">
    <el-card class="box-card">
      <h2 style="text-align: center; color: #409EFF;">🔥 加入 RIGHTSPELL</h2>
      <p style="text-align: center; color: #909399; font-size: 0.9rem; margin-bottom: 20px;">
        仅支持北邮邮箱注册 (@bupt.edu.cn / @bupt.cn)
      </p>

      <el-form :model="regForm" label-position="top">
        <el-form-item label="乐手昵称">
          <el-input v-model="regForm.nickname" placeholder="想一个响亮的艺名" />
        </el-form-item>

        <el-form-item label="邮箱">
          <div style="display: flex; gap: 10px; width: 100%;">
            <el-input v-model="regForm.email" placeholder="xxx@bupt.edu.cn / @bupt.cn" />
            <el-button 
              :disabled="countdown > 0" 
              @click="sendCode"
              :type="countdown > 0 ? '' : 'primary'"
              plain
              style="width: 120px"
            >
              {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
            </el-button>
          </div>
        </el-form-item>

        <el-form-item label="6位验证码">
          <el-input v-model="regForm.code" placeholder="邮件里的 6 位数字" />
        </el-form-item>

        <el-form-item label="设置密码">
          <el-input v-model="regForm.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>

        <el-button type="primary" @click="handleRegister" style="width: 100%; margin-top: 10px; height: 40px;">
          开启音乐之旅
        </el-button>

        <div style="margin-top: 20px; text-align: center;">
          <el-link type="info" @click="$router.push('/login')">已有账号？返回登录</el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'

const router = useRouter()
const countdown = ref(0)
const regForm = ref({
  nickname: '',
  email: '',
  code: '',
  password: ''
})

// --- 发送验证码逻辑 (已修正为 POST) ---
const sendCode = async () => {
  const email = regForm.value.email.trim()
  
  const isBuptEmail = email.endsWith('@bupt.edu.cn') || email.endsWith('@bupt.cn')
  if (!isBuptEmail) {
    return ElMessage.warning('请输入正确的北邮校友邮箱哦 (@bupt.edu.cn 或 @bupt.cn)')
  }

  try {
    /** * 关键修改点：
     * 这里将 axios.get 改为 axios.post。
     * 即使后端是用 @RequestParam 接收的，axios.post 也可以通过 URL 参数传递。
     */
    await request.post(`/users/send-code?email=${email}`)
    
    ElMessage.success('验证码已发送至你的邮箱')
    
    countdown.value = 60
    const timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) clearInterval(timer)
    }, 1000)
  } catch (error) {
    //ElMessage.error(error.response?.data || '发送失败，请检查后端接口')
  }
}

// --- 提交注册逻辑 ---
const handleRegister = async () => {
  if (!regForm.value.nickname || !regForm.value.email || !regForm.value.code || !regForm.value.password) {
    return ElMessage.warning('请把信息填完整再出发哦')
  }

  try {
    await request.post('/users/register', regForm.value)
    ElMessage.success('注册成功！正在前往登录页...')
    setTimeout(() => {
      router.push('/login')
    }, 1500)
  } catch (error) {
    ElMessage.error(error.response?.data || '注册失败')
  }
}
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}
.box-card {
  width: 440px;
  border-radius: 12px;
}
</style>