package com.f.a.kobe.pojo.enums;

public enum OffSetEnum {
	
	NEGATIVE_MAX("-3","很低"),
	NEGATIVE_MEDIUM("-2","较低"),
	NEGATIVE_MIN("-1","偏低"),
	STANDARD("0","标准"),
	POSITIVE_MIN("+1","偏高"),
	POSITIVE_MEDIUM("+2","较高"),
	POSITIVE_MAX("+3","很高"),
	FAT_OVER_0("level0","超重"),
	FAT_OVER_1("level1","一度肥胖"),
	FAT_OVER_2("level2","二度肥胖"),
	FAT_OVER_3("level3","三度肥胖");
	
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

	private OffSetEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}
	
	
	
	
	
}
