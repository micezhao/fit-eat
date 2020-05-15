package com.f.a.allan.enums;

import org.apache.commons.lang3.StringUtils;

public enum GoodsItemCategoryEnum {
	
	VIRTUAL("virtual","虚拟商品"),
	
	SUBSTAINTIAL("substantial","实物商品");
	
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

	private GoodsItemCategoryEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
	public static GoodsItemCategoryEnum getEnumByCode(String code) {
		if(StringUtils.isEmpty(code)) {
			return null;
		}else {
			for (GoodsItemCategoryEnum item : GoodsItemCategoryEnum.values()) {
				if(StringUtils.equals(item.getCode(), code)) {
					return item;
				}
			}
		}
		return null;
	}
	
}
