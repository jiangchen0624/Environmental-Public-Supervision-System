package cn.edu.shou.s2436217.aqiserver.service;

import cn.edu.shou.s2436217.aqiserver.bean.Measurement;
import cn.edu.shou.s2436217.aqiserver.repostory.MeasurementRepository;
import cn.edu.shou.s2436217.aqiserver.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeasurementService {
    @Autowired private MeasurementRepository repo;
    @Autowired private ReportService reportService;

    public Measurement save(Measurement m) {
        Measurement saved = repo.save(m);
        reportService.markDetected(m.getReportId());
        return saved;
    }

    public Measurement getByReportId(int reportId) {
        return repo.findByReportId(reportId);
    }
}
