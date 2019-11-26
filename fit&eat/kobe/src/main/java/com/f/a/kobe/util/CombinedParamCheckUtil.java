package com.f.a.kobe.util;

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
	
	public Map<String,String> check() throws Exception{
		Method[] methods = this.combinedParam.getClass().getMethods();
		for(Method method : methods) {
			String methodName = method.getName();
			if(methodName.contains("get")) {
				Object invoke = method.invoke(this.combinedParam, null);
				if(invoke != null && !methodName.contains("Class")) {
					Method method2 = CombinedParamCheckUtil.class.getMethod(methodName+"Check",Object.class);
					Result result = (Result)method2.invoke(this, invoke);
					if(!result.tag) {
						errResultMap.put(result.errCode,result.errMsg);
					}
				}
			}
		}
		if(!errResultMap.isEmpty()) {
			return errResultMap;
		}
		return null;
	}
	
	class Result{
		public Result(boolean tag, String errCode, String errMsg) {
			super();
			this.tag = tag;
			this.errCode = errCode;
			this.errMsg = errMsg;
		}
		public Result(boolean tag) {
			super();
			this.tag = tag;
		}
		boolean tag;
		String errCode;
		String errMsg;
	}
	
	public Result getMobileCheck(Object obj) {
		String phone = (String)obj;
		if(phone.length() != 11) {
			return new Result(false,"mobile","手机号格式错误");
		}
		return new Result(true);
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
