package com.f.a.kobe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.manager.CustomerCredentialManager;
import com.f.a.kobe.pojo.CustomerCredential;

//改造为实现统一接口
@Service
public abstract class CustomerCredentialService {
	
	@Autowired
	private CustomerCredentialManager manager;
	
	//判断用户是否存在
	public boolean existsed(CustomerCredential customerCredential) {
		 List<CustomerCredential> list= manager.listByConditional(customerCredential);
		 if(list.isEmpty()) {
			 return false;
		 }
		 if(list.size() > 1) {
			 throw new InvaildException(ErrEnum.REDUPICATE_RECORD.getErrCode(),"用户凭证"+ErrEnum.REDUPICATE_RECORD.getErrMsg());
		 }
		 return true;
	}
	
	//原子服务
	
	//获取授权信息，根据code获取存储字符串
	protected abstract String getAuthStringByCode(String code);
	
	
	protected abstract CustomerCredential queryCustomerCredential(String authCode) ;
	
	//查询授权用户
	public CustomerCredential queryCustomerCredentialByConditional(CustomerCredential conditional) {
		List<CustomerCredential> customerCredentialList = manager.listByConditional(conditional);
		 if(customerCredentialList.isEmpty()) {
			 throw new InvaildException(ErrEnum.CUSTOMER_NOT_FOUND.getErrCode(),ErrEnum.CUSTOMER_NOT_FOUND.getErrMsg());
		 }
		 if(customerCredentialList.size() > 1) {
			 throw new InvaildException(ErrEnum.REDUPICATE_RECORD.getErrCode(),"用户"+ErrEnum.REDUPICATE_RECORD.getErrMsg());
		 }
		return customerCredentialList.get(0);
	}
	
	//新增授权用户
	public void insertCustomerCredential(CustomerCredential customerCredential) {
		manager.insert(customerCredential);
	}
	
	//更新授权用户
	public void updateCustomerCredential(CustomerCredential customerCredential) {
		manager.update(customerCredential);
	}
	
	//合并授权用户
	public void combineCustomerCredential(CustomerCredential source,CustomerCredential destine) {
	}
	
	//
	
}
