package com.f.a.kobe.service.impl.wx;

import lombok.Data;

//微信用户授权进入从微信端拿到的授权信息
@Data
public class WxAuthInfoBean {

	private String openid;
	
	private String sessionKey;
}
