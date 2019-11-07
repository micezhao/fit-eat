package com.f.a.kobe.util;

import java.lang.reflect.Method;

import lombok.Data;

@Data
public class CombinedParam {

	private String phone;
	
	private Integer age;
	
	private Long customerId;
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		System.out.println("aaa");
		Method method = CombinedParam.class.getMethod("aaa");
		System.out.println(method.getName());
	}
	
	public void aaa() {}
}
