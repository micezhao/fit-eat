package com.f.a.kobe.exceptions;

public class InvaildException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5314902931767614069L;
	
	private ErrCodeEnum errCode;
	
	private String errMsg;
	
	public ErrCodeEnum getErrCode() {
		return errCode;
	}

	public void setErrCode(ErrCodeEnum errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public InvaildException(ErrCodeEnum errCode,String errMsg) {
		super(errMsg);
		this.errCode = errCode;
		
	}
}
