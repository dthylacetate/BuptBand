<template>
  <div class="show-detail-container" v-loading="loading">
    <el-page-header @back="router.back()" content="演出详情" class="page-header" />

    <el-row :gutter="30">
      <el-col :md="16" :xs="24">
        <el-card class="info-card" v-if="show">
          <div class="show-main-header">
            <h1 class="title">{{ show.title }}</h1>
            <el-tag type="danger" effect="dark" class="status-tag">即将开演</el-tag>
          </div>

          <div class="meta-list">
            <div class="meta-item">
              <el-icon class="icon"><Calendar /></el-icon>
              <div class="text">
                <div class="label">演出日期</div>
                <div class="value">{{ formatDate(show.showTime) }}</div>
              </div>
            </div>

            <div class="meta-item">
              <el-icon class="icon"><Clock /></el-icon>
              <div class="text">
                <div class="label">入场时间</div>
                <div class="value">{{ formatTime(show.showTime) }}</div>
              </div>
            </div>

            <div class="meta-item">
              <el-icon class="icon"><Location /></el-icon>
              <div class="text">
                <div class="label">演出地点</div>
                <div class="value">{{ show.venue }}</div>
              </div>
            </div>
          </div>

          <el-divider>演出介绍</el-divider>
          
          <div class="content-body">
            {{ show.content || '暂无详细介绍，现场见分晓！' }}
          </div>
        </el-card>
      </el-col>

      <el-col :md="8" :xs="24">
        <el-card class="publisher-card" v-if="show">
          <template #header>
            <span class="pub-header">发布者 / 主办方</span>
          </template>
          <div class="publisher-box">
            <el-avatar 
              :size="60" 
              :src="serverRoot + show.publisherAvatar" 
              icon="User"
              class="pub-avatar"
            />
            <h3 class="pub-nickname">{{ show.publisherNickname }}</h3>
            <el-button 
              type="primary" 
              plain 
              size="small" 
              @click="router.push(`/user/${show.publisherNickname}`)"
            >
              查看主页
            </el-button>
          </div>
        </el-card>

        <div class="safety-tips">
          <h4>💡 温馨提示</h4>
          <p>1. 请提前准时入场，以免错过精彩开场。</p>
          <p>2. 现场注意财物安全，享受纯粹的现场魅力。</p>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Calendar, Clock, Location } from '@element-plus/icons-vue'
import request from '../api/request'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const show = ref(null)

const apiUrl = import.meta.env.VITE_API_BASE_URL || ''
const serverRoot = apiUrl.replace('/api', '')

const loadShowDetail = async () => {
  loading.value = true
  try {
    const res = await request.get(`/shows/${route.params.id}`)
    show.value = res
  } finally {
    loading.value = false
  }
}

// 时间格式化工具
const formatDate = (dateStr) => {
  const d = new Date(dateStr)
  return d.toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' })
}

const formatTime = (dateStr) => {
  const d = new Date(dateStr)
  const weeks = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  const time = d.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit', hour12: false })
  return `${weeks[d.getDay()]} ${time}`
}

onMounted(loadShowDetail)
</script>

<style scoped>
.show-detail-container { max-width: 1000px; margin: 20px auto; padding: 0 20px; }
.page-header { margin-bottom: 30px; }

/* 主卡片样式 */
.info-card { border-radius: 16px; border: none; padding: 10px; }
.show-main-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 30px; }
.title { margin: 0; font-size: 2rem; color: #303133; flex: 1; }
.status-tag { margin-left: 20px; }

/* 关键信息排版 */
.meta-list { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-bottom: 30px; }
.meta-item { display: flex; align-items: center; gap: 15px; }
.icon { font-size: 24px; color: #f56c6c; background: #fef0f0; padding: 10px; border-radius: 12px; }
.label { font-size: 12px; color: #909399; margin-bottom: 4px; }
.value { font-size: 16px; font-weight: bold; color: #303133; }

.content-body { line-height: 2; color: #606266; font-size: 16px; white-space: pre-wrap; min-height: 200px; }

/* 右侧栏样式 */
.publisher-card { border-radius: 16px; border: none; margin-bottom: 20px; text-align: center; }
.pub-header { font-weight: bold; color: #606266; }
.publisher-box { padding: 10px 0; }
.pub-avatar { margin-bottom: 15px; border: 2px solid #fef0f0; }
.pub-nickname { margin: 0 0 15px; font-size: 1.1rem; }

.safety-tips { background: #fdf6ec; border-radius: 12px; padding: 15px; border: 1px solid #faecd8; }
.safety-tips h4 { margin: 0 0 10px; color: #e6a23c; }
.safety-tips p { font-size: 13px; color: #8d9095; margin: 5px 0; line-height: 1.6; }

@media (max-width: 768px) {
  .title { font-size: 1.5rem; }
  .meta-list { grid-template-columns: 1fr; }
}
</style>