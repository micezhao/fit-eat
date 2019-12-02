package com.f.a.kobe.service.aop;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.util.CombinedParam;
import com.f.a.kobe.util.CombinedParamBuilderTest;
import com.f.a.kobe.util.CombinedParamCheckUtil;
import com.f.a.kobe.util.CombinedParamCheckor;
import com.f.a.kobe.util.ObjectTransUtils;

@Component
public class LoginCtrlParamCheckor implements ParamCheckHandler{

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> commonCheck(Object obj,String value) {
		try {
			Map<String, String> invoke = (Map<String, String>)this.getClass().getDeclaredMethod(value, Object.class).invoke(this, obj);
			return invoke;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private Map<String, String> authCode(Object t) {
		ParamRequest paramRequest = (ParamRequest)t;
		Map<String, String> checkEmpty = CombinedParamCheckUtil.checkEmpty(paramRequest.getCode(), "authCode",  "请求code不允许为空");
		if(checkEmpty != null) {
			return checkEmpty;
		}
		return null;
	}

	@SuppressWarnings("unused")
	private Map<String, String> binding(Object t) {
		ParamRequest paramRequest = (ParamRequest)t;
		Map<String, String> checkEmpty = CombinedParamCheckUtil.checkEmpty(paramRequest.getMobile(), "molibe",  "联系人电话不得为空");
		if(checkEmpty != null) {
			return checkEmpty;
		}
		CombinedParam combinedParam = new CombinedParam();
		ObjectTransUtils.copy(combinedParam, paramRequest);
		//合法性判断
		CombinedParamCheckUtil checkor = new CombinedParamCheckUtil();
		CombinedParamCheckor combinedParamCheckor = new CombinedParamBuilderTest().setMobile(paramRequest.getMobile()).build();
		checkor.setCombinedParamCheckor(combinedParamCheckor);
		try {
			Map<String, String> checkResult = checkor.check2();
			if(checkResult != null) {
				return checkResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
