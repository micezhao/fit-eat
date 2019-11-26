package com.f.a.kobe.util;

public class CombinedParamBuilder {
	CombinedParam combinedParam;
	
	public CombinedParamBuilder() {
		this.combinedParam = new CombinedParam();
	}
	
	public CombinedParamBuilder setMobile(String mobile ) {
		this.combinedParam.setMobile(mobile);
		return this;
	}
	
	public CombinedParamBuilder setAge(Integer age ) {
		this.combinedParam.setAge(age);
		return this;
	}
	
	public CombinedParamBuilder setCustomerId(Long id ) {
		this.combinedParam.setCustomerId(id);
		return this;
	}
	
	public CombinedParam build() {
		return this.combinedParam;
	}
}
