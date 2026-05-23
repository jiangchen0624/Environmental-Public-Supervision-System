package cn.edu.shou.s2436217.aqiserver.controller;

import cn.edu.shou.s2436217.aqiserver.bean.Measurement;
import cn.edu.shou.s2436217.aqiserver.bean.Report;
import cn.edu.shou.s2436217.aqiserver.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller @RequestMapping("/stats")
public class StatsController {
    @Autowired private ReportService reportService;
    @Autowired private MeasurementService measurementService;

    /** 1. 省分组检查统计 */
    @CrossOrigin(origins = "*") @RequestMapping("/province") @ResponseBody
    public Map<String, Object> province() {
        List<Report> all = reportService.listAll();
        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();
        for (Report r : all) {
            String p = r.getProvince() != null ? r.getProvince() : "未知";
            result.putIfAbsent(p, new HashMap<>());
            Map<String, Integer> m = result.get(p);
            m.merge("so2超标", r.getSo2Level() >= 3 ? 1 : 0, Integer::sum);
            m.merge("co超标", r.getCoLevel() >= 3 ? 1 : 0, Integer::sum);
            m.merge("pm25超标", r.getPm25Level() >= 3 ? 1 : 0, Integer::sum);
            m.merge("aqi超标", r.getOverallLevel() >= 3 ? 1 : 0, Integer::sum);
        }
        Map<String, Object> resp = new HashMap<>(); resp.put("code",200); resp.put("data",result); return resp;
    }

    /** 2. AQI指数分布统计 */
    @CrossOrigin(origins = "*") @RequestMapping("/distribution") @ResponseBody
    public Map<String, Object> distribution() {
        List<Report> all = reportService.listAll();
        int[] counts = new int[6];
        for (Report r : all) counts[r.getOverallLevel()]++;
        Map<String, Object> resp = new HashMap<>(); resp.put("code",200);
        resp.put("data", Map.of("优",counts[1],"良",counts[2],"轻度污染",counts[3],"中度污染",counts[4],"重度污染",counts[5]));
        return resp;
    }

    /** 3. AQI指数趋势统计(12个月) */
    @CrossOrigin(origins = "*") @RequestMapping("/trend") @ResponseBody
    public Map<String, Object> trend() {
        List<Report> all = reportService.listAll();
        Map<String, Integer> monthly = new LinkedHashMap<>();
        Calendar cal = Calendar.getInstance();
        for (int i = 11; i >= 0; i--) {
            cal.setTime(new Date()); cal.add(Calendar.MONTH, -i);
            String key = cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH)+1);
            monthly.put(key, 0);
        }
        for (Report r : all) {
            if (r.getCreateTime() == null) continue;
            String key = r.getCreateTime().getYear() + "-" + String.format("%02d", r.getCreateTime().getMonthValue());
            if (monthly.containsKey(key)) monthly.merge(key, 1, Integer::sum);
        }
        Map<String, Object> resp = new HashMap<>(); resp.put("code",200); resp.put("data",monthly); return resp;
    }

    /** 4. 空气质量检测数量实时统计 */
    @CrossOrigin(origins = "*") @RequestMapping("/detection") @ResponseBody
    public Map<String, Object> detection() {
        List<Report> all = reportService.listAll();
        int total = all.size();
        int good = (int) all.stream().filter(r -> r.getOverallLevel() <= 2).count();
        int over = (int) all.stream().filter(r -> r.getOverallLevel() >= 3).count();
        Map<String, Object> resp = new HashMap<>(); resp.put("code",200);
        resp.put("data", Map.of("total",total,"good",good,"over",over)); return resp;
    }

    /** 5a. 城市级统计（某省下各市） */
    @CrossOrigin(origins = "*") @RequestMapping("/city") @ResponseBody
    public Map<String, Object> city(@RequestParam String province) {
        List<Report> all = reportService.listAll().stream()
            .filter(r -> province.equals(r.getProvince())).collect(Collectors.toList());
        Map<String, Map<String, Integer>> result = new LinkedHashMap<>();
        for (Report r : all) {
            String c = r.getCity() != null ? r.getCity() : "未知";
            result.putIfAbsent(c, new HashMap<>());
            Map<String, Integer> m = result.get(c);
            m.merge("so2超标", r.getSo2Level() >= 3 ? 1 : 0, Integer::sum);
            m.merge("co超标", r.getCoLevel() >= 3 ? 1 : 0, Integer::sum);
            m.merge("pm25超标", r.getPm25Level() >= 3 ? 1 : 0, Integer::sum);
            m.merge("aqi超标", r.getOverallLevel() >= 3 ? 1 : 0, Integer::sum);
            m.merge("total", 1, Integer::sum);
        }
        Map<String, Object> resp = new HashMap<>(); resp.put("code",200); resp.put("data",result); return resp;
    }

    /** 5. 全国网格覆盖率统计 */
    @CrossOrigin(origins = "*") @RequestMapping("/coverage") @ResponseBody
    public Map<String, Object> coverage() {
        List<Report> all = reportService.listAll();
        Set<String> provinces = all.stream().map(r -> r.getProvince()).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<String> cities = all.stream().map(r -> r.getCity()).filter(Objects::nonNull).collect(Collectors.toSet());
        int totalProvinces = 34; // 34个省级行政区
        int totalCities = 373;    // 全部地级市/自治州/省直辖县级市
        Map<String, Object> resp = new HashMap<>(); resp.put("code",200);
        resp.put("data", Map.of(
            "coveredProvinces", provinces.size(), "totalProvinces", totalProvinces,
            "provinceRate", Math.round(provinces.size()*100.0/totalProvinces),
            "coveredCities", cities.size(), "totalCities", totalCities,
            "cityRate", Math.round(cities.size()*100.0/totalCities),
            "provinceList", provinces, "cityList", cities
        ));
        return resp;
    }
}
