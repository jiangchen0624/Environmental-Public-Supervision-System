<template>
  <div class="dv-page">
    <div class="top-banner">
      <el-button
        type="default"
        size="small"
        @click="router.push('/')"
        style="position: absolute; left: 20px"
        ><el-icon><ArrowLeft /></el-icon> 首页</el-button
      ><el-icon :size="28"><Monitor /></el-icon><span>NEPV 决策者可视化大屏</span
      ><el-tag type="primary" size="small" style="margin-left: 12px">{{
        userStore.userRole
      }}</el-tag
      ><el-button
        type="danger"
        size="small"
        @click="userStore.logout(); router.push('/')"
        style="position: absolute; right: 20px"
        >退出</el-button
      >
    </div>

    <!-- 时间范围选择 -->
    <div class="time-bar">
      <span class="time-label">数据范围：</span>
      <el-radio-group v-model="timeRange" @change="onTimeRangeChange" size="small">
        <el-radio-button value="7d">近7天</el-radio-button>
        <el-radio-button value="30d">近30天</el-radio-button>
        <el-radio-button value="12m">近12个月</el-radio-button>
        <el-radio-button value="all">全部</el-radio-button>
      </el-radio-group>
      <span class="time-range-text">{{ timeRangeText }}</span>
    </div>

    <div class="main-container">
      <!-- Row 1: 统计卡片 -->
      <el-row :gutter="12" class="row">
        <el-col :span="4"
          ><div class="card c1">
            <div class="card-n">{{ detectStats.total }}</div>
            <div class="card-t">检测累计</div>
            <div class="card-sub">上报记录总数</div>
          </div></el-col
        >
        <el-col :span="4"
          ><div class="card c2">
            <div class="card-n">
              {{ detectStats.good }}<small> {{ goodRate }}%</small>
            </div>
            <div class="card-t">结果良好</div>
            <div class="card-sub">AQI等级 ≤ 2</div>
          </div></el-col
        >
        <el-col :span="4"
          ><div class="card c3">
            <div class="card-n">
              {{ detectStats.over }}<small> {{ overRate }}%</small>
            </div>
            <div class="card-t">结果超标</div>
            <div class="card-sub">AQI等级 ≥ 3</div>
          </div></el-col
        >
        <el-col :span="4"
          ><div class="card c4">
            <div class="card-n">
              {{ coverStats.coveredProvinces }}<small>/{{ coverStats.totalProvinces }}</small>
            </div>
            <div class="card-t">省覆盖率</div>
            <div class="card-sub">{{ coverStats.provinceRate }}%</div>
          </div></el-col
        >
        <el-col :span="4"
          ><div class="card c5">
            <div class="card-n">
              {{ coverStats.coveredCities }}<small>/{{ coverStats.totalCities }}</small>
            </div>
            <div class="card-t">城市覆盖率</div>
            <div class="card-sub">{{ coverStats.cityRate }}%</div>
          </div></el-col
        >
        <el-col :span="4"><div ref="detectPieChart" class="mini-chart" /></el-col>
      </el-row>

      <!-- Row 2: 省分组统计(表格+高亮) + AQI分布 -->
      <el-row :gutter="12" class="row">
        <el-col :span="14">
          <el-card shadow="hover"
            ><template #header><h3>📊 各省空气质量统计（等级1-2合格，3-5超标）</h3></template>
            <el-table
              :data="provTable"
              border
              stripe
              size="small"
              max-height="380"
              style="font-size: 12px"
            >
              <el-table-column prop="province" label="省" width="60" fixed />
              <el-table-column label="检测数" width="52" prop="total" />
              <el-table-column label="SO₂超标" width="74"
                ><template #default="{ row }"
                  ><span :class="{ over: row.so2超标 }">{{ row.so2超标 }}</span
                  ><span class="sub-pct">{{ row.so2Pct }}%</span></template
                ></el-table-column
              >
              <el-table-column label="CO超标" width="70"
                ><template #default="{ row }"
                  ><span :class="{ over: row.co超标 }">{{ row.co超标 }}</span
                  ><span class="sub-pct">{{ row.coPct }}%</span></template
                ></el-table-column
              >
              <el-table-column label="PM2.5超标" width="80"
                ><template #default="{ row }"
                  ><span :class="{ over: row.pm25超标 }">{{ row.pm25超标 }}</span
                  ><span class="sub-pct">{{ row.pm25Pct }}%</span></template
                ></el-table-column
              >
              <el-table-column label="AQI不合格" width="78"
                ><template #default="{ row }"
                  ><span :class="{ over: row.aqi超标 }">{{ row.aqi超标 }}</span
                  ><span class="sub-pct">{{ row.maxPct }}%</span></template
                ></el-table-column
              >
              <el-table-column label="不合格率" min-width="130"
                ><template #default="{ row }"
                  ><div class="rate-cell">
                    <span
                      class="rate-num"
                      :style="{
                        color:
                          row.maxPct > 50 ? '#f44336' : row.maxPct > 20 ? '#ff9800' : '#4caf50',
                      }"
                      >{{ row.maxPct }}%</span
                    >
                    <div class="mini-bar">
                      <div
                        class="mini-bar-fill"
                        :style="{
                          width: Math.min(row.maxPct, 100) + '%',
                          background:
                            row.maxPct > 50 ? '#f44336' : row.maxPct > 20 ? '#ff9800' : '#4caf50',
                        }"
                      ></div>
                    </div></div></template
              ></el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="10">
          <el-card shadow="hover"
            ><template #header><h3>🥧 AQI指数分布统计</h3></template>
            <div ref="distChart" class="chart"
          /></el-card>
        </el-col>
      </el-row>

      <!-- Row 3: 趋势 + 覆盖率(可点击下钻) -->
      <el-row :gutter="12" class="row">
        <el-col :span="14">
          <el-card shadow="hover"
            ><template #header><h3>📈 月度检测数量趋势</h3></template>
            <div ref="trendChart" class="chart"
          /></el-card>
        </el-col>
        <el-col :span="10">
          <el-card shadow="hover"
            ><template #header><h3>🗺️ 全国网格覆盖率</h3></template>
            <div class="cover-panel">
              <div class="cover-item">
                <div class="cover-val">
                  {{ coverStats.coveredProvinces }}<small>/{{ coverStats.totalProvinces }}</small>
                </div>
                <div class="cover-label">覆盖省份</div>
                <el-progress
                  :percentage="coverStats.provinceRate"
                  :color="cColor(coverStats.provinceRate)"
                  :stroke-width="14"
                />
              </div>
              <div class="cover-item" style="margin-top: 10px">
                <div class="cover-val">
                  {{ coverStats.coveredCities }}<small>/{{ coverStats.totalCities }}</small>
                </div>
                <div class="cover-label">覆盖大城市 (全国{{ coverStats.totalCities }}个)</div>
                <el-progress
                  :percentage="coverStats.cityRate"
                  :color="cColor(coverStats.cityRate)"
                  :stroke-width="14"
                />
              </div>
              <div class="grid-legend-sm">
                <span class="dot-sm covered" />已覆盖：{{ coverStats.coveredProvinces }}
                <span class="dot-sm" />未覆盖：{{
                  coverStats.totalProvinces - coverStats.coveredProvinces
                }}
              </div>
              <div class="province-grid-sm">
                <div
                  v-for="p in provinceGrid"
                  :key="p.name"
                  class="pg-sm"
                  :class="{ covered: p.covered, active: selProvince === p.name }"
                  @click="selProvince = selProvince === p.name ? '' : p.name"
                  :title="p.covered ? '已覆盖 ' + cityCoverCount(p.name) + ' 市' : '未覆盖'"
                >
                  {{ p.name
                  }}<span v-if="p.covered" class="pg-cnt">{{ cityCoverCount(p.name) }}</span>
                </div>
              </div>
              <div
                v-if="selProvince"
                style="margin-top: 10px; border-top: 1px solid #37474f; padding-top: 8px"
              >
                <div style="font-size: 11px; color: #90caf9; margin-bottom: 6px">
                  {{ selProvince }} 已覆盖 {{ cityCoverCount(selProvince) }} 市
                </div>
                <div class="city-grid-sm">
                  <div
                    v-for="c in selCityGrid"
                    :key="c.name"
                    class="cg-sm"
                    :class="{ covered: c.covered }"
                  >
                    {{ c.name }}
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- Row 4: 中国地图可视化 -->
      <el-row :gutter="12" class="row">
        <el-col :span="24">
          <el-card shadow="hover">
            <template #header>
              <div class="map-header">
                <h3>🗺️ 全国AQI超标分布地图</h3>
                <el-button v-if="drillProvince" type="primary" size="small" @click="backToNational">
                  <el-icon><ArrowLeft /></el-icon> 返回全国
                </el-button>
              </div>
            </template>
            <div v-loading="mapLoading" class="map-container" ref="mapChartEl"></div>
            <div class="map-legend">
              <span class="legend-label">AQI超标数：</span>
              <span class="legend-item"
                ><span class="legend-color" style="background: #1b5e20"></span>0</span
              >
              <span class="legend-item"
                ><span class="legend-color" style="background: #4caf50"></span>1-2</span
              >
              <span class="legend-item"
                ><span class="legend-color" style="background: #ff9800"></span>3-5</span
              >
              <span class="legend-item"
                ><span class="legend-color" style="background: #f44336"></span>6-10</span
              >
              <span class="legend-item"
                ><span class="legend-color" style="background: #b71c1c"></span>&gt;10</span
              >
              <span class="legend-item"
                ><span class="legend-color" style="background: #37474f"></span>无数据</span
              >
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

interface DetectStats {
  total: number
  good: number
  over: number
}
interface CoverageStats {
  coveredProvinces: number
  totalProvinces: number
  provinceRate: number
  coveredCities: number
  totalCities: number
  cityRate: number
  provinceList: string[]
  cityList: string[]
}
interface ProvinceTableRow {
  province: string
  total: number
  so2超标: number
  so2Pct: number
  co超标: number
  coPct: number
  pm25超标: number
  pm25Pct: number
  aqi超标: number
  maxPct: number
}
interface CityGridItem {
  name: string
  covered: boolean
}
interface GeoFeature {
  type: 'Feature'
  properties: {
    name: string
    cityCode: number
    center?: [number, number]
    provinceAdcode: number
    districtCount: number
  }
  geometry: Record<string, unknown>
}
interface GeoJSONData {
  type: 'FeatureCollection'
  features: GeoFeature[]
}

const userStore = useUserStore()
const detectStats = ref<DetectStats>({ total: 0, good: 0, over: 0 })
const coverStats = ref<CoverageStats>({
  coveredProvinces: 0,
  totalProvinces: 34,
  provinceRate: 0,
  coveredCities: 0,
  totalCities: 373,
  cityRate: 0,
  provinceList: [] as string[],
  cityList: [] as string[],
})
const distChart = ref<HTMLElement>()
const trendChart = ref<HTMLElement>()
const detectPieChart = ref<HTMLElement>()
const mapChartEl = ref<HTMLElement>()
const provTable = ref<ProvinceTableRow[]>([])
const provinceData = ref<Record<string, Record<string, number>>>({})
const selProvince = ref('')
const timeRange = ref('all')
const mapLoading = ref(false)
const drillProvince = ref('')
const geoJSON = ref<GeoJSONData | null>(null)

// 34个省级行政区 → adcode 映射
const PROVINCE_ADCODE_MAP: Record<string, number> = {
  北京: 110000,
  天津: 120000,
  河北: 130000,
  山西: 140000,
  内蒙古: 150000,
  辽宁: 210000,
  吉林: 220000,
  黑龙江: 230000,
  上海: 310000,
  江苏: 320000,
  浙江: 330000,
  安徽: 340000,
  福建: 350000,
  江西: 360000,
  山东: 370000,
  河南: 410000,
  湖北: 420000,
  湖南: 430000,
  广东: 440000,
  广西: 450000,
  海南: 460000,
  重庆: 500000,
  四川: 510000,
  贵州: 520000,
  云南: 530000,
  西藏: 540000,
  陕西: 610000,
  甘肃: 620000,
  青海: 630000,
  宁夏: 640000,
  新疆: 650000,
  台湾: 100000,
  香港: 810000,
  澳门: 820000,
}

const ADCODE_PROVINCE_MAP: Record<number, string> = Object.fromEntries(
  Object.entries(PROVINCE_ADCODE_MAP).map(([k, v]) => [v, k]),
)

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
const goodRate = computed(() => {
  const t = detectStats.value.total
  return t > 0 ? Math.round((detectStats.value.good / t) * 100) : 0
})
const overRate = computed(() => {
  const t = detectStats.value.total
  return t > 0 ? Math.round((detectStats.value.over / t) * 100) : 0
})

function colorByRate(r: number): string {
  if (r < 30) return '#f44336'
  if (r < 60) return '#ff9800'
  if (r < 80) return '#2196f3'
  return '#4caf50'
}
const cColor = colorByRate

function mapColorByOver(over: number): string {
  if (over === 0) return '#1b5e20'
  if (over <= 2) return '#4caf50'
  if (over <= 5) return '#ff9800'
  if (over <= 10) return '#f44336'
  return '#b71c1c'
}

const provinceGrid = computed<{ name: string; covered: boolean }[]>(() => {
  const list = allProvinces.map((p: string) => ({
    name: p,
    covered: coverStats.value.provinceList.includes(p),
  }))
  return list.sort((a, b) => (b.covered ? 1 : -1) - (a.covered ? 1 : -1))
})

function normCity(n: string) {
  return n.replace(/市$/, '').replace(/盟$/, '').replace(/地区$/, '')
}
const coveredCityNorm = computed(() => new Set((coverStats.value.cityList || []).map(normCity)))
const provinceCities: Record<string, string[]> = provinceCitiesMap
const selCityGrid = computed<CityGridItem[]>(() => {
  const cities: string[] = provinceCities[selProvince.value] || []
  return cities.map((c: string) => ({
    name: c,
    covered: coveredCityNorm.value.has(normCity(c)),
  }))
})

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

function renderDistChart(data: Record<string, number>) {
  if (!distChart.value) return
  const total = Object.values(data).reduce((a, b) => a + b, 0)
  const c2 = initChart(distChart.value)
  c2.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} 条 ({d}%)' },
    graphic:
      total > 0
        ? [
            {
              type: 'text',
              left: 'center',
              top: '42%',
              style: {
                text: `共${total}条`,
                textAlign: 'center',
                fill: '#90caf9',
                fontSize: 13,
                fontWeight: 'bold',
              },
            },
            {
              type: 'text',
              left: 'center',
              top: '52%',
              style: { text: 'AQI等级分布', textAlign: 'center', fill: '#546e7a', fontSize: 10 },
            },
          ]
        : [],
    series: [
      {
        type: 'pie',
        radius: ['45%', '75%'],
        center: ['50%', '50%'],
        data: Object.entries(data).map(([k, v]) => ({
          name: k,
          value: v,
          itemStyle: {
            color:
              k === '优'
                ? '#4caf50'
                : k === '良'
                  ? '#2196f3'
                  : k.includes('轻度')
                    ? '#ff9800'
                    : k.includes('中度')
                      ? '#f44336'
                      : '#9c27b0',
          },
        })),
        label: { formatter: '{b}: {c}条\n({d}%)', fontSize: 10 },
      },
    ],
  })
}

function renderTrendChart(data: Record<string, number>) {
  if (!trendChart.value) return
  const vals = Object.values(data)
  const c3 = initChart(trendChart.value)
  c3.setOption({
    tooltip: { trigger: 'axis', formatter: '{b}<br/>检测数量: {c} 条' },
    grid: { left: 50, right: 20, bottom: 35, top: 15 },
    xAxis: { type: 'category', data: Object.keys(data), axisLabel: { rotate: 30, fontSize: 10 } },
    yAxis: {
      type: 'value',
      name: '检测数(条)',
      nameTextStyle: { color: '#78909c', fontSize: 11 },
      min: 0,
      max: Math.max(...vals, 10) + 2,
    },
    series: [
      {
        data: Object.values(data),
        type: 'line',
        smooth: true,
        areaStyle: { opacity: 0.35 },
        itemStyle: { color: '#f44336' },
        lineStyle: { width: 3 },
        markLine:
          vals.length > 0
            ? {
                silent: true,
                data: [
                  {
                    type: 'average',
                    name: '均值',
                    lineStyle: { color: '#ff9800', type: 'dashed' },
                  },
                ],
              }
            : undefined,
      },
    ],
  })
}

function renderDetectPieChart() {
  if (!detectPieChart.value) return
  const c4 = initChart(detectPieChart.value)
  const t = detectStats.value.total || 1
  const goodPct = Math.round((detectStats.value.good / t) * 100)
  c4.setOption({
    graphic: [
      {
        type: 'text',
        left: 'center',
        top: '38%',
        style: {
          text: `${goodPct}%`,
          textAlign: 'center',
          fill: '#fff',
          fontSize: 13,
          fontWeight: 'bold',
        },
      },
      {
        type: 'text',
        left: 'center',
        top: '55%',
        style: { text: '良好率', textAlign: 'center', fill: '#90caf9', fontSize: 9 },
      },
    ],
    series: [
      {
        type: 'pie',
        radius: ['55%', '80%'],
        center: ['50%', '50%'],
        data: [
          { name: '良好', value: detectStats.value.good, itemStyle: { color: '#4caf50' } },
          { name: '超标', value: detectStats.value.over, itemStyle: { color: '#f44336' } },
        ],
        label: { show: false },
      },
    ],
  })
}

async function renderMap() {
  if (!mapChartEl.value) return
  mapLoading.value = true

  try {
    if (!geoJSON.value) {
      const res = await fetch('/china-city-level.json')
      geoJSON.value = (await res.json()) as GeoJSONData
    }
    const geo = geoJSON.value

    // 获取市级AQI超标数据
    const cityRes = await get('/stats/city-all', timeParams.value)
    const cityAqiData = (cityRes.data || {}) as Record<string, Record<string, number>>

    // 构建 归一化城市名 → AQI超标/原始名 映射
    const cityOverMap: Record<string, number> = {}
    const normToOrig: Record<string, string> = {}
    for (const [city, d] of Object.entries(cityAqiData)) {
      const n = normCity(city)
      cityOverMap[n] = d['aqi超标'] || 0
      normToOrig[n] = city
    }

    if (drillProvince.value) {
      // 省级下钻：仅显示该省的城市
      const provinceAdcode = PROVINCE_ADCODE_MAP[drillProvince.value]
      const provinceCities = geo.features.filter(
        (f: GeoFeature) => f.properties.provinceAdcode === provinceAdcode,
      )
      const drillGeo: GeoJSONData = { type: 'FeatureCollection', features: provinceCities }
      echarts.registerMap(
        'drill-province',
        drillGeo as unknown as Parameters<typeof echarts.registerMap>[1],
      )

      const data = provinceCities.map((f: GeoFeature) => {
        const name = f.properties.name
        const aqiOver = cityOverMap[normCity(name)] ?? -1
        const color = aqiOver >= 0 ? mapColorByOver(aqiOver) : '#37474f'
        return { name, itemStyle: { areaColor: color } }
      })

      const instance = initChart(mapChartEl.value)
      instance.setOption({
        tooltip: {
          trigger: 'item',
          formatter: (params: { name: string }) => {
            const n = normCity(params.name)
            const aqi = cityOverMap[n] ?? 0
            const origName = normToOrig[n]
            const cd = origName ? cityAqiData[origName] : undefined
            const so2 = cd?.['so2超标'] ?? 0
            const co = cd?.['co超标'] ?? 0
            const pm25 = cd?.['pm25超标'] ?? 0
            const total = cd?.['total'] ?? 0
            const rate = total > 0 ? Math.round((aqi / total) * 100) : 0
            return `<b>${params.name}</b><br/>检测总数: ${total} | AQI不合格: ${aqi}条 (${rate}%)<br/>SO₂超标: ${so2}条 | CO超标: ${co}条 | PM₂.₅超标: ${pm25}条`
          },
        },
        series: [
          {
            type: 'map',
            map: 'drill-province',
            data,
            label: { show: true, color: '#90caf9', fontSize: 11 },
            itemStyle: { borderColor: '#1a2a4a', borderWidth: 1 },
            emphasis: { label: { color: '#fff' }, itemStyle: { areaColor: '#ff9800' } },
          },
        ],
      })
    } else {
      // 全国视图：每个城市按自身AQI超标数着色
      echarts.registerMap('china', geo as unknown as Parameters<typeof echarts.registerMap>[1])

      const data = geo.features.map((f: GeoFeature) => {
        const name = f.properties.name
        const aqiOver = cityOverMap[normCity(name)] ?? -1
        const color = aqiOver >= 0 ? mapColorByOver(aqiOver) : '#37474f'
        return { name, itemStyle: { areaColor: color } }
      })

      const instance = initChart(mapChartEl.value)
      instance.setOption({
        tooltip: {
          trigger: 'item',
          formatter: (params: { name: string }) => {
            const n = normCity(params.name)
            const aqi = cityOverMap[n] ?? 0
            // find matching city data
            const origName = normToOrig[n]
            const cd = origName ? cityAqiData[origName] : undefined
            const so2 = cd?.['so2超标'] ?? 0
            const co = cd?.['co超标'] ?? 0
            const pm25 = cd?.['pm25超标'] ?? 0
            const feat = geo.features.find((f: GeoFeature) => f.properties.name === params.name)
            const provName = feat ? ADCODE_PROVINCE_MAP[feat.properties.provinceAdcode] || '' : ''
            const total = cd?.['total'] ?? 0
            const rate = total > 0 ? Math.round((aqi / total) * 100) : 0
            return `<b>${params.name}</b> (${provName})<br/>检测总数: ${total} | AQI不合格: ${aqi}条 (${rate}%)<br/>SO₂超标: ${so2}条 | CO超标: ${co}条 | PM₂.₅超标: ${pm25}条`
          },
        },
        series: [
          {
            type: 'map',
            map: 'china',
            data,
            label: { show: false },
            itemStyle: { borderColor: '#1a2a4a', borderWidth: 0.3 },
            emphasis: {
              label: { show: true, color: '#fff', fontSize: 9 },
              itemStyle: { areaColor: '#ff9800' },
            },
          },
        ],
      })

      // 点击下钻到省
      instance.off('click')
      instance.on('click', (params: { componentType?: string; name?: string }) => {
        if (params.componentType === 'series' && params.name) {
          const feat = geo.features.find((f: GeoFeature) => f.properties.name === params.name)
          if (feat) {
            const provName = ADCODE_PROVINCE_MAP[feat.properties.provinceAdcode]
            if (provName) {
              drillProvince.value = provName
              renderMap()
            }
          }
        }
      })
    }
  } catch {
    ElMessage.error('地图加载失败，请检查网络连接')
  } finally {
    mapLoading.value = false
  }
}

async function fetchAndRenderAll() {
  try {
    const params = timeParams.value
    const [prov, dist, trend, detect, cover] = await Promise.all([
      get('/stats/province', params),
      get('/stats/distribution', params),
      get('/stats/trend', params),
      get('/stats/detection', params),
      get('/stats/coverage', params),
    ])

    detectStats.value = (detect.data as DetectStats) || { total: 0, good: 0, over: 0 }
    coverStats.value = (cover.data as CoverageStats) || coverStats.value
    provinceData.value = (prov.data as Record<string, Record<string, number>>) || {}

    const pd = provinceData.value
    provTable.value = Object.entries(pd).map(([k, v]) => {
      const aqi = v['aqi超标'] ?? 0
      const total = v['total'] ?? Math.max(aqi, 1)
      const so2 = v['so2超标'] ?? 0
      const co = v['co超标'] ?? 0
      const pm25 = v['pm25超标'] ?? 0
      const base = Math.max(total, 1)
      return {
        province: k,
        total,
        so2超标: so2,
        so2Pct: Math.round((so2 / base) * 100),
        co超标: co,
        coPct: Math.round((co / base) * 100),
        pm25超标: pm25,
        pm25Pct: Math.round((pm25 / base) * 100),
        aqi超标: aqi,
        maxPct: Math.round((aqi / base) * 100),
      }
    })

    await nextTick()

    renderDistChart((dist.data || {}) as Record<string, number>)
    renderTrendChart((trend.data || {}) as Record<string, number>)
    renderDetectPieChart()
    renderMap()
  } catch {
    ElMessage.error('加载可视化数据失败，请检查后端服务')
  }
}

function onTimeRangeChange() {
  drillProvince.value = ''
  fetchAndRenderAll()
}

function backToNational() {
  drillProvince.value = ''
  renderMap()
}

// 窗口缩放时自适应图表
function onResize() {
  chartInstances.forEach((c) => c.resize())
}
onMounted(() => {
  fetchAndRenderAll()
  window.addEventListener('resize', onResize)
})
onUnmounted(() => {
  chartInstances.forEach((c) => c.dispose())
  window.removeEventListener('resize', onResize)
})

function cityCoverCount(province: string): string {
  const cities: string[] = provinceCities[province] || []
  const covered = cities.filter((c) => coveredCityNorm.value.has(normCity(c))).length
  return covered + '/' + cities.length
}
</script>

<style scoped>
.dv-page {
  min-height: 100vh;
  background: linear-gradient(160deg, #0a1628 0%, #1a2a4a 30%, #0d2137 60%, #1a1a3e 100%);
}
.top-banner {
  width: 100%;
  background: linear-gradient(135deg, #0d47a1 0%, #1565c0 40%, #1976d2 100%);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 14px 0;
  font-size: 18px;
  letter-spacing: 2px;
}
.main-container {
  padding: 12px 20px 20px;
  max-width: 1600px;
  margin: 0 auto;
}
.row {
  margin-bottom: 12px;
  align-items: stretch;
}
.row .el-card {
  height: 100%;
}
h3 {
  margin: 0;
  font-size: 14px;
  color: #90caf9;
}
.chart {
  width: 100%;
  height: 380px;
}
.mini-chart {
  height: 100%;
  min-height: 90px;
}

/* 时间范围选择条 */
.time-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 0 2px;
  gap: 8px;
}
.time-label {
  color: #90caf9;
  font-size: 13px;
}
.time-range-text {
  color: #78909c;
  font-size: 11px;
  margin-left: 8px;
}

.card {
  border-radius: 10px;
  padding: 14px 16px;
  color: #fff;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: 90px;
}
.card.c1 {
  background: linear-gradient(135deg, #1565c0, #1976d2);
}
.card.c2 {
  background: linear-gradient(135deg, #2e7d32, #43a047);
}
.card.c3 {
  background: linear-gradient(135deg, #c62828, #e53935);
}
.card.c4 {
  background: linear-gradient(135deg, #6a1b9a, #8e24aa);
}
.card.c5 {
  background: linear-gradient(135deg, #00695c, #00897b);
}
.card-n {
  font-size: 30px;
  font-weight: 700;
  line-height: 1.1;
}
.card-n small {
  font-size: 14px;
  font-weight: 400;
  opacity: 0.8;
  margin-left: 2px;
}
.card-n-s {
  font-size: 28px;
  font-weight: 700;
}
.card-t {
  font-size: 12px;
  opacity: 0.9;
  margin-top: 2px;
}
.card-sub {
  font-size: 10px;
  opacity: 0.65;
  margin-top: 1px;
}

.cover-panel {
  padding: 4px;
}
.cover-val {
  font-size: 22px;
  font-weight: 700;
  color: #90caf9;
}
.cover-val small {
  font-size: 14px;
  color: #546e7a;
}
.cover-label {
  font-size: 11px;
  color: #78909c;
  margin-bottom: 4px;
}

.province-grid-sm {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  margin-top: 10px;
  justify-content: center;
}
.pg-sm {
  width: 44px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  border-radius: 3px;
  background: #37474f;
  color: #90a4ae;
  cursor: pointer;
  transition: 0.2s;
  position: relative;
}
.pg-sm:hover {
  transform: scale(1.1);
  z-index: 1;
}
.pg-sm.covered {
  background: #4caf50;
  color: #fff;
  font-weight: 600;
}
.pg-sm.active {
  outline: 2px solid #ff9800;
  outline-offset: 1px;
}
.pg-cnt {
  font-size: 8px;
  opacity: 0.8;
  margin-left: 2px;
}
.grid-legend-sm {
  display: flex;
  gap: 14px;
  justify-content: center;
  margin-bottom: 8px;
  font-size: 10px;
  color: #90a4ae;
  align-items: center;
}
.dot-sm {
  width: 10px;
  height: 10px;
  border-radius: 2px;
  background: #37474f;
  display: inline-block;
}
.dot-sm.covered {
  background: #4caf50;
}

.city-grid-sm {
  display: flex;
  flex-wrap: wrap;
  gap: 3px;
  justify-content: center;
}
.cg-sm {
  padding: 2px 8px;
  font-size: 9px;
  border-radius: 3px;
  background: #37474f;
  color: #78909c;
}
.cg-sm.covered {
  background: #66bb6a;
  color: #fff;
  font-weight: 500;
}

.over {
  color: #f44336;
  font-weight: 700;
}
.sub-pct {
  font-size: 9px;
  color: #546e7a;
  margin-left: 2px;
}
.rate-cell {
  display: flex;
  align-items: center;
  gap: 4px;
}
.rate-num {
  font-size: 11px;
  font-weight: 600;
  min-width: 32px;
  display: inline-block;
  text-align: right;
}
.mini-bar {
  height: 10px;
  background: #263238;
  border-radius: 5px;
  overflow: hidden;
  flex: 1;
  min-width: 40px;
}
.mini-bar-fill {
  height: 100%;
  border-radius: 5px;
  min-width: 4px;
  transition: width.3s;
}

/* 地图区块 */
.map-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.map-container {
  width: 100%;
  height: 750px;
}
.map-legend {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 8px 0 4px;
  flex-wrap: wrap;
}
.legend-label {
  color: #90caf9;
  font-size: 11px;
}
.legend-item {
  display: flex;
  align-items: center;
  gap: 3px;
  color: #78909c;
  font-size: 10px;
}
.legend-color {
  width: 14px;
  height: 10px;
  border-radius: 2px;
  display: inline-block;
}
</style>
