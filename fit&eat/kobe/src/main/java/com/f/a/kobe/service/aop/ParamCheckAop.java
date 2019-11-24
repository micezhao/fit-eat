package com.f.a.kobe.service.aop;

import java.util.Locale;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ParamCheckAop {
	
	// 注入选择器接口实现的集合
	@Autowired
	protected Map<String,ParamCheckHandler> checkHandlerMap;
	
	private static final String SUFFIX = "ParamCheckor";
	
	// 选择参数校验器
	private  ParamCheckHandler getCheckHandler(String subject) {
		ParamCheckHandler handler= checkHandlerMap.get(subject);
		if(handler == null) {
			throw new RuntimeException("can not find paramCheckHandler for this subject named :"+subject);
		}
		return handler;
		
	}
	
	@Before(value = "@annotation(ParamCheck)")
	public void dofore(JoinPoint joinPoint) throws ClassNotFoundException {
	
		String clazzName = joinPoint.getSignature().getDeclaringType().getSimpleName();
		getCheckHandler(extractHandlerName(clazzName)).commonCheck(joinPoint.getArgs()[0]); 
	}
	
	// 抽取校验器的beanName
	public String extractHandlerName(String clazzName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(clazzName.substring(0,1).toLowerCase(Locale.ENGLISH))
		.append(clazzName.substring(1,clazzName.length()));
		return buffer.toString().replace("Service", SUFFIX);
	}
	
}
