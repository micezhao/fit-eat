package com.fa.kater.biz.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.view.UserAgent;
import com.fa.kater.annotations.AccessLogAnnot;
import com.fa.kater.annotations.AccessLogAnnot.AccessLogType;
import com.fa.kater.biz.UserInfoBiz;
import com.fa.kater.entity.requset.LoginParam;
import com.fa.kater.enums.AuthTypeEnum;
import com.fa.kater.pojo.UserInfo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public abstract class LoginBiz {

	@Autowired
	private UserInfoBiz userInfoBiz;
	
	@AccessLogAnnot(logType = AccessLogType.LOG_IN )
	public UserAgent login(LoginParam param) {
		UserInfo userInfo = existed(param);
		if (userInfo != null) {
			return userInfoBiz.generator(userInfo.getUserAccount(),param.getLoginType());
		}
		if(!StringUtils.equalsAny(param.getAuthType(), AuthTypeEnum.AUTH_PASSWORD.type,AuthTypeEnum.AUTH_SMS_VALIDATE.type) ) {
			return null;
		}
		UserAgent userAgent = register(param);
		return userAgent;
	}

	public abstract UserInfo existed(LoginParam param);
	
	
	/**
	 * 抽象方法，由各个实现类去实现自己的逻辑
	 * @param param
	 * @return
	 */
	public abstract UserAgent register(LoginParam param);
	
	@AccessLogAnnot(logType = AccessLogType.LOG_OUT)
	public void logout(UserAgent userAgent) {
		// TODO 从缓存中删除当前userAgent
		log.info("用户编号:{}已从系统登出",userAgent.getUserAccount());
	}
	
}
