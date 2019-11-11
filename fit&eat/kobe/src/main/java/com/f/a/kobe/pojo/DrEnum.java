package com.f.a.kobe.pojo;

import org.apache.commons.lang.StringUtils;

public enum DrEnum {
	
	FORBBIDEN("0","fobbiden","禁用"),
	AVAILABLE("1","available","可用");
	
	private String code;
	
	private String states;
	
	private String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private DrEnum(String code, String states, String description) {
		this.code = code;
		this.states = states;
		this.description = description;
	}
	
	public static DrEnum getByCode(String code) {
		if(StringUtils.isBlank(code)) {
			return null;
		}else {
			for (DrEnum item : DrEnum.values()) {
				if(StringUtils.equals( item.getCode(), code)) {
					return item;
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(DrEnum.getByCode("0").getDescription()); 
	}
	
}
