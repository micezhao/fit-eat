package com.fa.kater.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 可用状态的枚举
 * @author micezhao
 *
 */
public enum DrEnum {
	
	AVAILABLE("1","可用"),
	DISABLE("0","不可用");
	
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

	private DrEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
	public static DrEnum getEnumByCode(String code) {
		if(StringUtils.isBlank(code)) {
			return null;
		}else {
			for (DrEnum item : DrEnum.values()) {
				if(StringUtils.equals(item.getCode(), code) ) {
					return item;
				}
			}
		}
		return null;
	}
	
}
