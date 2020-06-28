package com.f.a.allan.enums;

import org.apache.commons.lang3.StringUtils;

public enum CommentVerifyEnum {
	
	WAIT_VERIFY("wait_verify","待审核"),
	UN_VERIFIED("un_verified","未通过"),
	VERIFIED("verified","已审核");
	
	private String code;
	
	private String description;

	private CommentVerifyEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public static CommentVerifyEnum getByCode(String code) {
		if(StringUtils.isBlank(code)) {
			return null;
		}else {
			for (CommentVerifyEnum element : CommentVerifyEnum.values()) {
				if(StringUtils.equals(element.code, code)) {
					return element;
				}
			}
		}
		return null;
	}
	
	
	
	
}
