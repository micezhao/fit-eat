package com.f.a.kobe.config.webconfig.interceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSONObject;

public class UserSessionInterceptor implements HandlerInterceptor {
	
	private final static String ATTR_NAME = "userDetail";
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setCharacterEncoding("utf-8");;
		response.setContentType("application/json; charset=utf-8");
		if(request.getSession().getAttribute(ATTR_NAME) == null ) {
			response.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
			PrintWriter writer = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("desc", "can not find userDetail from session");
			writer.write(json.toJSONString());
			return false;
		}
		return true;
	}
}
