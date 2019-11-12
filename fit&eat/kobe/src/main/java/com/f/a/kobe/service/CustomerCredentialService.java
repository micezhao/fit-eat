package com.f.a.kobe.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.f.a.kobe.pojo.CustomerBaseInfo;
import com.f.a.kobe.pojo.CustomerCredential;

//改造为实现统一接口
@Service
public abstract class CustomerCredentialService {
	
	//获取授权信息，根据code获取存储字符串
	public abstract String getAuthStringByCode(String code);
		
	//新增授权用户
	public abstract void insertCustomerCredential(CustomerCredential customerCredential);
		
	//新增授权用户的基本信息
	public abstract void insertCustomerBaseInfoWithCustomerCredential(CustomerBaseInfo customerBaseInfo,CustomerCredential customerCredential);
	
	//修改授权用户
	public abstract void updateCustomerCredential(CustomerCredential customerCredential);
	
	//绑定手机号完成注册
	public abstract void registerCustomerBaseInfo(CustomerBaseInfo customerBaseInfo,CustomerCredential customerCredential, Object obj);
	
	//查询用户授权信息列表
	public abstract List<CustomerCredential> listCustomerCredential(CustomerCredential conditional);
}
