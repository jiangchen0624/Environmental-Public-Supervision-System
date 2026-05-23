# NEPV 空气质量监测系统

基于 Vue 3 + Spring Boot 的全栈空气质量监测平台，支持公众监督、网格员检测、管理员调度、决策者可视化四种角色。

## 技术栈

| 层 | 技术 |
|---|---|
| 前端 | Vue 3 (Composition API) + TypeScript + Vite 8 |
| UI 框架 | Element Plus 2.14 |
| 可视化 | ECharts 6.0（含中国市级地图 GeoJSON） |
| 状态管理 | Pinia 3 |
| 后端 | Spring Boot 3.5 + JPA |
| 数据库 | MySQL 8+ |

## 项目结构

```
EPS/
├── aqiserver/                          # Spring Boot 后端
│   └── src/main/java/.../aqiserver/
│       ├── AqiserverApplication.java   # 启动类
│       ├── bean/                       # 实体类
│       │   ├── User.java               # 用户（4种角色）
│       │   ├── Report.java             # 监督员上报记录
│       │   └── Measurement.java        # 网格员仪器检测数据
│       ├── config/
│       │   └── DataInitializer.java    # 演示数据自动初始化
│       ├── controller/
│       │   ├── UserController.java     # 用户注册/登录/管理
│       │   ├── ReportController.java   # 上报记录CRUD + 指派
│       │   ├── MeasurementController.java  # 检测数据录入/查询
│       │   └── StatsController.java    # 可视化统计API
│       ├── service/                    # 业务逻辑
│       └── repostory/                  # JPA 数据访问
│
├── environmental protection system/    # Vue 3 前端
│   └── src/
│       ├── view/
│       │   ├── LoginView.vue           # 登录页
│       │   ├── RegistView.vue          # 注册页
│       │   ├── HomeView.vue            # 首页导航
│       │   ├── ReportView.vue          # 监督员：上报/查看
│       │   ├── InspectorView.vue       # 网格员：仪器检测
│       │   ├── AdminView.vue           # 管理员：指派/管理/统计
│       │   ├── DecisionView.vue        # 决策者：可视化大屏
│       │   └── ProfileView.vue         # 个人中心
│       ├── router/index.ts             # 路由配置
│       ├── stores/user.ts              # 用户状态
│       ├── util/
│       │   ├── request.ts              # HTTP 封装
│       │   └── area-data.ts            # 全国省/市/区划数据
│       └── asserts/
│           └── china-full-city.json    # 中国市级 GeoJSON（备用）
│
├── aqi.sql                             # 数据库完整导出（含演示数据）
└── README.md
```

## 四种角色

| 角色 | 核心功能 |
|---|---|
| **公众监督员** | 目测上报 PM₂.₅ / SO₂ / CO 等级，查看上报历史 |
| **AQI检测网格员** | 接收指派任务，录入仪器检测数据（精确读数），查看检测历史 |
| **系统管理员** | 用户管理，指派待处理上报给网格员，确认检测结果，省级覆盖率统计 |
| **决策者** | 可视化大屏：统计卡片、省/市超标表格、AQI分布饼图、月度趋势、全国覆盖率面板、**中国地图（市级着色+省下钻）** |

## 快速开始

### 1. 环境要求

- JDK 17+
- MySQL 8.0+
- Node.js 20+
- Maven 3.6+

### 2. 数据库

```bash
# 创建数据库并导入
mysql -uroot -p -e "CREATE DATABASE IF NOT EXISTS aqi CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -uroot -p aqi < aqi.sql
```

`aqi.sql` 包含完整的表结构 + 演示数据（46个用户、70条上报、55条检测记录，覆盖25个省份）。

### 3. 后端

```bash
cd aqiserver

# 修改数据库连接（如需要）
# src/main/resources/application.properties
# spring.datasource.url=jdbc:mysql://localhost:3306/aqi
# spring.datasource.username=root
# spring.datasource.password=你的密码

# 启动
mvn spring-boot:run
# 后端运行在 http://localhost:8080
```

> 首次启动时，如果 `users` 表为空，`DataInitializer` 会自动填充演示数据。如果已导入 `aqi.sql` 则跳过。

### 4. 前端

```bash
cd "environmental protection system"

npm install
npm run dev
# 前端运行在 http://localhost:5173
```

### 5. 演示账号

| 角色 | 手机号 | 密码 |
|---|---|---|
| 系统管理员 | 13800000001 | 123456 |
| 决策者 | 13800000002 | 123456 |
| 公众监督员（上海） | 13801003001 | 123456 |
| 公众监督员（北京） | 13801002001 | 123456 |
| AQI检测网格员（北京） | 13802001001 | 123456 |
| AQI检测网格员（上海） | 13802003001 | 123456 |

> 所有账号密码均为 `123456`。完整账号列表见 `aqi.sql` 中 `users` 表。

## 统计 API

所有统计接口均支持可选的时间范围过滤：

```
GET /stats/province?startDate=2026-01-01&endDate=2026-05-23   # 省分组超标统计
GET /stats/distribution?startDate=...&endDate=...               # AQI等级分布
GET /stats/trend?startDate=...&endDate=...                      # 月度趋势
GET /stats/detection?startDate=...&endDate=...                  # 检测数量汇总
GET /stats/city?province=河北                                   # 某省下各市统计
GET /stats/city-all                                             # 全国所有城市统计
GET /stats/coverage                                             # 全国网格覆盖率
```

## 地图可视化

决策者大屏集成 ECharts 中国市级地图：
- **全国视图**：每个城市按 AQI 超标数量独立着色（绿→黄→橙→红→暗红），无数据城市显示深灰色
- **省级下钻**：点击任意城市 → 地图缩放到该省，显示省内每个城市的超标详情
- **返回全国**：一键恢复全国视图
- 地图数据文件 `public/china-city-level.json`（345个市级特征，约3.9MB）在页面首次加载时 fetch

## License

Educational project.
