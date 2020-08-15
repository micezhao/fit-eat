package com.fa.kater.enums;

import org.apache.commons.lang3.StringUtils;

public enum AuthTypeEnum {
	
	AUTH_WX("wx","微信登录"),
	AUTH_ALI("alipay","支付宝登录"),
	AUTH_PASSWORD("password","账号密码登录"),
	AUTH_SMS_VALIDATE("sms","短信验证码登录");
	
	public String type;
	public String desc;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private AuthTypeEnum(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public static AuthTypeEnum getEnumByType(String type) {
		if(StringUtils.isBlank(type)) {
			return null;
		}else {
			for (AuthTypeEnum item : AuthTypeEnum.values()) {
				if(StringUtils.equals(type, item.getType())) {
					return item;
				}
				
			}
		}
		return null;
	}
}
