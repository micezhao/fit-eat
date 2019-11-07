package com.f.a.kobe.service.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ParamCheckAop {

	@Before(value = "@annotation(ParamCheck)")
	public void dofore(JoinPoint joinPoint) throws ClassNotFoundException {
		String methodName = joinPoint.getSignature().getName();
		StringBuffer bizName = new StringBuffer(methodName.substring(0, 1).toUpperCase()).append(methodName.substring(1));
		String name = this.getClass().getName();
		String substring = name.substring(0, name.lastIndexOf("."));
		StringBuffer bizNameWithPathBuf = new StringBuffer(substring).append(".").append(bizName.toString()).append("ParamCheckor");
		String bizNameWithPath = bizNameWithPathBuf.toString();
		try {
			ParamCheckUtil p = new ParamCheckUtil((ParamCheckor)Class.forName(bizNameWithPath).newInstance());
			p.check(joinPoint.getArgs()[0]);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			//未找到指定的检查类
			e.printStackTrace();
		} 
	}
}
