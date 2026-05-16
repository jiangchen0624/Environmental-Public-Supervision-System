<template>
  <!-- 注册页面主布局 -->
  <div class="regist-page">
    <!-- 顶部装饰条 -->
    <div class="top-banner">
      <el-icon :size="28"><Monitor /></el-icon>
      <span>NEU 空气质量监测系统</span>
    </div>

    <!-- 注册表单区域 -->
    <div class="form-container">
      <div class="form-card">
        <!-- 卡片头部 -->
        <div class="card-header">
          <div class="header-icon">
            <el-icon :size="32"><UserFilled /></el-icon>
          </div>
          <h2>监督员注册</h2>
          <p>加入我们，共同守护空气质量</p>
        </div>

        <!-- 注册表单 -->
        <el-form label-position="top" @submit.prevent="register">
          <el-form-item label="手机号">
            <el-input
              v-model="loginName"
              placeholder="请输入手机号"
              maxlength="11"
              :prefix-icon="Phone"
            />
          </el-form-item>

          <el-form-item label="姓名">
            <el-input
              v-model="name"
              placeholder="请输入姓名"
              :prefix-icon="User"
            />
          </el-form-item>

          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item label="密码">
                <el-input
                  v-model="password"
                  placeholder="请输入密码"
                  type="password"
                  show-password
                  :prefix-icon="Lock"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="确认密码">
                <el-input
                  v-model="confirmPassword"
                  placeholder="请确认密码"
                  type="password"
                  show-password
                  :prefix-icon="Lock"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="16">
            <el-col :span="12">
              <el-form-item label="省">
                <el-select v-model="province" placeholder="请选择省" style="width: 100%">
                  <el-option
                    v-for="(item, idx) in area"
                    :key="idx"
                    :label="item.province"
                    :value="item.province"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="市">
                <el-select v-model="city" placeholder="请选择市" style="width: 100%">
                  <el-option
                    v-for="(item, idx) in currentCityList"
                    :key="idx"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item>
            <el-button
              type="success"
              @click="register"
              :loading="loading"
              size="large"
              class="submit-btn"
            >
              <el-icon v-if="!loading"><CircleCheck /></el-icon>
              <span>注 册</span>
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 底部链接 -->
        <div class="form-footer">
          <span>已有账号？</span>
          <el-link type="success" @click="router.push('/')">返回首页登录</el-link>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
// 导入 Element Plus 消息提示组件
import { ElMessage } from 'element-plus'
// 导入 Element Plus 图标
import { Monitor, UserFilled, Phone, User, Lock, CircleCheck } from '@element-plus/icons-vue'
// 导入 Vue 响应式 API
import { ref, watch } from 'vue'
// 导入路由组合式函数
import { useRouter } from 'vue-router'
// 导入工具函数：表单校验、请求封装
import { validatePhone, validateName, validatePassword, validateConfirmPassword } from '@/util/validate'
import { post } from '@/util/request'

// 初始化路由实例
const router = useRouter()

// 表单数据响应式变量
const loginName = ref('')           // 手机号
const name = ref('')                // 姓名
const password = ref('')            // 密码
const confirmPassword = ref('')     // 确认密码
const loading = ref(false)          // 加载状态

// 省市区数据（模拟数据）
const area = [
  { province: '上海', citys: ['上海市'] },
  { province: '北京', citys: ['北京市'] },
  { province: '天津', citys: ['天津市'] },
  { province: '重庆', citys: ['重庆市'] },
  { province: '河北', citys: ['石家庄市', '唐山市', '保定市', '邯郸市', '廊坊市', '沧州市', '邢台市', '衡水市'] },
  { province: '山西', citys: ['太原市', '大同市', '长治市', '晋中市', '临汾市', '运城市'] },
  { province: '内蒙古', citys: ['呼和浩特市', '包头市', '鄂尔多斯市', '赤峰市', '呼伦贝尔市'] },
  { province: '辽宁', citys: ['沈阳市', '大连市', '鞍山市', '抚顺市', '锦州市', '营口市', '丹东市'] },
  { province: '吉林', citys: ['长春市', '吉林市', '延边州', '四平市', '通化市'] },
  { province: '黑龙江', citys: ['哈尔滨市', '齐齐哈尔市', '大庆市', '牡丹江市', '佳木斯市'] },
  { province: '江苏', citys: ['南京市', '苏州市', '无锡市', '常州市', '南通市', '徐州市', '扬州市', '镇江市', '泰州市'] },
  { province: '浙江', citys: ['杭州市', '宁波市', '温州市', '嘉兴市', '湖州市', '绍兴市', '金华市', '台州市'] },
  { province: '安徽', citys: ['合肥市', '芜湖市', '蚌埠市', '安庆市', '马鞍山市', '滁州市', '阜阳市'] },
  { province: '福建', citys: ['福州市', '厦门市', '泉州市', '漳州市', '莆田市', '龙岩市', '三明市'] },
  { province: '江西', citys: ['南昌市', '九江市', '赣州市', '景德镇市', '上饶市', '宜春市'] },
  { province: '山东', citys: ['济南市', '青岛市', '烟台市', '潍坊市', '临沂市', '淄博市', '济宁市', '泰安市', '威海市'] },
  { province: '河南', citys: ['郑州市', '洛阳市', '开封市', '南阳市', '新乡市', '安阳市', '信阳市'] },
  { province: '湖北', citys: ['武汉市', '宜昌市', '襄阳市', '荆州市', '黄冈市', '十堰市', '孝感市'] },
  { province: '湖南', citys: ['长沙市', '株洲市', '湘潭市', '衡阳市', '岳阳市', '常德市', '郴州市'] },
  { province: '广东', citys: ['广州市', '深圳市', '珠海市', '东莞市', '佛山市', '惠州市', '中山市', '茂名市', '汕头市', '湛江市'] },
  { province: '广西', citys: ['南宁市', '柳州市', '桂林市', '北海市', '玉林市', '梧州市'] },
  { province: '海南', citys: ['海口市', '三亚市', '儋州市', '三沙市'] },
  { province: '四川', citys: ['成都市', '绵阳市', '德阳市', '宜宾市', '南充市', '泸州市', '乐山市'] },
  { province: '贵州', citys: ['贵阳市', '遵义市', '毕节市', '六盘水市', '安顺市'] },
  { province: '云南', citys: ['昆明市', '曲靖市', '大理州', '丽江市', '玉溪市'] },
  { province: '西藏', citys: ['拉萨市', '日喀则市', '昌都市', '林芝市'] },
  { province: '陕西', citys: ['西安市', '咸阳市', '宝鸡市', '渭南市', '延安市', '汉中市'] },
  { province: '甘肃', citys: ['兰州市', '天水市', '酒泉市', '庆阳市', '张掖市'] },
  { province: '青海', citys: ['西宁市', '海东市', '格尔木市', '玉树州'] },
  { province: '宁夏', citys: ['银川市', '石嘴山市', '吴忠市', '固原市'] },
  { province: '新疆', citys: ['乌鲁木齐市', '克拉玛依市', '吐鲁番市', '哈密市', '喀什地区'] },
  { province: '香港', citys: ['香港岛', '九龙', '新界'] },
  { province: '澳门', citys: ['澳门半岛', '氹仔', '路环'] },
  { province: '台湾', citys: ['台北市', '高雄市', '台中市', '台南市', '新北市'] },
]

// 省份选择（默认选中第一个）
const province = ref(area[0]!.province)
// 当前选中省份对应的城市列表
const currentCityList = ref(area[0]!.citys)
// 城市选择（默认选中第一个）
const city = ref(currentCityList.value[0]!)

/**
 * 监听省份变化，联动更新城市列表
 * @param {string} newVal - 新选中的省份值
 */
watch(province, (newVal) => {
  const found = area.find((item) => item.province === newVal)
  if (found) {
    currentCityList.value = found.citys
    city.value = found.citys[0]!
  }
})

/**
 * 注册表单验证与提交
 * 调用 util/validate 进行前端校验，通过后调用 util/request 发送请求到后端
 */
const register = () => {
  // 前端表单校验
  const msg = validatePhone(loginName.value)
    || validateName(name.value)
    || validatePassword(password.value)
    || validateConfirmPassword(password.value, confirmPassword.value)
  if (msg) {
    ElMessage.error(msg)
    return
  }

  // 设置加载状态，发送注册请求到后端
  loading.value = true

  // 构造注册数据并发送 POST 请求（Vite 代理 /api -> localhost:8080）
  post('/user/register', {
    phone: loginName.value,
    name: name.value,
    password: password.value,
    province: province.value,
    city: city.value,
    role: "公众监督员",
  })
    .then((result) => {
      loading.value = false
      if (result.code === 200) {
        ElMessage.success(result.msg)
        // 注册成功后跳转到首页
        router.push('/')
      } else {
        ElMessage.error(result.msg)
      }
    })
    .catch(() => {
      loading.value = false
      ElMessage.error('网络错误，请检查后端服务是否启动')
    })
}
</script>

<style scoped>
/* ========== 页面整体 ========== */
.regist-page {
  min-height: 100vh;
  background: linear-gradient(160deg, #e8f5e9 0%, #c8e6c9 20%, #e0f2e9 40%, #e3f2fd 70%, #e8eaf6 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  overflow: hidden;
}

/* 背景装饰圆 */
.regist-page::before {
  content: '';
  position: absolute;
  top: -120px;
  right: -80px;
  width: 400px;
  height: 400px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(76, 175, 80, 0.08) 0%, transparent 70%);
  pointer-events: none;
}

.regist-page::after {
  content: '';
  position: absolute;
  bottom: -100px;
  left: -100px;
  width: 500px;
  height: 500px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(33, 150, 243, 0.06) 0%, transparent 70%);
  pointer-events: none;
}

/* ========== 顶部横幅 ========== */
.top-banner {
  width: 100%;
  background: linear-gradient(135deg, #2e7d32 0%, #388e3c 40%, #43a047 100%);
  color: #fff;
  text-align: center;
  padding: 14px 0;
  font-size: 18px;
  font-weight: 500;
  letter-spacing: 2px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  box-shadow: 0 2px 12px rgba(46, 125, 50, 0.3);
  position: relative;
  z-index: 1;
}

/* ========== 表单容器 ========== */
.form-container {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  position: relative;
  z-index: 1;
}

.form-card {
  width: 560px;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.08), 0 2px 8px rgba(0, 0, 0, 0.04);
  padding: 36px 40px 28px;
  border: 1px solid rgba(76, 175, 80, 0.12);
}

/* ========== 卡片头部 ========== */
.card-header {
  text-align: center;
  margin-bottom: 28px;
}

.header-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: linear-gradient(135deg, #e8f5e9, #c8e6c9);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
  color: #2e7d32;
}

.card-header h2 {
  margin: 0 0 6px;
  font-size: 22px;
  color: #2e7d32;
  font-weight: 600;
  letter-spacing: 1px;
}

.card-header p {
  margin: 0;
  font-size: 13px;
  color: #81c784;
  letter-spacing: 1px;
}

/* ========== 注册按钮 ========== */
.submit-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  letter-spacing: 4px;
  --el-button-bg-color: linear-gradient(135deg, #43a047, #2e7d32);
  --el-button-border-color: transparent;
  --el-button-hover-bg-color: linear-gradient(135deg, #4caf50, #388e3c);
  --el-button-hover-border-color: transparent;
}

.submit-btn :deep(span) {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

/* ========== 底部链接 ========== */
.form-footer {
  text-align: center;
  padding-top: 8px;
  font-size: 13px;
  color: #999;
}

/* ========== 底部装饰 ========== */
.bottom-decor {
  text-align: center;
  padding: 8px 0 20px;
  color: #81c784;
  font-size: 13px;
  letter-spacing: 2px;
  position: relative;
  z-index: 1;
}

/* ========== Element Plus 覆盖 ========== */
:deep(.el-input) {
  --el-input-focus-border-color: #66bb6a;
  --el-input-hover-border-color: #81c784;
}

:deep(.el-select) {
  --el-select-input-focus-border-color: #66bb6a;
}

:deep(.el-button--success) {
  --el-button-bg-color: #43a047;
  --el-button-border-color: #43a047;
  --el-button-hover-bg-color: #4caf50;
  --el-button-hover-border-color: #4caf50;
  --el-button-active-bg-color: #388e3c;
}

:deep(.el-form-item__label) {
  color: #555;
  font-weight: 500;
}

:deep(.el-link--primary) {
  --el-link-text-color: #43a047;
  --el-link-hover-text-color: #2e7d32;
}
</style>
