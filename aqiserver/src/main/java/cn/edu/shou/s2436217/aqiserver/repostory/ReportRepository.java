package cn.edu.shou.s2436217.aqiserver.repostory;

import cn.edu.shou.s2436217.aqiserver.bean.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findBySupervisorIdOrderByCreateTimeDesc(int supervisorId);
    List<Report> findAllByOrderByCreateTimeDesc();
    List<Report> findByStatusOrderByCreateTimeDesc(String status);
    List<Report> findByAssigneeIdOrderByCreateTimeDesc(int assigneeId);
    List<Report> findByCityContainingOrProvinceContainingOrderByCreateTimeDesc(String city, String province);
    List<Report> findByCreateTimeBetweenOrderByCreateTimeDesc(LocalDateTime start, LocalDateTime end);
}
