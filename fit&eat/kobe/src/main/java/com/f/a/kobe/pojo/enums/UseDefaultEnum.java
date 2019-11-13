package com.f.a.kobe.pojo.enums;

public enum UseDefaultEnum {
	
	USE_DEFAULT("1","默认");
	
	private String code;
	
	private String description;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private UseDefaultEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
	
	
	
}
