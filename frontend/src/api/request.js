// src/api/request.js
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

// 1. 创建 axios 实例
const service = axios.create({
  // 这里的地址以后上线了直接改这一行就行
  baseURL: 'http://localhost:8080/api', 
  timeout: 5000 // 请求超时时间
})

// 2. 请求拦截器 (Request Interceptor)
// 作用：在请求发出去之前，自动把 Token 塞进 Header
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      // 给每个请求都加上 Bearer Token
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 3. 响应拦截器 (Response Interceptor)
// 作用：统一处理后端返回的错误码
service.interceptors.response.use(
  response => {
    // 如果返回的是成功，直接把数据给到页面
    return response.data
  },
  error => {
    // 统一处理报错
    const { response } = error
    if (response) {
      if (response.status === 403 || response.status === 401) {
        ElMessage.error('身份过期，请重新登录')
        localStorage.removeItem('token') // 清理坏掉的令牌
        router.push('/login')
      } else {
        // 弹出后端传回来的中文错误提示（比如“验证码错误”）
        ElMessage.error(response.data || '系统繁忙，请稍后再试')
      }
    } else {
      ElMessage.error('无法连接到服务器，请检查网络或后端是否启动')
    }
    return Promise.reject(error)
  }
)

export default service