<template>
  <div class="login-page">
    <el-card class="box-card">
      <h2>🎸 乐手登录</h2>
      <el-form label-position="top">
        <el-form-item label="昵称">
          <el-input v-model="loginForm.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-button type="primary" @click="handleLogin" style="width: 100%">登录</el-button>
      </el-form>
      <div style="margin-top: 15px; text-align: center;">
        <el-link type="info" @click="$router.push('/register')">没有账号？去注册</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loginForm = ref({
  nickname: '',
  password: ''
})

const handleLogin = async () => {
  try {
    // 调用你后端的登录接口
    const res = await axios.post('http://localhost:8080/api/users/login', loginForm.value)
    // 成功后会收到 Token
    const token = res.data
    // 存入本地
    localStorage.setItem('token', token)
    ElMessage.success('欢迎回来，乐手！')
    // 跳转到广场
    router.push('/')
  } catch (error) {
    // 这里会触发你后端写的 GlobalExceptionHandler 返回的中文报错
    ElMessage.error(error.response?.data || '登录失败')
  }
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}
.box-card {
  width: 380px;
}
</style>