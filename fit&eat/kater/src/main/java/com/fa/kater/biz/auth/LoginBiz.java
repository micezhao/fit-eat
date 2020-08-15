package com.fa.kater.biz.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.contants.Contants;
import com.f.a.kobe.view.UserAgent;
import com.fa.kater.biz.UserAgentBiz;
import com.fa.kater.entity.requset.LoginParam;
import com.fa.kater.enums.LoginTypeEnum;
import com.fa.kater.pojo.AccessLog;
import com.fa.kater.pojo.UserInfo;
import com.fa.kater.service.impl.AccessLogServiceImpl;

@Service
public abstract class LoginBiz {

	@Autowired
	private AccessLogServiceImpl accessLogServiceImpl;

	@Autowired
	private UserAgentBiz userAgentBiz;
	
	
	public UserAgent login(LoginParam param) {
		UserInfo userInfo = existed(param);
		if (userInfo != null) {
			return userAgentBiz.generator(userInfo.getUserAccount(),param.getLoginType());
		}
		return register(param);

	}

	public boolean login(UserAgent userAgent) {
		AccessLog accessLog = new AccessLog();
		accessLog.setUserAccount(userAgent.getUserAccount());
		accessLog.setAgentId(userAgent.getLoginType());
		accessLog.setAuthType(accessLog.getAuthType());
		accessLog.setEvent(Contants.LOGIN_TYPE_LOGIN);
		return accessLogServiceImpl.save(accessLog);
	}
	
	
	public abstract UserInfo existed(LoginParam param);
	
	
	/**
	 * 抽象方法，由各个实现类去实现自己的逻辑
	 * @param param
	 * @return
	 */
	public abstract UserAgent register(LoginParam param);
	

	public boolean logout(UserAgent userAgent) {
		AccessLog accessLog = new AccessLog();
		accessLog.setUserAccount(userAgent.getUserAccount());
		accessLog.setAgentId(userAgent.getLoginType());
		accessLog.setAuthType(accessLog.getAuthType());
		accessLog.setEvent(Contants.LOGIN_TYPE_LOGOUT);
		return accessLogServiceImpl.save(accessLog);
	}
	

	
}
