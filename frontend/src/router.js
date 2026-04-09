import { createRouter, createWebHistory } from 'vue-router'
import Home from './views/Home.vue'
import Login from './views/Login.vue'
import Register from './views/Register.vue'
import Profile from './views/Profile.vue' // 1. 导入新创建的组件

const routes = [
  { 
    path: '/', 
    component: Home,
    meta: { requiresAuth: true } // 标记：这个页面需要登录才能看
  },
  { 
    path: '/login', 
    component: Login 
  },
  { 
    path: '/register', 
    component: Register 
  },
  { 
    path: '/profile', 
    component: Profile,
    meta: { requiresAuth: true } // 标记：个人中心也必须登录
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 2. 路由守卫：像个“保安”一样检查 Token
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  // 如果要去的地方需要登录 (requiresAuth) 且没有 Token
  if (to.meta.requiresAuth && !token) {
    // 没登录？滚去登录页！
    next('/login')
  } else {
    // 有 Token 或者是去登录/注册页？放行！
    next()
  }
})

export default router