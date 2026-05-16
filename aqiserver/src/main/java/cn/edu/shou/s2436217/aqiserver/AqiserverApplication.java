package cn.edu.shou.s2436217.aqiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * AQI服务器应用主入口类
 * Spring Boot应用的启动类，负责启动整个应用程序
 */
@SpringBootApplication
@EnableTransactionManagement
public class AqiserverApplication {

    /**
     * 应用程序主入口方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(AqiserverApplication.class, args);
    }

}

