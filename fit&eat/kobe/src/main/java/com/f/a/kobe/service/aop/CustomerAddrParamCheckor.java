package com.f.a.kobe.service.aop;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.f.a.kobe.pojo.CustomerAddr;
import com.f.a.kobe.pojo.request.LoginRequest;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.util.CombinedParam;
import com.f.a.kobe.util.CombinedParamBuilder;
import com.f.a.kobe.util.CombinedParamCheckUtil;

@Component
public class  CustomerAddrParamCheckor implements ParamCheckHandler{

	@Override
	public Map<String, String> commonCheck(Object obj,String value) {
		CustomerAddr customerAddr = (CustomerAddr)obj;
		customerAddr.setConnectorMobile("15827310817");
		String connectorMobile = customerAddr.getConnectorMobile();
		String addrDetail = customerAddr.getAddrDetail();
		String connectorName = customerAddr.getConnectorName();
		CombinedParamCheckUtil.checkEmpty(connectorMobile, "connectorMobile", "联系人电话不得为空");
		CombinedParamCheckUtil.checkEmpty(addrDetail, "addrDetail", "联系人详细地址不得为空");
		CombinedParamCheckUtil.checkEmpty(connectorName, "connectorName", "联系人姓名不得为空");
		CombinedParam combinedParam = new CombinedParamBuilder().setMobile(customerAddr.getConnectorMobile()).build();
		CombinedParamCheckUtil cutil = new CombinedParamCheckUtil();
		cutil.setCombinedParam(combinedParam);
		try {
			return cutil.check();
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
		
		ParamRequest paramRequest = (ParamRequest)t;
		String mobile = paramRequest.getMobile();
		
		return false;
	}


}
