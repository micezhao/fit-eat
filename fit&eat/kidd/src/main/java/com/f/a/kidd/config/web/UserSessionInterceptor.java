package com.f.a.kidd.config.web;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSONObject;

public class UserSessionInterceptor implements HandlerInterceptor {	
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		if(request.getSession().getAttribute(SystemContanst.USER_AGENT) == null ) {
			response.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
			PrintWriter writer = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("desc", "Non Authoritative Information");
			writer.write(json.toJSONString());
			return false;
		}
		Object object = request.getSession().getAttribute(SystemContanst.USER_AGENT);
		UserAgent userAgent = JSONObject.parseObject(JSONObject.toJSONString(object), UserAgent.class);
		String name = ((HandlerMethod)handler).getMethod().getName();
		if(!("binding".equals(name)||"logout".equals(name))) {
			if(StringUtils.isEmpty(userAgent.getMobile())) {
				response.setStatus(HttpStatus.SC_NOT_ACCEPTABLE);
				PrintWriter writer = response.getWriter();
				JSONObject json = new JSONObject();
				json.put("desc", "Non Finished Binding Access Refused");
				writer.write(json.toJSONString());
				return false;
			}
		}
		return true;
	}
}
