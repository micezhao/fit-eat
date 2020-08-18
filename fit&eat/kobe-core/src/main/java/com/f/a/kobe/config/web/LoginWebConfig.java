package com.f.a.kobe.config.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginWebConfig implements WebMvcConfigurer  {

	private static final Logger logger = LoggerFactory.getLogger(LoginWebConfig.class);

	
	@Autowired
	private Map<String,HandlerMethodArgumentResolver> resolverMap = new HashMap<String,HandlerMethodArgumentResolver>();
	
	@Bean
	protected List<HandlerMethodArgumentResolver> initResolvers(){
		List<HandlerMethodArgumentResolver> list = new ArrayList<HandlerMethodArgumentResolver>();
		for (Entry<String,HandlerMethodArgumentResolver> entry : this.resolverMap.entrySet()) {
			list.add(entry.getValue());
			logger.info("----->>> 读取并加载请求参数解析器:{}",entry.getKey());
		}
		return list;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserSessionInterceptor()).addPathPatterns("/**")
			.excludePathPatterns("/login/thirdPart/**","/login/registerTest","/admin/**")
			.excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
	}
	
	/**
	 * 注册一个自定义的对象解析器
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.addAll(initResolvers());
		logger.info("----->>> 参数转换器注入完成,注入数量:{}",initResolvers().size());
	}
}
