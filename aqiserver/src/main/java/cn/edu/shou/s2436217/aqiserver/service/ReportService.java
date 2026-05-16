package cn.edu.shou.s2436217.aqiserver.service;

import cn.edu.shou.s2436217.aqiserver.bean.Report;
import cn.edu.shou.s2436217.aqiserver.repostory.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReportService {
    @Autowired private ReportRepository reportRepository;

    public Report submit(Report r) { r.setStatus("待指派"); return reportRepository.save(r); }
    public List<Report> listBySupervisor(int sid) { return reportRepository.findBySupervisorIdOrderByCreateTimeDesc(sid); }
    public List<Report> listAll() { return reportRepository.findAllByOrderByCreateTimeDesc(); }
    public List<Report> listByStatus(String s) { return reportRepository.findByStatusOrderByCreateTimeDesc(s); }
    public List<Report> listByAssignee(int aid) { return reportRepository.findByAssigneeIdOrderByCreateTimeDesc(aid); }

    public Report assign(int id, int assigneeId, String assigneeName) {
        Report r = reportRepository.findById(id).orElse(null);
        if (r != null) { r.setAssigneeId(assigneeId); r.setAssigneeName(assigneeName); r.setStatus("已指派"); reportRepository.save(r); }
        return r;
    }

    public Report markDetected(int id) {
        Report r = reportRepository.findById(id).orElse(null);
        if (r != null) { r.setStatus("已检测"); reportRepository.save(r); }
        return r;
    }

    public void delete(int id) { reportRepository.deleteById(id); }
    public void clearAll() { reportRepository.deleteAll(); }
}
