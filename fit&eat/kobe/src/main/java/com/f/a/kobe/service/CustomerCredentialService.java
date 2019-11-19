package com.f.a.kobe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.manager.CustomerCredentialManager;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.bo.AuthResult;

//改造为实现统一接口
@Service
public abstract class CustomerCredentialService {
	
	@Autowired
	private CustomerCredentialManager manager;
	
	//判断用户是否存在
//	public boolean existsed(AuthResult authInfoByLoginRequest) {
//		 List<CustomerCredential> list= manager.listByConditional(authInfoByLoginRequest);
//		 if(list.isEmpty()) {
//			 return false;
//		 }
//		 if(list.size() > 1) {
//			 throw new InvaildException(ErrEnum.REDUPICATE_RECORD.getErrCode(),"用户凭证"+ErrEnum.REDUPICATE_RECORD.getErrMsg());
//		 }
//		 return true;
//	}
	
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
	
	//用户认证
	public abstract AuthResult getAuthInfoByLoginRequest(Object requestAuth);
	
	public abstract boolean existsed(AuthResult authInfoByLoginRequest);
	
	public abstract void insertCustomerCredential(AuthResult authInfoByLoginRequest);
	
	//新增授权用户
//	public void insertCustomerCredential(AuthResult authInfoByLoginRequest) {
//		manager.insert(authInfoByLoginRequest);
//	}
	
	//更新授权用户
	public void updateCustomerCredential(CustomerCredential customerCredential) {
		manager.update(customerCredential);
	}
	
	//合并授权用户
	public void combineCustomerCredential(CustomerCredential source,CustomerCredential destine) {
	}
	
	//
	
}
