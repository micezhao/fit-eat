package com.f.a.kobe.pojo.request;

import lombok.Data;

@Data
public class LoginRequest {
	
	private String loginType;

	private String wxOpenid;

	private String aliOpenid;

	private String username;

	private String password;

}
