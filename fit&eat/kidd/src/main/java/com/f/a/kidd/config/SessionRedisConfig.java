package com.f.a.kidd.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
/**
 * 采用redis-session的方式获取会话信息
 * @author micezhao
 *
 */
@Configuration
public class SessionRedisConfig {
	
	private static final Logger log = LoggerFactory.getLogger(SessionRedisConfig.class);

	@Bean("springSessionDefaultRedisSerializer")
	public RedisSerializer<Object> defaultRedisSerializer() {
		log.debug("自定义Redis Session序列化加载成功");
		return valueSerializer();
	}

	private RedisSerializer<Object> valueSerializer() {
		return new FastJsonRedisSerializer<>(Object.class);
	}
	// 在网关系统中，依旧是通过request.header.X-AUTH-TOKEN 字段中获取sessionId，并通过RedisSessionRepository获取到session中的内容
	@Bean
	public HttpSessionIdResolver httpSessionIdResolver() {
		return HeaderHttpSessionIdResolver.xAuthToken();
	}
}
