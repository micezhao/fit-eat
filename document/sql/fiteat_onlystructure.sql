/*
Navicat MySQL Data Transfer

Source Server         : mac
Source Server Version : 50722
Source Host           : 118.190.53.214:3386
Source Database       : fiteat

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2019-10-24 22:51:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `addr`
-- ----------------------------
DROP TABLE IF EXISTS `addr`;
CREATE TABLE `addr` (
  `id` bigint(64) NOT NULL,
  `customid` bigint(64) DEFAULT NULL,
  `provinceno` varchar(16) DEFAULT NULL COMMENT '省编码',
  `cityno` varchar(16) DEFAULT NULL COMMENT '市编码',
  `distrcno` varchar(16) DEFAULT NULL COMMENT '区编码',
  `usedefault` char(255) DEFAULT NULL COMMENT '是否选择为默认收货地址',
  `addrdetail` varchar(1024) DEFAULT NULL COMMENT '详细收货地址',
  `connectormobile` varchar(32) DEFAULT NULL COMMENT '收货人手机号码',
  `connectorname` varchar(32) DEFAULT NULL COMMENT '收货人姓名',
  `cdt` datetime DEFAULT NULL,
  `mdt` datetime DEFAULT NULL,
  `ext1` varchar(128) DEFAULT NULL,
  `ext2` varchar(128) DEFAULT NULL,
  `ext3` varchar(128) DEFAULT NULL,
  `ext4` varchar(128) DEFAULT NULL,
  `ext5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of addr
-- ----------------------------

-- ----------------------------
-- Table structure for `auth`
-- ----------------------------
DROP TABLE IF EXISTS `auth`;
CREATE TABLE `auth` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `customid` varchar(32) NOT NULL,
  `mobile` varchar(32) DEFAULT NULL COMMENT '绑定手机号',
  `wxopenid` varchar(64) DEFAULT NULL COMMENT '微信openid',
  `aliopenid` varchar(64) DEFAULT NULL COMMENT '支付宝生活号openid',
  `status` char(255) DEFAULT NULL COMMENT '状态 1可用 0不可用',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `email` varchar(64) DEFAULT NULL COMMENT '电子邮箱',
  `cdt` datetime DEFAULT NULL,
  `mdt` datetime DEFAULT NULL,
  `ext1` varchar(128) DEFAULT NULL,
  `ext2` varchar(128) DEFAULT NULL,
  `ext3` varchar(128) DEFAULT NULL,
  `ext4` varchar(128) DEFAULT NULL,
  `ext5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of auth
-- ----------------------------

-- ----------------------------
-- Table structure for `china`
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
-- Records of china
-- ----------------------------

-- ----------------------------
-- Table structure for `custom`
-- ----------------------------
DROP TABLE IF EXISTS `custom`;
CREATE TABLE `custom` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT,
  `customid` bigint(64) DEFAULT NULL,
  `realname` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `birthday` varchar(32) DEFAULT NULL COMMENT '出生日期',
  `age` int(8) DEFAULT NULL COMMENT '年龄',
  `height` varchar(16) DEFAULT NULL COMMENT '身高',
  `weight` varchar(16) DEFAULT NULL COMMENT '体重',
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `headimg` varchar(512) DEFAULT NULL COMMENT '头像',
  `sorce` int(16) DEFAULT NULL COMMENT '积分',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号码',
  `cdt` datetime DEFAULT NULL,
  `mdt` datetime DEFAULT NULL,
  `ext1` varchar(128) DEFAULT NULL,
  `ext2` varchar(128) DEFAULT NULL,
  `ext3` varchar(128) DEFAULT NULL,
  `ext4` varchar(128) DEFAULT NULL,
  `ext5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of custom
-- ----------------------------

-- ----------------------------
-- Table structure for `customlog`
-- ----------------------------
DROP TABLE IF EXISTS `customlog`;
CREATE TABLE `customlog` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT,
  `customid` bigint(64) DEFAULT NULL,
  `logchannel` int(4) DEFAULT NULL COMMENT '操作渠道 1 微信 2 支付宝 3app 4网页',
  `optype` int(4) DEFAULT NULL COMMENT '操作类型 1 登陆 2 登出',
  `ocurtime` datetime DEFAULT NULL COMMENT '发生时间',
  `authtype` int(4) DEFAULT NULL COMMENT '凭证类型',
  `token` varchar(128) DEFAULT NULL COMMENT '访问令牌',
  `tokenexpiretime` datetime DEFAULT NULL COMMENT '访问令牌过期时间',
  `cdt` datetime DEFAULT NULL,
  `mdt` datetime DEFAULT NULL,
  `ext1` varchar(128) DEFAULT NULL,
  `ext2` varchar(128) DEFAULT NULL,
  `ext3` varchar(128) DEFAULT NULL,
  `ext4` varchar(128) DEFAULT NULL,
  `ext5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of customlog
-- ----------------------------

-- ----------------------------
-- Table structure for `score`
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
  `id` bigint(64) NOT NULL,
  `customid` bigint(64) NOT NULL,
  `type` int(4) DEFAULT NULL COMMENT '积分变更类型 1 增加 2扣减',
  `count` int(16) DEFAULT NULL COMMENT '积分变化数量',
  `score` int(16) DEFAULT NULL COMMENT '当前积分',
  `cdt` datetime DEFAULT NULL,
  `mdt` datetime DEFAULT NULL,
  `ext1` varchar(128) DEFAULT NULL,
  `ext2` varchar(128) DEFAULT NULL,
  `ext3` varchar(128) DEFAULT NULL,
  `ext4` varchar(128) DEFAULT NULL,
  `ext5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of score
-- ----------------------------
