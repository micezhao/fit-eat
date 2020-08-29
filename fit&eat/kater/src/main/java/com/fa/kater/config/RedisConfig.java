package com.fa.kater.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;

@Configuration
@EnableCaching
//@EnableAutoConfiguration // 配置redis的多数据源
public class RedisConfig extends CachingConfigurerSupport {

	private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
	
	@Bean(name = "redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		logger.debug("重写redisTemplate的序列化策略");
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer()); //key的序列化策略
		redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Object.class)); //value 的序列化策略
		
		// 设置hash key & value的序列化策略
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
		
		//regionRedisTemplate.setDefaultSerializer(new FastJsonRedisSerializer<>(Object.class));
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
	

}
