package com.f.a.allan.config.webconfig;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
	
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
	
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


//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new UserSessionInterceptor()).addPathPatterns("/**").
//		excludePathPatterns("/login/getAuthCode","/login/registerByThird","/login/thirdPart/**","/login/registerTest");
//	}
	
	/**
	 * 注册一个自定义的对象解析器
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.addAll(initResolvers());
		logger.info("----->>> 参数转换器注入完成,注入数量:{}",initResolvers().size());
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
            	JavaTimeModule javaTimeModule = new JavaTimeModule();
        	    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        	    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        	    
        	    javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        	    javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        	    javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        	    mapper.registerModule(javaTimeModule);
        	    ((MappingJackson2HttpMessageConverter) converter).setObjectMapper(mapper);
            }
        }
    }
}
