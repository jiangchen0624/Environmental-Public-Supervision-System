package cn.edu.shou.s2436217.aqiserver.bean;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * 用户实体类
 * 对应数据库中的users表，用于存储用户信息
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {

    /**
     * 用户ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * 用户手机号，用于登录和身份验证
     */
    private String phone;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户密码，数据库字段名为pwd
     */
    @Column(name = "pwd")
    private String password;

    /**
     * 用户所在省份
     */
    private String province;

    /**
     * 用户所在城市
     */
    private String city;

    /**
     * 用户角色身份
     * 公众监督员 / AQI检测网格员 / 系统管理员 / 决策者
     */
    private String role;
}
