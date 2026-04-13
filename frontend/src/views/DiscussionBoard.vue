<template>
  <div :class="['discussion-container', `theme-${currentTheme.id}`]">
    <!-- 页面头部 -->
    <div :class="['page-header', `header-${currentTheme.id}`]">
      <el-page-header @back="router.push('/')" content="讨论区">
        <template #title>
          <span class="back-link">← 回广场</span>
        </template>
      </el-page-header>
    </div>

    <!-- 主内容区 -->
    <div :class="['discussion-content', `content-${currentTheme.id}`]">
      <!-- 板块选择器 -->
      <div :class="['section-selector', `selector-${currentTheme.id}`]">
        <div
          v-for="section in sections"
          :key="section.value"
          :class="['section-tab', { 'active': currentSection === section.value }]"
          @click="currentSection = section.value"
        >
          {{ section.label }}
        </div>
      </div>

      <!-- 工具栏 - 根据主题显示 -->
      <div v-if="currentTheme.id === 'heavy-metal'" class="toolbar-wrapper">
        <HeavyMetalToolbar button-text="🔥 发布讨论 🔥" @add-click="showAddDialog = true" />
      </div>
      <div v-else-if="currentTheme.id === 'cyberpunk'" class="toolbar-wrapper">
        <CyberpunkToolbar button-text="⚡ 发布讨论 ⚡" @add-click="showAddDialog = true" />
      </div>
      <div v-else-if="currentTheme.id === 'retro-wave'" class="toolbar-wrapper">
        <RetroWaveToolbar button-text="✨ 发布讨论 ✨" @add-click="showAddDialog = true" />
      </div>

      <!-- 讨论列表 -->
      <div v-loading="loading" class="discussions-grid">
        <div v-if="posts.length > 0">
          <el-row :gutter="30">
            <el-col v-for="post in posts" :key="post.id" :xs="24" :sm="12" :md="8">
              <!-- 重金属主题卡片 -->
              <HeavyMetalDiscussionCard
                v-if="currentTheme.id === 'heavy-metal'"
                :item="post"
                :server-root="serverRoot"
                @click="router.push(`/discussions/${post.id}`)"
                @like="toggleLike(post.id, 'DISCUSSION_POST')"
                @user-click="goToUser(post.publisherNickname)"
              />
              <!-- 赛博朋克主题卡片 -->
              <CyberpunkDiscussionCard
                v-else-if="currentTheme.id === 'cyberpunk'"
                :item="post"
                :server-root="serverRoot"
                @click="router.push(`/discussions/${post.id}`)"
                @like="toggleLike(post.id, 'DISCUSSION_POST')"
                @user-click="goToUser(post.publisherNickname)"
              />
              <!-- 复古霓虹主题卡片 -->
              <RetroWaveDiscussionCard
                v-else-if="currentTheme.id === 'retro-wave'"
                :item="post"
                :server-root="serverRoot"
                @click="router.push(`/discussions/${post.id}`)"
                @like="toggleLike(post.id, 'DISCUSSION_POST')"
                @user-click="goToUser(post.publisherNickname)"
              />
            </el-col>
          </el-row>
        </div>

        <!-- 空状态 -->
        <div v-else :class="['empty-state', `empty-${currentTheme.id}`]">
          <div class="empty-icon">🎨</div>
          <h3 class="empty-title">暂无讨论</h3>
          <p class="empty-desc">快来发起第一个话题吧！</p>
        </div>
      </div>

      <!-- 发帖对话框 -->
      <el-dialog
        v-model="showAddDialog"
        title="发布讨论"
        :width="500"
        :close-on-click-modal="false"
      >
        <el-form :model="newPost" label-position="top">
          <el-form-item label="标题">
            <el-input v-model="newPost.title" placeholder="请输入标题" />
          </el-form-item>
          <el-form-item label="内容">
            <el-input
              v-model="newPost.content"
              type="textarea"
              :rows="6"
              placeholder="请输入讨论内容..."
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showAddDialog = false">取消</el-button>
          <el-button type="primary" @click="submitPost">发布</el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../api/request'
import { useTheme } from '../composables/useTheme'
import HeavyMetalDiscussionCard from '../components/themes/HeavyMetalDiscussionCard.vue'
import CyberpunkDiscussionCard from '../components/themes/CyberpunkDiscussionCard.vue'
import RetroWaveDiscussionCard from '../components/themes/RetroWaveDiscussionCard.vue'
import HeavyMetalToolbar from '../components/themes/HeavyMetalToolbar.vue'
import CyberpunkToolbar from '../components/themes/CyberpunkToolbar.vue'
import RetroWaveToolbar from '../components/themes/RetroWaveToolbar.vue'

const { currentTheme, loadTheme } = useTheme()
const router = useRouter()

const sections = [
  { label: '💬 闲聊树洞', value: 'CHIT_CHAT' },
  { label: '🎸 技术乐理', value: 'TECH_THEORY' }
]

const currentSection = ref('CHIT_CHAT')
const posts = ref([])
const loading = ref(false)
const showAddDialog = ref(false)
const newPost = ref({
  title: '',
  content: '',
  section: 'CHIT_CHAT'
})

const apiUrl = import.meta.env.VITE_API_BASE_URL || ''
const serverRoot = apiUrl.replace('/api', '')

const fetchPosts = async () => {
  loading.value = true
  try {
    const res = await request.get('/discussions', { params: { section: currentSection.value } })
    posts.value = res.sort((a, b) => b.id - a.id)
  } finally {
    loading.value = false
  }
}

const toggleLike = async (id, type) => {
  try {
    const post = posts.value.find(p => p.id === id)
    if (post) {
      post.isLiked = !post.isLiked
      post.likeCount = post.isLiked ? (post.likeCount || 0) + 1 : Math.max((post.likeCount || 0) - 1, 0)
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

const submitPost = async () => {
  if (!newPost.value.title.trim()) {
    ElMessage.warning('请输入标题')
    return
  }
  if (!newPost.value.content.trim()) {
    ElMessage.warning('请输入内容')
    return
  }

  try {
    newPost.value.section = currentSection.value
    await request.post('/discussions', newPost.value)
    ElMessage.success('发布成功！')
    newPost.value = { title: '', content: '', section: currentSection.value }
    showAddDialog.value = false
    fetchPosts()
  } catch (error) {
    console.error('发布失败', error)
  }
}

watch(currentSection, () => {
  fetchPosts()
})

onMounted(() => {
  loadTheme()
  fetchPosts()
})
</script>

<style scoped>
.discussion-container {
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

.discussion-content {
  position: relative;
  z-index: 1;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px 60px;
}

.section-selector {
  display: flex;
  gap: 10px;
  margin-bottom: 30px;
  padding: 15px;
  background: rgba(255, 255, 255, 0.05);
  border: 2px solid #2d2d2d;
  border-radius: 8px;
  flex-wrap: wrap;
}

.section-tab {
  padding: 10px 20px;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.3s ease;
  font-family: 'Arial Black', sans-serif;
  font-size: 14px;
  color: #8c8c8c;
}

.section-tab:hover {
  background: rgba(255, 69, 0, 0.1);
  color: #d4d4d4;
}

.section-tab.active {
  background: rgba(255, 69, 0, 0.2);
  color: #ff4500;
  border-color: #ff4500;
}

.discussions-grid {
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

/* 主题特定样式 */
.theme-cyberpunk .section-selector {
  border-color: #00ffff;
  background: rgba(0, 255, 255, 0.05);
}

.theme-cyberpunk .section-tab.active {
  background: rgba(0, 255, 255, 0.2);
  color: #00ffff;
  border-color: #00ffff;
}

.theme-retro-wave .section-selector {
  border-style: dashed;
  border-color: #ff1493;
  background: rgba(255, 20, 147, 0.05);
}

.theme-retro-wave .section-tab.active {
  background: rgba(255, 20, 147, 0.2);
  color: #ff1493;
  border-color: #ff1493;
}

@keyframes pulse-glow {
  0%, 100% { transform: scale(1); filter: drop-shadow(0 0 10px rgba(255, 69, 0, 0.5)); }
  50% { transform: scale(1.1); filter: drop-shadow(0 0 20px rgba(255, 69, 0, 0.8)); }
}
</style>
