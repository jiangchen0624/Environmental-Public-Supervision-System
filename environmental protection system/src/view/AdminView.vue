<template>
  <div class="admin-page">
    <div class="top-banner"><el-button type="default" size="small" @click="router.push('/')" style="position:absolute;left:20px"><el-icon><ArrowLeft /></el-icon> 首页</el-button><el-icon :size="28"><Monitor /></el-icon><span>NEPM 系统管理端</span><el-tag type="warning" size="small" style="margin-left:12px">{{ userStore.userRole }}</el-tag><el-button type="danger" size="small" @click="userStore.logout();router.push('/')" style="position:absolute;right:20px">退出</el-button></div>
    <div class="layout">
      <!-- 左侧菜单 -->
      <div class="sidebar">
        <el-menu :default-active="menu" @select="onMenuSelect" :default-openeds="['g1','g2']">
          <el-sub-menu index="g1">
            <template #title><el-icon><Document /></el-icon> 公众监督数据管理</template>
            <el-menu-item index="list">📋 公众监督数据列表</el-menu-item>
            <el-menu-item index="confirmed">✅ 确认AQI数据列表</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="g2">
            <template #title><el-icon><DataAnalysis /></el-icon> 统计数据管理</template>
            <el-menu-item index="province">📊 省分组检查统计</el-menu-item>
            <el-menu-item index="dist">📈 AQI指数分布统计</el-menu-item>
            <el-menu-item index="trend">📉 AQI指数趋势统计</el-menu-item>
            <el-menu-item index="detect">🔢 检测数量实时统计</el-menu-item>
            <el-menu-item index="cover">🗺️ 全国网格覆盖率统计</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <!-- 右侧内容 -->
      <div class="content">
        <!-- 公众监督数据列表 -->
        <template v-if="menu==='list'">
          <div class="toolbar">
            <el-input v-model="search" placeholder="搜索省市..." clearable style="width:180px" @input="filterList" />
            <el-select v-model="filterStatus" placeholder="状态" clearable style="width:120px" @change="filterList"><el-option v-for="s in statuses" :key="s" :label="s" :value="s" /></el-select>
            <el-select v-model="filterLevel" placeholder="AQI等级" clearable style="width:130px" @change="filterList"><el-option v-for="i in 5" :key="i" :label="i+'级 '+L(i)" :value="i" /></el-select>
            <el-button @click="loadAll" type="primary" size="small">刷新</el-button>
          </div>
          <el-table :data="pagedList" border stripe size="small" @row-click="showDetail" style="cursor:pointer" max-height="460">
            <el-table-column prop="id" label="ID" width="50"/><el-table-column prop="province" label="省" width="60"/><el-table-column prop="city" label="市" width="75"/>
            <el-table-column label="PM2.5/SO₂/CO" width="140"><template #default="{row}">{{ L(row.pm25Level) }}/{{ L(row.so2Level) }}/{{ L(row.coLevel) }}</template></el-table-column>
            <el-table-column label="综合" width="70"><template #default="{row}"><el-tag :type="T(row.overallLevel)" size="small">{{ L(row.overallLevel) }}</el-tag></template></el-table-column>
            <el-table-column label="状态" width="70"><template #default="{row}"><el-tag :type="ST(row.status)" size="small">{{ row.status }}</el-tag></template></el-table-column>
            <el-table-column prop="assigneeName" label="网格员" width="70"/>
            <el-table-column label="操作" width="130"><template #default="{row}"><el-button v-if="row.status==='待指派'" type="warning" size="small" @click.stop="openAssign(row)">指派</el-button><el-button type="danger" size="small" @click.stop="del(row.id)">删除</el-button></template></el-table-column>
          </el-table>
          <el-pagination v-model:current-page="page" :page-size="10" :total="filteredList.length" layout="prev,pager,next" style="margin-top:10px;justify-content:center" />
        </template>

        <!-- 确认AQI数据列表 -->
        <template v-if="menu==='confirmed'">
          <el-table :data="confirmedList" border stripe size="small" max-height="520" @row-click="showDetail" style="cursor:pointer">
            <el-table-column prop="id" label="ID" width="45"/><el-table-column prop="province" label="省" width="55"/><el-table-column prop="city" label="市" width="70"/>
            <el-table-column label="目测 PM2.5/SO₂/CO" width="135"><template #default="{row}">{{ L(row.pm25Level) }}/{{ L(row.so2Level) }}/{{ L(row.coLevel) }}</template></el-table-column>
            <el-table-column label="目测" width="60"><template #default="{row}"><el-tag :type="T(row.overallLevel)" size="small">{{ L(row.overallLevel) }}</el-tag></template></el-table-column>
            <el-table-column label="实测 PM2.5" width="90"><template #default="{row}"><b v-if="row.pm25Raw!=null">{{ row.pm25Raw }}μg</b><span v-else style="color:#ccc">—</span></template></el-table-column>
            <el-table-column label="实测 SO₂" width="90"><template #default="{row}"><b v-if="row.so2Raw!=null">{{ row.so2Raw }}μg</b><span v-else style="color:#ccc">—</span></template></el-table-column>
            <el-table-column label="实测 CO" width="90"><template #default="{row}"><b v-if="row.coRaw!=null">{{ row.coRaw }}mg</b><span v-else style="color:#ccc">—</span></template></el-table-column>
            <el-table-column label="实测等级" width="70"><template #default="{row}"><el-tag v-if="row.measuredOverall!=null" :type="T(row.measuredOverall)" size="small">{{ L(row.measuredOverall) }}</el-tag></template></el-table-column>
            <el-table-column label="偏差" width="65"><template #default="{row}"><span v-if="row.measuredOverall!=null" :style="{color:row.measuredOverall>row.overallLevel?'#f44336':row.measuredOverall<row.overallLevel?'#4caf50':'#999'}">{{ row.measuredOverall>row.overallLevel?'↑高':row.measuredOverall<row.overallLevel?'↓低':'=' }}</span></template></el-table-column>
            <el-table-column prop="assigneeName" label="检测人" width="65"/>
            <el-table-column prop="notes" label="备注" min-width="100" show-overflow-tooltip/>
          </el-table>
        </template>

        <!-- 省分组检查统计 -->
        <template v-if="menu==='province'">
          <el-button v-if="drillProvince" type="info" size="small" @click="drillProvince=''" style="margin-bottom:8px">← 返回省级汇总</el-button>
          <el-table v-if="!drillProvince" :data="provStats" border stripe size="small" @row-click="drillDown" style="cursor:pointer" highlight-current-row>
            <el-table-column prop="province" label="省" width="80"/><el-table-column label="SO₂超标" width="80"><template #default="{row}"><el-tag :type="row.so2超标>0?'danger':'info'" size="small">{{ row.so2超标 }}</el-tag></template></el-table-column>
            <el-table-column label="CO超标" width="80"><template #default="{row}"><el-tag :type="row.co超标>0?'danger':'info'" size="small">{{ row.co超标 }}</el-tag></template></el-table-column>
            <el-table-column label="PM2.5超标" width="90"><template #default="{row}"><el-tag :type="row.pm25超标>0?'danger':'info'" size="small">{{ row.pm25超标 }}</el-tag></template></el-table-column>
            <el-table-column label="AQI超标" width="80"><template #default="{row}"><el-tag :type="row.aqi超标>0?'danger':'info'" size="small">{{ row.aqi超标 }}</el-tag></template></el-table-column>
            <el-table-column label="操作" width="80"><template #default><el-button type="primary" link size="small">市级明细 →</el-button></template></el-table-column>
          </el-table>
          <el-table v-if="drillProvince" :data="cityDrillData" border stripe size="small">
            <el-table-column prop="city" label="市" width="100"/><el-table-column label="SO₂超标" width="80"><template #default="{row}"><el-tag :type="row.so2超标>0?'danger':'info'" size="small">{{ row.so2超标 }}</el-tag></template></el-table-column>
            <el-table-column label="CO超标" width="80"><template #default="{row}"><el-tag :type="row.co超标>0?'danger':'info'" size="small">{{ row.co超标 }}</el-tag></template></el-table-column>
            <el-table-column label="PM2.5超标" width="90"><template #default="{row}"><el-tag :type="row.pm25超标>0?'danger':'info'" size="small">{{ row.pm25超标 }}</el-tag></template></el-table-column>
            <el-table-column label="AQI超标" width="80"><template #default="{row}"><el-tag :type="row.aqi超标>0?'danger':'info'" size="small">{{ row.aqi超标 }}</el-tag></template></el-table-column>
            <el-table-column label="总数" width="55"><template #default="{row}">{{ row.total }}</template></el-table-column>
          </el-table>
        </template>

        <!-- AQI指数分布统计 -->
        <template v-if="menu==='dist'"><div ref="distChart" style="height:400px" /></template>

        <!-- AQI指数趋势统计 -->
        <template v-if="menu==='trend'"><div ref="trendChart" style="height:400px" /></template>

        <!-- 检测数量实时统计 -->
        <template v-if="menu==='detect'">
          <el-row :gutter="16" style="margin-bottom:16px">
            <el-col :span="8"><div class="big-stat s-total"><div class="big-n">{{ detectStats.total }}</div><div>累计检测</div></div></el-col>
            <el-col :span="8"><div class="big-stat s-good"><div class="big-n">{{ detectStats.good }}</div><div>结果良好</div></div></el-col>
            <el-col :span="8"><div class="big-stat s-over"><div class="big-n">{{ detectStats.over }}</div><div>结果超标</div></div></el-col>
          </el-row>
          <el-row :gutter="16"><el-col :span="12"><div ref="detectChart" style="height:320px"/></el-col><el-col :span="12"><div ref="detectBarChart" style="height:320px"/></el-col></el-row>
        </template>

        <!-- 全国网格覆盖率统计 -->
        <template v-if="menu==='cover'">
          <el-row :gutter="16">
            <el-col :span="8">
              <el-card shadow="hover" style="margin-bottom:10px"><template #header>省级覆盖率</template><div class="cover-rate">{{ coverStats.provinceRate }}%</div><el-progress :percentage="coverStats.provinceRate" :color="cColor(coverStats.provinceRate)" :stroke-width="14"/><p style="color:#999;font-size:11px;margin-top:6px">{{ coverStats.coveredProvinces }}/{{ coverStats.totalProvinces }} 省</p></el-card>
              <el-card shadow="hover"><template #header>大城市覆盖率</template><div class="cover-rate">{{ coverStats.cityRate }}%</div><el-progress :percentage="coverStats.cityRate" :color="cColor(coverStats.cityRate)" :stroke-width="14"/><p style="color:#999;font-size:11px;margin-top:6px">{{ coverStats.coveredCities }}/{{ coverStats.totalCities }} 城市</p></el-card>
            </el-col>
            <el-col :span="16">
              <el-card shadow="hover"><template #header><h3>🗺️ 省份覆盖（点击查看市级）</h3></template>
                <div class="province-grid">
                  <div v-for="p in provinceGrid" :key="p.name" class="pg-item" :class="{covered:p.covered,active:selCovProv===p.name}" @click="selCovProv=selCovProv===p.name?'':p.name" :title="(p.covered?'已覆盖 '+cityCovCnt(p.name)+' 市':'未覆盖')">{{ p.name }}<span v-if="p.covered" class="pg-cnt">{{ cityCovCnt(p.name) }}</span></div>
                </div>
                <div class="grid-legend"><span class="dot covered"/>已覆盖：{{ coverStats.coveredProvinces }} <span class="dot"/>未覆盖：{{ coverStats.totalProvinces-coverStats.coveredProvinces }}</div>
                <div v-if="selCovProv" style="margin-top:12px;border-top:1px solid #eee;padding-top:8px"><h4 style="margin:0 0 6px;color:#e65100">{{ selCovProv }} 市级网格</h4><div class="city-grid"><div v-for="c in covCityGrid" :key="c.name" class="cg-item" :class="{covered:c.covered}">{{ c.name }}</div></div></div>
              </el-card>
            </el-col>
          </el-row>
        </template>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailDlg" title="反馈数据详情" width="600px">
      <el-descriptions v-if="detail" :column="2" border size="small">
        <el-descriptions-item label="ID">{{detail.id}}</el-descriptions-item><el-descriptions-item label="城市">{{detail.city}}</el-descriptions-item>
        <el-descriptions-item label="PM2.5">{{L(detail.pm25Level)}}级</el-descriptions-item><el-descriptions-item label="SO₂">{{L(detail.so2Level)}}级</el-descriptions-item>
        <el-descriptions-item label="CO">{{L(detail.coLevel)}}级</el-descriptions-item><el-descriptions-item label="综合"><el-tag :type="T(detail.overallLevel)">{{L(detail.overallLevel)}}</el-tag></el-descriptions-item>
        <el-descriptions-item label="状态">{{detail.status}}</el-descriptions-item><el-descriptions-item label="网格员">{{detail.assigneeName||'—'}}</el-descriptions-item>
        <el-descriptions-item label="实测PM2.5" :span="2">{{detail.pm25Raw!=null?detail.pm25Raw+'μg → '+L(detail.measuredPm25!)+'级':'—'}}</el-descriptions-item>
        <el-descriptions-item label="实测SO₂" :span="2">{{detail.so2Raw!=null?detail.so2Raw+'μg → '+L(detail.measuredSo2!)+'级':'—'}}</el-descriptions-item>
        <el-descriptions-item label="实测CO" :span="2">{{detail.coRaw!=null?detail.coRaw+'mg → '+L(detail.measuredCo!)+'级':'—'}}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{detail.notes||'—'}}</el-descriptions-item>
      </el-descriptions>
      <template #footer><el-button @click="detailDlg=false">关闭</el-button><el-button v-if="detail&&detail.status==='待指派'" type="warning" @click="detailDlg=false;openAssign(detail)">指派网格员</el-button></template>
    </el-dialog>

    <!-- 指派弹窗 -->
    <el-dialog v-model="assignDlg" title="指派网格员" width="420px">
      <el-form label-position="top">
        <el-form-item label="ID / 地区"><strong>#{{assignForm.id}}</strong> | {{assignForm.city}}</el-form-item>
        <el-alert title="本地优先" type="info" :closable="false" show-icon style="margin-bottom:12px">优先同城网格员；无则指派邻近城市。</el-alert>
        <el-form-item label="选择网格员"><el-select v-model="assignForm.assigneeId" style="width:100%"><el-option v-for="g in sortedInspectors" :key="g.id" :label="g.name+' ('+g.city+')'+(g.city===assignForm.city?' ★本地':'')" :value="g.id"/></el-select></el-form-item>
      </el-form>
      <template #footer><el-button @click="assignDlg=false">取消</el-button><el-button type="primary" @click="doAssign">确认指派</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Monitor, ArrowLeft, Document, DataAnalysis } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { post, get } from '@/util/request'
import { levelName, levelTagType, statusTagType } from '@/util/helpers'
import { allProvinces, provinceCitiesMap } from '@/util/area-data'
import * as echarts from 'echarts'

interface CoverStats {
  coveredProvinces: number; totalProvinces: number; provinceRate: number
  coveredCities: number; totalCities: number; cityRate: number
  provinceList: string[]; cityList: string[]
}
interface ReportItem {
  id: number; province: string; city: string
  pm25Level: number; so2Level: number; coLevel: number; overallLevel: number
  status: string; assigneeName: string; assigneeId: number; supervisorId: number
  createTime: string
  measuredPm25?: number; measuredSo2?: number; measuredCo?: number; measuredOverall?: number
  pm25Raw?: number; so2Raw?: number; coRaw?: number; notes?: string
}
interface InspectorItem { id: number; name: string; city: string; role: string }

const router = useRouter(); const userStore = useUserStore()
const menu = ref('list'); const search = ref(''); const filterStatus = ref(''); const filterLevel = ref<number | null>(null)
const page = ref(1); const allList = ref<ReportItem[]>([]); const statuses = ['待指派', '已指派', '已检测']
const detailDlg = ref(false); const detail = ref<ReportItem | null>(null)
const assignDlg = ref(false); const assignForm = ref({ id: 0, city: '', assigneeId: 0 }); const gridInspectors = ref<InspectorItem[]>([])
interface StatRow { province?: string; city?: string; total?: number; so2超标?: number; co超标?: number; pm25超标?: number; aqi超标?: number; [key: string]: string | number | undefined }
const provStats = ref<StatRow[]>([]); const drillProvince = ref(''); const cityDrillData = ref<StatRow[]>([])
const detectStats = ref({ total: 0, good: 0, over: 0 })
const coverStats = ref<CoverStats>({ coveredProvinces: 0, totalProvinces: 34, provinceRate: 0, coveredCities: 0, totalCities: 373, cityRate: 0, provinceList: [], cityList: [] })
const distChart = ref<HTMLElement>(); const trendChart = ref<HTMLElement>(); const detectChart = ref<HTMLElement>(); const detectBarChart = ref<HTMLElement>()
const selCovProv = ref('')

const L = levelName
const T = levelTagType
const ST = statusTagType
const cColor = (r: number) => r < 30 ? '#f44336' : r < 60 ? '#ff9800' : r < 80 ? '#2196f3' : '#4caf50'
const provinceGrid = computed(() => {
  const list = allProvinces.map(p => ({ name: p, covered: coverStats.value.provinceList?.includes(p) || false }))
  return list.sort((a, b) => (b.covered ? 1 : -1) - (a.covered ? 1 : -1))
})
const provinceCities = provinceCitiesMap
const covCityGrid = computed(() => (provinceCities[selCovProv.value] || []).map((c: string) => ({ name: c, covered: coverStats.value.cityList?.includes(c) || false })))

// ECharts 实例管理，防止内存泄漏
const chartInstances: echarts.ECharts[] = []
function initChart(el: HTMLElement): echarts.ECharts {
  const existing = echarts.getInstanceByDom(el)
  if (existing) {
    const idx = chartInstances.indexOf(existing)
    if (idx > -1) chartInstances.splice(idx, 1)
    existing.dispose()
  }
  const instance = echarts.init(el)
  chartInstances.push(instance)
  return instance
}
function disposeCharts() {
  chartInstances.forEach((c) => c.dispose())
  chartInstances.length = 0
}
onUnmounted(disposeCharts)

const filteredList = computed(() => {
  let l = allList.value
  if (search.value) l = l.filter((r) => (r.city || '').includes(search.value) || (r.province || '').includes(search.value))
  if (filterStatus.value) l = l.filter((r) => r.status === filterStatus.value)
  if (filterLevel.value) l = l.filter((r) => r.overallLevel === filterLevel.value)
  return l
})
const pagedList = computed(() => filteredList.value.slice((page.value - 1) * 10, page.value * 10))
const confirmedList = computed(() => allList.value.filter((r) => r.measuredPm25 != null))
const sortedInspectors = computed(() => [...gridInspectors.value].sort((a, b) => (a.city === assignForm.value.city ? -1 : 1) - (b.city === assignForm.value.city ? -1 : 1)))
function filterList() { page.value = 1 }
onMounted(() => { loadAll(); loadGridInspectors() })
function loadAll() { get('/report/all').then((d) => { allList.value = (d.data as ReportItem[]) || [] }).catch(() => ElMessage.error('加载数据列表失败')) }
function loadGridInspectors() { get('/user/list?role=AQI检测网格员').then((d) => { gridInspectors.value = (d.data as InspectorItem[]) || [] }).catch(() => ElMessage.error('加载网格员列表失败')) }

function onMenuSelect(name: string) {
  menu.value = name
  disposeCharts()
  if (name === 'province') loadProvStats()
  if (name === 'dist') renderDist()
  if (name === 'trend') renderTrend()
  if (name === 'detect') {
    get('/stats/detection').then((d) => { detectStats.value = (d.data as { total: number; good: number; over: number }) || { total: 0, good: 0, over: 0 }; renderDetect() }).catch(() => ElMessage.error('加载统计数据失败'))
  }
  if (name === 'cover') get('/stats/coverage').then((d) => { coverStats.value = (d.data as CoverStats) || coverStats.value }).catch(() => ElMessage.error('加载覆盖率数据失败'))
}
function loadProvStats() { get('/stats/province').then((d) => { const pd = (d.data || {}) as Record<string, Record<string, number>>; provStats.value = Object.entries(pd).map(([k, v]) => ({ province: k, ...v })) }).catch(() => ElMessage.error('加载省统计失败')) }
function drillDown(row: StatRow) { drillProvince.value = row.province!; const m = new Map<string, StatRow>(); const rs = allList.value.filter((r) => r.province === row.province); for (const r of rs) { const c = r.city || '未知'; if (!m.has(c)) m.set(c, { city: c, total: 0, so2超标: 0, co超标: 0, pm25超标: 0, aqi超标: 0 }); const d = m.get(c)!; d.total = (d.total || 0) + 1; if (r.so2Level >= 3) d.so2超标 = (d.so2超标 || 0) + 1; if (r.coLevel >= 3) d.co超标 = (d.co超标 || 0) + 1; if (r.pm25Level >= 3) d.pm25超标 = (d.pm25超标 || 0) + 1; if (r.overallLevel >= 3) d.aqi超标 = (d.aqi超标 || 0) + 1 } cityDrillData.value = [...m.values()] }

function renderDist() { nextTick(() => { if (!distChart.value) return; get('/stats/distribution').then((dd) => { const data = (dd.data || {}) as Record<string, number>; const c = initChart(distChart.value!); c.setOption({ tooltip: { trigger: 'item' }, series: [{ type: 'pie', radius: ['40%', '70%'], data: Object.entries(data).map(([k, v]) => ({ name: k, value: v, itemStyle: { color: k === '优' ? '#4caf50' : k === '良' ? '#2196f3' : k.includes('轻度') ? '#ff9800' : k.includes('中度') ? '#f44336' : '#9c27b0' } })), label: { formatter: '{b}\n{d}%' } }] }) }).catch(() => ElMessage.error('加载AQI分布失败')) }) }
function renderTrend() { nextTick(() => { if (!trendChart.value) return; get('/stats/trend').then((td) => { const data = (td.data || {}) as Record<string, number>; const c = initChart(trendChart.value!); c.setOption({ tooltip: { trigger: 'axis' }, xAxis: { type: 'category', data: Object.keys(data) }, yAxis: { type: 'value' }, series: [{ data: Object.values(data), type: 'line', smooth: true, areaStyle: { opacity: 0.3 }, itemStyle: { color: '#f44336' } }] }) }).catch(() => ElMessage.error('加载AQI趋势失败')) }) }
function renderDetect() { nextTick(() => { if (detectChart.value) { const c = initChart(detectChart.value); c.setOption({ tooltip: { trigger: 'item' }, series: [{ type: 'pie', radius: ['50%', '75%'], data: [{ name: '良好', value: detectStats.value.good, itemStyle: { color: '#4caf50' } }, { name: '超标', value: detectStats.value.over, itemStyle: { color: '#f44336' } }], label: { formatter: '{b}:{c}\n({d}%)' } }] }) }; if (detectBarChart.value) { get('/stats/distribution').then((dd) => { const c = initChart(detectBarChart.value!); const data = (dd.data || {}) as Record<string, number>; c.setOption({ tooltip: { trigger: 'axis' }, xAxis: { type: 'category', data: Object.keys(data) }, yAxis: { type: 'value' }, series: [{ data: Object.values(data), type: 'bar', itemStyle: { color: (p: { dataIndex: number }) => ['#4caf50', '#2196f3', '#ff9800', '#f44336', '#9c27b0'][p.dataIndex] || '#999' } }] }) }).catch(() => ElMessage.error('加载分布数据失败')) } }) }

function showDetail(row: ReportItem) { detail.value = row; detailDlg.value = true }
function openAssign(row: ReportItem) { assignForm.value = { id: row.id, city: row.city, assigneeId: 0 }; assignDlg.value = true }
function doAssign() { const g = gridInspectors.value.find((i) => i.id === assignForm.value.assigneeId); post('/report/assign', { id: assignForm.value.id, assigneeId: assignForm.value.assigneeId, assigneeName: g?.name || '' }).then((r) => { if (r.code === 200) { ElMessage.success('指派成功'); assignDlg.value = false; loadAll() } }) }
function del(id: number) { ElMessageBox.confirm('删除此记录？', '确认', { type: 'warning' }).then(() => get(`/report/delete?id=${id}`).then(() => loadAll()).catch(() => ElMessage.error('删除失败'))).catch(() => {}) }
function cityCovCnt(province: string) { const cities = provinceCities[province] || []; const covered = cities.filter((c) => coverStats.value.cityList?.includes(c)).length; return covered + '/' + cities.length }
</script>

<style scoped>
.admin-page{height:100vh;overflow:hidden;display:flex;flex-direction:column;background:linear-gradient(160deg,#fff3e0 0%,#ffe0b2 20%,#fff8e1 40%,#e8f5e9 70%,#e8eaf6 100%)}
.top-banner{width:100%;background:linear-gradient(135deg,#e65100 0%,#ef6c00 40%,#f57c00 100%);color:#fff;display:flex;align-items:center;justify-content:center;gap:10px;padding:12px 0;font-size:18px;letter-spacing:2px;position:relative;flex-shrink:0}
.layout{display:flex;flex:1;overflow:hidden}
.sidebar{width:220px;background:#fff;border-right:1px solid #e8e8e8;flex-shrink:0}
.content{flex:1;padding:16px 20px;overflow:auto}
.toolbar{display:flex;gap:10px;align-items:center;margin-bottom:10px;flex-wrap:wrap}
.big-stat{text-align:center;padding:30px 16px;border-radius:10px;color:#fff}.big-n{font-size:42px;font-weight:700}
.big-stat.s-total{background:#607d8b}.big-stat.s-good{background:#4caf50}.big-stat.s-over{background:#f44336}
.cover-rate{font-size:38px;font-weight:700;text-align:center;margin:8px 0}
.province-grid{display:flex;flex-wrap:wrap;gap:5px;justify-content:center;padding:6px}
.pg-item{width:50px;height:28px;display:flex;align-items:center;justify-content:center;font-size:10px;border-radius:4px;background:#eee;color:#999;cursor:pointer;transition:.2s}
.pg-item.covered{background:#4caf50;color:#fff;font-weight:600}.pg-item.active{outline:2px solid #e65100;outline-offset:1px}
.pg-cnt{font-size:8px;opacity:0.8;margin-left:1px}
.city-grid{display:flex;flex-wrap:wrap;gap:4px;justify-content:center}
.cg-item{padding:2px 8px;font-size:10px;border-radius:3px;background:#f5f5f5;color:#bbb}.cg-item.covered{background:#66bb6a;color:#fff;font-weight:500}
.grid-legend{display:flex;gap:12px;justify-content:center;margin-top:8px;font-size:11px;color:#999;align-items:center}
.dot{width:10px;height:10px;border-radius:2px;background:#eee;display:inline-block}.dot.covered{background:#4caf50}
h3{margin:0;font-size:13px;color:#e65100}
</style>
