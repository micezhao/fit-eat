package com.f.a.allan.enums;

import org.apache.commons.lang3.StringUtils;

public enum PackageStatusEnum {
	
	CTEATE("create","已创建"),
	PAID("paid","已支付"),
	CLOSED("closed","已关闭"),
	ALL("all","全部");
	
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
	
	public static PackageStatusEnum getEnumByCode(String code) {
		if(StringUtils.isEmpty(code)) {
			return null;
		}else {
			for (PackageStatusEnum item : PackageStatusEnum.values()) {
				if(StringUtils.equals(item.getCode(), code)) {
					return item;
				}
			}
		}
		return null;
	}
}
