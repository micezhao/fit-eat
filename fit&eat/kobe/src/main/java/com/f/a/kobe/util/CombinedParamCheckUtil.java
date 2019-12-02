package com.f.a.kobe.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.f.a.kobe.util.enums.ParamCheckorRuleEnumList;

public class CombinedParamCheckUtil {

	CombinedParam combinedParam;
	
	CombinedParamCheckor combinedParamCheckor;

	public void setCombinedParam(CombinedParam combinedParam) {
		this.combinedParam = combinedParam;
	}
	
	public void setCombinedParamCheckor(CombinedParamCheckor combinedParamCheckor) {
		this.combinedParamCheckor = combinedParamCheckor;
	}
	
	public Map<String, String> errResultMap = new HashMap<String, String>();

	public static Map<String, String> notEmptyResultMap = new HashMap<String, String>();

	public Map<String, String> check() throws Exception {
		Method[] methods = this.combinedParam.getClass().getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.contains("get")) {
				Object invoke = method.invoke(this.combinedParam, null);
				if (invoke != null && !methodName.contains("Class")) {
					Method method2 = CombinedParamCheckUtil.class.getMethod(methodName + "Check", Object.class);
					Result result = (Result) method2.invoke(this, invoke);
					if (!result.tag) {
						errResultMap.put(result.errCode, result.errMsg);
					}
				}
			}
		}
		if (!errResultMap.isEmpty()) {
			return errResultMap;
		}
		return null;
	}
	
	//改造统一接受参数校验
	public Map<String, String> check2() throws Exception {
		Method[] methods = this.combinedParamCheckor.getClass().getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if (methodName.contains("get")) {
				Object invoke = method.invoke(this.combinedParamCheckor, null);
				if (invoke != null && !methodName.contains("Class")) {
					if(invoke instanceof List) {
						Method method2 = CombinedParamCheckUtil.class.getMethod(methodName + "Check", Object.class);
						Result result = (Result) method2.invoke(this, invoke);
						if (!result.tag) {
							errResultMap.put(result.errCode, result.errMsg);
						}
					}else {
						Method method2 = CombinedParamCheckUtil.class.getMethod(methodName + "Check", Object.class);
						Result result = (Result) method2.invoke(this, invoke);
						if (!result.tag) {
							errResultMap.put(result.errCode, result.errMsg);
						}
					}
				}
			}
		}
		if (!errResultMap.isEmpty()) {
			return errResultMap;
		}
		return null;
	}

	class Result {
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
	
	//通用的校验方法
	public boolean commonCheck(String target,String regex) {
		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(target).matches();
	}
	
	@SuppressWarnings("unchecked")
	public Result getFloatListCheck(Object obj) {
		StringBuffer errMsgBuf = new StringBuffer();
		List<String> floatList = (List<String>)obj;
		for(String floatj : floatList) {
			boolean checkResult = commonCheck(floatj,ParamCheckorRuleEnumList.FLOATJ.getRegex());
			if(!checkResult) {
				errMsgBuf.append(floatj);
				errMsgBuf.append(" ");
			}
		}
		if(!StringUtils.isBlank(errMsgBuf.toString())) {
			errMsgBuf.append(ParamCheckorRuleEnumList.FLOATJ.getErrMsg());
		}
		String errMsg = errMsgBuf.toString();
		if(StringUtils.isBlank(errMsg)) {
			return new Result(true);
		}
		return new Result(false, "float", errMsg);			
	}
	
	@SuppressWarnings("unchecked")
	public Result getMobileListCheck(Object obj) {
		StringBuffer errMsgBuf = new StringBuffer();
		List<String> mobileList = (List<String>)obj;
		for(String mobile : mobileList) {
			boolean checkResult = commonCheck(mobile,ParamCheckorRuleEnumList.MOBILE.getRegex());
			if(!checkResult) {
				errMsgBuf.append(mobile);
				errMsgBuf.append(" ");
			}
		}
		if(!StringUtils.isBlank(errMsgBuf.toString())) {
			errMsgBuf.append(ParamCheckorRuleEnumList.MOBILE.getErrMsg());
		}
		String errMsg = errMsgBuf.toString();
		if(StringUtils.isBlank(errMsg)) {
			return new Result(true);
		}
		return new Result(false, "mobile", errMsg);			
	}
	
	public Result getRealNameCheck(Object obj) {
		String target = (String)obj;
		boolean checkResult = commonCheck(target,ParamCheckorRuleEnumList.REALNAME.getRegex());
		if(checkResult) {
			return new Result(true);
		}
		return new Result(false, "REALNAME:" + target, ParamCheckorRuleEnumList.REALNAME.getErrMsg());			
	}
	
	public Result getNickNameCheck(Object obj) {
		String target = (String)obj;
		boolean checkResult = commonCheck(target,ParamCheckorRuleEnumList.NICKNAME.getRegex());
		if(checkResult) {
			return new Result(true);
		}
		return new Result(false, "NICKNAME:" + target, ParamCheckorRuleEnumList.NICKNAME.getErrMsg());			
	}
	
	public Result getMobileCheck(Object obj) {
		String target = (String)obj;
		boolean checkResult = commonCheck(target,ParamCheckorRuleEnumList.MOBILE.getRegex());
		if(checkResult) {
			return new Result(true);
		}
		return new Result(false, "MOBILE:" + target, ParamCheckorRuleEnumList.MOBILE.getErrMsg());			
	}
	
	public Result getGenderCheck(Object obj) {
		String target = (String)obj;
		boolean checkResult = commonCheck(target,ParamCheckorRuleEnumList.GENDER.getRegex());
		if(checkResult) {
			return new Result(true);
		}
		return new Result(false, "GENDER:" + target, ParamCheckorRuleEnumList.GENDER.getErrMsg());			
	}
	
	public Result getBirthdayCheck(Object obj) {
		String target = (String)obj;
		boolean checkResult = commonCheck(target,ParamCheckorRuleEnumList.BIRTHDAY.getRegex());
		if(checkResult) {
			return new Result(true);
		}
		return new Result(false, "BIRTHDAY:"+target, ParamCheckorRuleEnumList.BIRTHDAY.getErrMsg());	
	}

	public Result getAgeCheck(Object obj) {
		String target = (String)obj;
		boolean checkResult = commonCheck(target,ParamCheckorRuleEnumList.AGE.getRegex());
		if(checkResult) {
			return new Result(true);
		}
		return new Result(false, "AGE:"+target, ParamCheckorRuleEnumList.AGE.getErrMsg());	
	}

	public Result getWebUrlCheck(Object obj) {
		String target = (String)obj;
		boolean checkResult = commonCheck(target,ParamCheckorRuleEnumList.WEBURL.getRegex());
		if(checkResult) {
			return new Result(true);
		}
		return new Result(false, "url:"+target, ParamCheckorRuleEnumList.WEBURL.getErrMsg());	
	}
	
	public Result getProvinceNoCheck(Object obj) {
		String target = (String)obj;
		boolean checkResult = commonCheck(target,ParamCheckorRuleEnumList.PROVINCENO.getRegex());
		if(checkResult) {
			return new Result(true);
		}
		return new Result(false, "PROVINCENO:" + target, ParamCheckorRuleEnumList.PROVINCENO.getErrMsg());	
	}

	public Result getCityNoCheck(Object obj) {
		String target = (String)obj;
		boolean checkResult = commonCheck(target,ParamCheckorRuleEnumList.CITYNO.getRegex());
		if(checkResult) {
			return new Result(true);
		}
		return new Result(false, "CITYNO:" + target, ParamCheckorRuleEnumList.CITYNO.getErrMsg());	
	}

	public Result getDistrcNoCheck(Object obj) {
		String target = (String)obj;
		boolean checkResult = commonCheck(target,ParamCheckorRuleEnumList.DISTRICTNO.getRegex());
		if(checkResult) {
			return new Result(true);
		}
		return new Result(false, "DISTRICTNO:" + target, ParamCheckorRuleEnumList.DISTRICTNO.getErrMsg());	
	}

	public Result getStreetNoCheck(Object obj) {
		String target = (String)obj;
		boolean checkResult = commonCheck(target,ParamCheckorRuleEnumList.STREETNO.getRegex());
		if(checkResult) {
			return new Result(true);
		}
		return new Result(false, "STREETNO:" + target, ParamCheckorRuleEnumList.STREETNO.getErrMsg());	
	}

	
	public static void main(String[] args) {
		String reg = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,3})?$";
		
		String realname = "205.86";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(realname);
		boolean matches = matcher.matches();
		System.out.println(matches);
	}
	
	
	public Result getCodeCheck(Object obj) {
		String phone = (String) obj;
		if (phone.length() > 100) {
			return new Result(false, "code", "微信授权code失效");
		}
		return new Result(true);
	}

	public Result getAddrDetailCheck(Object obj) {
		String phone = (String) obj;
		if (phone.length() > 100) {
			return new Result(false, "AddrDetail", "详细地址");
		}
		return new Result(true);
	}

	public static boolean checkEmpty(Integer value, String attrName, String tip) {
		return false;
	}

	public static boolean checkEmpty(Long value, String attrName, String tip) {
		return false;
	}

	public static Map<String, String> checkEmpty(String value, String attrName, String tip) {
		if (StringUtils.isEmpty(value)) {
			notEmptyResultMap.put(attrName, tip);
		}
		return notEmptyResultMap;
	}

}
