package com.f.a.allan.enums;

public enum OrderEnum {
	
	NEED_DELIVERY("need_delivery","待发货"),
	
	NEED_CHECK("need_check","待收货"),
	
	NEED_COMMENT("need_comment","待评价"),
	
	RETURN_APPLY("return_apply","申请退单"),
	
	RETURN_COMPLETED("return_completed","已退单");
	
	private String code;
	
	private String desc;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private OrderEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	
}
