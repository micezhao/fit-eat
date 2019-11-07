package com.f.a.kobe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.manager.CustomerAddrManager;
import com.f.a.kobe.pojo.CustomerAddr;
import com.f.a.kobe.service.aop.ParamCheck;

@Service
public class CustomerAddrService {
	
	@Autowired
	private CustomerAddrManager manager;
	
	
	public void insertAddr(CustomerAddr customerAddr) {
		manager.insert(customerAddr);
	}
	
	public void deleteAddr(Long id) {
		manager.delete(id);
	}
	
	public void setDefaultAddr() {
		
	}
}
