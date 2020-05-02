package com.f.a.kobe.service.previous;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.manager.previous.CustomerCredentialManager;
import com.f.a.kobe.pojo.enums.LoginTypeEnum;
import com.f.a.kobe.pojo.previous.CustomerCredential;

@Service
public class WxCredentialService{
	
	
	@Autowired
	private CustomerCredentialManager manager;
	
	protected String getAuthStringByCode(String code) {
		// TODO Auto-generated method stub
		return "";
	}

	protected CustomerCredential queryCustomerCredential(String authCode) {
		CustomerCredential record = manager.queryByAutCode(authCode, LoginTypeEnum.WECHAT.getLoginTypeCode());
		return record;
	}


}
