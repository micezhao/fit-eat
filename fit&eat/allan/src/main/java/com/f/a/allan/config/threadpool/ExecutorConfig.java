package com.f.a.allan.config.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecutorConfig {
	
	private static final int CORE_POOL_SIZE = 50;
	
	private static final int MAX_POOL_SIZE = 10000;
	
	private static final int QUEUE_CAPACITY = 200;
	
	private static final int KEEP_ALIVE_SECONDS = 30;
	
	/**
	 * 线程池配置类
	 * @return
	 */
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		//最少线程
		threadPoolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
		//最大线程
		threadPoolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
		//缓冲队列的最大容量200
		threadPoolTaskExecutor.setQueueCapacity(QUEUE_CAPACITY);
		//线程空闲时间
		threadPoolTaskExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
		//超过空闲时间，就关闭线程
		threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		
		return threadPoolTaskExecutor;
	}
	
}
