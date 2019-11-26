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
		ParamRequest paramRequest = (ParamRequest)t;
		boolean checkEmpty = CombinedParamCheckUtil.checkEmpty(paramRequest.getMobile(), "molibe",  "联系人电话不得为空");
		if(!checkEmpty) {
			throw new InvaildException("molibe", "联系人电话不得为空");
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

	@Override
	public boolean insertCheck(Object t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateCheck(Object t) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean binding(Object t) {
		// TODO Auto-generated method stub
		return false;
	}

}
