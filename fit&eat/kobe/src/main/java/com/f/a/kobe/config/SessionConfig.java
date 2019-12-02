package com.f.a.kobe.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;

@Configuration
//@EnableRedisHttpSession
public class SessionConfig {
	
	private static final Logger log = LoggerFactory.getLogger(SessionConfig.class);
	
    @Bean("springSessionDefaultRedisSerializer")
    public RedisSerializer<Object> defaultRedisSerializer(){
        log.debug("自定义Redis Session序列化加载成功");
        return valueSerializer();
    }
 
    private RedisSerializer<Object> valueSerializer() {
        return new FastJsonRedisSerializer<>(Object.class);
    }
    
    @Bean
	public HttpSessionIdResolver httpSessionIdResolver() {
		return HeaderHttpSessionIdResolver.xAuthToken(); 
	}
    
}
