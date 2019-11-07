package com.f.a.kobe.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class CombinedParamCheckUtil {

	CombinedParam combinedParam;
	
	public void setCombinedParam(CombinedParam combinedParam) {
		this.combinedParam = combinedParam;
	}
	
	public Map<String,String> errResultMap = new HashMap<String,String>();
	
	public static Map<String,String> notEmptyResultMap = new HashMap<String,String>();
	
	public boolean check() throws Exception{
		Method[] methods = this.combinedParam.getClass().getMethods();
		for(Method method : methods) {
			String methodName = method.getName();
			if(methodName.contains("get")) {
				Object invoke = method.invoke(this.combinedParam, null);
				if(invoke != null && !methodName.contains("Class")) {
					String methodCheck = methodName+"Check";
					
					Method method2 = CombinedParamCheckUtil.class.getMethod(methodName+"Check",Object.class);
					boolean invoke2 = (boolean)method2.invoke(this, invoke);
				}
			}
		}
		
		return true;
	}
	
	public boolean getPhoneCheck(Object obj) {
		
		String phone = (String)obj;
		return true;
	}
	
	
	public static boolean checkEmpty(Integer value,String attrName,String tip) {
		return false;
	}
	
	public static boolean checkEmpty(Long value,String attrName,String tip) {
		return false;
	}
	
	public static boolean checkEmpty(String value,String attrName,String tip) {
		if(StringUtils.isEmpty(value)) {
			notEmptyResultMap.put(attrName,tip);
			return false;
		}
		return true;
	}
}
