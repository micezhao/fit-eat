drop table if exists `merchant`;
CREATE TABLE `merchant`  (
  `id` bigint(16) NOT NULL primary key comment '主键' auto_increment ,
  `merchant_no` varchar(16) not null comment '商户编号',
  `name`  varchar(16) not null comment '商户名称',
  `register_date` varchar(32) not null comment '注册时间',
  `type` varchar(16) not null comment '商户类型',
  `contacter` varchar(16)  comment '联系人',
  `contact_phone` varchar(16)  comment '联系电话',
  `status` varchar(8) not null comment '商户状态',
  `cdt` datetime DEFAULT NULL COMMENT '创建时间',
  `mdt` datetime DEFAULT NULL COMMENT '修改时间'
) ENGINE = InnoDB ;

drop table if exists `goods`;
CREATE TABLE `goods`  (
  `id` bigint(16) NOT NULL primary key comment '主键' auto_increment ,
  `goods_id` varchar(32) not null comment '商品编号',
  `name`  varchar(16) not null comment '商品名称',
  `merchant_no` varchar(16) not null comment '所属商户编号',
  `type` varchar(16) not null comment '商品分类',
  `price` varchar(16) not null  comment '商品价格',
  `status` varchar(8) not null comment '商户状态',
  `cdt` datetime DEFAULT NULL COMMENT '创建时间',
  `mdt` datetime DEFAULT NULL COMMENT '修改时间'
) ENGINE = InnoDB ;

drop table if exists `order`;
CREATE TABLE `order`  (
  `id` bigint(16) NOT NULL primary key comment '主键' auto_increment ,
  `order_id` varchar(32) not null comment '订单编号',
  `customer_id` varchar(32) not null comment '用户编号',
  `total` varchar(16) not null comment '订单总价',
  `discount_price`  varchar(16) default null comment '优惠金额',
  `settlement_price`  varchar(16) not null comment '结算金额',
  `order_time`  datetime DEFAULT NULL COMMENT '下单时间',
  `expire_time` datetime DEFAULT NULL COMMENT'订单过期时间',
  `close_time`  datetime DEFAULT NULL COMMENT '订单关闭时间',
  `finish_time`  datetime DEFAULT NULL COMMENT'订单完成时间',
  `status` varchar(8) not null comment '订单状态',
  `cdt` datetime DEFAULT NULL COMMENT '创建时间',
  `mdt` datetime DEFAULT NULL COMMENT '修改时间'
) ENGINE = InnoDB ;




