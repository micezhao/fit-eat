package com.f.a.kobe.config.redis;

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
//@Import(RedisProperties.class)
public class RedisConfig extends CachingConfigurerSupport {

	private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
	
	@Autowired
	RedisProperties redisProperties;

	@Value("${redis.region.database}")
	private int regionDatabase = 5;
	
	@Value("${redis.lettuce.pool.max-active}")
	private int maxActive = 8;
	
	@Value("${redis.lettuce.pool.max-wait}")
	private Long maxWait = 10000L;
	
	@Value("${redis.lettuce.pool.max-idle}")
	private int maxIdle = 4;
	
	@Value("${redis.lettuce.pool.min-idle}")
	private int minIdle = 0;
	
	@Value("${redis.lettuce.timeout}")
	private Long timeOut = 1000000L;
	
	@Value("${redis.lettuce.shutdown-timeout}")
	private Long shutdown = 400L ;

	@Bean(name = "regionRedisTemplate")
	public RedisTemplate<String, Object> regionRedisTemplate(RedisConnectionFactory connectionFactory) {
		logger.debug("加载自定义redisTemplate:{}", "regionRedisTemplate");
		RedisTemplate<String, Object> regionRedisTemplate = new RedisTemplate<>();
		LettuceConnectionFactory lettuceConnectionFactory = createLettuceConnectionFactory(regionDatabase,
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
