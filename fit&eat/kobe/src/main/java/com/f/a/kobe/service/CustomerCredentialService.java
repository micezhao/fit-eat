package com.f.a.kobe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.manager.CustomerCredentialManager;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.bo.AuthResult;
import com.f.a.kobe.pojo.request.ParamRequest;

//改造为实现统一接口
@Service
public abstract class CustomerCredentialService {

	@Autowired
	private CustomerCredentialManager manager;

	// 原子服务

	protected abstract CustomerCredential queryCustomerCredential(String authCode);

	// 查询授权用户
	public CustomerCredential queryCustomerCredentialByConditional(CustomerCredential conditional) {
		List<CustomerCredential> customerCredentialList = manager.listByConditional(conditional);
		if (customerCredentialList.isEmpty()) {
			throw new InvaildException(ErrEnum.CUSTOMER_NOT_FOUND.getErrCode(), ErrEnum.CUSTOMER_NOT_FOUND.getErrMsg());
		}
		if (customerCredentialList.size() > 1) {
			throw new InvaildException(ErrEnum.REDUPICATE_RECORD.getErrCode(),
					"用户" + ErrEnum.REDUPICATE_RECORD.getErrMsg());
		}
		return customerCredentialList.get(0);
	}

	// 用户认证
	public abstract AuthResult getAuthInfoByLoginRequest(ParamRequest requestAuth);

	public abstract CustomerCredential existsed(AuthResult authInfoByLoginRequest);

	public abstract long insertCustomerCredential(AuthResult authInfoByLoginRequest);

	// 合并授权用户
	public void combineCustomerCredential(CustomerCredential source, CustomerCredential destine) {
	}

	// 更新授权用户
	public void updateCustomerCredential(CustomerCredential customerCredential) {
		manager.update(customerCredential);
	}

	public CustomerCredential queryById(long id) {
		return manager.queryById(id);
	}

	public CustomerCredential queryByBizId(long customerId) {
		return manager.queryByBiz(customerId);
	}

	public boolean compareAccessSource(CustomerCredential customerCredential, String loginType) {
		String aliOpenid = customerCredential.getAliOpenid();
		String wxOpenid = customerCredential.getWxOpenid();
		String username = customerCredential.getUsername();
		if("wx".equals(loginType)) {
			if(StringUtils.isEmpty(wxOpenid)) {
				return true;
			}
		}else if("ali".equals(loginType)) {
			if(StringUtils.isEmpty(aliOpenid)) {
				return true;
			}
		}else {
			if(StringUtils.isEmpty(username)) {
				return true;
			}
		}
		
		return false;
	}

	public abstract void registerCustomer(ParamRequest request);

}
