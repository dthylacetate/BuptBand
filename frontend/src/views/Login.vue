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
import request from '../api/request'

const router = useRouter()
const loginForm = ref({
  nickname: '',
  password: ''
})

const handleLogin = async () => {
  try {
    const res = await request.post('/users/login', loginForm.value)
    
    // 💡 调试秘籍：你可以先加一行打印，看看后端回来的到底是什么
    console.log('登录成功后的返回结果:', res)

    // 情况 A：如果后端直接回的就是 Token 字符串
    localStorage.setItem('token', res) 
    
    // 情况 B：如果后端回的是 JSON 对象，如 { "token": "xxxx" }
    // 则要改为：localStorage.setItem('token', res.token)

    ElMessage.success('欢迎回来！')
    router.push('/')
  } catch (err) {
    // 报错已被拦截器处理
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