/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50724 (5.7.24)
 Source Host           : localhost:3306
 Source Schema         : aqi

 Target Server Type    : MySQL
 Target Server Version : 50724 (5.7.24)
 File Encoding         : 65001

 Date: 16/05/2026 18:47:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for measurements
-- ----------------------------
DROP TABLE IF EXISTS `measurements`;
CREATE TABLE `measurements`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `co_level` int(11) NOT NULL,
  `co_raw` double NULL DEFAULT NULL,
  `inspector_id` int(11) NOT NULL,
  `inspector_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `measure_time` datetime(6) NULL DEFAULT NULL,
  `notes` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `overall_level` int(11) NOT NULL,
  `pm25level` int(11) NOT NULL,
  `pm25raw` double NULL DEFAULT NULL,
  `report_id` int(11) NOT NULL,
  `so2level` int(11) NOT NULL,
  `so2raw` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of measurements
-- ----------------------------
INSERT INTO `measurements` VALUES (1, 3, 12, 5, '王五', '2026-05-16 14:49:05.121729', '', 3, 3, 88, 7, 1, 50);

-- ----------------------------
-- Table structure for reports
-- ----------------------------
DROP TABLE IF EXISTS `reports`;
CREATE TABLE `reports`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `co_level` int(11) NOT NULL,
  `create_time` datetime(6) NULL DEFAULT NULL,
  `overall_level` int(11) NOT NULL,
  `pm25level` int(11) NOT NULL,
  `so2level` int(11) NOT NULL,
  `supervisor_id` int(11) NOT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `assignee_id` int(11) NOT NULL,
  `assignee_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `confirm_time` datetime(6) NULL DEFAULT NULL,
  `measured_co` int(11) NULL DEFAULT NULL,
  `measured_overall` int(11) NULL DEFAULT NULL,
  `measured_pm25` int(11) NULL DEFAULT NULL,
  `measured_so2` int(11) NULL DEFAULT NULL,
  `notes` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `co_raw` double NULL DEFAULT NULL,
  `pm25raw` double NULL DEFAULT NULL,
  `so2raw` double NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of reports
-- ----------------------------
INSERT INTO `reports` VALUES (5, 2, '2026-05-16 11:57:56.067375', 3, 2, 3, 1, '上海市', '上海', 0, NULL, '2026-05-16 14:22:49.708201', 2, 3, 3, 2, NULL, '已检测', 5.3, 82.5, 280);
INSERT INTO `reports` VALUES (6, 1, '2026-05-16 13:54:30.429343', 4, 1, 4, 1, '上海市', '上海', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '待指派', NULL, NULL, NULL);
INSERT INTO `reports` VALUES (7, 1, '2026-05-16 13:54:33.838996', 2, 1, 2, 1, '上海市', '上海', 5, '王五', NULL, NULL, NULL, NULL, NULL, NULL, '已检测', NULL, NULL, NULL);
INSERT INTO `reports` VALUES (8, 1, '2026-05-16 14:22:38.937750', 3, 3, 2, 1, '广州市', '广东', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '待指派', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `role` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '张三', '13800000000', '123456', '上海', '上海市', '公众监督员');
INSERT INTO `users` VALUES (2, '余剑豪', '13340082233', '123456', '上海', '上海市', '决策者');
INSERT INTO `users` VALUES (3, '江辰', '19397261717', '123456', '上海', '上海市', '决策者');
INSERT INTO `users` VALUES (4, '李四', '13800000001', '123456', '北京', '北京市', '系统管理员');
INSERT INTO `users` VALUES (5, '王五', '13800000002', '123456', '北京', '北京市', 'AQI检测网格员');

SET FOREIGN_KEY_CHECKS = 1;
