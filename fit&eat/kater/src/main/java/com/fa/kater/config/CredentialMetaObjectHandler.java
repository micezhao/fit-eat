package com.fa.kater.config;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fa.kater.pojo.AccessLog;

import lombok.extern.slf4j.Slf4j;


//指定时间插入字段
@Slf4j
@Component
public class CredentialMetaObjectHandler  implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("插入时间插入");
        if(metaObject.getOriginalObject() instanceof AccessLog){
            this.setFieldValByName("createTime",new Date(),metaObject);
        }
        this.setFieldValByName("gmtCreate",new Date(),metaObject);
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        log.info("更新时间插入");
       // this.setFieldValByName("updateTime",new Date(),metaObject);
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }
}
