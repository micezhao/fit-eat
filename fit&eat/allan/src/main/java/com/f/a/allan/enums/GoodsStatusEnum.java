package com.f.a.allan.enums;

import org.apache.commons.lang3.StringUtils;

public enum GoodsStatusEnum {
	
	UN_SOLD("un_sold","未上架"),
	
	ON_SALE("on_sale","在售"),
	
	SUSPENSION("suspension","停售"),
	
	LACK("lack","库存不足");
	
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

	private GoodsStatusEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
	public static GoodsStatusEnum getEnumByCode(String code) {
		if(StringUtils.isEmpty(code)) {
			return null;
		}else {
			for (GoodsStatusEnum item : GoodsStatusEnum.values()) {
				if(StringUtils.equals(item.getCode(), code)) {
					return item;
				}
			}
		}
		return null;
	}
}	
