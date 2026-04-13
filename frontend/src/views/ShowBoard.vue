<template>
  <div :class="['show-container', `theme-${currentTheme.id}`]">
    <!-- 页面头部 -->
    <div :class="['page-header', `header-${currentTheme.id}`]">
      <el-page-header @back="router.push('/')" content="演出公告">
        <template #title>
          <span class="back-link">← 回广场</span>
        </template>
      </el-page-header>
    </div>

    <!-- 主内容区 -->
    <div :class="['show-content', `content-${currentTheme.id}`]">
      <!-- 工具栏 - 根据主题显示 -->
      <div v-if="currentTheme.id === 'heavy-metal'" class="toolbar-wrapper">
        <HeavyMetalToolbar button-text="🔥 发布演出预报 🔥" @add-click="showAddDialog = true" />
      </div>
      <div v-else-if="currentTheme.id === 'cyberpunk'" class="toolbar-wrapper">
        <CyberpunkToolbar button-text="⚡ 发布演出预报 ⚡" @add-click="showAddDialog = true" />
      </div>
      <div v-else-if="currentTheme.id === 'retro-wave'" class="toolbar-wrapper">
        <RetroWaveToolbar button-text="✨ 发布演出预报 ✨" @add-click="showAddDialog = true" />
      </div>

      <!-- 演出列表 -->
      <div v-loading="loading" class="shows-grid">
        <div v-if="shows.length > 0">
          <el-row :gutter="30">
            <el-col v-for="item in shows" :key="item.id" :xs="24" :sm="12" :md="8" :lg="6">
              <!-- 重金属主题卡片 -->
              <HeavyMetalShowCard
                v-if="currentTheme.id === 'heavy-metal'"
                :item="item"
                :server-root="serverRoot"
                @click="router.push(`/shows/${item.id}`)"
                @like="toggleLike(item.id, 'SHOW_ANNOUNCEMENT')"
                @user-click="goToUser(item.publisherNickname)"
              />
              <!-- 赛博朋克主题卡片 -->
              <CyberpunkShowCard
                v-else-if="currentTheme.id === 'cyberpunk'"
                :item="item"
                :server-root="serverRoot"
                @click="router.push(`/shows/${item.id}`)"
                @like="toggleLike(item.id, 'SHOW_ANNOUNCEMENT')"
                @user-click="goToUser(item.publisherNickname)"
              />
              <!-- 复古霓虹主题卡片 -->
              <RetroWaveShowCard
                v-else-if="currentTheme.id === 'retro-wave'"
                :item="item"
                :serverRoot="serverRoot"
                @click="router.push(`/shows/${item.id}`)"
                @like="toggleLike(item.id, 'SHOW_ANNOUNCEMENT')"
                @user-click="goToUser(item.publisherNickname)"
              />
            </el-col>
          </el-row>
        </div>

        <!-- 空状态 -->
        <div v-else :class="['empty-state', `empty-${currentTheme.id}`]">
          <div class="empty-icon">🎸</div>
          <h3 class="empty-title">暂无演出预告</h3>
          <p class="empty-desc">快来发布第一个演出吧！</p>
        </div>
      </div>
    </div>

    <!-- 添加演出对话框 -->
    <el-dialog v-model="showAddDialog" title="🎸 发布演出公告" width="500px">
      <el-form :model="addForm" label-position="top">
        <el-form-item label="演出标题" required>
          <el-input v-model="addForm.title" placeholder="如：沙河重金属之夜" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="演出时间" required>
              <el-date-picker
                v-model="addForm.showTime"
                type="datetime"
                placeholder="选择时间"
                style="width: 100%"
                format="YYYY-MM-DD HH:mm"
                value-format="YYYY-MM-DDTHH:mm:ss"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="演出地点" required>
              <el-input v-model="addForm.venue" placeholder="如：大学生活动中心" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="演出详情">
          <el-input v-model="addForm.content" type="textarea" :rows="4" placeholder="描述一下演出信息..." />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="submitShow">发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'
import { useTheme } from '../composables/useTheme'
import HeavyMetalToolbar from '../components/themes/HeavyMetalToolbar.vue'
import CyberpunkToolbar from '../components/themes/CyberpunkToolbar.vue'
import RetroWaveToolbar from '../components/themes/RetroWaveToolbar.vue'
import HeavyMetalShowCard from '../components/themes/HeavyMetalShowCard.vue'
import CyberpunkShowCard from '../components/themes/CyberpunkShowCard.vue'
import RetroWaveShowCard from '../components/themes/RetroWaveShowCard.vue'

const { currentTheme, loadTheme } = useTheme()
const router = useRouter()

const shows = ref([])
const loading = ref(false)
const showAddDialog = ref(false)

const apiUrl = import.meta.env.VITE_API_BASE_URL || ''
const serverRoot = apiUrl.replace('/api', '')

const addForm = ref({
  title: '',
  showTime: '',
  venue: '',
  content: ''
})

const fetchShows = async () => {
  loading.value = true
  try {
    const res = await request.get('/shows')
    shows.value = res.sort((a, b) => b.id - a.id)
  } finally {
    loading.value = false
  }
}

const submitShow = async () => {
  if (!addForm.value.title || !addForm.value.showTime || !addForm.value.venue) {
    ElMessage.warning('请填写所有必填项')
    return
  }

  try {
    await request.post('/shows', addForm.value)
    ElMessage.success('发布成功！')
    showAddDialog.value = false
    addForm.value = { title: '', showTime: '', venue: '', content: '' }
    fetchShows()
  } catch (err) {
    console.error(err)
  }
}

const toggleLike = async (id, type) => {
  try {
    const item = shows.value.find(s => s.id === id)
    if (item) {
      item.isLiked = !item.isLiked
      item.likeCount = item.isLiked ? (item.likeCount || 0) + 1 : Math.max((item.likeCount || 0) - 1, 0)
    }
    await request.post(`/likes/${type}/${id}`)
  } catch (error) {
    console.error('点赞失败', error)
  }
}

const goToUser = (nickname) => {
  if (!nickname) return
  router.push(`/user/${nickname}`)
}

onMounted(() => {
  loadTheme()
  fetchShows()
})
</script>

<style scoped>
.show-container {
  min-height: 100vh;
  position: relative;
}

.page-header {
  position: relative;
  z-index: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.show-content {
  position: relative;
  z-index: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 60px;
}

.toolbar-wrapper {
  margin-bottom: 30px;
}

.shows-grid {
  min-height: 400px;
}

.empty-state {
  text-align: center;
  padding: 80px 20px;
}

.empty-icon {
  font-size: 80px;
  margin-bottom: 20px;
  animation: pulse-glow 2s ease-in-out infinite;
}

.empty-title {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 28px;
  color: #d4d4d4;
  margin: 0 0 10px;
}

.empty-desc {
  font-size: 16px;
  color: #8c8c8c;
  margin: 0;
}

@keyframes pulse-glow {
  0%, 100% { transform: scale(1); filter: drop-shadow(0 0 10px rgba(255, 69, 0, 0.5)); }
  50% { transform: scale(1.1); filter: drop-shadow(0 0 20px rgba(255, 69, 0, 0.8)); }
}
</style>
