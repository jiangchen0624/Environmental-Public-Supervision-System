<template>
  <div class="report-page">
    <div class="top-banner">
      <el-button type="default" size="small" @click="router.push('/')" style="position:absolute;left:20px"><el-icon><ArrowLeft /></el-icon> 首页</el-button>
      <el-icon :size="28"><Monitor /></el-icon>
      <span>NEU 空气质量监测系统 — 监督上报</span>
      <el-button type="danger" size="small" @click="userStore.logout();router.push('/')" style="position:absolute;right:20px">退出</el-button>
    </div>

    <div class="layout">
      <div class="sidebar">
        <el-menu :default-active="tab" @select="tab=$event">
          <el-menu-item index="submit">📝 提交上报</el-menu-item>
          <el-menu-item index="history">📋 我的上报记录</el-menu-item>
        </el-menu>
      </div>
      <div class="content">
        <template v-if="tab==='submit'">
      <!-- 口诀 -->
      <el-card class="guide-card">
        <template #header><div class="card-title"><el-icon><Sunny /></el-icon> 简易判别口诀</div></template>
        <div class="guide-text">
          <span>看远景清浊，判颗粒 <strong>PM2.5</strong></span>
          <span class="sep">|</span>
          <span>闻硫呛异味，判 <strong>二氧化硫 SO₂</strong></span>
          <span class="sep">|</span>
          <span>闷洼头晕沉，判 <strong>一氧化碳 CO</strong></span>
          <span class="sep">|</span>
          <span>三项取最差，定空气等级</span>
        </div>
      </el-card>

      <!-- 五级判定标准参考 -->
      <el-collapse v-model="activeGuide" style="margin-top: 12px">
        <el-collapse-item title="📖 五级空气质量人工判定标准（点击展开参考）" name="guide">
          <el-table :data="criteriaGuide" border stripe size="small" style="font-size: 13px">
            <el-table-column prop="level" label="等级" width="100" />
            <el-table-column prop="label" label="名称" width="100" />
            <el-table-column prop="visual" label="👁️ 视觉判断" min-width="200" />
            <el-table-column prop="smell" label="👃 嗅觉判断" min-width="200" />
            <el-table-column prop="body" label="🫁 体感判断" min-width="180" />
            <el-table-column prop="summary" label="综合判定" min-width="180" />
          </el-table>
        </el-collapse-item>
      </el-collapse>

      <el-row :gutter="20" style="margin-top: 16px">
        <!-- PM2.5 -->
        <el-col :span="8">
          <el-card class="pollutant-card" shadow="hover">
            <template #header>
              <div class="pollutant-header pm25">🌫️ PM2.5 · 视觉判别 <span class="method">（看远景 / 看天色 / 看悬浮物）</span></div>
            </template>
            <el-collapse>
              <el-collapse-item title="📋 PM2.5 判别要点">
                <ul class="criteria-list">
                  <li><strong>看远景：</strong>远山、高楼越模糊 → PM2.5 ↑</li>
                  <li><strong>看天色：</strong>天蓝通透=优，灰白/发黄/暗沉=污染</li>
                  <li><strong>看悬浮物：</strong>空气有漂浮灰尘、行车有浮尘感 → 颗粒物偏高</li>
                  <li><strong>1级优：</strong>天空湛蓝通透，远处清晰，阳光清亮</li>
                  <li><strong>2级良：</strong>轻微泛白薄雾，远处略微朦胧</li>
                  <li><strong>3级轻度：</strong>灰蒙蒙，3-5公里外模糊，阳光发白</li>
                  <li><strong>4级中度：</strong>明显灰霾，远处完全模糊，天色偏黄发灰</li>
                  <li><strong>5级重度：</strong>重度灰霾笼罩，近处楼宇都看不清，天色土黄灰黑</li>
                </ul>
              </el-collapse-item>
            </el-collapse>
            <el-form label-position="top" style="margin-top: 8px">
              <el-form-item label="PM2.5 颗粒物等级">
                <el-radio-group v-model="pm25Level" size="large">
                  <el-radio-button :value="1"><div class="lv"><b>1</b><i>优</i></div></el-radio-button>
                  <el-radio-button :value="2"><div class="lv"><b>2</b><i>良</i></div></el-radio-button>
                  <el-radio-button :value="3"><div class="lv"><b>3</b><i>轻度</i></div></el-radio-button>
                  <el-radio-button :value="4"><div class="lv"><b>4</b><i>中度</i></div></el-radio-button>
                  <el-radio-button :value="5"><div class="lv"><b>5</b><i>重度</i></div></el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>

        <!-- SO2 -->
        <el-col :span="8">
          <el-card class="pollutant-card" shadow="hover">
            <template #header>
              <div class="pollutant-header so2">👃 SO₂ · 嗅觉判别 <span class="method">（闻硫磺味 / 烧煤味 / 酸涩呛味）</span></div>
            </template>
            <el-collapse>
              <el-collapse-item title="📋 SO₂ 判别要点">
                <ul class="criteria-list">
                  <li><strong>识别方式：</strong>靠嗅觉 + 咽喉刺激</li>
                  <li><strong>核心特征：</strong>硫磺味、烧煤味、酸涩呛味；吸入喉咙发干发紧想咳嗽</li>
                  <li><strong>高发地点：</strong>工厂周边、燃煤锅炉房、小型作坊、排烟餐饮区</li>
                  <li><strong>1级优：</strong>空气清新，无任何刺激性异味</li>
                  <li><strong>2级良：</strong>仅有轻微尘土味，无硫磺呛味</li>
                  <li><strong>3级轻度：</strong>靠近污染源能闻到淡淡烧煤味、轻微硫磺涩味</li>
                  <li><strong>4级中度：</strong>明显呛人，浓郁烧煤味、硫磺酸涩味，大口呼吸喉咙发紧</li>
                  <li><strong>5级重度：</strong>强烈刺激性呛味、硫化味，不敢深呼吸</li>
                </ul>
              </el-collapse-item>
            </el-collapse>
            <el-form label-position="top" style="margin-top: 8px">
              <el-form-item label="SO₂ 二氧化硫等级">
                <el-radio-group v-model="so2Level" size="large">
                  <el-radio-button :value="1"><div class="lv"><b>1</b><i>优</i></div></el-radio-button>
                  <el-radio-button :value="2"><div class="lv"><b>2</b><i>良</i></div></el-radio-button>
                  <el-radio-button :value="3"><div class="lv"><b>3</b><i>轻度</i></div></el-radio-button>
                  <el-radio-button :value="4"><div class="lv"><b>4</b><i>中度</i></div></el-radio-button>
                  <el-radio-button :value="5"><div class="lv"><b>5</b><i>重度</i></div></el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>

        <!-- CO -->
        <el-col :span="8">
          <el-card class="pollutant-card" shadow="hover">
            <template #header>
              <div class="pollutant-header co">😵 CO · 体感+场景判别 <span class="method">（无色无味，靠场景+体感）</span></div>
            </template>
            <el-collapse>
              <el-collapse-item title="📋 CO 判别要点">
                <ul class="criteria-list">
                  <li><strong>识别方式：</strong>无色无味看不见闻不到，只能靠场景+体感</li>
                  <li><strong>高风险场景：</strong>车流密集拥堵路段、通风差低洼地带、地下车库、密闭房间、周边有焚烧/燃煤/发电机</li>
                  <li><strong>身体典型信号：</strong>无感冒劳累，突然头晕/犯困/心慌/恶心/四肢无力，到室外通风后快速好转 → 直接判定 CO 异常</li>
                  <li><strong>1级优：</strong>通风良好，身体无任何不适</li>
                  <li><strong>2级良：</strong>体感正常，无头晕胸闷</li>
                  <li><strong>3级轻度：</strong>敏感人群嗓子发干、轻微异物感，无风天气空气沉闷</li>
                  <li><strong>4级中度：</strong>普通人群呼吸发闷、胸口压抑，低洼/车库周边易头昏</li>
                  <li><strong>5级重度：</strong>密闭空间极易头晕恶心，眼涩流泪、持续咳嗽、胸闷气短</li>
                </ul>
              </el-collapse-item>
            </el-collapse>
            <el-form label-position="top" style="margin-top: 8px">
              <el-form-item label="CO 一氧化碳等级">
                <el-radio-group v-model="coLevel" size="large">
                  <el-radio-button :value="1"><div class="lv"><b>1</b><i>优</i></div></el-radio-button>
                  <el-radio-button :value="2"><div class="lv"><b>2</b><i>良</i></div></el-radio-button>
                  <el-radio-button :value="3"><div class="lv"><b>3</b><i>轻度</i></div></el-radio-button>
                  <el-radio-button :value="4"><div class="lv"><b>4</b><i>中度</i></div></el-radio-button>
                  <el-radio-button :value="5"><div class="lv"><b>5</b><i>重度</i></div></el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>

      <!-- 观察项单选 → 自动联动污染物等级 -->
      <el-row :gutter="20" style="margin-top: 16px">
        <!-- 视觉 → PM2.5 -->
        <el-col :span="8">
          <el-card class="observe-card" shadow="hover">
            <template #header><div class="check-title">👁️ 视觉状况 → PM2.5</div></template>
            <el-radio-group v-model="checks.visualLevel" size="small">
              <el-radio :value="1">1级 天空湛蓝通透，远处楼宇山体轮廓清晰，阳光清亮</el-radio>
              <el-radio :value="2">2级 轻微泛白薄雾，远处景物略微朦胧，但不浑浊</el-radio>
              <el-radio :value="3">3级 灰蒙蒙，3-5公里外建筑模糊，阳光发白不透亮</el-radio>
              <el-radio :value="4">4级 明显灰霾，远处完全模糊，天色偏黄发灰，早晚能见度低</el-radio>
              <el-radio :value="5">5级 重度灰霾笼罩，近处楼宇都看不清，天色土黄灰黑</el-radio>
            </el-radio-group>
          </el-card>
        </el-col>
        <!-- 嗅觉 → SO2 -->
        <el-col :span="8">
          <el-card class="observe-card" shadow="hover">
            <template #header><div class="check-title">👃 嗅觉感受 → SO₂</div></template>
            <el-radio-group v-model="checks.smellLevel" size="small">
              <el-radio :value="1">1级 空气清新无异味，无尘土味、无刺激性气味</el-radio>
              <el-radio :value="2">2级 仅有轻微尘土味，无硫磺呛味，无焚烧刺激味</el-radio>
              <el-radio :value="3">3级 可闻到淡淡烧煤味、硫磺涩味，靠近污染源时明显</el-radio>
              <el-radio :value="4">4级 明显呛人，浓郁烧煤味和硫磺酸涩味，大口呼吸喉咙发紧</el-radio>
              <el-radio :value="5">5级 强烈刺激性呛味、硫化味和焚烧异味，不敢深呼吸</el-radio>
            </el-radio-group>
          </el-card>
        </el-col>
        <!-- 体感 → CO -->
        <el-col :span="8">
          <el-card class="observe-card" shadow="hover">
            <template #header><div class="check-title">🫁 身体体感 → CO</div></template>
            <el-radio-group v-model="checks.bodyLevel" size="small">
              <el-radio :value="1">1级 呼吸顺畅无不适，无嗓子干痒，无胸闷头晕</el-radio>
              <el-radio :value="2">2级 体感完全正常，无任何不适，可正常户外活动</el-radio>
              <el-radio :value="3">3级 嗓子发干有轻微异物感，无风时空气沉闷不流通</el-radio>
              <el-radio :value="4">4级 呼吸发闷胸口压抑，低洼路段和车库周边易头昏发沉</el-radio>
              <el-radio :value="5">5级 头晕恶心、眼涩流泪、持续咳嗽，密闭空间胸闷气短</el-radio>
            </el-radio-group>
          </el-card>
        </el-col>
      </el-row>

      <!-- 周边污染源（独立，不影响等级） -->
      <el-row :gutter="20" style="margin-top: 12px">
        <el-col :span="24">
          <el-card class="source-card" shadow="hover">
            <template #header><div class="check-title">🏭 周边污染源（可多选）</div></template>
            <el-checkbox-group v-model="checks.source">
              <el-checkbox label="无" />
              <el-checkbox label="工厂锅炉" />
              <el-checkbox label="露天焚烧" />
              <el-checkbox label="密集车流" />
              <el-checkbox label="低洼密闭区域" />
              <el-checkbox label="燃煤取暖" />
              <el-checkbox label="排烟餐饮区" />
            </el-checkbox-group>
          </el-card>
        </el-col>
      </el-row>

      <!-- 综合结果 -->
      <el-card class="result-card" shadow="hover">
        <div class="result-header">
          <span class="result-label">综合 AQI 等级</span>
          <span class="result-formula">= MAX( PM2.5等级, SO₂等级, CO等级 )</span>
        </div>
        <div class="result-body">
          <div class="result-items">
            <div class="result-item">
              <span class="item-name">PM2.5</span>
              <el-tag :type="levelTagType(pm25Level)" size="large">{{ levelName(pm25Level) }}</el-tag>
            </div>
            <el-icon :size="24"><Plus /></el-icon>
            <div class="result-item">
              <span class="item-name">SO₂</span>
              <el-tag :type="levelTagType(so2Level)" size="large">{{ levelName(so2Level) }}</el-tag>
            </div>
            <el-icon :size="24"><Plus /></el-icon>
            <div class="result-item">
              <span class="item-name">CO</span>
              <el-tag :type="levelTagType(coLevel)" size="large">{{ levelName(coLevel) }}</el-tag>
            </div>
          </div>
          <el-icon :size="32" color="#999"><Right /></el-icon>
          <div class="final-level" :class="'level-' + overallLevel">
            <div class="final-num">{{ overallLevel }} 级</div>
            <div class="final-name">{{ levelName(overallLevel) }}</div>
          </div>
        </div>
        <div class="result-actions">
          <el-button type="success" size="large" @click="submitReport" :loading="submitting">
            <el-icon><Upload /></el-icon> 提交上报
          </el-button>
          <el-button size="large" @click="router.push('/')">返回首页</el-button>
        </div>
      </el-card>
        </template>

        <!-- 我的上报记录 -->
        <template v-if="tab==='history'">
          <el-table :data="myReports" border stripe size="small" empty-text="暂无上报记录" max-height="520">
            <el-table-column prop="createTime" label="时间" width="155"><template #default="{row}">{{ row.createTime?.substring(0,16) }}</template></el-table-column>
            <el-table-column prop="city" label="地区" width="90" />
            <el-table-column label="PM2.5/SO₂/CO" width="140"><template #default="{row}">{{ L(row.pm25Level) }}/{{ L(row.so2Level) }}/{{ L(row.coLevel) }}</template></el-table-column>
            <el-table-column label="预估AQI" width="80"><template #default="{row}"><el-tag :type="T(row.overallLevel)" size="small">{{ L(row.overallLevel) }}</el-tag></template></el-table-column>
            <el-table-column label="状态" width="70"><template #default="{row}"><el-tag :type="ST(row.status)" size="small">{{ row.status }}</el-tag></template></el-table-column>
            <el-table-column prop="assigneeName" label="网格员" width="70" />
            <el-table-column label="实测等级" width="80"><template #default="{row}"><el-tag v-if="row.measuredOverall!=null" :type="T(row.measuredOverall)" size="small">{{ L(row.measuredOverall) }}</el-tag><span v-else style="color:#ccc">—</span></template></el-table-column>
          </el-table>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Monitor, Sunny, Plus, Right, Upload, Clock, ArrowLeft } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { post, get } from '@/util/request'
import { levelName, levelTagType, statusTagType } from '@/util/helpers'

const router = useRouter()
const userStore = useUserStore()
const tab = ref('submit')

const myReports = ref<any[]>([])
const L = levelName
const T = levelTagType
const ST = statusTagType

function loadMyReports() {
  if (userStore.user?.id)
    get(`/report/list?supervisorId=${userStore.user.id}`)
      .then((d) => { myReports.value = (d.data as any[]) || [] })
      .catch(() => ElMessage.error('加载上报记录失败'))
}
onMounted(loadMyReports)

const activeGuide = ref(['guide'])

const criteriaGuide = [
  { level: '1 级', label: '优', visual: '天空湛蓝通透，远处轮廓清晰，阳光清亮', smell: '空气清新，无尘土味、无刺激性异味', body: '呼吸顺畅，无不适', summary: '无灰霾、无呛味、无身体不适' },
  { level: '2 级', label: '良', visual: '轻微泛白薄雾，远处略微朦胧但不浑浊', smell: '仅有轻微尘土味，无硫磺呛味', body: '身体无任何不适，可正常户外活动', summary: '略有朦胧感，无刺鼻气味，体感正常' },
  { level: '3 级', label: '轻度污染', visual: '灰蒙蒙，3-5公里外模糊，阳光发白不透亮', smell: '靠近污染源有淡淡烧煤味、轻微硫磺涩味', body: '敏感人群嗓子发干、轻微异物感', summary: '能见度下降，有轻微硫味，局部存在污染源' },
  { level: '4 级', label: '中度污染', visual: '明显灰霾，远处完全模糊，天色偏黄发灰', smell: '明显呛人，浓郁烧煤味、硫磺酸涩味', body: '普通人群呼吸发闷、胸口压抑', summary: '雾霾明显、刺激气味突出，需警惕 CO 堆积' },
  { level: '5 级', label: '重度污染', visual: '重度灰霾笼罩，近处楼宇看不清，天色土黄灰黑', smell: '强烈刺激性呛味、硫化味，不敢深呼吸', body: '眼涩流泪、持续咳嗽、胸闷气短、头晕恶心', summary: '视野严重受阻、气味强烈刺激，三类污染物均大概率超标' },
]

const pm25Level = ref(1)
const so2Level = ref(1)
const coLevel = ref(1)
const submitting = ref(false)

const checks = ref({
  visualLevel: 1,   // 视觉 → PM2.5
  smellLevel: 1,    // 嗅觉 → SO₂
  bodyLevel: 1,     // 体感 → CO
  source: [] as string[],
})

import { watch } from 'vue'
// 视觉选几级 → PM2.5 自动跳几级
watch(() => checks.value.visualLevel, (v) => { pm25Level.value = v })
// 嗅觉选几级 → SO₂ 自动跳几级
watch(() => checks.value.smellLevel, (v) => { so2Level.value = v })
// 体感选几级 → CO 自动跳几级
watch(() => checks.value.bodyLevel, (v) => { coLevel.value = v })

const overallLevel = computed(() =>
  Math.max(pm25Level.value, so2Level.value, coLevel.value)
)

function submitReport() {
  if (submitting.value) return
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录后再提交上报')
    router.push({ name: 'Login' })
    return
  }
  submitting.value = true
  post('/report/submit', {
    pm25Level: pm25Level.value,
    so2Level: so2Level.value,
    coLevel: coLevel.value,
    overallLevel: overallLevel.value,
    supervisorId: userStore.user?.id,
    province: userStore.user?.province,
    city: userStore.user?.city,
  })
    .then((result) => {
      submitting.value = false
      if (result.code === 200) {
        ElMessage.success('上报成功！')
        loadMyReports()
      } else {
        ElMessage.error(result.msg || '上报失败')
      }
    })
    .catch(() => {
      submitting.value = false
      ElMessage.error('网络错误，请检查后端服务')
    })
}
</script>

<style scoped>
.report-page { height: 100vh; overflow: hidden; display: flex; flex-direction: column; background: linear-gradient(160deg, #e8f5e9 0%, #c8e6c9 20%, #e0f2e9 40%, #e3f2fd 70%, #e8eaf6 100%); }
.top-banner { width: 100%; background: linear-gradient(135deg, #2e7d32 0%, #388e3c 40%, #43a047 100%); color: #fff; text-align: center; padding: 14px 0; font-size: 18px; font-weight: 500; letter-spacing: 2px; display: flex; align-items: center; justify-content: center; gap: 10px; box-shadow: 0 2px 12px rgba(46,125,50,0.3); flex-shrink: 0; }
.main-container { padding: 20px 40px 40px; max-width: 1300px; margin: 0 auto; }

.guide-card { }
.guide-text { display: flex; align-items: center; justify-content: center; gap: 8px; flex-wrap: wrap; font-size: 14px; color: #555; }
.guide-text .sep { color: #ccc; }

.pollutant-card { height: 100%; }
.pollutant-header { font-size: 15px; font-weight: 600; }
.pollutant-header .method { font-size: 11px; font-weight: 400; color: #999; }
.pollutant-header.pm25 { color: #e65100; }
.pollutant-header.so2 { color: #6a1b9a; }
.pollutant-header.co { color: #c62828; }

.criteria-list { font-size: 12px; line-height: 1.8; color: #555; padding-left: 16px; margin: 0; }
.criteria-list li { margin-bottom: 2px; }

.lv { display: flex; flex-direction: column; align-items: center; gap: 0; padding: 2px 6px; }
.lv b { font-size: 16px; }
.lv i { font-size: 10px; font-style: normal; }

.check-title { font-size: 14px; font-weight: 600; }
.observe-card { height: 100%; }
.observe-card :deep(.el-radio) { font-size: 12px; color: #555; }
.source-card :deep(.el-checkbox) { margin-right: 20px; font-size: 13px; }

.result-card { margin-top: 20px; text-align: center; }
.result-header { margin-bottom: 20px; }
.result-label { font-size: 18px; font-weight: 700; color: #333; margin-right: 12px; }
.result-formula { font-size: 12px; color: #999; }
.result-body { display: flex; align-items: center; justify-content: center; gap: 16px; padding: 16px 0; flex-wrap: wrap; }
.result-items { display: flex; align-items: center; gap: 10px; }
.result-item { display: flex; flex-direction: column; align-items: center; gap: 4px; }
.item-name { font-size: 12px; color: #999; }

.final-level { padding: 16px 32px; border-radius: 16px; color: #fff; min-width: 140px; }
.final-level.level-1 { background: linear-gradient(135deg, #66bb6a, #43a047); }
.final-level.level-2 { background: linear-gradient(135deg, #42a5f5, #1e88e5); }
.final-level.level-3 { background: linear-gradient(135deg, #ffa726, #ef6c00); }
.final-level.level-4 { background: linear-gradient(135deg, #ef5350, #d32f2f); }
.final-level.level-5 { background: linear-gradient(135deg, #7b1fa2, #4a148c); }
.final-num { font-size: 28px; font-weight: 700; }
.final-name { font-size: 14px; margin-top: 4px; }

.result-actions { margin-top: 20px; display: flex; gap: 12px; justify-content: center; }

.layout{display:flex;flex:1;overflow:hidden}
.sidebar{width:200px;background:#fff;border-right:1px solid #e8e8e8;flex-shrink:0}
.content{flex:1;padding:16px 20px;overflow:auto}
</style>
