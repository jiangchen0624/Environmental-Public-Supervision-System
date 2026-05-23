<template>
  <div class="dv-page">
    <div class="top-banner"><el-button type="default" size="small" @click="router.push('/')" style="position:absolute;left:20px"><el-icon><ArrowLeft /></el-icon> 首页</el-button><el-icon :size="28"><Monitor /></el-icon><span>NEPV 决策者可视化大屏</span><el-tag type="primary" size="small" style="margin-left:12px">{{ userStore.userRole }}</el-tag><el-button type="danger" size="small" @click="userStore.logout();router.push('/')" style="position:absolute;right:20px">退出</el-button></div>
    <div class="main-container">
      <!-- Row 1: 统计卡片 -->
      <el-row :gutter="12" class="row">
        <el-col :span="4"><div class="card c1"><div class="card-n">{{ detectStats.total }}</div><div class="card-t">检测累计</div></div></el-col>
        <el-col :span="4"><div class="card c2"><div class="card-n">{{ detectStats.good }}</div><div class="card-t">结果良好</div></div></el-col>
        <el-col :span="4"><div class="card c3"><div class="card-n">{{ detectStats.over }}</div><div class="card-t">结果超标</div></div></el-col>
        <el-col :span="4"><div class="card c4"><div class="card-n">{{ coverStats.provinceRate }}%</div><div class="card-t">省覆盖率</div></div></el-col>
        <el-col :span="4"><div class="card c5"><div class="card-n-s">{{ coverStats.cityRate }}%</div><div class="card-t">城市覆盖率</div></div></el-col>
        <el-col :span="4"><div ref="detectPieChart" class="mini-chart" /></el-col>
      </el-row>

      <!-- Row 2: 省分组统计(表格+高亮) + AQI分布 -->
      <el-row :gutter="12" class="row">
        <el-col :span="14">
          <el-card shadow="hover"><template #header><h3>📊 省分组超标统计（等级≥3即为超标）</h3></template>
            <el-table :data="provTable" border stripe size="small" max-height="380" style="font-size:12px">
              <el-table-column prop="province" label="省" width="70" />
              <el-table-column label="SO₂" width="60"><template #default="{row}"><span :class="{over:row.so2超标}">{{ row.so2超标 }}</span></template></el-table-column>
              <el-table-column label="CO" width="60"><template #default="{row}"><span :class="{over:row.co超标}">{{ row.co超标 }}</span></template></el-table-column>
              <el-table-column label="PM2.5" width="65"><template #default="{row}"><span :class="{over:row.pm25超标}">{{ row.pm25超标 }}</span></template></el-table-column>
              <el-table-column label="AQI" width="60"><template #default="{row}"><span :class="{over:row.aqi超标}">{{ row.aqi超标 }}</span></template></el-table-column>
              <el-table-column label="超标趋势" min-width="120"><template #default="{row}"><div class="mini-bar"><div class="mini-bar-fill" :style="{width:row.maxPct+'%',background:row.maxPct>50?'#f44336':row.maxPct>20?'#ff9800':'#4caf50'}"></div></div></template></el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="10">
          <el-card shadow="hover"><template #header><h3>🥧 AQI指数分布统计</h3></template><div ref="distChart" class="chart" /></el-card>
        </el-col>
      </el-row>

      <!-- Row 3: 趋势 + 覆盖率(可点击下钻) -->
      <el-row :gutter="12" class="row">
        <el-col :span="14">
          <el-card shadow="hover"><template #header><h3>📈 AQI指数趋势统计（近12个月）</h3></template><div ref="trendChart" class="chart" /></el-card>
        </el-col>
        <el-col :span="10">
          <el-card shadow="hover"><template #header><h3>🗺️ 全国网格覆盖率</h3></template>
            <div class="cover-panel">
              <div class="cover-item"><div class="cover-val">{{ coverStats.coveredProvinces }}<small>/{{ coverStats.totalProvinces }}</small></div><div class="cover-label">覆盖省份</div><el-progress :percentage="coverStats.provinceRate" :color="cColor(coverStats.provinceRate)" :stroke-width="14" /></div>
              <div class="cover-item" style="margin-top:10px"><div class="cover-val">{{ coverStats.coveredCities }}<small>/{{ coverStats.totalCities }}</small></div><div class="cover-label">覆盖大城市 (全国{{ coverStats.totalCities }}个)</div><el-progress :percentage="coverStats.cityRate" :color="cColor(coverStats.cityRate)" :stroke-width="14" /></div>
              <div class="grid-legend-sm"><span class="dot-sm covered"/>已覆盖：{{ coverStats.coveredProvinces }} <span class="dot-sm"/>未覆盖：{{ coverStats.totalProvinces - coverStats.coveredProvinces }}</div>
              <div class="province-grid-sm">
                <div v-for="p in provinceGrid" :key="p.name" class="pg-sm" :class="{covered:p.covered, active:selProvince===p.name}" @click="selProvince = selProvince===p.name ? '' : p.name" :title="(p.covered?'已覆盖 '+cityCoverCount(p.name)+' 市':'未覆盖')">{{ p.name }}<span v-if="p.covered" class="pg-cnt">{{ cityCoverCount(p.name) }}</span></div>
              </div>
              <!-- 市级下钻 -->
              <div v-if="selProvince" style="margin-top:10px;border-top:1px solid #37474f;padding-top:8px">
                <div style="font-size:11px;color:#90caf9;margin-bottom:6px">{{ selProvince }} 已覆盖 {{ cityCoverCount(selProvince) }} 市</div>
                <div class="city-grid-sm">
                  <div v-for="c in selCityGrid" :key="c.name" class="cg-sm" :class="{covered:c.covered}">{{ c.name }}</div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Monitor, ArrowLeft } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { get } from '@/util/request'
import { allProvinces, provinceCitiesMap } from '@/util/area-data'
import * as echarts from 'echarts'

const router = useRouter()

const userStore = useUserStore()
const detectStats = ref({ total: 0, good: 0, over: 0 })
const coverStats = ref({ coveredProvinces: 0, totalProvinces: 34, provinceRate: 0, coveredCities: 0, totalCities: 373, cityRate: 0, provinceList: [], cityList: [] })
const distChart = ref<HTMLElement>(); const trendChart = ref<HTMLElement>(); const detectPieChart = ref<HTMLElement>()
const cColor = (r: number) => r < 30 ? '#f44336' : r < 60 ? '#ff9800' : r < 80 ? '#2196f3' : '#4caf50'
const provinceGrid = computed(() => {
  const list = allProvinces.map(p => ({ name: p, covered: coverStats.value.provinceList?.includes(p) || false }))
  return list.sort((a, b) => (b.covered ? 1 : -1) - (a.covered ? 1 : -1))
})
const provTable = ref<any[]>([])
const selProvince = ref('')

const provinceCities = provinceCitiesMap
const selCityGrid = computed(() => (provinceCities[selProvince.value] || []).map(c => ({ name: c, covered: coverStats.value.cityList?.includes(c) || false })))

// ECharts 实例管理
const chartInstances: echarts.ECharts[] = []
function initChart(el: HTMLElement): echarts.ECharts {
  const instance = echarts.init(el)
  chartInstances.push(instance)
  return instance
}
onUnmounted(() => chartInstances.forEach((c) => c.dispose()))

onMounted(async () => {
  try {
    const [prov, dist, trend, detect, cover] = await Promise.all([
      get('/stats/province'),
      get('/stats/distribution'),
      get('/stats/trend'),
      get('/stats/detection'),
      get('/stats/coverage'),
    ])
    detectStats.value = (detect.data as any) || { total: 0, good: 0, over: 0 }
    coverStats.value = (cover.data as any) || coverStats.value

    // 省分组表格
    const pd = prov.data || {}
    provTable.value = Object.entries(pd).map(([k, v]: [string, any]) => {
      const total = Math.max(v['aqi超标'] || 1, 1)
      return { province: k, ...v, maxPct: Math.round(Math.max(v['so2超标'], v['co超标'], v['pm25超标']) / total * 100) }
    })

    await nextTick()
    // AQI分布
    const dd = dist.data || {}
    const c2 = initChart(distChart.value!)
    c2.setOption({ tooltip: { trigger: 'item' }, series: [{ type: 'pie', radius: ['45%', '75%'], center: ['50%', '50%'], data: Object.entries(dd).map(([k, v]) => ({ name: k, value: v, itemStyle: { color: k === '优' ? '#4caf50' : k === '良' ? '#2196f3' : k.includes('轻度') ? '#ff9800' : k.includes('中度') ? '#f44336' : '#9c27b0' } })), label: { formatter: '{b}\n{d}%' } }] })

    // 趋势
    const td = trend.data || {}
    const c3 = initChart(trendChart.value!)
    c3.setOption({ tooltip: { trigger: 'axis' }, grid: { left: 40, right: 20, bottom: 30, top: 10 }, xAxis: { type: 'category', data: Object.keys(td) }, yAxis: { type: 'value' }, series: [{ data: Object.values(td), type: 'line', smooth: true, areaStyle: { opacity: 0.35 }, itemStyle: { color: '#f44336' }, lineStyle: { width: 3 } }] })

    // 迷你饼图
    if (detectPieChart.value) {
      const c4 = initChart(detectPieChart.value)
      c4.setOption({ series: [{ type: 'pie', radius: ['55%', '80%'], center: ['50%', '50%'], data: [{ name: '良好', value: detectStats.value.good, itemStyle: { color: '#4caf50' } }, { name: '超标', value: detectStats.value.over, itemStyle: { color: '#f44336' } }], label: { show: false } }] })
    }
  } catch {
    ElMessage.error('加载可视化数据失败，请检查后端服务')
  }
})

function cityCoverCount(province: string) {
  const cities = provinceCities[province] || []
  const covered = cities.filter(c => coverStats.value.cityList?.includes(c)).length
  return covered + '/' + cities.length
}
</script>

<style scoped>
.dv-page{min-height:100vh;background:linear-gradient(160deg,#0a1628 0%,#1a2a4a 30%,#0d2137 60%,#1a1a3e 100%)}
.top-banner{width:100%;background:linear-gradient(135deg,#0d47a1 0%,#1565c0 40%,#1976d2 100%);color:#fff;display:flex;align-items:center;justify-content:center;gap:10px;padding:14px 0;font-size:18px;letter-spacing:2px}
.main-container{padding:12px 20px 20px;max-width:1600px;margin:0 auto}
.row{margin-bottom:12px}
h3{margin:0;font-size:14px;color:#90caf9}
.chart{width:100%;height:320px}
.mini-chart{height:100%;min-height:90px}

.card{border-radius:10px;padding:16px 20px;color:#fff;text-align:center}
.card.c1{background:linear-gradient(135deg,#1565c0,#1976d2)}.card.c2{background:linear-gradient(135deg,#2e7d32,#43a047)}
.card.c3{background:linear-gradient(135deg,#c62828,#e53935)}.card.c4{background:linear-gradient(135deg,#6a1b9a,#8e24aa)}
.card.c5{background:linear-gradient(135deg,#00695c,#00897b)}
.card-n{font-size:36px;font-weight:700}.card-n-s{font-size:28px;font-weight:700}.card-t{font-size:12px;opacity:0.85;margin-top:4px}

.cover-panel{padding:4px}
.cover-val{font-size:22px;font-weight:700;color:#90caf9}.cover-val small{font-size:14px;color:#546e7a}.cover-label{font-size:11px;color:#78909c;margin-bottom:4px}

.province-grid-sm{display:flex;flex-wrap:wrap;gap:3px;margin-top:10px;justify-content:center}
.pg-sm{width:38px;height:20px;display:flex;align-items:center;justify-content:center;font-size:9px;border-radius:3px;background:#37474f;color:#78909c;cursor:pointer;transition:.2s}
.pg-sm.covered{background:#4caf50;color:#fff;font-weight:600}
.pg-sm.active{outline:2px solid #ff9800;outline-offset:1px}
.pg-cnt{font-size:7px;opacity:0.8;margin-left:2px}
.grid-legend-sm{display:flex;gap:12px;justify-content:center;margin-bottom:6px;font-size:10px;color:#90a4ae;align-items:center}
.dot-sm{width:8px;height:8px;border-radius:2px;background:#37474f;display:inline-block}.dot-sm.covered{background:#4caf50}

.city-grid-sm{display:flex;flex-wrap:wrap;gap:3px;justify-content:center}
.cg-sm{padding:2px 8px;font-size:9px;border-radius:3px;background:#37474f;color:#78909c}
.cg-sm.covered{background:#66bb6a;color:#fff;font-weight:500}

.over{color:#f44336;font-weight:700}
.mini-bar{height:10px;background:#263238;border-radius:5px;overflow:hidden}
.mini-bar-fill{height:100%;border-radius:5px;min-width:4px;transition:width.3s}
</style>
