package com.f.a.kobe.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.manager.CustomerScoreTransManager;
import com.f.a.kobe.mapper.CustomerScoreTransMapper;
import com.f.a.kobe.pojo.CustomerScoreTrans;
import com.f.a.kobe.pojo.CustomerScoreTransExample;
import com.github.pagehelper.Page;

@Service
public class CustomerScoreTransService {
	
	@Autowired
	private CustomerScoreTransManager manager;
	
	@Autowired
	private CustomerScoreTransMapper mapper;
	
	public List<CustomerScoreTrans> listByCustomerId(Long customerId){
		return manager.listByConditional(customerId);
	} 
	
	public List<CustomerScoreTrans> listByCustomerId(Long customerId,Date strartDate,Date endDate){
		CustomerScoreTransExample example = new CustomerScoreTransExample();
		example.createCriteria()
			.andCustomerIdEqualTo(customerId)
			.andCdtGreaterThan(strartDate)
			.andCdtLessThanOrEqualTo(endDate);
		return mapper.selectByExample(example);
	} 	
}
