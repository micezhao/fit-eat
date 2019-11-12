package com.f.a.kobe.biz;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.service.CustomerCredentialService;

@Service
public class LoginBiz {
	
	@Autowired
	private Map<String,CustomerCredentialService> map;
	
	private static final String PREFFIX = "CredentialService";
	
	/**
	 * 通过loginType 获取服务实例
	 * @param loginType
	 * @return
	 */
	private CustomerCredentialService getServiceInstance(String loginType) {
		return map.get(loginType+PREFFIX);
	}
	
	/**
	 * 通过具体的实现类来判断这个用户是否存在
	 * @param loginType 登陆类型
	 * @return
	 */
	public boolean userExistsed(String loginType) {
		
		return getServiceInstance(loginType).existsed(customerCredential);
	}

	
	
}
