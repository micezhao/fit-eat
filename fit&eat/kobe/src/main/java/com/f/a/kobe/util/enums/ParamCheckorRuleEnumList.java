package com.f.a.kobe.util.enums;

public enum ParamCheckorRuleEnumList {
	
	REALNAME("^[\\u4e00-\\u9fa5]{0,}$","中文姓名不合法"),
	MOBILE("^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$","手机号不合法"),
	PROVINCENO("[1-9]\\d{5}(?!\\d)","省编码不合法"),
	CITYNO("[1-9]\\d{5}(?!\\d)","市编码不合法"),
	DISTRICTNO("[1-9]\\d{5}(?!\\d)","区编码不合法"),
	STREETNO("[1-9]\\d{8}(?!\\d)","街道编码不合法"),
	BIRTHDAY("\\d{4}-\\d{1,2}-\\d{1,2}$","生日格式不合法"),
	GENDER("^\\s{male,female}$","性别不合法"),
	AGE("^(?:[1-9][0-9]?|1[01][0-9]|120)$","年龄不合法"),
	INTEGERJ("^\\+?[1-9][0-9]*$","要求整数"),
	WENURL("^https://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$","web地址不合法"),
	EMAIL("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$","电子邮箱地址不合法"),
	NICKNAME("^[\\u4E00-\\u9FA5A-Za-z0-9_]+$","昵称不合法");

	private String regex;
	
	private String errMsg;

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	private ParamCheckorRuleEnumList(String regex, String errMsg) {
		this.regex = regex;
		this.errMsg = errMsg;
	}
}
