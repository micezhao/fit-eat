package com.fa.kater.biz.auth;

import org.springframework.stereotype.Service;

import com.f.a.kobe.view.UserAgent;
import com.fa.kater.entity.requset.LoginParam;
import com.fa.kater.pojo.UserInfo;

/**
 * 通过手机短信登录
 * @author micezhao
 *
 */
@Service
public class MobileSMSAuthBiz implements LoginBizInterface {

	@Override
	public UserAgent login(LoginParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logout(UserAgent userAgent) {
		// TODO Auto-generated method stub
		
	}
	
}
