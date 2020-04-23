package com.f.a.allan.exceptions;

import java.io.Serializable;

import lombok.Data;

@Data
public class ErrRtn implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5934914205959562339L;

	private String errCode;
	
	private String errDesc;

	public ErrRtn(String errCode, String errDesc) {
		super();
		this.errCode = errCode;
		this.errDesc = errDesc;
	}
	
	
}	
