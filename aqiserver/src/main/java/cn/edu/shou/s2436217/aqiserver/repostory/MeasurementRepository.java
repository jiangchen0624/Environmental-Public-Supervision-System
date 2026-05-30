package cn.edu.shou.s2436217.aqiserver.repostory;

import cn.edu.shou.s2436217.aqiserver.bean.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    List<Measurement> findByReportIdOrderByMeasureTimeDesc(int reportId);
}
