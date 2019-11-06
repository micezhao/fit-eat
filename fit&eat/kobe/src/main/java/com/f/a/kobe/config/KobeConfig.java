package com.f.a.kobe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//配置kobe项目的工具bean
@Configuration
public class KobeConfig {
	
	//注入用于rest请求的工具bean
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
