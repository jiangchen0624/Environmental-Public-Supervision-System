package cn.edu.shou.s2436217.aqiserver.controller;

import cn.edu.shou.s2436217.aqiserver.bean.Measurement;
import cn.edu.shou.s2436217.aqiserver.bean.Report;
import cn.edu.shou.s2436217.aqiserver.service.MeasurementService;
import cn.edu.shou.s2436217.aqiserver.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller @RequestMapping("/report")
public class ReportController {
    @Autowired private ReportService reportService;
    @Autowired private MeasurementService measurementService;

    @CrossOrigin(origins = "*") @RequestMapping("/submit") @ResponseBody
    public Map<String, Object> submit(@RequestBody Report r) {
        reportService.submit(r); Map<String, Object> m = new HashMap<>(); m.put("code",200); m.put("msg","上报成功"); return m;
    }
    @CrossOrigin(origins = "*") @RequestMapping("/list") @ResponseBody
    public Map<String, Object> list(@RequestParam int supervisorId) {
        List<Report> l = reportService.listBySupervisor(supervisorId);
        List<Map<String, Object>> result = joinMeasurements(l);
        Map<String, Object> m = new HashMap<>(); m.put("code",200); m.put("data",result); return m;
    }
    @CrossOrigin(origins = "*") @RequestMapping("/all") @ResponseBody
    public Map<String, Object> all() {
        List<Report> l = reportService.listAll();
        List<Map<String, Object>> result = joinMeasurements(l);
        Map<String, Object> m = new HashMap<>(); m.put("code",200); m.put("data",result); return m;
    }
    @CrossOrigin(origins = "*") @RequestMapping("/byStatus") @ResponseBody
    public Map<String, Object> byStatus(@RequestParam String status) {
        List<Report> l = reportService.listByStatus(status); Map<String, Object> m = new HashMap<>(); m.put("code",200); m.put("data",l); return m;
    }
    @CrossOrigin(origins = "*") @RequestMapping("/myTasks") @ResponseBody
    public Map<String, Object> myTasks(@RequestParam int assigneeId) {
        List<Report> l = reportService.listByAssignee(assigneeId); Map<String, Object> m = new HashMap<>(); m.put("code",200); m.put("data",l); return m;
    }
    @CrossOrigin(origins = "*") @RequestMapping("/assign") @ResponseBody
    public Map<String, Object> assign(@RequestBody Map<String, Object> p) {
        reportService.assign((int)p.get("id"), (int)p.get("assigneeId"), (String)p.get("assigneeName"));
        Map<String, Object> m = new HashMap<>(); m.put("code",200); m.put("msg","指派成功"); return m;
    }
    @CrossOrigin(origins = "*") @RequestMapping("/delete") @ResponseBody
    public Map<String, Object> delete(@RequestParam int id) {
        reportService.delete(id); Map<String, Object> m = new HashMap<>(); m.put("code",200); m.put("msg","删除成功"); return m;
    }
    @CrossOrigin(origins = "*") @RequestMapping("/clear") @ResponseBody
    public Map<String, Object> clear() {
        reportService.clearAll(); Map<String, Object> m = new HashMap<>(); m.put("code",200); m.put("msg","已清空"); return m;
    }

    /** 将 Measurement 数据 JOIN 到 Report 列表 */
    private List<Map<String, Object>> joinMeasurements(List<Report> reports) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Report r : reports) {
            Map<String, Object> rm = new HashMap<>();
            rm.put("id", r.getId()); rm.put("supervisorId", r.getSupervisorId());
            rm.put("pm25Level", r.getPm25Level()); rm.put("so2Level", r.getSo2Level()); rm.put("coLevel", r.getCoLevel());
            rm.put("overallLevel", r.getOverallLevel()); rm.put("province", r.getProvince()); rm.put("city", r.getCity());
            rm.put("status", r.getStatus()); rm.put("assigneeId", r.getAssigneeId()); rm.put("assigneeName", r.getAssigneeName());
            rm.put("createTime", r.getCreateTime());
            Measurement meas = measurementService.getByReportId(r.getId());
            if (meas != null) {
                rm.put("measuredPm25", meas.getPm25Level()); rm.put("measuredSo2", meas.getSo2Level());
                rm.put("measuredCo", meas.getCoLevel()); rm.put("measuredOverall", meas.getOverallLevel());
                rm.put("pm25Raw", meas.getPm25Raw()); rm.put("so2Raw", meas.getSo2Raw()); rm.put("coRaw", meas.getCoRaw());
                rm.put("notes", meas.getNotes());
            }
            result.add(rm);
        }
        return result;
    }
}
