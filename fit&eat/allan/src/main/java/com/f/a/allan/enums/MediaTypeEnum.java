package com.f.a.allan.enums;

import org.apache.commons.lang3.StringUtils;

public enum MediaTypeEnum {
	
	IMG("img","静态图片"),
	
	GIF("gif","动态图片"),
	
	VIDEO("video","视频");
	
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

	private MediaTypeEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}
	 
	public static MediaTypeEnum getEnumByCode(String code) {
		if(StringUtils.isBlank(code)) {
			return null;
		}else {
			for (MediaTypeEnum item : MediaTypeEnum.values()) {
				if(StringUtils.equals(item.getCode(), code)) {
					return item;
				}
			}
		}
		return null;
	}
	
	
}
