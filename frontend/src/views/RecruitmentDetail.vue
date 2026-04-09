<template>
  <div class="detail-container" v-loading="loading">
    <el-page-header @back="router.back()" content="招募详情" class="page-header" />

    <el-card class="detail-card" v-if="post">
      <div class="detail-header">
        <el-tag :type="post.type === 'BAND_SEEKING_MEMBER' ? 'success' : 'warning'">
          {{ post.type === 'BAND_SEEKING_MEMBER' ? '乐队招人' : '个人求队' }}
        </el-tag>
        <h1>{{ post.title }}</h1>
      </div>

      <el-descriptions :column="2" border class="main-info">
        <el-descriptions-item label="招募/我的位置">
          <el-tag effect="dark">{{ post.position }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="音乐风格">{{ post.style }}</el-descriptions-item>
        <el-descriptions-item label="发布者">{{ post.publisherNickname }}</el-descriptions-item>
        <el-descriptions-item label="联系方式">
          <span class="contact-text">{{ post.contactInformation }}</span>
        </el-descriptions-item>
      </el-descriptions>

      <div class="section">
        <h3>🎸 技术要求/个人说明</h3>
        <p class="content-box">{{ post.requirements || '暂无详细要求' }}</p>
      </div>

      <div class="section">
        <h3>📝 详细介绍</h3>
        <p class="content-box detail-text">{{ post.detail }}</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../api/request'

const route = useRoute()
const router = useRouter()
const post = ref(null)
const loading = ref(false)

const fetchDetail = async () => {
  loading.value = true
  try {
    const res = await request.get(`/recruitments/${route.params.id}`)
    post.value = res
  } finally {
    loading.value = false
  }
}

onMounted(fetchDetail)
</script>

<style scoped>
.detail-container { max-width: 800px; margin: 30px auto; padding: 0 20px; }
.page-header { margin-bottom: 20px; }
.detail-card { border-radius: 16px; border: none; box-shadow: 0 4px 20px rgba(0,0,0,0.08); }
.detail-header { display: flex; align-items: center; gap: 15px; margin-bottom: 20px; }
.detail-header h1 { margin: 0; font-size: 1.6rem; color: #303133; }
.main-info { margin-bottom: 30px; }
.section { margin-top: 25px; }
.section h3 { border-left: 4px solid #409eff; padding-left: 10px; margin-bottom: 15px; }
.content-box { background: #f8f9fa; padding: 20px; border-radius: 8px; line-height: 1.6; color: #606266; }
.detail-text { white-space: pre-wrap; }
.contact-text { font-weight: bold; color: #f56c6c; font-size: 1.1rem; }
</style>