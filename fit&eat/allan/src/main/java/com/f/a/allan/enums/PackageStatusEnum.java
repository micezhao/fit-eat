package com.f.a.allan.enums;

public enum PackageStatusEnum {
	
	CTEATE("create","已创建"),
	PAYED("payed","已支付"),
	CLOSED("closed","已关闭");
	
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

	private PackageStatusEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
}
