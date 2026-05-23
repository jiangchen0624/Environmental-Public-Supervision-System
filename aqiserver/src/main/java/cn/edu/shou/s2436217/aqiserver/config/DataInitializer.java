package cn.edu.shou.s2436217.aqiserver.config;

import cn.edu.shou.s2436217.aqiserver.bean.*;
import cn.edu.shou.s2436217.aqiserver.repostory.ReportRepository;
import cn.edu.shou.s2436217.aqiserver.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 演示数据初始化器 — 首次启动时自动填充各省份、各角色的完整演示数据。
 * 仅在 users 表为空时执行，避免重复插入。
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserService userService;
    @Autowired private ReportService reportService;
    @Autowired private MeasurementService measurementService;
    @Autowired private ReportRepository reportRepository;

    private final Random rng = new Random(42);

    @Override
    public void run(String... args) {
        if (!userService.listAll().isEmpty()) return; // 已有数据则跳过

        System.out.println("========== 开始初始化演示数据 ==========");

        // ==================== 1. 创建用户 ====================
        // 管理员 + 决策者
        User admin = createUser("13800000001", "123456", "系统管理员", "北京", "北京", "系统管理员");
        User decider = createUser("13800000002", "123456", "决策者", "北京", "北京", "决策者");

        // 各省监督员 — 每省至少1名，覆盖25个省份
        String[][] supervisors = {
            {"13801001001", "赵监督", "河北", "石家庄"}, {"13801001002", "钱监督", "河北", "唐山"},
            {"13801002001", "孙监督", "北京", "北京"},
            {"13801003001", "李监督", "上海", "上海"},
            {"13801004001", "周监督", "广东", "广州"}, {"13801004002", "吴监督", "广东", "深圳"},
            {"13801005001", "郑监督", "四川", "成都"},
            {"13801006001", "王监督", "湖北", "武汉"},
            {"13801007001", "冯监督", "陕西", "西安"},
            {"13801008001", "陈监督", "江苏", "南京"}, {"13801008002", "褚监督", "江苏", "苏州"},
            {"13801009001", "卫监督", "浙江", "杭州"},
            {"13801010001", "蒋监督", "山东", "济南"}, {"13801010002", "沈监督", "山东", "青岛"},
            {"13801011001", "韩监督", "河南", "郑州"},
            {"13801012001", "杨监督", "湖南", "长沙"},
            {"13801013001", "朱监督", "辽宁", "沈阳"},
            {"13801014001", "秦监督", "山西", "太原"},
            {"13801015001", "许监督", "安徽", "合肥"},
            {"13801016001", "何监督", "福建", "福州"},
            {"13801017001", "吕监督", "江西", "南昌"},
            {"13801018001", "张监督", "广西", "南宁"},
            {"13801019001", "孔监督", "云南", "昆明"},
            {"13801020001", "曹监督", "贵州", "贵阳"},
            {"13801021001", "严监督", "甘肃", "兰州"},
            {"13801022001", "华监督", "吉林", "长春"},
            {"13801023001", "金监督", "黑龙江", "哈尔滨"},
            {"13801024001", "魏监督", "海南", "海口"},
            {"13801025001", "陶监督", "重庆", "重庆"},
        };

        for (String[] s : supervisors)
            createUser(s[0], "123456", s[1], s[2], s[3], "公众监督员");

        // 各省网格员
        String[][] inspectors = {
            {"13802001001", "网格员-京", "北京", "北京"},
            {"13802002001", "网格员-津", "天津", "天津"},
            {"13802003001", "网格员-沪", "上海", "上海"},
            {"13802004001", "网格员-冀", "河北", "石家庄"},
            {"13802005001", "网格员-粤", "广东", "广州"},
            {"13802006001", "网格员-川", "四川", "成都"},
            {"13802007001", "网格员-鄂", "湖北", "武汉"},
            {"13802008001", "网格员-陕", "陕西", "西安"},
            {"13802009001", "网格员-苏", "江苏", "南京"},
            {"13802010001", "网格员-浙", "浙江", "杭州"},
            {"13802011001", "网格员-鲁", "山东", "济南"},
            {"13802012001", "网格员-豫", "河南", "郑州"},
            {"13802013001", "网格员-湘", "湖南", "长沙"},
            {"13802014001", "网格员-辽", "辽宁", "沈阳"},
            {"13802015001", "网格员-渝", "重庆", "重庆"},
        };

        for (String[] ins : inspectors)
            createUser(ins[0], "123456", ins[1], ins[2], ins[3], "AQI检测网格员");

        System.out.println("用户创建完成: " + userService.listAll().size() + " 人");

        // ==================== 2. 创建上报记录 ====================
        // 覆盖25个省份，时间分布在过去180天，AQI等级1~5
        String[][] reportDefs = {
            // {province, city, pm25Level, so2Level, coLevel, overallLevel, daysAgo, status}
            {"河北", "石家庄", "2", "1", "1", "2", "3", "已检测"},
            {"河北", "石家庄", "4", "3", "2", "4", "15", "已检测"},
            {"河北", "唐山", "3", "4", "2", "4", "28", "已检测"},
            {"河北", "保定", "2", "2", "1", "2", "45", "已指派"},
            {"河北", "石家庄", "5", "4", "3", "5", "60", "已检测"},
            {"北京", "北京", "1", "1", "1", "1", "2", "已检测"},
            {"北京", "北京", "2", "2", "1", "2", "10", "已检测"},
            {"北京", "北京", "3", "3", "2", "3", "40", "已检测"},
            {"北京", "北京", "4", "3", "3", "4", "90", "已检测"},
            {"北京", "北京", "2", "1", "2", "2", "120", "已指派"},
            {"上海", "上海", "1", "1", "1", "1", "1", "已检测"},
            {"上海", "上海", "2", "2", "2", "2", "8", "已检测"},
            {"上海", "上海", "3", "3", "2", "3", "35", "已检测"},
            {"上海", "上海", "4", "4", "3", "4", "75", "已指派"},
            {"上海", "上海", "5", "5", "4", "5", "110", "已检测"},
            {"广东", "广州", "2", "1", "1", "2", "5", "已检测"},
            {"广东", "广州", "3", "2", "2", "3", "20", "已检测"},
            {"广东", "深圳", "4", "3", "3", "4", "55", "已检测"},
            {"广东", "广州", "3", "4", "2", "4", "85", "已检测"},
            {"广东", "深圳", "2", "2", "1", "2", "130", "待指派"},
            {"四川", "成都", "2", "1", "2", "2", "7", "已检测"},
            {"四川", "成都", "3", "2", "3", "3", "30", "已检测"},
            {"四川", "成都", "4", "4", "3", "4", "65", "已检测"},
            {"湖北", "武汉", "3", "3", "2", "3", "12", "已检测"},
            {"湖北", "武汉", "5", "4", "4", "5", "42", "已检测"},
            {"湖北", "武汉", "2", "2", "2", "2", "100", "已检测"},
            {"陕西", "西安", "2", "1", "1", "2", "6", "已检测"},
            {"陕西", "西安", "4", "3", "3", "4", "38", "已检测"},
            {"陕西", "西安", "3", "2", "2", "3", "70", "已检测"},
            {"江苏", "南京", "1", "1", "1", "1", "4", "已检测"},
            {"江苏", "南京", "2", "2", "1", "2", "25", "已检测"},
            {"江苏", "苏州", "3", "3", "3", "3", "50", "已检测"},
            {"江苏", "南京", "4", "3", "3", "4", "80", "已指派"},
            {"浙江", "杭州", "2", "1", "1", "2", "9", "已检测"},
            {"浙江", "杭州", "3", "2", "2", "3", "33", "已检测"},
            {"浙江", "宁波", "1", "1", "2", "2", "95", "已检测"},
            {"山东", "济南", "3", "2", "1", "3", "14", "已检测"},
            {"山东", "青岛", "4", "3", "2", "4", "48", "已检测"},
            {"山东", "济南", "5", "5", "4", "5", "105", "已检测"},
            {"河南", "郑州", "2", "2", "2", "2", "11", "已检测"},
            {"河南", "郑州", "3", "3", "3", "3", "36", "已检测"},
            {"河南", "郑州", "4", "4", "4", "4", "72", "已指派"},
            {"湖南", "长沙", "2", "1", "1", "2", "18", "已检测"},
            {"湖南", "长沙", "3", "3", "2", "3", "52", "已检测"},
            {"辽宁", "沈阳", "3", "2", "2", "3", "22", "已检测"},
            {"辽宁", "大连", "2", "3", "1", "3", "58", "已检测"},
            {"山西", "太原", "4", "3", "3", "4", "16", "已检测"},
            {"山西", "太原", "5", "4", "4", "5", "44", "已检测"},
            {"安徽", "合肥", "2", "2", "1", "2", "24", "已检测"},
            {"福建", "福州", "1", "2", "1", "2", "27", "已检测"},
            {"江西", "南昌", "3", "2", "2", "3", "32", "已检测"},
            {"广西", "南宁", "2", "1", "1", "2", "19", "已检测"},
            {"云南", "昆明", "1", "1", "1", "1", "8", "已检测"},
            {"贵州", "贵阳", "2", "2", "2", "2", "46", "已检测"},
            {"甘肃", "兰州", "3", "3", "2", "3", "62", "已检测"},
            {"吉林", "长春", "2", "1", "1", "2", "29", "已检测"},
            {"黑龙江", "哈尔滨", "4", "3", "3", "4", "53", "已检测"},
            {"海南", "海口", "1", "1", "1", "1", "5", "已检测"},
            {"重庆", "重庆", "3", "2", "2", "3", "17", "已检测"},
            {"河北", "保定", "3", "3", "2", "3", "150", "待指派"},
            {"河北", "唐山", "5", "4", "5", "5", "160", "待指派"},
            {"北京", "北京", "2", "1", "1", "2", "140", "待指派"},
            {"上海", "上海", "3", "2", "3", "3", "135", "待指派"},
            {"广东", "深圳", "4", "4", "3", "4", "125", "待指派"},
            {"四川", "成都", "3", "3", "3", "3", "115", "已指派"},
            {"湖北", "武汉", "2", "1", "1", "2", "165", "已指派"},
            {"陕西", "西安", "5", "5", "4", "5", "175", "待指派"},
            {"江苏", "苏州", "2", "2", "3", "3", "155", "待指派"},
            {"浙江", "宁波", "3", "2", "2", "3", "145", "已指派"},
            {"山东", "青岛", "4", "3", "4", "4", "132", "待指派"},
        };

        List<Report> createdReports = new ArrayList<>();
        List<Report> needMeasurement = new ArrayList<>();
        for (String[] def : reportDefs) {
            Report r = new Report();
            r.setProvince(def[0]);
            r.setCity(def[1]);
            r.setPm25Level(Integer.parseInt(def[2]));
            r.setSo2Level(Integer.parseInt(def[3]));
            r.setCoLevel(Integer.parseInt(def[4]));
            r.setOverallLevel(Integer.parseInt(def[5]));
            r.setCreateTime(LocalDateTime.now().minusDays(Long.parseLong(def[6])));

            // 随机分配监督员 (id从3开始，前2个是admin和决策者)
            int supIdx = rng.nextInt(supervisors.length);
            r.setSupervisorId(3 + supIdx);

            Report saved = reportService.submit(r); // submit 默认置为"待指派"
            String targetStatus = def[7];

            if ("已指派".equals(targetStatus) || "已检测".equals(targetStatus)) {
                int inspectorId = 3 + supervisors.length + rng.nextInt(inspectors.length);
                saved.setAssigneeId(inspectorId);
                saved.setAssigneeName(inspectors[rng.nextInt(inspectors.length)][1]);
                saved.setStatus("已指派");
                reportRepository.save(saved);
            }
            if ("已检测".equals(targetStatus)) {
                needMeasurement.add(saved);
            }
            createdReports.add(saved);
        }

        System.out.println("上报记录创建完成: " + createdReports.size() + " 条");

        // ==================== 3. 为"已检测"记录创建测量数据 ====================
        int measCount = 0;
        for (Report r : needMeasurement) {
            Measurement m = new Measurement();
            m.setReportId(r.getId());
            m.setInspectorId(r.getAssigneeId());
            m.setInspectorName(r.getAssigneeName() != null ? r.getAssigneeName() : "检测员");

            // 根据目测等级生成仪器读数
            int pmL = r.getPm25Level();
            double pm25Raw = pmL == 1 ? 15 + rng.nextDouble() * 20 : pmL == 2 ? 35 + rng.nextDouble() * 40 : pmL == 3 ? 75 + rng.nextDouble() * 40 : pmL == 4 ? 115 + rng.nextDouble() * 35 : 150 + rng.nextDouble() * 100;
            int soL = r.getSo2Level();
            double so2Raw = soL == 1 ? 15 + rng.nextDouble() * 35 : soL == 2 ? 50 + rng.nextDouble() * 100 : soL == 3 ? 150 + rng.nextDouble() * 325 : soL == 4 ? 475 + rng.nextDouble() * 325 : 800 + rng.nextDouble() * 500;
            int coL = r.getCoLevel();
            double coRaw = coL == 1 ? 0.5 + rng.nextDouble() * 1.5 : coL == 2 ? 2 + rng.nextDouble() * 2 : coL == 3 ? 4 + rng.nextDouble() * 10 : coL == 4 ? 14 + rng.nextDouble() * 10 : 25 + rng.nextDouble() * 25;

            m.setPm25Raw(Math.round(pm25Raw * 10.0) / 10.0);
            m.setSo2Raw(Math.round(so2Raw * 10.0) / 10.0);
            m.setCoRaw(Math.round(coRaw * 100.0) / 100.0);
            m.setPm25Level(rawToLevel("pm25", m.getPm25Raw()));
            m.setSo2Level(rawToLevel("so2", m.getSo2Raw()));
            m.setCoLevel(rawToLevel("co", m.getCoRaw()));
            m.setOverallLevel(Math.max(Math.max(m.getPm25Level(), m.getSo2Level()), m.getCoLevel()));
            m.setNotes("仪器型号: AQI-8000, 环境温度: " + (15 + rng.nextInt(20)) + "°C");

            measurementService.save(m);
            measCount++;
        }

        System.out.println("测量数据创建完成: " + measCount + " 条");
        System.out.println("========== 演示数据初始化完成 ==========");
    }

    private User createUser(String phone, String pwd, String name, String province, String city, String role) {
        User u = new User();
        u.setPhone(phone);
        u.setPassword(pwd);
        u.setName(name);
        u.setProvince(province);
        u.setCity(city);
        u.setRole(role);
        return userService.register(u);
    }

    private int rawToLevel(String pollutant, double raw) {
        return switch (pollutant) {
            case "pm25" -> raw <= 35 ? 1 : raw <= 75 ? 2 : raw <= 115 ? 3 : raw <= 150 ? 4 : 5;
            case "so2" -> raw <= 50 ? 1 : raw <= 150 ? 2 : raw <= 475 ? 3 : raw <= 800 ? 4 : 5;
            case "co" -> raw <= 2 ? 1 : raw <= 4 ? 2 : raw <= 14 ? 3 : raw <= 24 ? 4 : 5;
            default -> 1;
        };
    }
}
