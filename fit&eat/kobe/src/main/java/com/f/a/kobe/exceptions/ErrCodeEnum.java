package com.f.a.kobe.exceptions;

public enum ErrCodeEnum {
	
	QUERY_PARAM_INVAILD("9001","查询参数验证异常"),
	REDUPICATE_RECORD("9002","存在重复的记录");
	
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

	private ErrCodeEnum(String errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	
	
	
	
	
}
