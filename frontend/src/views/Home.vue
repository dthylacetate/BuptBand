<template>
  <div :class="['home-container', `theme-${currentTheme.id}`]">
    <!-- 🎸 NAVIGATION BAR 🎸 -->
    <nav :class="['navbar', `navbar-${currentTheme.id}`]">
      <div class="nav-container">
        <div :class="['logo', `logo-${currentTheme.id}`]">
          <span class="logo-icon">{{ currentTheme.icon }}</span>
          <span class="logo-text">RIGHTSPELL</span>
          <span class="logo-sub">乐手广场</span>
        </div>

        <div :class="['nav-menu', `nav-menu-${currentTheme.id}`]">
          <div :class="['nav-item', `nav-item-${currentTheme.id}`]" @click="router.push('/discussions')">
            <span class="nav-icon">💬</span>
            <span class="nav-text">讨论区</span>
            <div :class="['nav-underline', `nav-underline-${currentTheme.id}`]"></div>
          </div>

          <div :class="['nav-item', `nav-item-${currentTheme.id}`]" @click="router.push('/shows')">
            <span class="nav-icon">📅</span>
            <span class="nav-text">演出公告</span>
            <div :class="['nav-underline', `nav-underline-${currentTheme.id}`]"></div>
          </div>
        </div>

        <div class="nav-user">
          <ThemeSwitcher />

          <el-dropdown>
            <div :class="['user-dropdown', `user-dropdown-${currentTheme.id}`]">
              <span class="user-icon">👤</span>
              <span class="user-text">我的账号</span>
              <span class="dropdown-arrow">▼</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu :class="['dropdown-menu', `dropdown-${currentTheme.id}`]">
                <el-dropdown-item @click="router.push('/profile')" :class="`dropdown-item-${currentTheme.id}`">
                  <span class="dropdown-icon">👤</span> 个人中心
                </el-dropdown-item>
                <el-dropdown-item @click="handleLogout" :class="`dropdown-item-${currentTheme.id}`">
                  <span class="dropdown-icon">🚪</span> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </nav>

    <!-- 🎯 MAIN CONTENT AREA 🎯 -->
    <main :class="['main-content', `main-${currentTheme.id}`]">
      <!-- 主题特定的工具栏 -->
      <div v-if="currentTheme.id === 'heavy-metal'" class="toolbar-wrapper">
        <HeavyMetalToolbar @add-click="showAddDialog = true" />
      </div>
      <div v-else-if="currentTheme.id === 'cyberpunk'" class="toolbar-wrapper">
        <CyberpunkToolbar @add-click="showAddDialog = true" />
      </div>
      <div v-else-if="currentTheme.id === 'retro-wave'" class="toolbar-wrapper">
        <RetroWaveToolbar @add-click="showAddDialog = true" />
      </div>

      <!-- 招募列表 -->
      <div :class="['content-grid', `grid-${currentTheme.id}`]">
        <el-row :gutter="30">
          <el-col
            v-for="item in recruitments"
            :key="item.id"
            :xs="24"
            :sm="12"
            :md="8"
            :lg="6"
          >
            <!-- 重金属主题卡片 -->
            <HeavyMetalCard
              v-if="currentTheme.id === 'heavy-metal'"
              :item="item"
              :server-root="serverRoot"
              @click="goToDetail(item.id)"
              @like="toggleLike(item.id, 'RECRUITMENT')"
              @user-click="goToUser(item.publisherNickname)"
            />
            <!-- 赛博朋克主题卡片 -->
            <CyberpunkCard
              v-else-if="currentTheme.id === 'cyberpunk'"
              :item="item"
              :server-root="serverRoot"
              @click="goToDetail(item.id)"
              @like="toggleLike(item.id, 'RECRUITMENT')"
              @user-click="goToUser(item.publisherNickname)"
            />
            <!-- 复古霓虹主题卡片 -->
            <RetroWaveCard
              v-else-if="currentTheme.id === 'retro-wave'"
              :item="item"
              :server-root="serverRoot"
              @click="goToDetail(item.id)"
              @like="toggleLike(item.id, 'RECRUITMENT')"
              @user-click="goToUser(item.publisherNickname)"
            />
          </el-col>
        </el-row>
      </div>
    </main>

    <!-- 添加招募对话框 -->
    <el-dialog
      v-model="showAddDialog"
      :title="dialogTitle"
      :width="600"
      :class="`dialog-${currentTheme.id}`"
    >
      <el-form :model="addForm" label-width="120px">
        <el-form-item label="招募类型">
          <el-radio-group v-model="addForm.type">
            <el-radio-button label="BAND_SEEKING_MEMBER">乐队招人</el-radio-button>
            <el-radio-button label="MEMBER_SEEKING_BAND">个人求队</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="标题" required>
          <el-input v-model="addForm.title" placeholder="起个响亮的标题" class="metal-input" />
        </el-form-item>

        <el-form-item label="招募位置" required>
          <el-input v-model="addForm.position" placeholder="比如：贝斯手" class="metal-input" />
        </el-form-item>

        <el-form-item label="音乐风格">
          <el-input v-model="addForm.style" placeholder="比如：重金属、朋克、爵士" class="metal-input" />
        </el-form-item>

        <el-form-item label="技术要求">
          <el-input v-model="addForm.requirements" type="textarea" :rows="3" placeholder="有什么要求?" class="metal-input" />
        </el-form-item>

        <el-form-item label="详情介绍" required>
          <el-input v-model="addForm.detail" type="textarea" :rows="4" placeholder="写点好玩的吸引队友吧！" class="metal-input" />
        </el-form-item>

        <el-form-item label="联系方式" required>
          <el-input v-model="addForm.contactInformation" placeholder="微信号或手机号" class="metal-input" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showAddDialog = false" :class="`cancel-btn-${currentTheme.id}`">取消</el-button>
        <el-button
          :type="addForm.type === 'BAND_SEEKING_MEMBER' ? 'success' : 'warning'"
          @click="submitRecruitment"
          :class="`submit-btn-${currentTheme.id}`"
        >
          🔥 立即发布 🔥
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '../api/request'
import ThemeSwitcher from '../components/ThemeSwitcher.vue'
import { useTheme } from '../composables/useTheme'
import HeavyMetalToolbar from '../components/themes/HeavyMetalToolbar.vue'
import CyberpunkToolbar from '../components/themes/CyberpunkToolbar.vue'
import RetroWaveToolbar from '../components/themes/RetroWaveToolbar.vue'
import HeavyMetalCard from '../components/themes/HeavyMetalCard.vue'
import CyberpunkCard from '../components/themes/CyberpunkCard.vue'
import RetroWaveCard from '../components/themes/RetroWaveCard.vue'

// 主题系统
const { currentTheme, loadTheme } = useTheme()
const router = useRouter()

const recruitments = ref([])
const showAddDialog = ref(false)

// ⚡️ 环境变量与路径处理
const apiUrl = import.meta.env.VITE_API_BASE_URL || '';
const serverRoot = apiUrl.replace('/api', '');

// 初始表单状态
const addForm = ref({
  type: 'BAND_SEEKING_MEMBER',
  title: '',
  position: '',
  style: '',
  requirements: '',
  detail: '',
  contactInformation: ''
})

const dialogTitle = computed(() => {
  return addForm.value.type === 'BAND_SEEKING_MEMBER' ? '🎸 发布乐队招募' : '🤘 发布个人求队'
})

// 获取招募列表
const fetchRecruitments = async () => {
  try {
    const res = await request.get('/recruitments')
    recruitments.value = res.sort((a, b) => b.id - a.id)
  } catch (err) {
    console.error(err)
  }
}

// 提交招募
const submitRecruitment = async () => {
  if (!addForm.value.title || !addForm.value.position || !addForm.value.detail || !addForm.value.contactInformation) {
    ElMessage.warning('请填写所有必填项')
    return
  }

  try {
    await request.post('/recruitments', addForm.value)
    ElMessage.success('发布成功！')
    showAddDialog.value = false
    addForm.value = {
      type: 'BAND_SEEKING_MEMBER',
      title: '',
      position: '',
      style: '',
      requirements: '',
      detail: '',
      contactInformation: ''
    }
    fetchRecruitments()
  } catch (err) {
    console.error(err)
  }
}

// 跳转详情
const goToDetail = (id) => {
  router.push(`/recruitments/${id}`)
}

// 查看用户主页
const goToUser = (nickname) => {
  if (!nickname) return
  router.push(`/user/${nickname}`)
}

// 点赞功能
const toggleLike = async (id, type) => {
  try {
    const item = recruitments.value.find(r => r.id === id)
    if (item) {
      item.isLiked = !item.isLiked
      item.likeCount = item.isLiked ? (item.likeCount || 0) + 1 : Math.max((item.likeCount || 0) - 1, 0)
    }
    await request.post(`/likes/${type}/${id}`)
  } catch (error) {
    console.error('点赞失败', error)
    if (item) {
      item.isLiked = !item.isLiked
      item.likeCount = item.isLiked ? (item.likeCount || 0) + 1 : Math.max((item.likeCount || 0) - 1, 0)
    }
  }
}

const handleLogout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}

onMounted(() => {
  loadTheme()
  fetchRecruitments()
})
</script>

<style scoped>
/* === GLOBAL CONTAINER STYLES === */
.home-container {
  min-height: 100vh;
  position: relative;
  overflow-x: hidden;
}

.main-content {
  position: relative;
  z-index: 1;
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 20px 60px;
}

.content-grid {
  margin-top: 40px;
}

.toolbar-wrapper {
  margin-bottom: 30px;
}

/* === NAVBAR BASE STYLES === */
.navbar {
  position: relative;
  z-index: 100;
  padding: 0;
}

.nav-container {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 15px 30px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.logo-icon {
  font-size: 28px;
  animation: pulse-glow 2s ease-in-out infinite;
}

.logo-text {
  font-family: 'Impact', 'Arial Black', sans-serif;
  font-size: 28px;
  font-weight: 900;
  letter-spacing: 2px;
  transition: all 0.3s ease;
}

.logo-sub {
  font-family: 'Arial Black', sans-serif;
  font-size: 12px;
  color: #8c8c8c;
  text-transform: uppercase;
  letter-spacing: 2px;
  margin-left: 8px;
}

.nav-menu {
  display: flex;
  gap: 40px;
}

.nav-item {
  position: relative;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 4px;
}

.nav-item:hover {
  background: rgba(255, 69, 0, 0.1);
}

.nav-icon {
  font-size: 18px;
}

.nav-text {
  font-family: 'Arial Black', sans-serif;
  font-size: 14px;
  color: #d4d4d4;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.nav-underline {
  position: absolute;
  bottom: 5px;
  left: 50%;
  width: 0;
  height: 2px;
  background: #ff4500;
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.nav-item:hover .nav-underline {
  width: 80%;
}

.nav-user {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.05);
  border: 2px solid #2d2d2d;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.user-dropdown:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: #ff4500;
  box-shadow: 0 0 15px rgba(255, 69, 0, 0.3);
}

.user-icon {
  font-size: 18px;
}

.user-text {
  font-family: 'Arial Black', sans-serif;
  font-size: 14px;
  color: #d4d4d4;
}

.dropdown-arrow {
  font-size: 10px;
  color: #d4d4d4;
}

/* === HEAVY METAL THEME STYLES === */
.theme-heavy-metal.navbar {
  background: rgba(10, 10, 10, 0.85);
  backdrop-filter: blur(20px);
  border-bottom: 3px solid transparent;
  border-image: linear-gradient(90deg, transparent, #ff4500, transparent) 1;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.8), 0 0 20px rgba(255, 69, 0, 0.2);
}

.theme-heavy-metal.logo-text {
  background: linear-gradient(45deg, #ff4500 0%, #dc143c 50%, #ffa500 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  font-family: 'Impact', 'Arial Black', sans-serif;
}

/* === CYBERPUNK THEME STYLES === */
.theme-cyberpunk.navbar {
  background: rgba(10, 10, 26, 0.9);
  backdrop-filter: blur(20px);
  border-bottom: 2px solid #00ffff;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.8), 0 0 30px rgba(0, 255, 255, 0.3);
  position: relative;
}

.theme-cyberpunk.navbar::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, #00ffff, #ff1493, #00ffff, transparent);
  animation: cyber-scan 3s linear infinite;
}

.theme-cyberpunk.logo-text {
  color: #00ffff;
  font-family: 'Courier New', monospace;
  text-shadow: 0 0 10px #00ffff, 0 0 20px #00ffff;
  letter-spacing: 4px;
}

.theme-cyberpunk .nav-item:hover {
  background: rgba(0, 255, 255, 0.1);
}

.theme-cyberpunk .nav-underline {
  background: #00ffff;
}

/* === RETRO WAVE THEME STYLES === */
.theme-retro-wave.navbar {
  background: linear-gradient(180deg, rgba(26, 10, 46, 0.95), rgba(26, 10, 46, 0.8));
  backdrop-filter: blur(20px);
  border-bottom: 4px solid transparent;
  border-image: linear-gradient(90deg, #ff1493, #9400d3, #00bfff, #ff1493) 1;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.8), 0 0 40px rgba(255, 20, 147, 0.4);
}

.theme-retro-wave.logo-text {
  color: #ff1493;
  font-family: 'Arial Black', sans-serif;
  text-shadow: 0 0 10px #ff1493, 2px 2px 0 #9400d3;
  letter-spacing: 3px;
}

.theme-retro-wave .nav-item:hover {
  background: rgba(255, 20, 147, 0.1);
}

.theme-retro-wave .nav-underline {
  background: #ff1493;
}

/* === ANIMATIONS === */
@keyframes pulse-glow {
  0%, 100% {
    transform: scale(1);
    filter: drop-shadow(0 0 10px rgba(255, 69, 0, 0.5));
  }
  50% {
    transform: scale(1.1);
    filter: drop-shadow(0 0 20px rgba(255, 69, 0, 0.8));
  }
}

@keyframes cyber-scan {
  0% { opacity: 0.3; }
  50% { opacity: 1; }
  100% { opacity: 0.3; }
}

.theme-retro-wave.logo-text {
  color: #ff1493;
  font-family: 'Arial Black', sans-serif;
  text-shadow: 0 0 10px #ff1493, 2px 2px 0 #9400d3;
  letter-spacing: 3px;
}

/* === ANIMATIONS === */
@keyframes cyber-scan {
  0% { opacity: 0.3; }
  50% { opacity: 1; }
  100% { opacity: 0.3; }
}
</style>
