-- 查看当前数据库的事件
show events;

-- 开启事件调度器 on/off 或 （1/0）
SET GLOBAL event_scheduler = on;

-- 查看事件调度器的状态
SELECT @@event_scheduler;

-- 删除存储过程
DROP PROCEDURE IF EXISTS order_create_by_day;

-- 添加; 的转义
DELIMITER ;;
create procedure order_create_by_day()
begin
DECLARE tblname VARCHAR(32);
set tblname =  CONCAT('order_', date_format(now(),'%Y%m%d%'));
set @sql_t = concat("create table ",tblname,'(
  `id` bigint(16) NOT NULL AUTO_INCREMENT COMMENT \'主键\',
  `order_id` varchar(32) NOT NULL COMMENT \'订单编号\',
  `customer_id` varchar(32) NOT NULL COMMENT \'用户编号\',
  `total` varchar(16) NOT NULL COMMENT \'订单总价\',
  `discount_price` varchar(16) DEFAULT NULL COMMENT \'优惠金额\',
  `settlement_price` varchar(16) NOT NULL COMMENT \'结算金额\',
  `order_time` datetime DEFAULT NULL COMMENT \'下单时间\',
  `expire_time` datetime DEFAULT NULL COMMENT \'订单过期时间\',
  `close_time` datetime DEFAULT NULL COMMENT \'订单关闭时间\',
  `finish_time` datetime DEFAULT NULL COMMENT \'订单完成时间\',
  `status` varchar(8) NOT NULL COMMENT \'订单状态\',
  `cdt` datetime DEFAULT NULL COMMENT \'创建时间\',
  `mdt` datetime DEFAULT NULL COMMENT \'修改时间\',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4');
prepare sql_t from @sql_t;
execute sql_t;
end;

DROP EVENT IF EXISTS table_auto_create_by_day;

-- 定义执行计划
CREATE EVENT table_auto_create_by_day
ON SCHEDULE every 1 day
starts timestamp(current_date,'00:00:00')
on completion PRESERVE
DO  call order_create_by_day();
