package com.f.a.kobe.exceptions;

public enum ErrEnum {
	
	QUERY_PARAM_INVAILD("9001","查询参数验证异常"),
	REDUPICATE_RECORD("9002","存在重复的记录"),
	INPUT_PARAM_INVAILD("9003","输入参数异常"),
	WX_AUTH_INVAILD("9004","微信授权异常"),
	WX_AUTH_INSERTFAil_INVAILD("9005","添加授权用户异常"),
	CUSTOMER_NOT_FOUND("9006","用户不存在异常"),
	OVER_LIMITS("9007","记录超过上限"),
	UNKNOWN_LOGIN_TYPE("9008","未知的登陆类型"),
	REDUPICATE_REGISTER("9009","重复注册");
	
	private String errCode;
	private String errMsg;
	
	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	private ErrEnum(String errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	
	
	
	
	
}
