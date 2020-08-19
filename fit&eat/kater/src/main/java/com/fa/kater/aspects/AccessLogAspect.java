package com.fa.kater.aspects;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.f.a.kobe.view.UserAgent;
import com.fa.kater.annotations.AccessLogAnnot;
import com.fa.kater.annotations.AccessLogAnnot.AccessLogType;
import com.fa.kater.pojo.AccessLog;
import com.fa.kater.service.impl.AccessLogServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class AccessLogAspect {

	private final static String LOG_IN = "login";

	private final static String LOG_OUT = "logout";

	private final static String EXPIRED = "expired";

	@Autowired
	private AccessLogServiceImpl accessLogService;

	@Pointcut("@annotation(com.fa.kater.annotations.AccessLogAnnot)")
	public void accessLog() {
	}

	/**
	 * 后置通知处理业务逻辑
	 * 
	 * @param joinPoint
	 * @param userAgent 绑定返回值类型
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	@AfterReturning(value = "accessLog()", returning = "userAgent")
	public void doAfter(JoinPoint joinPoint, UserAgent userAgent) throws NoSuchMethodException {
		try {
			Class<?> clazz = joinPoint.getTarget().getClass();
			Signature signature = joinPoint.getSignature();
			String methodName = signature.getName();
			Method [] methdos = clazz.getMethods();
			Method method = null;
			for (Method md: methdos) {
				if(md.getName().equals(methodName)) {
					method = md;
					break;
				}
			}
			AccessLogAnnot annotation = method.getAnnotation(AccessLogAnnot.class);
			if (annotation.logType() == AccessLogType.LOG_IN) {
				insertAccessLog(userAgent, LOG_IN);
			} else if ((annotation.logType() == AccessLogType.LOG_OUT)) {
				insertAccessLog(userAgent, LOG_OUT);
			} else if (annotation.logType() == AccessLogType.EXPIRE) {
				insertAccessLog(userAgent, EXPIRED);
			}
		} catch (Exception e) {
			log.warn("记录访问事件的切面方法执行出现异常:{}",e.getMessage());
		}
	}

	public void insertAccessLog(UserAgent userAgent, String accessType) {
		AccessLog accessLog = new AccessLog();
		accessLog.setUserAccount(userAgent.getUserAccount());
		accessLog.setLoginType(userAgent.getLoginType());
		accessLog.setAgentId(userAgent.getAgentId());
		accessLog.setAuthType(userAgent.getAuthType());
		accessLog.setEvent(accessType);
		accessLogService.save(accessLog); // 记录日志
		log.info("访问事件:{},日志插入成功", accessType);
	}
}
