package com.f.a.kobe.service;

import com.f.a.kobe.manager.CustomerBaseInfoManager;
import com.f.a.kobe.pojo.CustomerBaseInfo;

public class CustomerBaseInfoService {

	CustomerBaseInfoManager customerBaseInfoManager;
	
	public void insertCustomerBaseInfo(CustomerBaseInfo customerBaseInfo) {
		customerBaseInfoManager.insert(customerBaseInfo);
	}
}
