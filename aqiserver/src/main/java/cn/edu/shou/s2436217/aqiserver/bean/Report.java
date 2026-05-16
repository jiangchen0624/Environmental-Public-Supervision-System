package cn.edu.shou.s2436217.aqiserver.bean;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/** 监督员上报记录 */
@Entity @Table(name = "reports") @Data @NoArgsConstructor @AllArgsConstructor
public class Report implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int supervisorId;
    private int pm25Level;
    private int so2Level;
    private int coLevel;
    private int overallLevel;
    private String province;
    private String city;
    private String status = "待指派";
    private int assigneeId;
    private String assigneeName;
    private LocalDateTime createTime = LocalDateTime.now();
}
