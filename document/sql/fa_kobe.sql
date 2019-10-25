/*
Navicat MySQL Data Transfer

Source Server         : mac
Source Server Version : 50722
Source Host           : 118.190.53.214:3386
Source Database       : fa_kobe

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-10-25 10:52:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for china
-- ----------------------------
DROP TABLE IF EXISTS `china`;
CREATE TABLE `china` (
  `Id` int(11) NOT NULL,
  `Name` varchar(40) DEFAULT NULL,
  `Pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_CHINA_REFERENCE_CHINA` (`Pid`),
  CONSTRAINT `FK_CHINA_REFERENCE_CHINA` FOREIGN KEY (`Pid`) REFERENCES `china` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_addr
-- ----------------------------
DROP TABLE IF EXISTS `customer_addr`;
CREATE TABLE `customer_addr` (
  `id` bigint(64) NOT NULL,
  `customer_id` bigint(64) DEFAULT NULL,
  `province_no` varchar(16) DEFAULT NULL COMMENT '省编码',
  `city_no` varchar(16) DEFAULT NULL COMMENT '市编码',
  `distrc_no` varchar(16) DEFAULT NULL COMMENT '区编码',
  `use_default` char(2) DEFAULT NULL COMMENT '是否选择为默认收货地址',
  `addr_detail` varchar(1024) DEFAULT NULL COMMENT '详细收货地址',
  `connector_mobile` varchar(32) DEFAULT NULL COMMENT '收货人手机号码',
  `connector_name` varchar(32) DEFAULT NULL COMMENT '收货人姓名',
  `ext1` varchar(128) DEFAULT NULL,
  `ext2` varchar(128) DEFAULT NULL,
  `ext3` varchar(128) DEFAULT NULL,
  `ext4` varchar(128) DEFAULT NULL,
  `ext5` varchar(128) DEFAULT NULL,
  `cdt` datetime DEFAULT NULL,
  `mdt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_baseinfo
-- ----------------------------
DROP TABLE IF EXISTS `customer_baseinfo`;
CREATE TABLE `customer_baseinfo` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(64) DEFAULT NULL COMMENT '用户id',
  `realname` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `birthday` varchar(32) DEFAULT NULL COMMENT '出生日期',
  `age` int(8) DEFAULT NULL COMMENT '年龄',
  `height` varchar(16) DEFAULT NULL COMMENT '身高',
  `weight` varchar(16) DEFAULT NULL COMMENT '体重',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `headimg` varchar(512) DEFAULT NULL COMMENT '头像',
  `sorce` int(16) DEFAULT NULL COMMENT '积分',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号码',
  `ext1` varchar(128) DEFAULT NULL,
  `ext2` varchar(128) DEFAULT NULL,
  `ext3` varchar(128) DEFAULT NULL,
  `ext4` varchar(128) DEFAULT NULL,
  `ext5` varchar(128) DEFAULT NULL,
  `dr` int(2) DEFAULT NULL COMMENT '用户是否可用 0 不可用 1可用',
  `cdt` datetime DEFAULT NULL,
  `mdt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_credential
-- ----------------------------
DROP TABLE IF EXISTS `customer_credential`;
CREATE TABLE `customer_credential` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customer_id` varchar(32) NOT NULL,
  `mobile` varchar(32) DEFAULT NULL COMMENT '绑定手机号',
  `wx_openid` varchar(64) DEFAULT NULL COMMENT '微信openid',
  `ali_openid` varchar(64) DEFAULT NULL COMMENT '支付宝生活号openid',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `email` varchar(64) DEFAULT NULL COMMENT '电子邮箱',
  `ext1` varchar(128) DEFAULT NULL,
  `ext2` varchar(128) DEFAULT NULL,
  `ext3` varchar(128) DEFAULT NULL,
  `ext4` varchar(128) DEFAULT NULL,
  `ext5` varchar(128) DEFAULT NULL,
  `dr` int(2) DEFAULT NULL COMMENT '用户是否可用 0 不可用 1可用',
  `cdt` datetime DEFAULT NULL,
  `mdt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_log
-- ----------------------------
DROP TABLE IF EXISTS `customer_log`;
CREATE TABLE `customer_log` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT,
  `customer_id` bigint(64) DEFAULT NULL COMMENT '用户id',
  `log_channel` int(4) DEFAULT NULL COMMENT '通过哪一个渠道进行的操作（登录、登出） 1 微信 2 支付宝 3app 4网页',
  `op_type` int(4) DEFAULT NULL COMMENT '完成的具体操作 1 登陆 2 登出',
  `ocurr_time` datetime DEFAULT NULL COMMENT '发生操作的时间',
  `auth_type` int(4) DEFAULT NULL COMMENT '完成操作的凭证类型 1. 微信 2 支付宝 3用户名密码',
  `token` varchar(512) DEFAULT NULL COMMENT '访问令牌',
  `token_expire_time` datetime DEFAULT NULL COMMENT '访问令牌过期时间',
  `ext1` varchar(128) DEFAULT NULL,
  `ext2` varchar(128) DEFAULT NULL,
  `ext3` varchar(128) DEFAULT NULL,
  `ext4` varchar(128) DEFAULT NULL,
  `ext5` varchar(128) DEFAULT NULL,
  `cdt` datetime DEFAULT NULL,
  `mdt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for customer_score_trans
-- ----------------------------
DROP TABLE IF EXISTS `customer_score_trans`;
CREATE TABLE `customer_score_trans` (
  `id` bigint(64) NOT NULL,
  `customer_id` bigint(64) NOT NULL COMMENT '用户id',
  `trans_type` varchar(8) DEFAULT NULL COMMENT '积分变更类型 add 增加 sub 扣减',
  `trans_count` int(16) DEFAULT NULL COMMENT '积分变化数量',
  `custom_score` int(16) DEFAULT NULL COMMENT '用户当前积分',
  `ext1` varchar(128) DEFAULT NULL,
  `ext2` varchar(128) DEFAULT NULL,
  `ext3` varchar(128) DEFAULT NULL,
  `ext4` varchar(128) DEFAULT NULL,
  `ext5` varchar(128) DEFAULT NULL,
  `cdt` datetime DEFAULT NULL,
  `mdt` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
