<template>
  <div class="profile-page">
    <div class="top-banner"><el-icon :size="28"><UserFilled /></el-icon><span>账号管理</span></div>
    <div class="main-container">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="hover"><template #header><h3>👤 个人信息</h3></template>
            <el-form label-position="top">
              <el-form-item label="手机号"><el-input :model-value="userStore.user?.phone" disabled /></el-form-item>
              <el-form-item label="角色"><el-tag>{{ userStore.userRole }}</el-tag></el-form-item>
              <el-form-item label="姓名"><el-input v-model="profile.name" /></el-form-item>
              <el-form-item label="省"><el-select v-model="profile.province" style="width:100%"><el-option v-for="a in area" :key="a.province" :label="a.province" :value="a.province" /></el-select></el-form-item>
              <el-form-item label="市"><el-select v-model="profile.city" style="width:100%"><el-option v-for="c in profileCities" :key="c" :label="c" :value="c" /></el-select></el-form-item>
              <el-form-item><el-button type="primary" @click="saveProfile" :loading="saving">保存修改</el-button></el-form-item>
            </el-form>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover"><template #header><h3>🔒 修改密码</h3></template>
            <el-form label-position="top">
              <el-form-item label="原密码"><el-input v-model="pwd.old" type="password" show-password /></el-form-item>
              <el-form-item label="新密码"><el-input v-model="pwd.new1" type="password" show-password /></el-form-item>
              <el-form-item label="确认新密码"><el-input v-model="pwd.new2" type="password" show-password /></el-form-item>
              <el-form-item><el-button type="warning" @click="changePwd" :loading="changing">修改密码</el-button></el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
      <div style="text-align:center;margin-top:20px"><el-button @click="router.back()">返回</el-button></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { UserFilled } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { post } from '@/util/request'

const router = useRouter()
const userStore = useUserStore()
const saving = ref(false); const changing = ref(false)

const area = [
  { province: '上海', citys: ['上海市'] },{ province: '北京', citys: ['北京市'] },{ province: '天津', citys: ['天津市'] },{ province: '重庆', citys: ['重庆市'] },
  { province: '广东', citys: ['广州市','深圳市','珠海市','东莞市','佛山市'] },{ province: '浙江', citys: ['杭州市','宁波市','温州市'] },{ province: '江苏', citys: ['南京市','苏州市','无锡市'] },
]

const profile = reactive({ name: userStore.user?.name||'', province: userStore.user?.province||'上海', city: userStore.user?.city||'上海市' })
const profileCities = computed(() => area.find(a=>a.province===profile.province)?.citys||[])
const pwd = reactive({ old: '', new1: '', new2: '' })

function saveProfile() {
  saving.value = true
  post('/user/updateProfile', { id: String(userStore.user?.id||''), name: profile.name, province: profile.province, city: profile.city })
    .then((r:any) => { saving.value = false; if(r.code===200){ userStore.setUser(r.data); ElMessage.success('个人信息已更新') } })
}
function changePwd() {
  if (!pwd.old || !pwd.new1) { ElMessage.error('请填写完整'); return }
  if (pwd.new1 !== pwd.new2) { ElMessage.error('两次新密码不一致'); return }
  if (pwd.new1.length < 6) { ElMessage.error('新密码至少6位'); return }
  changing.value = true
  post('/user/changePassword', { id: String(userStore.user?.id||''), oldPassword: pwd.old, newPassword: pwd.new1 })
    .then((r:any) => { changing.value = false; if(r.code===200){ ElMessage.success('密码已修改'); pwd.old='';pwd.new1='';pwd.new2='' } else ElMessage.error(r.msg) })
}
</script>

<style scoped>
.profile-page{min-height:100vh;background:linear-gradient(160deg,#e8f5e9 0%,#e3f2fd 50%,#e8eaf6 100%)}
.top-banner{width:100%;background:linear-gradient(135deg,#2e7d32 0%,#43a047 100%);color:#fff;display:flex;align-items:center;justify-content:center;gap:10px;padding:14px 0;font-size:18px;letter-spacing:2px}
.main-container{padding:30px;max-width:900px;margin:0 auto}
h3{margin:0;font-size:15px;color:#555}
</style>
