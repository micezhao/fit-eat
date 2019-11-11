package com.f.a.kobe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.f.a.kobe.util.IdWorker;

@Configuration
public class IdWorkConfig {

	@Bean
	public IdWorker idworker() {
		return new IdWorker();
	}

}
