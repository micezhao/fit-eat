package com.f.a.allan.enums;

import org.apache.commons.lang3.StringUtils;

public enum MerchantStatus {
	/******** 审核状态 ******/
	WAIT_VERIFY("wait_verify","待审核"),
	
	UN_VERIFIED("un_verified","审核未通过"),

	VERIFIED("verified","已审核"),	
	/******** 审核状态 ******/
	
	/******** 运营状态 ******/
	OPENING("opening","正在营业"),
	
	SUSPENSION("suspension","休业"), // 可搜索到店铺，可搜索到商品，但是商品状态为停售
	
	CLOSED("closed","停业"), // 客户端无法搜到到当前店铺及其商品
	
	OFF("off","已注销");    // 商户注销，商户不可进行任何操作
	
	/******** 运营状态 ******/
	
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

	private MerchantStatus(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public static MerchantStatus getEnumByCode(String code) {
		if(StringUtils.isBlank(code)) {
			return null;
		}else {
			for (MerchantStatus item : MerchantStatus.values()) {
				if(StringUtils.equals(item.getCode(), code)) {
					return item;
				}
			}
		}
		return null;
	}
	
	
}
