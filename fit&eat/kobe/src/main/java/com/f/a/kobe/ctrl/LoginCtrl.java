package com.f.a.kobe.ctrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.kobe.biz.LoginBiz;
import com.f.a.kobe.contants.Contants;
import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.ErrRtn;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.bo.AuthBo;
import com.f.a.kobe.pojo.enums.LoginTypeEnum;
import com.f.a.kobe.pojo.request.LoginRequest;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.service.CustomerCredentialService;
import com.f.a.kobe.service.aop.ParamCheck;
import com.f.a.kobe.view.UserAgent;

@RestController
@RequestMapping("/login")
public class LoginCtrl {

	private final static Logger logger = LoggerFactory.getLogger(LoginCtrl.class);

	@Autowired
	private LoginBiz loginBiz;

	private CustomerCredentialService getServiceInstance(final String loginType) {
		return loginBiz.getServiceInstance(loginType);
	}

	@ParamCheck("authCode")
	@PostMapping("/getAuthCode")
	public ResponseEntity<AuthBo> getAuthCode(@RequestBody ParamRequest request) {
		//2.获取授权结果
		AuthBo authResult = getServiceInstance(request.getLoginType()).getAuthInfoByLoginRequest(request);
		return new ResponseEntity<AuthBo>(authResult, HttpStatus.OK);
	}
	
	@GetMapping("/thirdPart/{loginType}/{thirdAuthId}")
	public ResponseEntity<Object>  login(@PathVariable("loginType") String loginType,@PathVariable("thirdAuthId") String thirdAuthId,HttpSession session){
		try {
			UserAgent userAgent = loginBiz.generateUserAgent(loginType, thirdAuthId);
			session.setAttribute(Contants.USER_AGENT, userAgent);
		}catch (InvaildException ex) {
			return new ResponseEntity<Object>(
					new ErrRtn(ex.getErrCode(), ex.getErrMsg()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
 		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@PostMapping("/registerByThird")
	public ResponseEntity<Object> registerByThird(@RequestBody ParamRequest request, HttpSession session) {

		String loginType = request.getLoginType();
		String thirdAuthId = "";
		// 判断当前凭证是否已经存在了，如果存在就不再注册
		if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
			thirdAuthId = request.getWxOpenid();
		} else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
			thirdAuthId = request.getAliOpenid();
		}
		boolean exsisted = loginBiz.checkExsistedByThirdAuthId(loginType, thirdAuthId);
		if (exsisted) {
			logger.error(" {}渠道 用户凭证 thirdAuthId ：{} 已经存在", loginType, thirdAuthId);
			return new ResponseEntity<Object>(
					new ErrRtn(ErrEnum.REDUPICATE_REGISTER.getErrCode(), ErrEnum.REDUPICATE_REGISTER.getErrMsg()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		UserAgent userAgent = loginBiz.registerByThirdPart(thirdAuthId, loginType);
		userAgent.setLoginType(loginType);
		session.setAttribute(Contants.USER_AGENT, userAgent); // 将这个用户凭证存入到session中
		
		return new ResponseEntity<Object>(userAgent,HttpStatus.OK);
	}
	
	@PostMapping("/registerTest")
	public ResponseEntity<Object> registerTest(@RequestBody ParamRequest request, HttpSession session) {

		String loginType = request.getLoginType();
		String thirdAuthId = "";
		// 判断当前凭证是否已经存在了，如果存在就不再注册
		if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
			thirdAuthId = request.getWxOpenid();
		} else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
			thirdAuthId = request.getAliOpenid();
		}
		boolean exsisted = loginBiz.checkExsistedByThirdAuthId(loginType, thirdAuthId);
		if (exsisted) {
			logger.error(" {}渠道 用户凭证 thirdAuthId ：{} 已经存在", loginType, thirdAuthId);
			return new ResponseEntity<Object>(
					new ErrRtn(ErrEnum.REDUPICATE_REGISTER.getErrCode(), ErrEnum.REDUPICATE_REGISTER.getErrMsg()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		UserAgent userAgent = loginBiz.registerByThirdPart(thirdAuthId, loginType);
		userAgent.setLoginType(loginType);
		session.setAttribute(Contants.USER_AGENT, userAgent); // 将这个用户凭证存入到session中
//		MultiValueMap<String, String> headers = 
		HttpHeaders headers = new HttpHeaders();
		headers.set("sessionId", session.getId());
		ResponseEntity<Object> response = new ResponseEntity<Object>(headers, HttpStatus.OK);
		return response;
	}

	/**
	 * 绑定手机号
	 * 
	 * @return
	 */
	@ParamCheck("binding")
	@PostMapping("/binding")
	public ResponseEntity<Object> binding(@RequestBody ParamRequest request, UserAgent userAgent, HttpSession session) {
		String mobile = request.getMobile();
		Long customerId = userAgent.getCustomerId();
		// TODO 检查手机号合法性
		String loginType = userAgent.getLoginType();
		String thirdAuthId = "";
		boolean hasBinded = false;
		if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT
				&& StringUtils.isNotBlank(userAgent.getWxOpenid())) {
			thirdAuthId = userAgent.getWxOpenid();
		} else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY
				&& StringUtils.isNotBlank(userAgent.getAliOpenid())) {
			thirdAuthId = userAgent.getAliOpenid();
		}
		try {
			hasBinded = loginBiz.checkMobileBinded(mobile, customerId, loginType, thirdAuthId);
		}catch (InvaildException ex) {
			ErrRtn errRtn = new ErrRtn(ex.getErrCode(),ex.getErrMsg());
			logger.info("当前手机号：{} 在已在{}渠道完成绑定，请勿重复绑定", mobile,loginType);
			return new ResponseEntity<Object>(errRtn, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		CustomerCredential userCredential = new CustomerCredential();
		if (!hasBinded) { // 如果这个用户没有绑定过手机号，那就直接执行绑定操作
			logger.info("手机号：[{}] 初次绑定", mobile);
			userCredential = loginBiz.binding(mobile, userAgent.getCustomerId(), loginType);
		} else { // 更新数据
			logger.info("手机号：[{}] 已绑定过，准备进行合并操作", mobile);
			userCredential = loginBiz.combine(mobile, userAgent.getCustomerId(), loginType);
		}
		userAgent.setMobile(userCredential.getMobile());
		if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
			userAgent.setWxOpenid(userCredential.getWxOpenid());
		} else if (LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY) {
			userAgent.setAliOpenid(userCredential.getAliOpenid());
		}
		session.setAttribute(Contants.USER_AGENT, userAgent); // 绑定后更新手机号
		return new ResponseEntity<Object>(userAgent, HttpStatus.OK);
	}

	@PostMapping("/logout")
	public ResponseEntity<Boolean> logout(UserAgent userAgent, HttpSession session) {
		if (userAgent == null) {
			throw new InvaildException(ErrEnum.UNLOGIN_ERROR.getErrCode(), ErrEnum.UNLOGIN_ERROR.getErrMsg());
		}
		boolean logouted = false;
		session.setAttribute(Contants.USER_AGENT, null);
		logouted = true;
		logger.info("用户id：[{}]执行登出操作，已清空会话", userAgent.getCustomerId());
		return new ResponseEntity<Boolean>(logouted, HttpStatus.OK);
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
