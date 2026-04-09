<template>
  <div class="show-container">
    <el-page-header @back="router.push('/')" content="演出公告" class="page-header" />

    <div class="action-bar">
      <div class="tips">📅 发现身边的现场演出</div>
      <el-button type="danger" @click="showAddDialog = true">
        <el-icon style="margin-right: 5px;"><Microphone /></el-icon>发布演出预告
      </el-button>
    </div>

    <div v-loading="loading" class="show-list">
      <el-row :gutter="20" v-if="shows.length > 0">
        <el-col :xs="24" :sm="12" :md="8" v-for="item in shows" :key="item.id">
          <el-card 
            class="show-card" 
            shadow="hover" 
            @click="router.push(`/shows/${item.id}`)"
          >
            <div class="time-tag">{{ formatShowDate(item.showTime) }}</div>
            
            <h3 class="show-title">{{ item.title }}</h3>
            
            <div class="show-info">
              <p><el-icon><Location /></el-icon> {{ item.venue }}</p>
              <p><el-icon><Clock /></el-icon> {{ formatShowTime(item.showTime) }}</p>
            </div>

            <div class="show-footer">
              <div class="publisher">
                <el-avatar :size="20" :src="serverRoot + item.publisherAvatar" icon="User" />
                <span>{{ item.publisherNickname }}</span>
              </div>
              <span class="detail-link">详情 ></span>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-else description="近期暂无演出预告，去发布一个吧？" />
    </div>

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
              <el-input v-model="addForm.venue" placeholder="如：学校礼堂 / 某Livehouse" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="演出详情 (阵容、票务等)">
          <el-input 
            v-model="addForm.content" 
            type="textarea" 
            :rows="5" 
            placeholder="写点演出介绍吸引乐迷吧..." 
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="danger" @click="submitShow">立即发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Microphone, Location, Clock } from '@element-plus/icons-vue'
import request from '../api/request'

const router = useRouter()
const loading = ref(false)
const shows = ref([])
const showAddDialog = ref(false)

const apiUrl = import.meta.env.VITE_API_BASE_URL || ''
const serverRoot = apiUrl.replace('/api', '')

const addForm = ref({ title: '', venue: '', showTime: '', content: '' })

// 1. 获取演出列表
const fetchShows = async () => {
  loading.value = true
  try {
    const res = await request.get('/shows')
    shows.value = res
  } finally {
    loading.value = false
  }
}

// 2. 提交发布
const submitShow = async () => {
  if (!addForm.value.title || !addForm.value.showTime || !addForm.value.venue) {
    return ElMessage.warning('请填写完整的演出信息喵')
  }
  await request.post('/shows', addForm.value)
  ElMessage.success('发布成功，期待演出！')
  showAddDialog.value = false
  addForm.value = { title: '', venue: '', showTime: '', content: '' }
  fetchShows()
}

// --- 时间格式化工具 ---
const formatShowDate = (dateStr) => {
  const d = new Date(dateStr)
  return `${d.getMonth() + 1}月${d.getDate()}日`
}
const formatShowTime = (dateStr) => {
  const d = new Date(dateStr)
  const days = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
  const hours = String(d.getHours()).padStart(2, '0')
  const mins = String(d.getMinutes()).padStart(2, '0')
  return `${days[d.getDay()]} ${hours}:${mins}`
}

onMounted(fetchShows)
</script>

<style scoped>
.show-container { max-width: 1000px; margin: 20px auto; padding: 0 20px; }
.page-header { margin-bottom: 25px; }

.action-bar { 
  display: flex; 
  justify-content: space-between; 
  align-items: center; 
  margin-bottom: 30px;
  background: #fff;
  padding: 15px 20px;
  border-radius: 10px;
}
.tips { color: #909399; font-size: 14px; }

.show-card { 
  margin-bottom: 20px; 
  border-radius: 12px; 
  border: none; 
  position: relative;
  cursor: pointer;
  transition: all 0.3s;
}
.show-card:hover { transform: scale(1.02); box-shadow: 0 10px 20px rgba(0,0,0,0.1); }

/* 时间标签样式 */
.time-tag {
  position: absolute;
  top: 0;
  right: 0;
  background: #f56c6c;
  color: #fff;
  padding: 5px 12px;
  font-size: 12px;
  font-weight: bold;
  border-bottom-left-radius: 12px;
  border-top-right-radius: 12px;
}

.show-title { margin: 15px 0 10px; font-size: 1.2rem; color: #303133; }
.show-info { font-size: 14px; color: #606266; margin-bottom: 20px; }
.show-info p { margin: 5px 0; display: flex; align-items: center; gap: 5px; }

.show-footer { 
  border-top: 1px solid #f2f6fc; 
  padding-top: 12px; 
  display: flex; 
  justify-content: space-between; 
  font-size: 12px; 
}
.publisher { display: flex; align-items: center; gap: 5px; color: #909399; }
.detail-link { color: #f56c6c; font-weight: bold; }
</style>