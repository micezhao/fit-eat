package com.fa.kobe.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

//@Component
public class UserRedisBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.contains("redisTemplate")){
            RedisTemplate bean1 = (RedisTemplate) bean;
            bean1.setDefaultSerializer(new FastJsonRedisSerializer<>(Object.class));
            return bean1;
        }
        return null;
    }
}
