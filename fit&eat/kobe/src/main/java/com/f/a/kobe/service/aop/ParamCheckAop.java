package com.f.a.kobe.service.aop;

import java.util.Locale;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.f.a.kobe.pojo.request.ParamRequest;

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
	
	@Around(value = "@annotation(ParamCheck)")
	@SuppressWarnings("unchecked")
	public ResponseEntity<Object> dofore(ProceedingJoinPoint joinPoint) throws Throwable{
		ParamCheck paramCheck = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ParamCheck.class);
		String value = paramCheck.value();
		String clazzName = joinPoint.getSignature().getDeclaringType().getSimpleName();
		String beanName = extractHandlerName(clazzName);
		ParamCheckHandler checkHandler = getCheckHandler(beanName+SUFFIX);
		Map<String, String> commonCheck = checkHandler.commonCheck(joinPoint.getArgs()[0],value);
		if(commonCheck != null) {
			return new ResponseEntity<Object>(commonCheck, HttpStatus.OK); 
		}
		ResponseEntity<Object> proceed = (ResponseEntity<Object>)joinPoint.proceed();
		return proceed;
	}
	
	// 抽取校验器的beanName
	public String extractHandlerName(String clazzName) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(clazzName.substring(0,1).toLowerCase(Locale.ENGLISH))
		.append(clazzName.substring(1,clazzName.length()));
		return buffer.toString().replace("Service", SUFFIX);
	}
	
}
