<template>
  <div class="inspector-page">
    <div class="top-banner"><el-button type="default" size="small" @click="router.push('/')" style="position:absolute;left:20px"><el-icon><ArrowLeft /></el-icon> 首页</el-button><el-icon :size="28"><Monitor /></el-icon><span>NEPG AQI检测网格员端</span><el-tag type="warning" size="small" style="margin-left:12px">{{ userStore.userRole }}</el-tag><el-button type="danger" size="small" @click="userStore.logout();router.push('/')" style="position:absolute;right:20px">退出</el-button></div>
    <!-- 时间范围选择 -->
    <div class="time-bar">
      <span class="time-label">数据范围：</span>
      <el-radio-group v-model="timeRange" @change="loadTasks" size="small">
        <el-radio-button value="7d">近7天</el-radio-button>
        <el-radio-button value="30d">近30天</el-radio-button>
        <el-radio-button value="12m">近12个月</el-radio-button>
        <el-radio-button value="all">全部</el-radio-button>
      </el-radio-group>
      <span class="time-range-text">{{ timeRangeText }}</span>
    </div>
    <div class="main-container">
      <div class="page-header">
        <h2>🔬 检测任务</h2>
        <el-tag type="success">检测方式：精密仪器测量（AQI国标）</el-tag>
      </div>
      <el-table :data="tasks" border stripe size="small">
        <el-table-column prop="id" label="ID" width="50" />
        <el-table-column prop="city" label="城市" width="90" />
        <el-table-column label="监督员目测等级" width="220">
          <template #default="{row}">🌫️PM2.5:{{ L(row.pm25Level) }} | 👃SO₂:{{ L(row.so2Level) }} | 😵CO:{{ L(row.coLevel) }}</template>
        </el-table-column>
        <el-table-column label="目测综合" width="90"><template #default="{row}"><el-tag :type="T(row.overallLevel)" size="small">{{ L(row.overallLevel) }}</el-tag></template></el-table-column>
        <el-table-column label="实测结果" min-width="280">
          <template #default="{row}">
            <template v-if="row.measuredPm25 != null">
              🌫️PM2.5:<b>{{ row.pm25Raw }}μg</b>({{ L(row.measuredPm25) }}) 👃SO₂:<b>{{ row.so2Raw }}μg</b>({{ L(row.measuredSo2) }}) 😵CO:<b>{{ row.coRaw }}mg</b>({{ L(row.measuredCo) }})
              → <el-tag :type="T(row.measuredOverall)" size="small">{{ L(row.measuredOverall) }}</el-tag>
            </template>
            <span v-else style="color:#ccc">待检测</span>
          </template>
        </el-table-column>
        <el-table-column prop="notes" label="备注" width="140" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{row}">
            <el-button v-if="row.measuredPm25 == null" type="primary" size="small" @click="open(row)">📝 录入仪器数据</el-button>
            <span v-else style="color:green;font-size:12px">✓ 已完成</span>
          </template>
        </el-table-column>
      </el-table>
      <p v-if="!tasks.length" style="text-align:center;color:#ccc;padding:40px">暂无检测任务</p>
    </div>

    <!-- 仪器录入弹窗 -->
    <el-dialog v-model="dlg" title="精密仪器检测数据录入" width="620px">
      <el-descriptions :column="2" border size="small" style="margin-bottom:16px">
        <el-descriptions-item label="上报ID">{{ form.id }}</el-descriptions-item>
        <el-descriptions-item label="城市">{{ form.city }}</el-descriptions-item>
        <el-descriptions-item label="监督员目测综合"><el-tag :type="T(form.supervisorOverall)" size="small">{{ L(form.supervisorOverall) }}</el-tag></el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left"><b>🌫️ PM2.5 颗粒物</b>（精密仪器24h均值 μg/m³）</el-divider>
      <el-row :gutter="12">
        <el-col :span="16"><el-form-item label="实测浓度 (μg/m³)"><el-input-number v-model="form.pm25Raw" :min="0" :max="999" :step="0.1" style="width:100%" placeholder="仪器读数" /></el-form-item></el-col>
        <el-col :span="8"><el-form-item label="对应等级"><el-tag :type="T(pm25Level)" size="large">{{ L(pm25Level) }}</el-tag></el-form-item></el-col>
      </el-row>
      <div style="font-size:11px;color:#999;margin:-8px 0 8px">国标：0-35=优 36-75=良 76-115=轻度 116-150=中度 ＞150=重度</div>

      <el-divider content-position="left"><b>👃 SO₂ 二氧化硫</b>（精密仪器1h均值 μg/m³）</el-divider>
      <el-row :gutter="12">
        <el-col :span="16"><el-form-item label="实测浓度 (μg/m³)"><el-input-number v-model="form.so2Raw" :min="0" :max="2000" :step="0.1" style="width:100%" placeholder="仪器读数" /></el-form-item></el-col>
        <el-col :span="8"><el-form-item label="对应等级"><el-tag :type="T(so2Level)" size="large">{{ L(so2Level) }}</el-tag></el-form-item></el-col>
      </el-row>
      <div style="font-size:11px;color:#999;margin:-8px 0 8px">国标：0-50=优 51-150=良 151-475=轻度 476-800=中度 ＞800=重度</div>

      <el-divider content-position="left"><b>😵 CO 一氧化碳</b>（精密仪器1h均值 mg/m³）</el-divider>
      <el-row :gutter="12">
        <el-col :span="16"><el-form-item label="实测浓度 (mg/m³)"><el-input-number v-model="form.coRaw" :min="0" :max="100" :step="0.1" style="width:100%" placeholder="仪器读数" /></el-form-item></el-col>
        <el-col :span="8"><el-form-item label="对应等级"><el-tag :type="T(coLevel)" size="large">{{ L(coLevel) }}</el-tag></el-form-item></el-col>
      </el-row>
      <div style="font-size:11px;color:#999;margin:-8px 0 8px">国标：0-2=优 2-4=良 4-14=轻度 14-24=中度 ＞24=重度</div>

      <el-divider />
      <el-row :gutter="12">
        <el-col :span="12"><el-form-item label="实测综合AQI等级"><el-tag :type="T(measureOverall)" size="large">{{ L(measureOverall) }}</el-tag><span style="font-size:11px;color:#999;margin-left:6px">= MAX(三者等级)</span></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="监督员目测"><el-tag :type="T(form.supervisorOverall)" size="large">{{ L(form.supervisorOverall) }}</el-tag><span style="font-size:11px;margin-left:6px;color:#999" v-if="measureOverall !== form.supervisorOverall">⚠ 与目测{{ measureOverall > form.supervisorOverall ? '偏高' : '偏低' }}</span></el-form-item></el-col>
      </el-row>

      <el-form-item label="检测备注"><el-input v-model="form.notes" type="textarea" :rows="3" placeholder="检测过程说明、仪器型号、异常情况等..." /></el-form-item>

      <template #footer><el-button @click="dlg=false">取消</el-button><el-button type="primary" @click="submit" :loading="submitting">提交检测报告</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Monitor, ArrowLeft } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { post, get } from '@/util/request'
import { levelName, levelTagType } from '@/util/helpers'

const router = useRouter()

interface InspectorTask { id: number; province: string; city: string; pm25Level: number; so2Level: number; coLevel: number; overallLevel: number; status: string; supervisorName: string; createTime: string; measuredPm25?: number; measuredSo2?: number; measuredCo?: number; measuredOverall?: number; pm25Raw?: number; so2Raw?: number; coRaw?: number; notes?: string }

const userStore = useUserStore()
const tasks = ref<InspectorTask[]>([])
const dlg = ref(false)
const submitting = ref(false)
const form = reactive({ id: 0, city: '', supervisorOverall: 0, pm25Raw: 0, so2Raw: 0, coRaw: 0, notes: '' })

const L = levelName
const T = levelTagType
const timeRange = ref('all')
const timeParams = computed<Record<string, string> | undefined>(() => {
  if (timeRange.value === 'all') return undefined
  const now = new Date()
  const end = now.toISOString().slice(0, 10)
  const start = new Date()
  if (timeRange.value === '7d') start.setDate(now.getDate() - 7)
  else if (timeRange.value === '30d') start.setDate(now.getDate() - 30)
  else if (timeRange.value === '12m') start.setFullYear(now.getFullYear() - 1)
  return { startDate: start.toISOString().slice(0, 10), endDate: end }
})
const timeRangeText = computed(() => {
  if (timeRange.value === 'all') return '全部历史数据'
  const p = timeParams.value
  return p ? `${p.startDate} ~ ${p.endDate}` : ''
})

/** 浓度→等级 */
function rawToLevel(pollutant: string, raw: number): number {
  if (pollutant === 'pm25') return raw <= 35 ? 1 : raw <= 75 ? 2 : raw <= 115 ? 3 : raw <= 150 ? 4 : 5
  if (pollutant === 'so2') return raw <= 50 ? 1 : raw <= 150 ? 2 : raw <= 475 ? 3 : raw <= 800 ? 4 : 5
  if (pollutant === 'co') return raw <= 2 ? 1 : raw <= 4 ? 2 : raw <= 14 ? 3 : raw <= 24 ? 4 : 5
  return 1
}
const pm25Level = computed(() => rawToLevel('pm25', form.pm25Raw))
const so2Level = computed(() => rawToLevel('so2', form.so2Raw))
const coLevel = computed(() => rawToLevel('co', form.coRaw))
const measureOverall = computed(() => Math.max(pm25Level.value, so2Level.value, coLevel.value))

function loadTasks() {
  get(`/report/myTasks?assigneeId=${userStore.user?.id}`, timeParams.value)
    .then((d) => { tasks.value = (d.data as InspectorTask[]) || [] })
    .catch((e) => {
      console.error('加载任务列表失败', e)
      ElMessage.error('加载任务列表失败：' + (e?.message || '未知错误'))
    })
}
onMounted(loadTasks)

function open(row: InspectorTask) {
  Object.assign(form, { id: row.id, city: row.city, supervisorOverall: row.overallLevel, pm25Raw: 0, so2Raw: 0, coRaw: 0, notes: '' })
  dlg.value = true
}
function submit() {
  if (submitting.value) return
  submitting.value = true
  post('/measure/submit', {
    reportId: form.id,
    inspectorId: userStore.user?.id,
    inspectorName: userStore.user?.name,
    pm25Level: pm25Level.value, so2Level: so2Level.value, coLevel: coLevel.value,
    overallLevel: measureOverall.value,
    pm25Raw: form.pm25Raw, so2Raw: form.so2Raw, coRaw: form.coRaw,
    notes: form.notes,
  }).then((r) => {
    submitting.value = false
    if (r.code === 200) { ElMessage.success('检测报告已提交'); dlg.value = false; loadTasks() }
    else ElMessage.error(r.msg)
  }).catch(() => {
    submitting.value = false
    ElMessage.error('网络错误，请检查后端服务')
  })
}
</script>

<style scoped>
.inspector-page { min-height: 100vh; background: linear-gradient(160deg, #e8f5e9 0%, #e0f2f1 30%, #e0f7fa 60%, #e8eaf6 100%); }
.top-banner { width: 100%; background: linear-gradient(135deg, #00695c 0%, #00796b 40%, #00897b 100%); color: #fff; display: flex; align-items: center; justify-content: center; gap: 10px; padding: 14px 0; font-size: 18px; letter-spacing: 2px; }
.time-bar{display:flex;align-items:center;justify-content:center;padding:6px 0;gap:8px;background:rgba(255,255,255,0.5)}
.time-label{color:#00695c;font-size:13px;font-weight:500}
.time-range-text{color:#999;font-size:11px;margin-left:8px}
.main-container { padding: 20px 40px 40px; max-width: 1400px; margin: 0 auto; }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px; }
.page-header h2 { margin: 0; color: #00695c; }
</style>
