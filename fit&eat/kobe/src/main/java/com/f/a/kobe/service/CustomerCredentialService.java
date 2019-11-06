package com.f.a.kobe.service;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.f.a.kobe.config.credential.WeChatConfigProperties;
import com.f.a.kobe.exceptions.ErrCodeEnum;
import com.f.a.kobe.manager.CustomerCredentialManager;
import com.f.a.kobe.pojo.CustomerCredential;

//改造为实现统一接口
@Service
public class CustomerCredentialService {
	
	@Autowired
	private WeChatConfigProperties weChatConfigProperties;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	CustomerCredentialManager customerCredentialManager;

	//请求第三方(以微信为例)获取取唯一标示和访问key
	public void getSessionKeyAndOpenid(String code) {
		String url = MessageFormat.format(weChatConfigProperties.getUrlPattern(), weChatConfigProperties.getAppId(),weChatConfigProperties.getAppSecret(),code);
		
		/*
		 * String result = restTemplate.getForObject(url, String.class);
		 * 
		 * if(StringUtils.contains(result, weChatConfigProperties.getErrtag())) { throw
		 * new RuntimeException(ErrCodeEnum.REDUPICATE_RECORD.getErrMsg()); }
		 */
		System.out.println(url);
		
	}
	
	//查询用户授权信息列表
	public List<CustomerCredential> listCustomerCredential(CustomerCredential conditional){
		return customerCredentialManager.listByConditional(conditional);
	}
	
	
	public void bindPhoneWithCustomer(Long customerId ,String phone) {
		
	}
	
}
