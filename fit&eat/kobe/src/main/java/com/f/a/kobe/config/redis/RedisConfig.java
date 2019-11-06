package com.f.a.kobe.config.redis;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	/**
	 * 设置 redis 数据默认过期时间 设置@cacheable 序列化方式
	 * 
	 * @return
	 */
	@Bean
	public RedisCacheConfiguration redisCacheConfiguration() {
		FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
		configuration = configuration
				.serializeValuesWith(
						RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer))
				.entryTtl(Duration.ofDays(30));
		return configuration;
	}

}
