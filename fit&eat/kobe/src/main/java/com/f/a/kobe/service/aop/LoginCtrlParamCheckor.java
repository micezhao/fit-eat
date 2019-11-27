package com.f.a.kobe.service.aop;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.util.CombinedParam;
import com.f.a.kobe.util.CombinedParamBuilder;
import com.f.a.kobe.util.CombinedParamCheckUtil;

@Component
public class LoginCtrlParamCheckor implements ParamCheckHandler{

	@Override
	public Map<String, String> commonCheck(Object t,String value) {
		if(value.equals("binding")) {
			return binding(t);
		}else if(value.equals("authCode")) {
			return authCode(t);
		}
		else {
			throw new InvaildException("9999", "找不到合适的校验规则");
		}
	}

	private Map<String, String> authCode(Object t) {
		ParamRequest paramRequest = (ParamRequest)t;
		Map<String, String> checkEmpty = CombinedParamCheckUtil.checkEmpty(paramRequest.getCode(), "authCode",  "请求code不允许为空");
		if(checkEmpty != null) {
			return checkEmpty;
		}
		return null;
	}

	private Map<String, String> binding(Object t) {
		ParamRequest paramRequest = (ParamRequest)t;
		Map<String, String> checkEmpty = CombinedParamCheckUtil.checkEmpty(paramRequest.getMobile(), "molibe",  "联系人电话不得为空");
		if(checkEmpty != null) {
			return checkEmpty;
		}
		CombinedParam combinedParam = new CombinedParamBuilder().setMobile(paramRequest.getMobile()).build();
		CombinedParamCheckUtil cutil = new CombinedParamCheckUtil();
		cutil.setCombinedParam(combinedParam);
		try {
			Map<String, String> checkResult = cutil.check();
			if(checkResult != null) {
				return checkResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
