package com.f.a.allan.config.mybatis;

import java.time.LocalDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单表，字段自动填充的配置
 * @author micezhao
 *
 */
@Slf4j
@Component
public class OrderMetaObjectHandler  implements MetaObjectHandler {
	
	@Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "cdt", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
//        this.fillStrategy(metaObject, "cdt", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug请升级到之后的版本如`3.3.1.8-SNAPSHOT`)
        this.strictInsertFill(metaObject, "orderTime", LocalDateTime.class, LocalDateTime.now()); 
	}

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "mdt", LocalDateTime.class, LocalDateTime.now()); // 起始版本 3.3.0(推荐使用)
//        this.fillStrategy(metaObject, "updateTime", LocalDateTime.now()); // 也可以使用(3.3.0 该方法有bug请升级到之后的版本如`3.3.1.8-SNAPSHOT`)
    }
	
}
