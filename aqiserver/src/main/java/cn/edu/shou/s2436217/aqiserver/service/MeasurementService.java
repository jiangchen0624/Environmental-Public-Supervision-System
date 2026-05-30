package cn.edu.shou.s2436217.aqiserver.service;

import cn.edu.shou.s2436217.aqiserver.bean.Measurement;
import cn.edu.shou.s2436217.aqiserver.repostory.MeasurementRepository;
import cn.edu.shou.s2436217.aqiserver.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MeasurementService {
    @Autowired private MeasurementRepository repo;
    @Autowired private ReportService reportService;

    public Measurement save(Measurement m) {
        // 如果已有该上报的测量记录，则更新（而非新增重复记录）
        List<Measurement> existing = repo.findByReportIdOrderByMeasureTimeDesc(m.getReportId());
        if (!existing.isEmpty()) {
            Measurement old = existing.get(0);
            old.setPm25Raw(m.getPm25Raw());
            old.setSo2Raw(m.getSo2Raw());
            old.setCoRaw(m.getCoRaw());
            old.setPm25Level(m.getPm25Level());
            old.setSo2Level(m.getSo2Level());
            old.setCoLevel(m.getCoLevel());
            old.setOverallLevel(m.getOverallLevel());
            old.setNotes(m.getNotes());
            old.setMeasureTime(java.time.LocalDateTime.now());
            return repo.save(old);
        }
        Measurement saved = repo.save(m);
        reportService.markDetected(m.getReportId());
        return saved;
    }

    public Measurement getByReportId(int reportId) {
        List<Measurement> list = repo.findByReportIdOrderByMeasureTimeDesc(reportId);
        return list.isEmpty() ? null : list.get(0);
    }
}
