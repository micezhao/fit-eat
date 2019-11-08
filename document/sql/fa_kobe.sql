/*
 Navicat Premium Data Transfer

 Source Server         : mac
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : 118.190.53.214:3386
 Source Schema         : fa_kobe

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 08/11/2019 09:36:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for china
-- ----------------------------
DROP TABLE IF EXISTS `china`;
CREATE TABLE `china`  (
  `Id` int(11) NOT NULL,
  `Name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `Pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `FK_CHINA_REFERENCE_CHINA`(`Pid`) USING BTREE,
  CONSTRAINT `FK_CHINA_REFERENCE_CHINA` FOREIGN KEY (`Pid`) REFERENCES `china` (`Id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customer_addr
-- ----------------------------
DROP TABLE IF EXISTS `customer_addr`;
CREATE TABLE `customer_addr`  (
  `id` bigint(64) UNSIGNED NOT NULL AUTO_INCREMENT,
  `addr_id` bigint(64) UNSIGNED NOT NULL COMMENT '地址编码',
  `customer_id` bigint(64) DEFAULT NULL COMMENT '用户id',
  `province_no` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '省编码',
  `province_name` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '省名',
  `city_no` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '市编码',
  `city_name` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '市名',
  `distrc_no` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '区编码',
  `distrc_name` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '区名',
  `addr_detail` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '详细收货地址',
  `connector_mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人手机号码',
  `connector_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '收货人姓名',
  `use_default` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '是否选择为默认收货地址 1 表示默认收获地址',
  `ext1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext3` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext4` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext5` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `cdt` datetime(0) DEFAULT NULL,
  `mdt` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customer_baseinfo
-- ----------------------------
DROP TABLE IF EXISTS `customer_baseinfo`;
CREATE TABLE `customer_baseinfo`  (
  `id` bigint(64) UNSIGNED NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(64) DEFAULT NULL COMMENT '用户id',
  `realname` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '真实姓名',
  `birthday` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '出生日期',
  `age` int(8) DEFAULT NULL COMMENT '年龄',
  `nickname` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
  `headimg` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `sorce` int(16) DEFAULT NULL COMMENT '积分',
  `mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号码',
  `ext1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext3` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext4` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext5` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `dr` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户是否可用 0 不可用 1可用',
  `cdt` datetime(0) DEFAULT NULL,
  `mdt` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customer_body_info
-- ----------------------------
DROP TABLE IF EXISTS `customer_body_info`;
CREATE TABLE `customer_body_info`  (
  `id` bigint(64) UNSIGNED NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(64) UNSIGNED DEFAULT NULL COMMENT '用户id',
  `record_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '记录编号',
  `register_date` datetime(0) DEFAULT NULL COMMENT '登记时间',
  `height` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '身高 cm',
  `weight` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '体重 kg',
  `chest` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '胸围 cm',
  `waistline` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '腰围 cm',
  `hipline` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '臀围 cm',
  `waist_hip_ratio` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '腰臀比',
  `left_arm_circumference` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '左大臂围  cm',
  `right_arm_circumference` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '右大臂围  cm',
  `left_thigh_circumference` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '左大腿围  cm',
  `right_thigh_circumference` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '右大腿围 cm',
  `fat percentage` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '体脂率 %',
  `heart_rate` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '心率  次/分',
  `sdp` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '血压(收缩压)  mm/Hg',
  `dbp` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `fat_content` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '脂肪含量 kg',
  `muscle_content` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '骨骼肌含量 kg',
  `ext1` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ext2` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ext3` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ext4` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `ext5` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `bmi` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '体质指数 身高/体重的（二次幂）',
  `cdt` date DEFAULT NULL COMMENT '创建时间',
  `mdt` date DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customer_credential
-- ----------------------------
DROP TABLE IF EXISTS `customer_credential`;
CREATE TABLE `customer_credential`  (
  `id` bigint(64) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customer_id` bigint(64) NOT NULL,
  `mobile` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '绑定手机号',
  `wx_openid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微信openid',
  `ali_openid` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '支付宝生活号openid',
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电子邮箱',
  `ext1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext3` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext4` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext5` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `dr` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户是否可用 0 不可用 1可用',
  `cdt` datetime(0) DEFAULT NULL,
  `mdt` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 101 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customer_log
-- ----------------------------
DROP TABLE IF EXISTS `customer_log`;
CREATE TABLE `customer_log`  (
  `id` bigint(64) UNSIGNED NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(64) DEFAULT NULL COMMENT '用户id',
  `log_channel` int(4) DEFAULT NULL COMMENT '通过哪一个渠道进行的操作（登录、登出） 1 微信 2 支付宝 3app 4网页',
  `op_type` int(4) DEFAULT NULL COMMENT '完成的具体操作 1 登陆 2 登出',
  `ocurr_time` datetime(0) DEFAULT NULL COMMENT '发生操作的时间',
  `auth_type` int(4) DEFAULT NULL COMMENT '完成操作的凭证类型 1. 微信 2 支付宝 3用户名密码',
  `token` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '访问令牌',
  `token_expire_time` datetime(0) DEFAULT NULL COMMENT '访问令牌过期时间',
  `ext1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext3` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext4` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext5` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `cdt` datetime(0) DEFAULT NULL,
  `mdt` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for customer_score_trans
-- ----------------------------
DROP TABLE IF EXISTS `customer_score_trans`;
CREATE TABLE `customer_score_trans`  (
  `id` bigint(64) UNSIGNED NOT NULL AUTO_INCREMENT,
  `trans_no` bigint(64) UNSIGNED NOT NULL COMMENT '积分变化流水号',
  `customer_id` bigint(64) NOT NULL COMMENT '用户id',
  `trans_type` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '积分变更类型 add 增加 sub 扣减',
  `trans_count` int(16) DEFAULT NULL COMMENT '积分变化数量',
  `customer_score` int(16) DEFAULT NULL COMMENT '用户当前积分',
  `ext1` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext2` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext3` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext4` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `ext5` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `cdt` datetime(0) DEFAULT NULL,
  `mdt` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
