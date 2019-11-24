package com.f.a.kobe.config.webconfig.interceptor;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSONObject;
import com.f.a.kobe.config.contants.SystemContanst;

public class UserSessionInterceptor implements HandlerInterceptor {	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setCharacterEncoding("utf-8");;
		response.setContentType("application/json; charset=utf-8");
		if(request.getSession().getAttribute(SystemContanst.USER_AGENT) == null ) {
			response.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
			PrintWriter writer = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("desc", "Non Authoritative Information");
			writer.write(json.toJSONString());
			return false;
		}
		return true;
	}
}
