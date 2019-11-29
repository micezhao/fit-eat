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

	public Result getRealnameCheck(Object obj) {
		String realname = (String) obj;

		return new Result(true);
	}

	public Result getBirthdayCheck(Object obj) {
		String birthday = (String) obj;
		return new Result(true);
	}

	public Result getGenderCheck(Object obj) {
		String gender = (String) obj;
		return new Result(true);
	}

	public Result getAgeCheck(Object obj) {
		int age = (int) obj;
		return new Result(true);
	}

	public Result getNicknameCheck(Object obj) {
		String realname = (String) obj;

		return new Result(true);
	}

	public Result getHeadimgCheck(Object obj) {
		String headimg = (String) obj;
		return new Result(true);
	}

	public Result getMobileCheck(Object obj) {
		String phone = (String) obj;
		if (phone.length() != 11) {
			return new Result(false, "mobile", "手机号格式错误");
		}
		return new Result(true);
	}

	public Result getConnectorNameCheck(Object obj) {
		String phone = (String) obj;
		if (phone.length() > 11) {
			return new Result(false, "ConnectorName", "联系人名称不恰当");
		}
		return new Result(true);
	}

	public Result getConnectorMobileCheck(Object obj) {
		String phone = (String) obj;
		if (phone.length() != 11) {
			return new Result(false, "ConnectorMobile", "手机号格式错误");
		}
		return new Result(true);
	}

	public Result getCodeCheck(Object obj) {
		String phone = (String) obj;
		if (phone.length() > 100) {
			return new Result(false, "code", "微信授权code失效");
		}
		return new Result(true);
	}

	public Result getProvinceNoCheck(Object obj) {
		String phone = (String) obj;
		if (phone.length() != 6) {
			return new Result(false, "ProvinceNo", "省编码错误");
		}
		return new Result(true);
	}

	public Result getCityNoCheck(Object obj) {
		String phone = (String) obj;
		if (phone.length() != 6) {
			return new Result(false, "CityNo", "市编码错误");
		}
		return new Result(true);
	}

	public Result getDistrcNoCheck(Object obj) {
		String phone = (String) obj;
		if (phone.length() != 6) {
			return new Result(false, "DistrcNo", "区编码式错误");
		}
		return new Result(true);
	}

	public Result getStreetNoCheck(Object obj) {
		String phone = (String) obj;
		if (phone.length() > 20) {
			return new Result(false, "StreetNo", "街道编码式错误");
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
			return notEmptyResultMap;
		}
		return null;
	}

}
