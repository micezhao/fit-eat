package com.f.a.kidd.filters;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.f.a.kidd.config.CustomizeZuulProperties;
import com.f.a.kobe.contants.Contants;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class UserSessionFilter extends ZuulFilter {
	
	@Autowired
	private CustomizeZuulProperties customizeZuulProperties;
	
	
	private AntPathMatcher pathMatcher = new AntPathMatcher();
	
	@Override
	public boolean shouldFilter() { // 设置这个拦截器是否生效
		return true;
	}

	@Override
	public Object run() throws ZuulException { // 编排当前过滤器的执行逻辑
		// 针对本过滤器的说明：验证拦截的请求是否包含登陆信息，各个子系统无需重复验证请求
		RequestContext currentContext = RequestContext.getCurrentContext();
		HttpServletRequest request = currentContext.getRequest();
		String uri = request.getRequestURI();
		
		List<String> excludedPaths=customizeZuulProperties.getExcludedPaths();
		
		for (String candidate : excludedPaths) { 
			boolean matched = pathMatcher.matchStart(candidate, uri);
			if(matched) {
				return null;
			}
		}
		if (request.getSession().getAttribute(Contants.USER_AGENT) == null) {
			currentContext.setSendZuulResponse(false); // 如果未找到用户信息就直接返回，不在转发请求
			currentContext.setResponseBody("Non Authoritative Information");
			currentContext.setResponseStatusCode(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
			return null;
		}
		return null;
	}

	@Override
	public String filterType() { // 定义当前filter作用于请求转发之前
		return "pre";
	}

	@Override
	public int filterOrder() { // 定义执行顺序
		return 0;
	}
	

}
