/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : staffdb

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 29/12/2022 21:34:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for depart
-- ----------------------------
DROP TABLE IF EXISTS `depart`;
CREATE TABLE `depart`  (
  `id` int(0) NOT NULL,
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `manager` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `intro` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of depart
-- ----------------------------
INSERT INTO `depart` VALUES (1, '总经办', 'jeazim', '公司的首脑');
INSERT INTO `depart` VALUES (2, '销售部', '小F', NULL);
INSERT INTO `depart` VALUES (3, '技术部', '小S', NULL);
INSERT INTO `depart` VALUES (4, '采购部', NULL, NULL);
INSERT INTO `depart` VALUES (5, '生产部', NULL, NULL);
INSERT INTO `depart` VALUES (6, '质管部', NULL, NULL);
INSERT INTO `depart` VALUES (7, '财务部', NULL, NULL);

-- ----------------------------
-- Table structure for edulevel
-- ----------------------------
DROP TABLE IF EXISTS `edulevel`;
CREATE TABLE `edulevel`  (
  `code` int(0) NOT NULL,
  `intro` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of edulevel
-- ----------------------------
INSERT INTO `edulevel` VALUES (0, '小学');
INSERT INTO `edulevel` VALUES (1, '初中');
INSERT INTO `edulevel` VALUES (2, '高中');
INSERT INTO `edulevel` VALUES (3, '职高');
INSERT INTO `edulevel` VALUES (4, '大本');
INSERT INTO `edulevel` VALUES (5, '大专');
INSERT INTO `edulevel` VALUES (6, '硕士');
INSERT INTO `edulevel` VALUES (7, '博士');
INSERT INTO `edulevel` VALUES (8, '博士后');

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job`  (
  `code` int(0) NOT NULL,
  `description` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job
-- ----------------------------
INSERT INTO `job` VALUES (1, '经理');
INSERT INTO `job` VALUES (2, '员工');

-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person`  (
  `id` int(0) NOT NULL,
  `password` varchar(40) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `name` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `sex` varchar(4) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `age` int(0) NOT NULL,
  `authority` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `department` int(0) NOT NULL,
  `job` varchar(8) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `education` varchar(8) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `address` varchar(8) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `specialty` varchar(8) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `tel` varchar(11) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `email` varchar(20) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  `state` varchar(8) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `remark` varchar(50) CHARACTER SET gbk COLLATE gbk_chinese_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `department`(`department`) USING BTREE,
  CONSTRAINT `department` FOREIGN KEY (`department`) REFERENCES `depart` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of person
-- ----------------------------
INSERT INTO `person` VALUES (1, '54091677309438', 'jeazim', '男', 19, '管理员', 1, '经理', '大本', '湖南', 'C/C++', '123456789', '123456789', 'T', '公司头头');
INSERT INTO `person` VALUES (2, '54091677114107', '小A', '男', 20, '用户', 1, '员工', '大本', '湖南', 'JAVA', '123456789', '123456123', 'T', '无');
INSERT INTO `person` VALUES (3, '54091677114107', '小C', '男', 19, '用户', 1, '员工', '大本', NULL, NULL, '123456789', NULL, 'T', NULL);
INSERT INTO `person` VALUES (4, '54091677114107', '小D', '男', 19, '用户', 1, '员工', '大本', NULL, NULL, '123456789', NULL, 'T', NULL);
INSERT INTO `person` VALUES (5, '54091677114107', '小E', '男', 19, '用户', 1, '员工', '大本', NULL, NULL, '123456789', NULL, 'T', NULL);
INSERT INTO `person` VALUES (6, '54091677114107', '小F', '男', 19, '用户', 2, '经理', '大本', NULL, NULL, '132456789', NULL, 'T', NULL);
INSERT INTO `person` VALUES (7, '54091677114107', '小F', '男', 19, '用户', 2, '员工', '大本', NULL, NULL, '123456789', NULL, 'T', NULL);
INSERT INTO `person` VALUES (8, '54091677114107', '小L', '男', 19, '用户', 2, '员工', '大本', NULL, NULL, '123456789', NULL, 'T', NULL);
INSERT INTO `person` VALUES (9, '54091677114107', '小M', '女', 19, '用户', 4, '员工', '大本', NULL, NULL, '123456789', NULL, 'F', NULL);
INSERT INTO `person` VALUES (10, '54091677114107', '小S', '女', 19, '用户', 3, '经理', '大本', NULL, NULL, '123456789', NULL, 'T', NULL);
INSERT INTO `person` VALUES (11, '54091677114107', '小P', '男', 19, '用户', 3, '员工', '大本', NULL, NULL, '123456789', NULL, 'T', NULL);

-- ----------------------------
-- Table structure for personnel
-- ----------------------------
DROP TABLE IF EXISTS `personnel`;
CREATE TABLE `personnel`  (
  `num` int(0) NOT NULL,
  `id` int(0) NOT NULL,
  `code` int(0) NOT NULL,
  `remark` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`num`) USING BTREE,
  INDEX `code`(`code`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  CONSTRAINT `personnel_ibfk_1` FOREIGN KEY (`code`) REFERENCES `personnel_change` (`num`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `personnel_ibfk_2` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of personnel
-- ----------------------------
INSERT INTO `personnel` VALUES (1, 1, 0, '部门1经理');
INSERT INTO `personnel` VALUES (2, 2, 0, '部门1员工');
INSERT INTO `personnel` VALUES (3, 3, 0, '部门1员工');
INSERT INTO `personnel` VALUES (4, 4, 0, '部门1员工');
INSERT INTO `personnel` VALUES (5, 5, 0, '部门1员工');
INSERT INTO `personnel` VALUES (6, 6, 0, '部门1员工');
INSERT INTO `personnel` VALUES (7, 6, 2, '部门2');
INSERT INTO `personnel` VALUES (8, 6, 1, '经理');
INSERT INTO `personnel` VALUES (9, 7, 0, '部门2员工');
INSERT INTO `personnel` VALUES (10, 6, 1, '员工');
INSERT INTO `personnel` VALUES (11, 6, 1, '经理');
INSERT INTO `personnel` VALUES (12, 6, 1, '员工');
INSERT INTO `personnel` VALUES (13, 6, 1, '经理');
INSERT INTO `personnel` VALUES (14, 6, 1, '员工');
INSERT INTO `personnel` VALUES (15, 6, 1, '经理');
INSERT INTO `personnel` VALUES (16, 6, 1, '员工');
INSERT INTO `personnel` VALUES (17, 8, 0, '部门2员工');
INSERT INTO `personnel` VALUES (18, 6, 1, '经理');
INSERT INTO `personnel` VALUES (19, 6, 1, '员工');
INSERT INTO `personnel` VALUES (20, 9, 0, '部门4员工');
INSERT INTO `personnel` VALUES (21, 6, 1, '经理');
INSERT INTO `personnel` VALUES (22, 10, 0, '部门3员工');
INSERT INTO `personnel` VALUES (23, 9, 3, '辞退F');
INSERT INTO `personnel` VALUES (24, 11, 0, '部门3员工');
INSERT INTO `personnel` VALUES (25, 10, 1, '经理');
INSERT INTO `personnel` VALUES (26, 10, 1, '员工');
INSERT INTO `personnel` VALUES (27, 10, 1, '经理');
INSERT INTO `personnel` VALUES (29, 6, 1, '员工');
INSERT INTO `personnel` VALUES (30, 6, 1, '经理');

-- ----------------------------
-- Table structure for personnel_change
-- ----------------------------
DROP TABLE IF EXISTS `personnel_change`;
CREATE TABLE `personnel_change`  (
  `num` int(0) NOT NULL,
  `intro` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`num`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of personnel_change
-- ----------------------------
INSERT INTO `personnel_change` VALUES (0, '新员工加入');
INSERT INTO `personnel_change` VALUES (1, '职务变动');
INSERT INTO `personnel_change` VALUES (2, '部门变动');
INSERT INTO `personnel_change` VALUES (3, '辞退');

SET FOREIGN_KEY_CHECKS = 1;
