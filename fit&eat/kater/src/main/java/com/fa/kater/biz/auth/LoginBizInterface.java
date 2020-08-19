package com.fa.kater.biz.auth;

import com.f.a.kobe.view.UserAgent;
import com.fa.kater.entity.requset.LoginParam;

public  interface LoginBizInterface {
	
	public UserAgent login(LoginParam param) ;
	
	
	public void logout(UserAgent userAgent);
	
}
