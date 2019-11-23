package com.f.a.kobe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.manager.CustomerCredentialManager;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.bo.AuthBo;
import com.f.a.kobe.pojo.enums.LoginTypeEnum;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.util.IdWorker;

//改造为实现统一接口
@Service
public abstract class CustomerCredentialService {

	@Autowired
	public CustomerCredentialManager manager;
	
	@Autowired
	public IdWorker idWorker;

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

	/**
	 * 从第三方获取用户的认证信息
	 * @param requestAuth
	 * @return
	 */
	public abstract AuthBo getAuthInfoByLoginRequest(ParamRequest requestAuth);
	
	/**
	 * 判断这个用户凭证是否存在
	 * @param authInfoByLoginRequest
	 * @return
	 */
	public abstract boolean existsed(AuthBo authInfoByLoginRequest);
	
	/**
	 * 将用户凭证存入系统
	 * @param authInfoByLoginRequest
	 * @return
	 */
	public abstract CustomerCredential insertCustomerCredential(AuthBo authBo);

	// 合并授权用户
	public void combineCustomerCredential(CustomerCredential source, CustomerCredential destine,String loginType) {
		//将 source 合并到 destine
		if(LoginTypeEnum.WECHAT.getLoginTypeCode().equalsIgnoreCase(loginType)) {
			destine.setWxOpenid(source.getWxOpenid());
		}else if(LoginTypeEnum.ALI_PAY.getLoginTypeCode().equalsIgnoreCase(loginType)) {
			destine.setAliOpenid(source.getAliOpenid());
		}else if(LoginTypeEnum.APP.getLoginTypeCode().equalsIgnoreCase(loginType)){
			destine.setUsername(source.getUsername());
			destine.setPassword(source.getPassword());
		}else {
			throw new InvaildException(ErrEnum.UNKNOWN_LOGIN_TYPE.getErrCode(),ErrEnum.UNKNOWN_LOGIN_TYPE.getErrMsg());
		}
		this.updateCustomerCredential(destine);
		manager.delete(source.getId());
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
