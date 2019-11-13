package com.f.a.kobe.pojo.enums;

public enum LoginTypeEnum {
	
	WECHAT("wx","微信登陆"),
	ALI_PAY("ali_pay","支付宝");
	
	private String loginTypeCode;
	
	private String description;

	public String getLoginTypeCode() {
		return loginTypeCode;
	}

	public void setLoginTypeCode(String loginTypeCode) {
		this.loginTypeCode = loginTypeCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private LoginTypeEnum(String loginTypeCode, String description) {
		this.loginTypeCode = loginTypeCode;
		this.description = description;
	}
	
	
	
}
