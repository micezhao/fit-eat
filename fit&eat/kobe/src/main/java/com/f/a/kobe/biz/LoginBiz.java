package com.f.a.kobe.biz;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.pojo.CustomerCredential;
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
	public boolean userExistsed(String loginType,CustomerCredential customerCredential) {
		return getServiceInstance(loginType).existsed(customerCredential);
	}
	
	//就登陆而言，分为第三方登录 与 用户名密码登陆
	
	//login
	/***
	 * 校验登录类型与传递参数的合法性 -- @ParamCheck
	 * 查找用户  -- 根据条件查找用户  根据code返回第三方用户基本信息 
	 * 登陆成功记录登陆流水
	 */
	
	//logout
	/***
	 * 清空session会话
	 * 记录登出流水
	 */
	
	//register
	/**
	 * 更新用户状态
	 */
	
	//checkregister
	/**
	 * 根据手机号条件判断注册
	 */
}
