package com.fa.kobe.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;


//指定时间插入字段
@Slf4j
@Component
public class CredentialMetaObjectHandler  implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
//        log.info("插入时间插入");
        this.setFieldValByName("createTime",new Date(),metaObject);
        this.setFieldValByName("updateTime",new Date(),metaObject);
        this.setFieldValByName("gmtCreate",new Date(),metaObject);
        this.setFieldValByName("gmtModified",new Date(),metaObject);
        log.info("");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        log.info("更新时间插入");
        this.setFieldValByName("updateTime",new Date(),metaObject);
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }
}
