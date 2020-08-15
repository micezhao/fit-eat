package com.fa.kater.exceptions;

import lombok.Data;

import java.io.Serializable;

@Data
public class ErrRtn extends Exception implements Serializable {

	
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
