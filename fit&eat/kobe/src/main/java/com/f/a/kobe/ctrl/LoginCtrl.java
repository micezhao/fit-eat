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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.kobe.biz.LoginBiz;
import com.f.a.kobe.config.contants.SystemContanst;
import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.pojo.bo.AuthBo;
import com.f.a.kobe.pojo.enums.LoginTypeEnum;
import com.f.a.kobe.pojo.request.LoginRequest;
import com.f.a.kobe.pojo.request.ParamRequest;
import com.f.a.kobe.pojo.view.UserAgent;
import com.f.a.kobe.service.CustomerCredentialService;

@RestController
@RequestMapping("/login")
public class LoginCtrl {

	private final static Logger logger = LoggerFactory.getLogger(LoginCtrl.class);

	@Autowired
	private LoginBiz loginBiz;
	
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
		
		String loginType = request.getLoginType();
		String thirdAuthId = "";
		// 判断当前凭证是否已经存在了，如果存在就不再注册
		if(LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.WECHAT) {
			thirdAuthId = request.getWxOpenid();
		}else if(LoginTypeEnum.getLoginTypeEnum(loginType) == LoginTypeEnum.ALI_PAY){
			thirdAuthId = request.getAliOpenid();
		}
		boolean exsisted = loginBiz.checkExsistedByThirdAuthId(loginType,thirdAuthId);
		if(exsisted) {
			logger.error(" {}渠道 用户凭证 thirdAuthId ：{} 已经存在",loginType,thirdAuthId);
			throw new InvaildException(ErrEnum.REDUPICATE_REGISTER.getErrCode(), ErrEnum.REDUPICATE_REGISTER.getErrMsg());
		}		
		UserAgent userAgent = loginBiz.registerByThirdPart(thirdAuthId, loginType);
		userAgent.setLoginType(loginType);
		session.setAttribute(SystemContanst.USER_AGENT, userAgent); // 将这个用户凭证存入到session中
		return new ResponseEntity<UserAgent>(userAgent,HttpStatus.OK);
	}
	
	/**
	 * 绑定手机号
	 * @return
	 */
	@PutMapping("/binding")
	public ResponseEntity<UserAgent> binding(@RequestBody ParamRequest request,UserAgent userAgent,HttpSession session){
		String  mobile= request.getMobile();
		Long customerId =  userAgent.getCustomerId();
		// TODO 检查手机号合法性
		String loginType = userAgent.getLoginType();
		boolean hasBinded = loginBiz.checkMobileBinded(mobile, customerId);
		if(!hasBinded) { // 如果这个用户没有绑定过手机号，那就直接执行绑定操作
			logger.info("手机号：[{}] 初次绑定",mobile);
			loginBiz.binding(mobile, userAgent.getCustomerId(),loginType); 
		}else { // 更新数据
			logger.info("手机号：[{}] 已绑定过，准备进行合并操作",mobile);
			loginBiz.binding(mobile, userAgent.getCustomerId(),loginType); 
		}
		return new ResponseEntity<UserAgent>(userAgent,HttpStatus.OK);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<Boolean> logout(UserAgent userAgent,HttpSession session){
		if(userAgent == null) {
			throw new InvaildException(ErrEnum.UNLOGIN_ERROR.getErrCode(), ErrEnum.UNLOGIN_ERROR.getErrMsg());
		}
		boolean logouted = false;
		session.setAttribute(SystemContanst.USER_AGENT, null);
		logouted = true;
		logger.info("用户id：[{}]执行登出操作，已清空会话",userAgent.getCustomerId());
		return new ResponseEntity<Boolean>(logouted,HttpStatus.OK);
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
