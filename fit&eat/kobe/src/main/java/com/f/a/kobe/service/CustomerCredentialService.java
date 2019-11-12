package com.f.a.kobe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.exceptions.ErrCodeEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.manager.CustomerCredentialManager;
import com.f.a.kobe.pojo.CustomerCredential;

//改造为实现统一接口
@Service
public abstract class CustomerCredentialService {
	
	@Autowired
	private CustomerCredentialManager manager;
	
	//获取授权信息，根据code获取存储字符串
	protected abstract String getAuthStringByCode(String code);
		
	//新增授权用户
	public  void insertCustomerCredential(CustomerCredential customerCredential) {
		manager.insert(customerCredential);
	}
	
	protected abstract  CustomerCredential queryCustomerCredential(String authCode) ;
	
	//判断用户是否存在
	public boolean  existsed(CustomerCredential customerCredential) {
		 List<CustomerCredential> list= manager.listByConditional(customerCredential);
		 if(list.isEmpty()) {
			 return false;
		 }
		 if(list.size() > 1) {
			 throw new InvaildException(ErrCodeEnum.REDUPICATE_RECORD.getErrCode(),"用户凭证"+ErrCodeEnum.REDUPICATE_RECORD.getErrMsg());
		 }
		 return true;
	}
	
	
	
		
	
}
