package com.fa.kobe.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
public class UserRedisBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.contains("redisTemplate")){
            RedisTemplate redisTemplate = (RedisTemplate) bean;
//            redisTemplate.setDefaultSerializer(new FastJsonRedisSerializer<>(String.class));
//
//            redisTemplate.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
//            redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//            redisTemplate.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));

            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(String.class));
            return redisTemplate;
        }
        return null;
    }
}
