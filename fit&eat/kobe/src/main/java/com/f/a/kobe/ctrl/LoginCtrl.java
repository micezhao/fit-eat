package com.f.a.kobe.ctrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.kobe.biz.LoginBiz;
import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.pojo.CustomerBaseInfo;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.bo.AuthBo;
import com.f.a.kobe.pojo.enums.LoginTypeEnum;
import com.f.a.kobe.pojo.request.LoginRequest;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.pojo.view.UserAgent;
import com.f.a.kobe.service.CustomerBaseInfoService;
import com.f.a.kobe.service.CustomerCredentialService;

@RestController
public class LoginCtrl {

	private final static Logger logger = LoggerFactory.getLogger(LoginCtrl.class);

	private final static String ATTR_NAME = "userDetail";

	@Autowired
	private LoginBiz loginBiz;
	
	@Autowired
	private CustomerBaseInfoService customerBaseInfoService;
	
	private CustomerCredentialService getServiceInstance(final String loginType) {
		return  loginBiz.getServiceInstance(loginType);
	}
	
	@PostMapping("/getAuthCode")
	public ResponseEntity<AuthBo> getAuthCode(@RequestBody ParamRequest request){
		// 2.获取授权结果
		AuthBo authResult = getServiceInstance(request.getLoginType()).getAuthInfoByLoginRequest(request);
		return new ResponseEntity<AuthBo>(authResult,HttpStatus.OK);
	}
	
	@PostMapping("/registerByThird")
	public ResponseEntity<UserAgent> registerByThird(@RequestBody ParamRequest request, HttpSession session) {
		UserAgent userAgent = new UserAgent();
		String loginType = request.getLoginType();
		boolean exsisted = false;
		AuthBo authResult = new AuthBo();
		CustomerCredentialService customerCredentialService = getServiceInstance(loginType);
		// 3.判断用户是否进入用户授权信息表
		if(LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
			authResult.setOpenid(request.getWxOpenid());
			exsisted=customerCredentialService.existsed(authResult);
		}else if(LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY){
			authResult.setOpenid(request.getAliOpenid());
			exsisted=customerCredentialService.existsed(authResult);
		}
		if(exsisted) {
			throw new InvaildException(ErrEnum.REDUPICATE_REGISTER.getErrCode(), ErrEnum.REDUPICATE_REGISTER.getErrMsg());
		}
		// 先要生成一条用户信息
		CustomerBaseInfo customerBaseInfo = new CustomerBaseInfo();
		customerBaseInfoService.insertCustomerBaseInfo(customerBaseInfo);
		authResult.setCustomerId(customerBaseInfo.getCustomerId());
		CustomerCredential customerCredential= customerCredentialService.insertCustomerCredential(authResult);
		userAgent.setCustomerId(customerBaseInfo.getCustomerId());
		if(LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
			userAgent.setWxOpenid(customerCredential.getWxOpenid());
		}else if(LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
			userAgent.setAliOpenid(customerCredential.getWxOpenid());
		}		
		session.setAttribute(ATTR_NAME, userAgent); // 将这个用户凭证存入到session中
		return new ResponseEntity<UserAgent>(userAgent,HttpStatus.OK);
	}
	
	/**
	 * 绑定手机号
	 * @return
	 */
	@PostMapping("binding")
	public ResponseEntity<UserAgent> binding(@RequestBody ParamRequest request,UserAgent userAgent,HttpSession session){
		String  mobile= request.getMobile();
		Long customerId =  userAgent.getCustomerId();
		// TODO 检查手机号合法性
		String loginType = request.getLoginType();
		CustomerBaseInfo currentUser = customerBaseInfoService.query(customerId);
		if(StringUtils.isBlank(currentUser.getMobile())) { //如果为空，吧手机号写入这个用户信息中，并同步更新凭证表
			
		}else { // 更新数据
			
		}
		return new ResponseEntity<UserAgent>(userAgent,HttpStatus.OK);
	}
	
	
	@PostMapping("/testlogin")
	public ResponseEntity<UserAgent> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserAgent userAgent = new UserAgent();
		if (StringUtils.equals("micezhao", loginRequest.getWxOpenid())
				&& StringUtils.equals("wx", loginRequest.getLoginType())) {
			userAgent = new UserAgent();
			userAgent.setAge(29);
			userAgent.setWxOpenid("micezhao");
			userAgent.setCustomerId(123456L);
			userAgent.setNickname("肿眼的熊test");
			session.setAttribute("userDetail", userAgent);
		}
		return new ResponseEntity<UserAgent>(userAgent, HttpStatus.OK);
	}

}
