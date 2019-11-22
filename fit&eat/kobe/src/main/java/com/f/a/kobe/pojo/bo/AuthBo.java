package com.f.a.kobe.pojo.bo;

import lombok.Data;

@Data
public class AuthBo {

	private String authToken;
	
	private String sessionKey;
	
	private String openid;
	
	private String authType;
	
	private Long customerId;
	
	private String mobile;
	
	
}
