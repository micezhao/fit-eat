package com.f.a.allan.exceptions;

public enum ErrEnum {
	
	QUERY_PARAM_INVAILD("9001","查询参数验证异常"),
	REDUPICATE_RECORD("9002","存在重复的记录"),
	INPUT_PARAM_INVAILD("9003","输入参数异常"),
	OVER_LIMITS("9007","记录超过上限"),
	VALIDATECODEERROR("9010","错误的验证码"),
	UNLOGIN_ERROR("9011","当前用户未登录"),
	OPERATION_RESTRITCED("9014","受限的操作"),
	
	NO_INSTANCE("8001","无法找到服务实例"),
	COPY_EXCEPTION("8002","对象复制发生异常");
	
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
