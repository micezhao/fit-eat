package com.f.a.kobe.service.impl.wx;

import lombok.Data;

@Data
public class WxLoginSuccess {
	private String session_key;
	
	private String openid;

}
