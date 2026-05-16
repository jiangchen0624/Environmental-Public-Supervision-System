package cn.edu.shou.s2436217.aqiserver.repostory;

import cn.edu.shou.s2436217.aqiserver.bean.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    Measurement findByReportId(int reportId);
}
