package com.f.a.kobe.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.manager.CustomerLogManager;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.CustomerLog;
import com.f.a.kobe.pojo.enums.LoginTypeEnum;

@Service
public class CustomerLogService {
	
	private static final int LOGINTYPE = 1;
	
	private static final int LOGOUTTYPE = 2;
	
	@Autowired
	private CustomerLogManager customerLogManager;

	public void recordLogin(String loginType,CustomerCredential customerCredential) {
		LoginTypeEnum loginTypeEnum = LoginTypeEnum.getLoginTypeEnum(loginType);
		if(loginTypeEnum == null) {
			throw new InvaildException(ErrEnum.UNKNOWN_LOGIN_TYPE.getErrCode(),ErrEnum.UNKNOWN_LOGIN_TYPE.getErrMsg());
		}
		
		CustomerLog customerLog = new CustomerLog();
		customerLog.setAuthType(loginTypeEnum.getLoginTypeNum());
		customerLog.setCustomerId(customerCredential.getCustomerId());
		customerLog.setLogChannel(loginTypeEnum.getLoginTypeNum());
		customerLog.setOcurrTime(Calendar.getInstance().getTime());
		customerLog.setOpType(LOGINTYPE);
		
		/**
		 * token
		 * token 过期时间 未存入
		 */
		customerLogManager.insert(customerLog);
	}
}
