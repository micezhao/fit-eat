package com.f.a.kobe.service;

import org.springframework.stereotype.Service;

import com.f.a.kobe.pojo.CustomerAddr;
import com.f.a.kobe.service.aop.ParamCheck;

@Service
public class CustomerAddrService {

	@ParamCheck("insertAddr")
	public void insertAddr(CustomerAddr customerAddr) {
		
	}
}
