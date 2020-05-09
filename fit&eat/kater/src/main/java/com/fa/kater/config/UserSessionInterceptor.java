package com.fa.kater.config;

import com.alibaba.fastjson.JSONObject;
import com.f.a.kobe.contants.Contants;
import com.f.a.kobe.view.UserAgent;
import org.apache.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class UserSessionInterceptor implements HandlerInterceptor {	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json; charset=utf-8");
		//验证是否存在合法用户信息的流程移至 zuul-kidd项目中
//		if(request.getSession().getAttribute(SystemContanst.USER_AGENT) == null ) {
//			response.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
//			PrintWriter writer = response.getWriter();
//			JSONObject json = new JSONObject();
//			json.put("desc", "Non Authoritative Information");
//			writer.write(json.toJSONString());
//			return false;
//		}
		Object object = request.getSession().getAttribute(Contants.USER_AGENT);
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