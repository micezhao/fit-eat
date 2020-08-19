package com.f.a.kobe.config.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSONObject;
import com.f.a.kobe.contants.Contants;
import com.f.a.kobe.view.UserAgent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserSessionInterceptor implements HandlerInterceptor {	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Object object = request.getSession().getAttribute(Contants.USER_AGENT);
		UserAgent userAgent = JSONObject.parseObject(JSONObject.toJSONString(object), UserAgent.class);
		if (userAgent == null) {
			throw new RuntimeException("用户信息获取失败,请重新登录");
		}
		log.info("请求拦截器执行...解析用户对象完成:{}",JSONObject.toJSONString(userAgent));
		String name = ((HandlerMethod)handler).getMethod().getName();
		if(!StringUtils.equalsAny(name, "mobileBind","logout","smscodeAttain","smscodeVaildate")) {
			if(!userAgent.isHasbinded()) {
				throw new RuntimeException("请先绑定手机号");
			}
		}
		return true;
	}
}
