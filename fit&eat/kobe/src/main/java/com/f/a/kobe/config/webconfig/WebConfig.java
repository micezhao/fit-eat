package com.f.a.kobe.config.webconfig;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.f.a.kobe.config.webconfig.interceptor.UserAgentResolver;
import com.f.a.kobe.config.webconfig.interceptor.UserSessionInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
	
	// 自定义的参数解析器
	@Bean
	public List<HandlerMethodArgumentResolver>  initResolvers() {
		List<HandlerMethodArgumentResolver> list = new ArrayList<HandlerMethodArgumentResolver>();
		list.add(new UserAgentResolver());
		return list;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new UserSessionInterceptor()).addPathPatterns("/**").
		excludePathPatterns("/login/getAuthCode","/login/registerByThird","/login/thirdPart/**","/login/registerTest");
	}
	
	/**
	 * 注册一个自定义的对象解析器
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.addAll(initResolvers());
	}
	
	/**
     *扩展消息转换器，防止中文乱码
     */
	@Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 解决controller返回字符串中文乱码问题
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter)converter).setDefaultCharset(StandardCharsets.UTF_8);
            }
            // 配置全局时间格式转换器 
            if(converter instanceof MappingJackson2HttpMessageConverter) {
            	ObjectMapper mapper = new ObjectMapper();
        	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        	    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        	    ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(mapper);
            }
        }
    }
}
