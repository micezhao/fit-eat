package com.f.a.allan.config.redis;

import java.time.Duration;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;

@Configuration
@EnableCaching
@EnableAutoConfiguration // 配置redis的多数据源
public class RedisConfig extends CachingConfigurerSupport {

	private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
	
	@Autowired
	RedisProperties redisProperties;

	@Value("${redis.allan.database}")
	private int allanDatabase;
	
	@Value("${redis.lettuce.pool.max-active}")
	private int maxActive ;
	
	@Value("${redis.lettuce.pool.max-wait}")
	private Long maxWait ;
	
	@Value("${redis.lettuce.pool.max-idle}")
	private int maxIdle ;
	
	@Value("${redis.lettuce.pool.min-idle}")
	private int minIdle ;
	
	@Value("${redis.lettuce.timeout}")
	private Long timeOut ;
	
	@Value("${redis.lettuce.shutdown-timeout}")
	private Long shutdown ;

	@Bean(name = "regionRedisTemplate")
	public RedisTemplate<String, Object> regionRedisTemplate(RedisConnectionFactory connectionFactory) {
		logger.debug("加载自定义redisTemplate:{}", "regionRedisTemplate");
		RedisTemplate<String, Object> regionRedisTemplate = new RedisTemplate<>();
		LettuceConnectionFactory lettuceConnectionFactory = createLettuceConnectionFactory(allanDatabase,
				redisProperties.getHost(), redisProperties.getPort(),redisProperties.getPassword() , maxIdle, minIdle, maxActive, maxWait, timeOut, shutdown);
		regionRedisTemplate.setConnectionFactory(lettuceConnectionFactory);
		regionRedisTemplate.setKeySerializer(new StringRedisSerializer()); //key的序列化策略
		regionRedisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Object.class)); //value 的序列化策略
		
		// 设置hash key & value的序列化策略
		regionRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
		regionRedisTemplate.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
		
		//regionRedisTemplate.setDefaultSerializer(new FastJsonRedisSerializer<>(Object.class));
		regionRedisTemplate.afterPropertiesSet();
		return regionRedisTemplate;
	}

	private LettuceConnectionFactory createLettuceConnectionFactory(int dbIndex, String hostName, int port,
			String password, int maxIdle, int minIdle, int maxActive, Long maxWait, Long timeOut,
			Long shutdownTimeOut) {

		// redis配置
		RedisConfiguration redisConfiguration = new RedisStandaloneConfiguration(hostName, port);
		((RedisStandaloneConfiguration) redisConfiguration).setDatabase(dbIndex);
		((RedisStandaloneConfiguration) redisConfiguration).setPassword(password);

		// redis客户端配置
		LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration
				.builder().commandTimeout(Duration.ofMillis(timeOut));
		//连接池配置
        GenericObjectPoolConfig<Object> genericObjectPoolConfig =
 												 new GenericObjectPoolConfig<Object>();
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMaxWaitMillis(maxWait);

		builder.shutdownTimeout(Duration.ofMillis(shutdownTimeOut));
        builder.poolConfig(genericObjectPoolConfig);
		LettuceClientConfiguration lettuceClientConfiguration = builder.build();

		// 根据配置和客户端配置创建连接
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration,
				lettuceClientConfiguration);
		lettuceConnectionFactory.afterPropertiesSet();

		return lettuceConnectionFactory;
	}

}
