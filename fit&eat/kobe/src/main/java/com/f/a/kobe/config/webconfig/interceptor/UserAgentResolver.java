package com.f.a.kobe.config.webconfig.interceptor;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSONObject;
import com.f.a.kobe.config.contants.SystemContanst;
import com.f.a.kobe.pojo.view.UserAgent;

public class UserAgentResolver implements HandlerMethodArgumentResolver {
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (parameter.getParameterType().isAssignableFrom(UserAgent.class)) {
            return true;
        }
        return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		 Object obj= webRequest.getAttribute(SystemContanst.USER_AGENT, RequestAttributes.SCOPE_SESSION);
		 UserAgent agent =  JSONObject.parseObject( JSONObject.toJSONString(obj), UserAgent.class);
		return agent;
	}

}