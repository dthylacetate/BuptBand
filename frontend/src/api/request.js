import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

// 1. 创建 axios 实例
const service = axios.create({
  // ⚡️ 核心修改：从 Vite 环境变量中读取地址
  // 运行 npm run dev 会读取 .env.development 中的配置
  // 运行 npm run build 会读取 .env.production 中的配置
  baseURL: import.meta.env.VITE_API_BASE_URL, 
  timeout: 15000 // 请求超时时间
})

// 2. 请求拦截器 (Request Interceptor)
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    console.log('请求拦截器 - 当前token:', token)
    console.log('请求拦截器 - 请求URL:', config.url)
    if (token && token !== 'undefined' && token !== 'null') {
      // 给每个请求都加上 Bearer Token，后端 Spring Security 才能识别
      config.headers['Authorization'] = `Bearer ${token}`
      console.log('请求拦截器 - 已添加Authorization头')
    } else {
      console.log('请求拦截器 - 没有有效token，跳过添加Authorization头')
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 3. 响应拦截器 (Response Interceptor)
service.interceptors.response.use(
  response => {
    // 拦截器直接返回 response.data，这样在组件里就不需要写 res.data 了
    return response.data
  },
  error => {
    console.log('拦截器捕获到的原始错误:', error);
    const { response } = error
    if (response) {
      // 身份过期处理 (401 是未授权，403 是拒绝访问，通常都是 Token 失效)
      if (response.status === 403 || response.status === 401) {
        ElMessage.error('身份过期，请重新登录')
        localStorage.removeItem('token') // 清理无效令牌
        router.push('/login')
      } else {
        // 弹出后端传回的错误信息 (例如“验证码错误”)
        ElMessage.error(response.data || '系统繁忙，请稍后再试')
      }
    } else {
      ElMessage.error('无法连接到服务器，请检查后端是否启动')
    }
    return Promise.reject(error)
  }
)

export default service