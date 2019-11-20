package com.f.a.kobe.pojo.bo;

import lombok.Data;

@Data
public class AuthResult {

	private String authToken;
	
	private String sessionKey;
	
	private String openid;
	
	private Long customerId;
}
