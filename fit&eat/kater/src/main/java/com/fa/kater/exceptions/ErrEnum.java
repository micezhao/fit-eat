package com.fa.kater.exceptions;

public enum ErrEnum {
	
	QUERY_PARAM_INVAILD("9001","查询参数验证异常"),
	REDUPICATE_RECORD("9002","存在重复的记录"),
	INPUT_PARAM_INVAILD("9003","输入参数异常"),
	WX_AUTH_INVAILD("9004","微信授权异常"),
	WX_AUTH_INSERTFAil_INVAILD("9005","添加授权用户异常"),
	CUSTOMER_NOT_FOUND("9006","用户未认证"),
	OVER_LIMITS("9007","记录超过上限"),
	UNKNOWN_LOGIN_TYPE("9008","未知的登陆类型"),
	REDUPICATE_REGISTER("9009","用户已存在请勿重复注册"),
	VALIDATECODEERROR("9010","错误的验证码"),
	UNLOGIN_ERROR("9011","当前用户未登录"),
	REDUPICATE_BIND("9012","当前手机号已经通过本渠道进行过绑定，请勿重复绑定"),
	NO_DEFAULT_ADDR("9013","当前用户未设置默认地址"),
	OPERATION_RESTRITCED("9014","受限的操作"),
	THIRD_AUTH_ERROR("9015","获取第三方openId失败"),
	
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
