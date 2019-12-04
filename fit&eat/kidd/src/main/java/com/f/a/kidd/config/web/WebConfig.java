package com.f.a.kidd.config.web;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
	
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
