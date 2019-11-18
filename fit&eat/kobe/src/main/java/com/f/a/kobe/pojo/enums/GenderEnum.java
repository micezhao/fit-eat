package com.f.a.kobe.pojo.enums;

public enum GenderEnum {
	
	MALE("male","男性"),
	FEMALE("female","女性");
	
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

	private GenderEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
}
