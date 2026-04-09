<template>
  <div class="public-profile">
    <el-page-header @back="router.back()" :content="`${nickname} 的名片`" />

    <el-card class="user-card" v-if="user">
      <div class="header">
        <el-avatar :size="100" :src="serverRoot + user.avatarUrl" icon="User" />
        <div class="info">
          <h2>{{ user.nickname }}</h2>
          <div class="tags">
            <el-tag effect="dark">{{ user.instrument || '神秘乐手' }}</el-tag>
            <el-tag type="info">琴龄: {{ user.playingYears || 0 }}年</el-tag>
          </div>
        </div>
      </div>
      <el-divider />
      <div class="bio-section">
        <h4>关于我</h4>
        <p>{{ user.bio || '这个乐手很懒，什么都没写~' }}</p>
      </div>
    </el-card>

    <h3 class="section-title">🎸 他的发布</h3>
    <el-row :gutter="20">
      <el-col :span="12" v-for="item in userPosts" :key="item.id">
        <el-card class="post-item" shadow="hover" @click="router.push(`/recruitments/${item.id}`)">
          <div class="post-header">
            <strong>{{ item.title }}</strong>
            <el-tag size="small">{{ item.position }}</el-tag>
          </div>
          <p class="post-detail">{{ item.detail }}</p>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../api/request'

const route = useRoute()
const router = useRouter()
const nickname = route.params.nickname
const user = ref(null)
const userPosts = ref([])

const serverRoot = import.meta.env.VITE_API_BASE_URL.replace('/api', '')

const loadData = async () => {
  try {
    // 1. 获取公开资料
    user.value = await request.get(`/users/public/${nickname}`)
    // 2. 复用之前的接口获取该乐手的帖子
    // 注意：你之前写的 /recruitments/my 是查当前登录人的，
    // 所以这里需要后端支持通过 nickname 查所有人的接口
    userPosts.value = await request.get(`/recruitments/user/${nickname}`)
  } catch (err) {
    console.error(err)
  }
}

onMounted(loadData)
</script>

<style scoped>
.public-profile { max-width: 800px; margin: 20px auto; padding: 0 20px; }
.user-card { border-radius: 15px; margin-top: 20px; border: none; }
.header { display: flex; align-items: center; gap: 30px; }
.info h2 { margin: 0 0 10px; font-size: 1.8rem; }
.tags { display: flex; gap: 10px; }
.bio-section h4 { color: #909399; margin-bottom: 10px; }
.section-title { margin: 30px 0 20px; border-left: 4px solid #409eff; padding-left: 10px; }
.post-item { cursor: pointer; margin-bottom: 15px; border-radius: 10px; }
.post-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.post-detail { font-size: 13px; color: #666; overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; }
</style>