package cn.edu.shou.s2436217.aqiserver.controller;

import cn.edu.shou.s2436217.aqiserver.bean.Measurement;
import cn.edu.shou.s2436217.aqiserver.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap; import java.util.Map;

@Controller @RequestMapping("/measure")
public class MeasurementController {
    @Autowired private MeasurementService service;

    /** 网格员提交仪器实测数据 */
    @CrossOrigin(origins = "*") @RequestMapping("/submit") @ResponseBody
    public Map<String, Object> submit(@RequestBody Measurement m) {
        service.save(m);
        Map<String, Object> r = new HashMap<>(); r.put("code",200); r.put("msg","检测数据提交成功"); return r;
    }

    /** 查询某上报的实测数据 */
    @CrossOrigin(origins = "*") @RequestMapping("/get") @ResponseBody
    public Map<String, Object> get(@RequestParam int reportId) {
        Measurement m = service.getByReportId(reportId);
        Map<String, Object> r = new HashMap<>(); r.put("code",200); r.put("data",m); return r;
    }
}
