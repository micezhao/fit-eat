package com.f.a.kobe.biz;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.pojo.CustomerBaseInfo;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.bo.AuthBo;
import com.f.a.kobe.pojo.enums.LoginTypeEnum;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.pojo.view.UserAgent;
import com.f.a.kobe.service.CustomerBaseInfoService;
import com.f.a.kobe.service.CustomerCredentialService;
import com.f.a.kobe.service.CustomerLogService;
import com.f.a.kobe.service.MobileValidateCodeService;

@Service
public class LoginBiz {

	@Autowired
	private Map<String, CustomerCredentialService> map;

	@Autowired
	private CustomerLogService customerLogService;

	@Autowired
	private CustomerBaseInfoService customerBaseInfoService;

	@Autowired
	MobileValidateCodeService mobileValidateCodeService;

	private static final String PREFFIX = "CustomerCredentialService";

	/**
	 * 通过loginType 获取服务实例
	 * 
	 * @param loginType
	 * @return
	 */
	public CustomerCredentialService getServiceInstance(String loginType) {
		if (map.get(loginType + PREFFIX) == null) {
			throw new InvaildException(ErrEnum.NO_INSTANCE.getErrCode(), ErrEnum.NO_INSTANCE.getErrMsg());
		}
		return map.get(loginType + PREFFIX);
	}

	public boolean checkExsistedByThirdAuthId(String loginType, String thirdAuthId) {
		boolean exsisted = false;
		CustomerCredentialService customerCredentialService = getServiceInstance(loginType);
		AuthBo authResult = new AuthBo();
		// 3.判断用户是否进入用户授权信息表
		if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
			authResult.setOpenid(thirdAuthId);
			exsisted = customerCredentialService.existsed(authResult);
		} else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
			authResult.setOpenid(thirdAuthId);
			exsisted = customerCredentialService.existsed(authResult);
		}
		return exsisted;
	}

	// register
	/**
	 * 三方用户注册业务服务（微信支付宝，要求）
	 * 
	 */
//	public String login(ParamRequest request,String loginType) {
//		//1.根据来源获取对应的实例
//		CustomerCredentialService customerCredentialService = getServiceInstance(loginType);
//		//2.获取授权结果
//		AuthResult authResult = customerCredentialService.getAuthInfoByLoginRequest(request);
//		//3.判断用户是否进入用户授权信息表 没有则创建
//		CustomerCredential customerCredential = customerCredentialService.existsed(authResult);
//		if(customerCredential == null) {
//			long id = customerCredentialService.insertCustomerCredential(authResult);
//			customerCredential = customerCredentialService.queryById(id);
//		}
//		//4.记流水
//		customerLogService.recordLogin(loginType, customerCredential);
//		//5.返回登陆凭证
//		return authResult.getAuthToken();
//	}
	
	@Transactional
	public UserAgent registerByThirdPart(String thirdAuthId, String loginType) {
		UserAgent userAgent = new UserAgent(); 
		AuthBo authResult = new AuthBo();
		CustomerCredentialService customerCredentialService = getServiceInstance(loginType);
		// 先要生成一条用户信息
		CustomerBaseInfo customerBaseInfo = new CustomerBaseInfo();
		customerBaseInfoService.insertCustomerBaseInfo(customerBaseInfo);
		// 再根据用户信息生成凭证记录
		authResult.setCustomerId(customerBaseInfo.getCustomerId());
		authResult.setOpenid(thirdAuthId);
		CustomerCredential customerCredential = customerCredentialService.insertCustomerCredential(authResult);
		// 组装userAgent
		userAgent.setCustomerId(customerBaseInfo.getCustomerId());
		if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
			userAgent.setWxOpenid(customerCredential.getWxOpenid());
		} else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
			userAgent.setAliOpenid(customerCredential.getWxOpenid());
		}
		return userAgent;
	}
	
	
	// 检查这个用户的是否绑定了手机号
	public boolean checkMobileBinded(String mobile, Long customerId,String loginType, String  thirdAuthId) {
		CustomerCredentialService customerCredentialService = getServiceInstance(loginType);
		// 如果当前的三方授权码不为空，则在检查绑定状态前，先判断当前操作是否存在重复绑定
		if(StringUtils.isNotBlank(thirdAuthId)) {
			CustomerCredential conditional = new CustomerCredential();
			conditional.setCustomerId(customerId);
			conditional.setMobile(mobile);
			if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
				conditional.setWxOpenid(thirdAuthId);
			} else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
				conditional.setAliOpenid(thirdAuthId);
			}
			List<CustomerCredential> list= customerCredentialService.listCustomerCredential(conditional);
			if(list.isEmpty()) {
				throw new InvaildException(ErrEnum.REDUPICATE_BIND.getErrCode(), ErrEnum.REDUPICATE_BIND.getErrMsg());
			}
		}
		return customerBaseInfoService.hasBinded(customerId, mobile);
	}
	
	
	//绑定手机号
	public CustomerCredential binding(String mobile, Long customerId, String loginType) {
		CustomerBaseInfo currentUser = customerBaseInfoService.query(customerId);
		currentUser.setMobile(mobile);
		customerBaseInfoService.updateCustomer(currentUser);
		CustomerCredentialService credentialService = getServiceInstance(loginType);
		CustomerCredential credential = getServiceInstance(loginType).queryByBizId(currentUser.getCustomerId());
		credential.setMobile(mobile);
		credentialService.updateCustomerCredential(credential);
		return credential;
	}
	
	public CustomerCredential combine(String mobile, Long customerId, String loginType) {
		CustomerCredentialService credentialService = getServiceInstance(loginType);
		CustomerCredential source = credentialService.queryByBizId(customerId);
		CustomerCredential conditional = new CustomerCredential();
		conditional.setMobile(mobile);
		CustomerCredential destine = credentialService.queryCustomerCredentialByConditional(conditional);
		//合并凭证记录
		credentialService.combineCustomerCredential(source, destine, loginType);
		//凭证合并完成后，删除这个新注册的用户信息，绑定后只保留一条用户记录
		CustomerBaseInfo souruceUserRecord = customerBaseInfoService.query(customerId);
		customerBaseInfoService.delete(souruceUserRecord.getId());
		return destine;
	}

	// checkregister
	/**
	 * 在需要已注册才能操作的功能时，弹出注册界面的依据 根据手机号条件判断注册
	 */
	public CustomerCredential checkBindStatus(ParamRequest request, String loginType) {
		CustomerCredential conditional = new CustomerCredential();
		conditional.setMobile(request.getMobile());
		CustomerCredentialService customerCredentialService = getServiceInstance(loginType);
		CustomerCredential customerCredential = customerCredentialService
				.queryCustomerCredentialByConditional(conditional);

		return customerCredential;
	}

	/**
	 * 忘记密码根据手机号找回，修改密码
	 */
	public void findBackPasswordByMobile(ParamRequest request, String loginType) {
		if (!mobileValidateCodeService.checkMobileValidateCode(request.getMobile(), request.getValidateCode())) {
			throw new InvaildException(ErrEnum.VALIDATECODEERROR.getErrCode(), ErrEnum.VALIDATECODEERROR.getErrMsg());
		}
		CustomerCredentialService customerCredentialService = getServiceInstance(loginType);
		CustomerCredential customerCredential = customerCredentialService.queryByBizId(request.getCustomerId());
		customerCredential.setPassword(request.getPassword());
		customerCredentialService.updateCustomerCredential(customerCredential);
	}

}
