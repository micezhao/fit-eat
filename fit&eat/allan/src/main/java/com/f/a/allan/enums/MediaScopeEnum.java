package com.f.a.allan.enums;

import org.apache.commons.lang3.StringUtils;

public enum MediaScopeEnum {
	
	LOOP("loop","轮播图"),
	
	EXHIBITION("exhibition","展位图"),
	
	LISTING("listing","列表图");
	
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

	private MediaScopeEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}
	 
	public static MediaScopeEnum getEnumByCode(String code) {
		if(StringUtils.isBlank(code)) {
			return null;
		}else {
			for (MediaScopeEnum item : MediaScopeEnum.values()) {
				if(StringUtils.equals(item.getCode(), code)) {
					return item;
				}
			}
		}
		return null;
	}
	
	
}
