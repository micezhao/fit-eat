package com.f.a.kobe.service.impl;

import lombok.Data;

/*微信完成注册需要手机号码，前端能够收集到或提供的数据，如下，
需要根据sessionid解析出sessionkey，通过sessionkey和iv解析出encryptedData中的手机号码*/
@Data
public class WxAuthRegistryBean {

	private String encryptedData;
	
	private String sessionId;
	
	private String iv;
}
