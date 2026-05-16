package cn.edu.shou.s2436217.aqiserver.bean;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/** 网格员仪器实测数据，与上报记录一对一 */
@Entity @Table(name = "measurements") @Data @NoArgsConstructor @AllArgsConstructor
public class Measurement implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /** 关联上报ID */
    private int reportId;

    private int inspectorId;
    private String inspectorName;

    private Double pm25Raw;
    private Double so2Raw;
    private Double coRaw;

    private int pm25Level;
    private int so2Level;
    private int coLevel;
    private int overallLevel;

    @Column(length = 500)
    private String notes;
    private LocalDateTime measureTime = LocalDateTime.now();
}
