package com.f.a.kobe.biz;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.service.CustomerBaseInfoService;
import com.f.a.kobe.service.CustomerCredentialService;
import com.f.a.kobe.service.CustomerLogService;
import com.f.a.kobe.service.MobileValidateCodeService;

@Service
public class LoginBiz {
	
	@Autowired
	private Map<String,CustomerCredentialService> map;
	
	@Autowired
	private CustomerLogService customerLogService;
	
	@Autowired
	private CustomerBaseInfoService customerBaseInfoService;
	
	@Autowired
	MobileValidateCodeService mobileValidateCodeService;
	
	private static final String PREFFIX = "CustomerCredentialService";
	
	/**
	 * 通过loginType 获取服务实例
	 * @param loginType
	 * @return
	 */
	public CustomerCredentialService getServiceInstance(String loginType) {
		if(map.get(loginType+PREFFIX) == null) {
			throw new InvaildException(ErrEnum.NO_INSTANCE.getErrCode(), ErrEnum.NO_INSTANCE.getErrMsg()); 
		}
		return map.get(loginType+PREFFIX);
	}
	

	
	//就登陆而言，分为第三方登录 与 用户名密码登陆
	
	//login
	/***
	 * 校验登录类型与传递参数的合法性 -- @ParamCheck
	 * 登陆类别  1.第三方直接登录  2.app 账号密码 3.app 手机号+验证码登陆
	 * 查找用户  -- 根据条件查找用户  根据code返回第三方用户基本信息 
	 * 登陆成功记录登陆流水
	 * 1.根据来源获取对应的实例
	 * 
	 */
	public String login(ParamRequest request,String loginType) {
		//1.根据来源获取对应的实例
		CustomerCredentialService customerCredentialService = getServiceInstance(loginType);
		//2.获取授权结果
		AuthResult authResult = customerCredentialService.getAuthInfoByLoginRequest(request);
		//3.判断用户是否进入用户授权信息表 没有则创建
		CustomerCredential customerCredential = customerCredentialService.existsed(authResult);
		if(customerCredential == null) {
			long id = customerCredentialService.insertCustomerCredential(authResult);
			customerCredential = customerCredentialService.queryById(id);
		}
		//4.记流水
		customerLogService.recordLogin(loginType, customerCredential);
		//5.返回登陆凭证
		return authResult.getAuthToken();
	}
	
	//logout
	/***
	 * 清空session会话
	 * 记录登出流水
	 */
	public void logout() {
		
	}
	
	//register
	/**
	 * 注册（微信支付宝，要求）
	 * 更新用户状态
	 */
	public void register(ParamRequest request,String loginType) {
		//1.获取服务实例
		CustomerCredentialService customerCredentialService = getServiceInstance(loginType);
		CustomerCredential conditional = new CustomerCredential();
		conditional.setMobile(request.getMobile());
		//根据手机号作为条件查询是否已经有记录
		CustomerCredential sourceCustomer = customerCredentialService.queryCustomerCredentialByConditional(conditional);
		CustomerCredential customerCredential = customerCredentialService.queryByBizId(request.getCustomerId());
		
		//2. 如果手机号之前不存在 则注册
		if(sourceCustomer == null) {
			//2.1 未绑定
			customerCredentialService.registerCustomer(request);
		}else {
			//2.2 已绑定
			boolean accordAccessSource = customerCredentialService.compareAccessSource(customerCredential,loginType);
			if(!accordAccessSource) {
				//2.3.1 不一致 则合并用户
				customerCredentialService.combineCustomerCredential(customerCredential, sourceCustomer ,loginType);
			}else {
				//2.3.2 一致则抛出异常 同一手机号不允许 重复绑定
				throw new InvaildException(ErrEnum.REDUPICATE_REGISTER.getErrCode(),ErrEnum.REDUPICATE_REGISTER.getErrMsg());
			}
		}
	}
	
	//检查这个用户的是否绑定了手机号
	public boolean checkMobileBinded(String mobile,Long customerId ) {
		return customerBaseInfoService.hasBinded(customerId, mobile);
	}
	
	//checkregister
	/**
	 * 在需要已注册才能操作的功能时，弹出注册界面的依据
	 * 根据手机号条件判断注册
	 */
	public CustomerCredential checkRegisterStatus(ParamRequest request,String loginType) {
		CustomerCredential conditional = new CustomerCredential();
		conditional.setMobile(request.getMobile());
		CustomerCredentialService customerCredentialService = getServiceInstance(loginType);
		CustomerCredential customerCredential = customerCredentialService.queryCustomerCredentialByConditional(conditional);
		
		return customerCredential;
	}
	
	/**
	 * 忘记密码根据手机号找回，修改密码
	 */
	public void findBackPasswordByMobile(ParamRequest request,String loginType) {
		if(!mobileValidateCodeService.checkMobileValidateCode(request.getMobile(), request.getValidateCode())) {
			throw new InvaildException(ErrEnum.VALIDATECODEERROR.getErrCode(),ErrEnum.VALIDATECODEERROR.getErrMsg());
		}
		CustomerCredentialService customerCredentialService = getServiceInstance(loginType);
		CustomerCredential customerCredential = customerCredentialService.queryByBizId(request.getCustomerId());
		customerCredential.setPassword(request.getPassword());
		customerCredentialService.updateCustomerCredential(customerCredential);
	}
	
}
