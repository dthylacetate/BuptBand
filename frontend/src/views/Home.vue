<template>
  <div class="home-container">
    <el-menu mode="horizontal" class="nav-menu" :ellipsis="false">
      <div class="logo">🎸 RIGHTSPELL 乐手广场</div>
      <div class="flex-grow" />
      <el-sub-menu index="user">
        <template #title>👤 我的账号</template>
        <el-menu-item index="profile" @click="router.push('/profile')">个人中心</el-menu-item>
        <el-menu-item index="logout" @click="handleLogout">退出登录</el-menu-item>
      </el-sub-menu>
    </el-menu>

    <el-main>
      <div class="toolbar">
        <div class="search-group">
          <el-input 
            v-model="searchQuery.instrument" 
            placeholder="搜索位置 (如: 贝斯手)" 
            style="width: 180px; margin-right: 10px;" 
            clearable 
          />
          <el-button type="primary" @click="fetchRecruitments">筛选</el-button>
        </div>
        
        <div class="action-group">
          <el-button type="success" @click="openDialog('BAND_SEEKING_MEMBER')">
            <el-icon style="margin-right: 5px;"><Plus /></el-icon>发布乐队招募
          </el-button>
          <el-button type="warning" @click="openDialog('MEMBER_SEEKING_BAND')">
            <el-icon style="margin-right: 5px;"><Search /></el-icon>乐手求组队
          </el-button>
        </div>
      </div>

      <el-row :gutter="20" v-if="recruitments.length > 0">
        <el-col 
          :xs="24" :sm="12" :md="8" :lg="6" 
          v-for="item in recruitments" 
          :key="item.id"
        >
          <el-card 
            :class="['recruitment-card', item.type === 'BAND_SEEKING_MEMBER' ? 'band-card' : 'member-card']" 
            shadow="hover"
            @click="router.push(`/recruitments/${item.id}`)"
            style="cursor: pointer;"
          >
            <template #header>
              <div class="card-header">
                <el-tag :type="item.type === 'BAND_SEEKING_MEMBER' ? 'success' : 'warning'" size="small">
                  {{ item.type === 'BAND_SEEKING_MEMBER' ? '招人' : '求队' }}
                </el-tag>
                <span class="title-text">{{ item.title }}</span>
              </div>
            </template>
            <div class="card-body">
              <p class="info-line"><strong>位置：</strong>{{ item.position }}</p>
              <p class="info-line"><strong>风格：</strong>{{ item.style }}</p>
              <p class="detail-text">{{ item.detail }}</p>
            </div>
            
            <div class="footer">
              <div class="publisher-info">
                <el-avatar 
                  :size="24" 
                  :src="item.publisherAvatar ? serverRoot + item.publisherAvatar : ''"
                  icon="User"
                  class="mini-avatar"
                />
                <span class="nickname">{{ item.publisherNickname || '匿名乐手' }}</span>
              </div>
              <span class="contact-hint">查看详情</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-else description="广场上空荡荡的，去发个帖子招募队友吧？" />
    </el-main>

    <el-dialog 
      v-model="showAddDialog" 
      :title="addForm.type === 'BAND_SEEKING_MEMBER' ? '🎸 发起乐队招募' : '🤘 乐手求组队'" 
      width="500px" 
      destroy-on-close
    >
      <el-form :model="addForm" label-position="top">
        <el-form-item label="帖子标题" required>
          <el-input 
            v-model="addForm.title" 
            :placeholder="addForm.type === 'BAND_SEEKING_MEMBER' ? '如：沙河重金属乐队招贝斯' : '如：五年琴龄老贝斯求收留'" 
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="addForm.type === 'BAND_SEEKING_MEMBER' ? '所需位置' : '我的位置'" required>
              <el-select v-model="addForm.position" placeholder="请选择" style="width: 100%">
                <el-option label="贝斯手" value="Bass" />
                <el-option label="吉他手" value="Guitar" />
                <el-option label="主唱" value="Vocal" />
                <el-option label="鼓手" value="Drum" />
                <el-option label="键盘手" value="Keyboard" />
                <el-option label="其他" value="Other" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="音乐风格" required>
              <el-input v-model="addForm.style" placeholder="如：Punk, Metal" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="技术要求/个人说明">
          <el-input v-model="addForm.requirements" placeholder="简单描述一下水平或设备要求..." />
        </el-form-item>

        <el-form-item label="详情介绍" required>
          <el-input v-model="addForm.detail" type="textarea" :rows="4" placeholder="写点好玩的吸引队友吧！" />
        </el-form-item>

        <el-form-item label="联系方式" required>
          <el-input v-model="addForm.contactInformation" placeholder="微信号或手机号" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button 
          :type="addForm.type === 'BAND_SEEKING_MEMBER' ? 'success' : 'warning'" 
          @click="submitRecruitment"
        >
          立即发布
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue' 
import request from '../api/request'

const router = useRouter()
const recruitments = ref([])
const showAddDialog = ref(false)
const searchQuery = ref({ instrument: '', keyword: '' })

// ⚡️ 环境变量与路径处理
const apiUrl = import.meta.env.VITE_API_BASE_URL;
const serverRoot = apiUrl.replace('/api', '');

// 初始表单状态
const addForm = ref({
  title: '',
  position: '',
  style: '',
  requirements: '',
  detail: '',
  contactInformation: '',
  type: '' 
})

const openDialog = (type) => {
  addForm.value.type = type
  showAddDialog.value = true
}

const fetchRecruitments = async () => {
  try {
    const res = await request.get('/recruitments', {
      params: { 
        position: searchQuery.value.instrument, 
        keyword: searchQuery.value.keyword 
      }
    })
    recruitments.value = res
  } catch (error) { 
    console.error('获取列表失败', error) 
  }
}

const submitRecruitment = async () => {
  if (!addForm.value.title || !addForm.value.detail || !addForm.value.contactInformation) {
    return ElMessage.warning('请填写必要信息喵')
  }

  try {
    await request.post('/recruitments', addForm.value)
    ElMessage.success('发布成功！')
    showAddDialog.value = false
    addForm.value = { title: '', position: '', style: '', requirements: '', detail: '', contactInformation: '', type: '' }
    fetchRecruitments()
  } catch (err) { 
    console.error(err) 
  }
}

const handleLogout = () => {
  localStorage.removeItem('token')
  router.push('/login')
}

onMounted(fetchRecruitments)
</script>

<style scoped>
.home-container { min-height: 100vh; background-color: #f5f7fa; }
.nav-menu { padding: 0 40px; display: flex; align-items: center; border-bottom: 1px solid #dcdfe6; }
.logo { font-weight: bold; color: #409eff; font-size: 1.2rem; }
.flex-grow { flex-grow: 1; }

.toolbar { 
  display: flex; 
  justify-content: space-between; 
  margin: 20px 0; 
  padding: 15px 25px; 
  background: #fff; 
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.recruitment-card { 
  margin-bottom: 20px; 
  border-radius: 12px; 
  border: none; 
  transition: transform 0.2s;
}
.recruitment-card:hover { transform: translateY(-5px); }

.band-card { border-top: 4px solid #67c23a; }
.member-card { border-top: 4px solid #e6a23c; }

.title-text { font-weight: 600; margin-left: 10px; color: #303133; }
.info-line { font-size: 14px; margin: 5px 0; color: #606266; }
.detail-text { 
  font-size: 13px; 
  color: #909399; 
  margin-top: 10px; 
  line-height: 1.6; 
  height: 50px; 
  overflow: hidden; 
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

/* ⚡️ 底部信息区优化 */
.footer { 
  border-top: 1px solid #f2f6fc; 
  padding-top: 12px; 
  display: flex; 
  justify-content: space-between; 
  align-items: center;
  font-size: 12px; 
}

.publisher-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.mini-avatar {
  border: 1px solid #eee;
}

.nickname {
  color: #606266;
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.contact-hint { color: #409eff; font-weight: bold; }
</style>