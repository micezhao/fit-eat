package com.f.a.kobe.pojo.bo;

import lombok.Data;

@Data
public class AuthResult {

	private String sessionKeyMD5;
	
	private String openid;
}
