-- MySQL dump 10.13  Distrib 9.7.0, for Win64 (x86_64)
--
-- Host: localhost    Database: aqi
-- ------------------------------------------------------
-- Server version	9.7.0
SET NAMES utf8mb4;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '63af0553-51ca-11f1-be17-e8b0c52c5172:1-327';

--
-- Current Database: `aqi`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `aqi` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `aqi`;

--
-- Table structure for table `measurements`
--

DROP TABLE IF EXISTS `measurements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `measurements` (
  `id` int NOT NULL AUTO_INCREMENT,
  `co_level` int NOT NULL,
  `co_raw` double DEFAULT NULL,
  `inspector_id` int NOT NULL,
  `inspector_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `measure_time` datetime(6) DEFAULT NULL,
  `notes` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `overall_level` int NOT NULL,
  `pm25level` int NOT NULL,
  `pm25raw` double DEFAULT NULL,
  `report_id` int NOT NULL,
  `so2level` int NOT NULL,
  `so2raw` double DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `measurements`
--

LOCK TABLES `measurements` WRITE;
/*!40000 ALTER TABLE `measurements` DISABLE KEYS */;
INSERT INTO `measurements` (`id`, `co_level`, `co_raw`, `inspector_id`, `inspector_name`, `measure_time`, `notes`, `overall_level`, `pm25level`, `pm25raw`, `report_id`, `so2level`, `so2raw`) VALUES (1,1,0.54,35,'网格员-冀','2026-05-23 16:44:09.532282','仪器型号: AQI-8000, 环境温度: 28°C',2,2,40.6,1,1,18.3),(2,2,3.97,32,'网格员-鲁','2026-05-23 16:44:09.552989','仪器型号: AQI-8000, 环境温度: 32°C',4,4,123.9,2,3,281.6),(3,2,3.62,40,'网格员-粤','2026-05-23 16:44:09.568775','仪器型号: AQI-8000, 环境温度: 23°C',4,3,88.8,3,4,479.6),(4,3,8.25,44,'网格员-豫','2026-05-23 16:44:09.583435','仪器型号: AQI-8000, 环境温度: 18°C',5,5,180.3,5,4,657),(5,1,1.38,38,'网格员-京','2026-05-23 16:44:09.598460','仪器型号: AQI-8000, 环境温度: 31°C',1,1,26.1,6,1,19.1),(6,1,1.56,46,'网格员-川','2026-05-23 16:44:09.611459','仪器型号: AQI-8000, 环境温度: 22°C',2,2,70.5,7,2,116.1),(7,2,3.92,43,'网格员-冀','2026-05-23 16:44:09.625487','仪器型号: AQI-8000, 环境温度: 21°C',3,3,108.3,8,3,234.6),(8,3,13.44,38,'网格员-鲁','2026-05-23 16:44:09.640695','仪器型号: AQI-8000, 环境温度: 26°C',4,4,144,9,3,291.4),(9,1,1.23,38,'网格员-川','2026-05-23 16:44:09.656929','仪器型号: AQI-8000, 环境温度: 28°C',1,1,28.2,11,1,36.5),(10,2,3.94,34,'网格员-沪','2026-05-23 16:44:09.672940','仪器型号: AQI-8000, 环境温度: 24°C',2,2,43,12,2,149.6),(11,2,3.02,45,'网格员-辽','2026-05-23 16:44:09.686836','仪器型号: AQI-8000, 环境温度: 34°C',3,3,105.3,13,3,386),(12,4,15.29,39,'网格员-川','2026-05-23 16:44:09.703844','仪器型号: AQI-8000, 环境温度: 34°C',5,5,227.1,15,5,1129.1),(13,1,1.59,39,'网格员-川','2026-05-23 16:44:09.720369','仪器型号: AQI-8000, 环境温度: 27°C',2,2,41.4,16,1,43),(14,2,2.85,45,'网格员-辽','2026-05-23 16:44:09.735033','仪器型号: AQI-8000, 环境温度: 25°C',3,3,105.6,17,2,114.3),(15,3,9.43,40,'网格员-津','2026-05-23 16:44:09.748860','仪器型号: AQI-8000, 环境温度: 18°C',4,4,119.5,18,3,349.6),(16,2,3.51,42,'网格员-辽','2026-05-23 16:44:09.762683','仪器型号: AQI-8000, 环境温度: 21°C',4,3,98.9,19,4,717.4),(17,2,2.03,35,'网格员-陕','2026-05-23 16:44:09.776985','仪器型号: AQI-8000, 环境温度: 20°C',2,2,63.8,21,1,28.6),(18,3,8.58,34,'网格员-津','2026-05-23 16:44:09.791501','仪器型号: AQI-8000, 环境温度: 28°C',3,3,95.8,22,2,89.2),(19,3,7.37,46,'网格员-苏','2026-05-23 16:44:09.808517','仪器型号: AQI-8000, 环境温度: 23°C',4,4,129.7,23,4,750.4),(20,2,3.93,32,'网格员-浙','2026-05-23 16:44:09.823545','仪器型号: AQI-8000, 环境温度: 28°C',3,3,80.1,24,3,200.3),(21,4,19.17,43,'网格员-辽','2026-05-23 16:44:09.837775','仪器型号: AQI-8000, 环境温度: 26°C',5,5,198.2,25,4,661.1),(22,2,2.53,45,'网格员-苏','2026-05-23 16:44:09.856425','仪器型号: AQI-8000, 环境温度: 23°C',2,2,73.7,26,2,55.5),(23,1,1.26,42,'网格员-川','2026-05-23 16:44:09.874485','仪器型号: AQI-8000, 环境温度: 16°C',2,2,46.6,27,1,27.9),(24,3,10.54,32,'网格员-粤','2026-05-23 16:44:09.891626','仪器型号: AQI-8000, 环境温度: 27°C',4,4,125.3,28,3,257.3),(25,2,3.15,34,'网格员-陕','2026-05-23 16:44:09.907626','仪器型号: AQI-8000, 环境温度: 20°C',3,3,92.4,29,2,104.9),(26,1,0.66,41,'网格员-浙','2026-05-23 16:44:09.925609','仪器型号: AQI-8000, 环境温度: 16°C',1,1,20.3,30,1,31),(27,1,1.76,36,'网格员-湘','2026-05-23 16:44:09.944608','仪器型号: AQI-8000, 环境温度: 17°C',2,2,49.9,31,2,114.6),(28,3,9.44,35,'网格员-京','2026-05-23 16:44:09.960615','仪器型号: AQI-8000, 环境温度: 20°C',3,3,85.3,32,3,215.7),(29,1,1.38,43,'网格员-辽','2026-05-23 16:44:09.973174','仪器型号: AQI-8000, 环境温度: 30°C',2,2,41.4,34,1,50),(30,2,3.23,42,'网格员-湘','2026-05-23 16:44:09.988444','仪器型号: AQI-8000, 环境温度: 32°C',3,3,92.3,35,2,111.1),(31,2,3.38,40,'网格员-津','2026-05-23 16:44:10.008459','仪器型号: AQI-8000, 环境温度: 25°C',2,1,18.5,36,1,32.4),(32,1,1.95,43,'网格员-鲁','2026-05-23 16:44:10.023471','仪器型号: AQI-8000, 环境温度: 29°C',3,3,110.3,37,2,96.7),(33,2,3.43,43,'网格员-粤','2026-05-23 16:44:10.036075','仪器型号: AQI-8000, 环境温度: 28°C',4,4,133.4,38,3,207.5),(34,4,23.68,33,'网格员-鄂','2026-05-23 16:44:10.051325','仪器型号: AQI-8000, 环境温度: 28°C',5,5,232,39,5,1121.1),(35,2,2.61,33,'网格员-鲁','2026-05-23 16:44:10.065353','仪器型号: AQI-8000, 环境温度: 34°C',2,2,66.9,40,2,120.5),(36,3,4.54,41,'网格员-鄂','2026-05-23 16:44:10.080399','仪器型号: AQI-8000, 环境温度: 20°C',3,3,93.4,41,3,207.5),(37,1,1.55,44,'网格员-陕','2026-05-23 16:44:10.095966','仪器型号: AQI-8000, 环境温度: 25°C',2,2,45.6,43,1,20.6),(38,2,3.52,45,'网格员-陕','2026-05-23 16:44:10.112953','仪器型号: AQI-8000, 环境温度: 16°C',3,3,110.5,44,3,190.3),(39,2,2.32,34,'网格员-辽','2026-05-23 16:44:10.126969','仪器型号: AQI-8000, 环境温度: 23°C',3,3,91.6,45,2,108.9),(40,1,1.27,44,'网格员-苏','2026-05-23 16:44:10.142970','仪器型号: AQI-8000, 环境温度: 32°C',3,2,43.7,46,3,299.7),(41,3,12.13,34,'网格员-沪','2026-05-23 16:44:10.160994','仪器型号: AQI-8000, 环境温度: 20°C',4,4,126.9,47,3,363.1),(42,4,14.99,40,'网格员-沪','2026-05-23 16:44:10.177508','仪器型号: AQI-8000, 环境温度: 22°C',5,5,222.7,48,4,693.2),(43,1,1.38,40,'网格员-渝','2026-05-23 16:44:10.190750','仪器型号: AQI-8000, 环境温度: 29°C',2,2,60.7,49,2,144.5),(44,1,0.9,35,'网格员-苏','2026-05-23 16:44:10.205749','仪器型号: AQI-8000, 环境温度: 18°C',2,1,22.7,50,2,122.4),(45,2,3.45,43,'网格员-鲁','2026-05-23 16:44:10.218780','仪器型号: AQI-8000, 环境温度: 33°C',3,3,96.5,51,2,56.6),(46,1,1.01,43,'网格员-京','2026-05-23 16:44:10.231032','仪器型号: AQI-8000, 环境温度: 29°C',2,2,35.6,52,1,17.9),(47,1,0.94,36,'网格员-川','2026-05-23 16:44:10.243576','仪器型号: AQI-8000, 环境温度: 23°C',1,1,31.2,53,1,42.5),(48,2,2.62,34,'网格员-鄂','2026-05-23 16:44:10.256526','仪器型号: AQI-8000, 环境温度: 33°C',2,2,53.4,54,2,140),(49,2,3.75,38,'网格员-川','2026-05-23 16:44:10.273936','仪器型号: AQI-8000, 环境温度: 21°C',3,3,108.2,55,3,416.8),(50,1,1.29,42,'网格员-京','2026-05-23 16:44:10.289045','仪器型号: AQI-8000, 环境温度: 16°C',2,2,42.5,56,1,30.9),(51,3,10.23,41,'网格员-渝','2026-05-23 16:44:10.302939','仪器型号: AQI-8000, 环境温度: 23°C',4,4,147.7,57,3,464),(52,1,1.37,46,'网格员-渝','2026-05-23 16:44:10.322935','仪器型号: AQI-8000, 环境温度: 27°C',1,1,28.9,58,1,46),(53,2,3.83,35,'网格员-津','2026-05-23 16:44:10.344933','仪器型号: AQI-8000, 环境温度: 26°C',3,3,77.8,59,2,60);
/*!40000 ALTER TABLE `measurements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reports`
--

DROP TABLE IF EXISTS `reports`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reports` (
  `id` int NOT NULL AUTO_INCREMENT,
  `co_level` int NOT NULL,
  `create_time` datetime(6) DEFAULT NULL,
  `overall_level` int NOT NULL,
  `pm25level` int NOT NULL,
  `so2level` int NOT NULL,
  `supervisor_id` int NOT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `assignee_id` int NOT NULL,
  `assignee_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `confirm_time` datetime(6) DEFAULT NULL,
  `measured_co` int DEFAULT NULL,
  `measured_overall` int DEFAULT NULL,
  `measured_pm25` int DEFAULT NULL,
  `measured_so2` int DEFAULT NULL,
  `notes` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `co_raw` double DEFAULT NULL,
  `pm25raw` double DEFAULT NULL,
  `so2raw` double DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reports`
--

LOCK TABLES `reports` WRITE;
/*!40000 ALTER TABLE `reports` DISABLE KEYS */;
INSERT INTO `reports` (`id`, `co_level`, `create_time`, `overall_level`, `pm25level`, `so2level`, `supervisor_id`, `city`, `province`, `assignee_id`, `assignee_name`, `confirm_time`, `measured_co`, `measured_overall`, `measured_pm25`, `measured_so2`, `notes`, `status`, `co_raw`, `pm25raw`, `so2raw`) VALUES (1,1,'2026-05-20 16:44:08.485955',2,2,1,18,'石家庄','河北',35,'网格员-冀',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(2,2,'2026-05-08 16:44:08.517948',4,4,3,18,'石家庄','河北',32,'网格员-鲁',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(3,2,'2026-04-25 16:44:08.533957',4,3,4,30,'唐山','河北',40,'网格员-粤',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(4,1,'2026-04-08 16:44:08.549948',2,2,2,28,'保定','河北',34,'网格员-沪',NULL,NULL,NULL,NULL,NULL,NULL,'已指派',NULL,NULL,NULL),(5,3,'2026-03-24 16:44:08.564956',5,5,4,21,'石家庄','河北',44,'网格员-豫',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(6,1,'2026-05-21 16:44:08.579125',1,1,1,23,'北京','北京',38,'网格员-京',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(7,1,'2026-05-13 16:44:08.594705',2,2,2,10,'北京','北京',46,'网格员-川',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(8,2,'2026-04-13 16:44:08.608712',3,3,3,30,'北京','北京',43,'网格员-冀',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(9,3,'2026-02-22 16:44:08.621721',4,4,3,8,'北京','北京',38,'网格员-鲁',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(10,2,'2026-01-23 16:44:08.637231',2,2,1,11,'北京','北京',39,'网格员-鄂',NULL,NULL,NULL,NULL,NULL,NULL,'已指派',NULL,NULL,NULL),(11,1,'2026-05-22 16:44:08.650453',1,1,1,4,'上海','上海',38,'网格员-川',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(12,2,'2026-05-15 16:44:08.664498',2,2,2,19,'上海','上海',34,'网格员-沪',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(13,2,'2026-04-18 16:44:08.678506',3,3,3,13,'上海','上海',45,'网格员-辽',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(14,3,'2026-03-09 16:44:08.693020',4,4,4,25,'上海','上海',46,'网格员-鲁',NULL,NULL,NULL,NULL,NULL,NULL,'已指派',NULL,NULL,NULL),(15,4,'2026-02-02 16:44:08.707038',5,5,5,26,'上海','上海',39,'网格员-川',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(16,1,'2026-05-18 16:44:08.720037',2,2,1,18,'广州','广东',39,'网格员-川',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(17,2,'2026-05-03 16:44:08.735036',3,3,2,7,'广州','广东',45,'网格员-辽',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(18,3,'2026-03-29 16:44:08.750818',4,4,3,5,'深圳','广东',40,'网格员-津',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(19,2,'2026-02-27 16:44:08.768379',4,3,4,29,'广州','广东',42,'网格员-辽',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(20,1,'2026-01-13 16:44:08.784143',2,2,2,25,'深圳','广东',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'待指派',NULL,NULL,NULL),(21,2,'2026-05-16 16:44:08.790227',2,2,1,24,'成都','四川',35,'网格员-陕',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(22,3,'2026-04-23 16:44:08.804208',3,3,2,9,'成都','四川',34,'网格员-津',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(23,3,'2026-03-19 16:44:08.819237',4,4,4,21,'成都','四川',46,'网格员-苏',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(24,2,'2026-05-11 16:44:08.836505',3,3,3,28,'武汉','湖北',32,'网格员-浙',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(25,4,'2026-04-11 16:44:08.851874',5,5,4,18,'武汉','湖北',43,'网格员-辽',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(26,2,'2026-02-12 16:44:08.869902',2,2,2,8,'武汉','湖北',45,'网格员-苏',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(27,1,'2026-05-17 16:44:08.886458',2,2,1,20,'西安','陕西',42,'网格员-川',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(28,3,'2026-04-15 16:44:08.900440',4,4,3,14,'西安','陕西',32,'网格员-粤',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(29,2,'2026-03-14 16:44:08.920465',3,3,2,12,'西安','陕西',34,'网格员-陕',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(30,1,'2026-05-19 16:44:08.934441',1,1,1,23,'南京','江苏',41,'网格员-浙',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(31,1,'2026-04-28 16:44:08.949440',2,2,2,21,'南京','江苏',36,'网格员-湘',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(32,3,'2026-04-03 16:44:08.964807',3,3,3,12,'苏州','江苏',35,'网格员-京',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(33,3,'2026-03-04 16:44:08.983151',4,4,3,17,'南京','江苏',35,'网格员-浙',NULL,NULL,NULL,NULL,NULL,NULL,'已指派',NULL,NULL,NULL),(34,1,'2026-05-14 16:44:08.996706',2,2,1,24,'杭州','浙江',43,'网格员-辽',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(35,2,'2026-04-20 16:44:09.018717',3,3,2,26,'杭州','浙江',42,'网格员-湘',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(36,2,'2026-02-17 16:44:09.042860',2,1,1,6,'宁波','浙江',40,'网格员-津',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(37,1,'2026-05-09 16:44:09.057865',3,3,2,10,'济南','山东',43,'网格员-鲁',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(38,2,'2026-04-05 16:44:09.073819',4,4,3,20,'青岛','山东',43,'网格员-粤',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(39,4,'2026-02-07 16:44:09.094362',5,5,5,22,'济南','山东',33,'网格员-鄂',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(40,2,'2026-05-12 16:44:09.108254',2,2,2,30,'郑州','河南',33,'网格员-鲁',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(41,3,'2026-04-17 16:44:09.122253',3,3,3,4,'郑州','河南',41,'网格员-鄂',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(42,4,'2026-03-12 16:44:09.143268',4,4,4,15,'郑州','河南',40,'网格员-辽',NULL,NULL,NULL,NULL,NULL,NULL,'已指派',NULL,NULL,NULL),(43,1,'2026-05-05 16:44:09.157274',2,2,1,4,'长沙','湖南',44,'网格员-陕',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(44,2,'2026-04-01 16:44:09.169809',3,3,3,9,'长沙','湖南',45,'网格员-陕',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(45,2,'2026-05-01 16:44:09.183648',3,3,2,18,'沈阳','辽宁',34,'网格员-辽',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(46,1,'2026-03-26 16:44:09.198203',3,2,3,3,'大连','辽宁',44,'网格员-苏',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(47,3,'2026-05-07 16:44:09.211192',4,4,3,25,'太原','山西',34,'网格员-沪',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(48,4,'2026-04-09 16:44:09.223207',5,5,4,30,'太原','山西',40,'网格员-沪',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(49,1,'2026-04-29 16:44:09.237051',2,2,2,19,'合肥','安徽',40,'网格员-渝',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(50,1,'2026-04-26 16:44:09.253047',2,1,2,28,'福州','福建',35,'网格员-苏',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(51,2,'2026-04-21 16:44:09.279062',3,3,2,23,'南昌','江西',43,'网格员-鲁',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(52,1,'2026-05-04 16:44:09.293796',2,2,1,21,'南宁','广西',43,'网格员-京',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(53,1,'2026-05-15 16:44:09.305798',1,1,1,22,'昆明','云南',36,'网格员-川',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(54,2,'2026-04-07 16:44:09.320813',2,2,2,8,'贵阳','贵州',34,'网格员-鄂',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(55,2,'2026-03-22 16:44:09.334794',3,3,3,13,'兰州','甘肃',38,'网格员-川',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(56,1,'2026-04-24 16:44:09.350797',2,2,1,16,'长春','吉林',42,'网格员-京',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(57,3,'2026-03-31 16:44:09.363799',4,4,3,15,'哈尔滨','黑龙江',41,'网格员-渝',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(58,1,'2026-05-18 16:44:09.378259',1,1,1,28,'海口','海南',46,'网格员-渝',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(59,2,'2026-05-06 16:44:09.391425',3,3,2,31,'重庆','重庆',35,'网格员-津',NULL,NULL,NULL,NULL,NULL,NULL,'已检测',NULL,NULL,NULL),(60,2,'2025-12-24 16:44:09.403442',3,3,3,11,'保定','河北',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'待指派',NULL,NULL,NULL),(61,5,'2025-12-14 16:44:09.409443',5,5,4,27,'唐山','河北',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'待指派',NULL,NULL,NULL),(62,1,'2026-01-03 16:44:09.416451',2,2,1,11,'北京','北京',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'待指派',NULL,NULL,NULL),(63,3,'2026-01-08 16:44:09.424450',3,3,2,19,'上海','上海',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'待指派',NULL,NULL,NULL),(64,3,'2026-01-18 16:44:09.433547',4,4,4,4,'深圳','广东',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'待指派',NULL,NULL,NULL),(65,3,'2026-01-28 16:44:09.444944',3,3,3,5,'成都','四川',36,'网格员-川',NULL,NULL,NULL,NULL,NULL,NULL,'已指派',NULL,NULL,NULL),(66,1,'2025-12-09 16:44:09.466955',2,2,1,13,'武汉','湖北',41,'网格员-川',NULL,NULL,NULL,NULL,NULL,NULL,'已指派',NULL,NULL,NULL),(67,4,'2025-11-29 16:44:09.486469',5,5,5,25,'西安','陕西',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'待指派',NULL,NULL,NULL),(68,3,'2025-12-19 16:44:09.495344',3,2,2,5,'苏州','江苏',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'待指派',NULL,NULL,NULL),(69,2,'2025-12-29 16:44:09.506510',3,3,2,26,'宁波','浙江',38,'网格员-苏',NULL,NULL,NULL,NULL,NULL,NULL,'已指派',NULL,NULL,NULL),(70,4,'2026-01-11 16:44:09.524676',4,4,3,10,'青岛','山东',0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'待指派',NULL,NULL,NULL);
/*!40000 ALTER TABLE `reports` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `phone` (`phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `name`, `phone`, `pwd`, `province`, `city`, `role`) VALUES (1,'系统管理员','13800000001','123456','北京','北京','系统管理员'),(2,'决策者','13800000002','123456','北京','北京','决策者'),(3,'赵监督','13801001001','123456','河北','石家庄','公众监督员'),(4,'钱监督','13801001002','123456','河北','唐山','公众监督员'),(5,'孙监督','13801002001','123456','北京','北京','公众监督员'),(6,'李监督','13801003001','123456','上海','上海','公众监督员'),(7,'周监督','13801004001','123456','广东','广州','公众监督员'),(8,'吴监督','13801004002','123456','广东','深圳','公众监督员'),(9,'郑监督','13801005001','123456','四川','成都','公众监督员'),(10,'王监督','13801006001','123456','湖北','武汉','公众监督员'),(11,'冯监督','13801007001','123456','陕西','西安','公众监督员'),(12,'陈监督','13801008001','123456','江苏','南京','公众监督员'),(13,'褚监督','13801008002','123456','江苏','苏州','公众监督员'),(14,'卫监督','13801009001','123456','浙江','杭州','公众监督员'),(15,'蒋监督','13801010001','123456','山东','济南','公众监督员'),(16,'沈监督','13801010002','123456','山东','青岛','公众监督员'),(17,'韩监督','13801011001','123456','河南','郑州','公众监督员'),(18,'杨监督','13801012001','123456','湖南','长沙','公众监督员'),(19,'朱监督','13801013001','123456','辽宁','沈阳','公众监督员'),(20,'秦监督','13801014001','123456','山西','太原','公众监督员'),(21,'许监督','13801015001','123456','安徽','合肥','公众监督员'),(22,'何监督','13801016001','123456','福建','福州','公众监督员'),(23,'吕监督','13801017001','123456','江西','南昌','公众监督员'),(24,'张监督','13801018001','123456','广西','南宁','公众监督员'),(25,'孔监督','13801019001','123456','云南','昆明','公众监督员'),(26,'曹监督','13801020001','123456','贵州','贵阳','公众监督员'),(27,'严监督','13801021001','123456','甘肃','兰州','公众监督员'),(28,'华监督','13801022001','123456','吉林','长春','公众监督员'),(29,'金监督','13801023001','123456','黑龙江','哈尔滨','公众监督员'),(30,'魏监督','13801024001','123456','海南','海口','公众监督员'),(31,'陶监督','13801025001','123456','重庆','重庆','公众监督员'),(32,'网格员-京','13802001001','123456','北京','北京','AQI检测网格员'),(33,'网格员-津','13802002001','123456','天津','天津','AQI检测网格员'),(34,'网格员-沪','13802003001','123456','上海','上海','AQI检测网格员'),(35,'网格员-冀','13802004001','123456','河北','石家庄','AQI检测网格员'),(36,'网格员-粤','13802005001','123456','广东','广州','AQI检测网格员'),(37,'网格员-川','13802006001','123456','四川','成都','AQI检测网格员'),(38,'网格员-鄂','13802007001','123456','湖北','武汉','AQI检测网格员'),(39,'网格员-陕','13802008001','123456','陕西','西安','AQI检测网格员'),(40,'网格员-苏','13802009001','123456','江苏','南京','AQI检测网格员'),(41,'网格员-浙','13802010001','123456','浙江','杭州','AQI检测网格员'),(42,'网格员-鲁','13802011001','123456','山东','济南','AQI检测网格员'),(43,'网格员-豫','13802012001','123456','河南','郑州','AQI检测网格员'),(44,'网格员-湘','13802013001','123456','湖南','长沙','AQI检测网格员'),(45,'网格员-辽','13802014001','123456','辽宁','沈阳','AQI检测网格员'),(46,'网格员-渝','13802015001','123456','重庆','重庆','AQI检测网格员');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-23 17:49:47
