package com.f.a.kobe.service.aop;

public class ParamCheckUtil {

	ParamCheckor paramCheckor;
	
	public ParamCheckUtil(ParamCheckor paramCheckor) {
		this.paramCheckor = paramCheckor;
	}
	
	public boolean check(Object t) {
		return this.paramCheckor.check(t);
	}
}
