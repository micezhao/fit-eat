package com.fa.kater.exceptions;

public class InvaildException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5314902931767614069L;
	
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

	public InvaildException(String errCode, String errMsg) {
		super();
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	
	
}
