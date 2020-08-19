package com.fa.kater.ctrl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.kobe.contants.Contants;
import com.f.a.kobe.view.UserAgent;
import com.fa.kater.annotations.AccessLogAnnot;
import com.fa.kater.annotations.AccessLogAnnot.AccessLogType;
import com.fa.kater.biz.auth.LoginBizInterface;
import com.fa.kater.entity.requset.LoginParam;
import com.fa.kater.exceptions.ErrEnum;
import com.fa.kater.exceptions.InvaildException;

@RestController
@RequestMapping("login")
public class LoginCtrl {

	@Autowired
	private Map<String, LoginBizInterface> loginBizMap;

	private static final Logger logger = LoggerFactory.getLogger(LoginCtrl.class);

	// 第三方登录
	@GetMapping("thirdPart/{agentId}/{loginType}/{thirdAuthId}/{authType}")
	public ResponseEntity<Object> login(@PathVariable(value = "agentId") String agentId,
			@PathVariable(value = "loginType") String loginType,
			@PathVariable(value = "thirdAuthId") String thirdAuthId, @PathVariable(value = "authType") String authType,
			HttpSession session) {
		UserAgent userAgent = null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 生成UserAgent
		LoginParam param = LoginParam.builder().loginType(loginType).authType(authType).thirdAuthId(thirdAuthId)
				.agentId(agentId).build();

		LoginBizInterface loginBiz = loginBizMap.get("thirdLoginBiz");
		userAgent = loginBiz.login(param);
		// 存入redis
		session.setAttribute(Contants.USER_AGENT, userAgent);
		logger.debug("当前sessionId:{}", session.getId());
		resultMap.put("X-AUTH-TOKEN", session.getId());
		// TODO 是否绑定了手机号 的实现逻辑
		resultMap.put("hasBinded", userAgent.getMobile() == null ? false : true);
		return new ResponseEntity<Object>(resultMap, HttpStatus.OK);
	}

	
	@GetMapping("logout")
	@AccessLogAnnot(logType = AccessLogType.LOG_OUT)
	public ResponseEntity<Object> logout(HttpSession session,UserAgent userAgent){
		logger.info("用户:{}登出",userAgent.getUserAccount());
		session.setAttribute(Contants.USER_AGENT,null);
		session.invalidate();
		return new ResponseEntity<Object>(true,HttpStatus.OK);
	}
	
	@GetMapping("/test")
	public String test(UserAgent userAgent) {
		if (userAgent == null) {
			throw new InvaildException(ErrEnum.UNLOGIN_ERROR.getErrCode(), ErrEnum.UNLOGIN_ERROR.getErrMsg());
		}
		return userAgent.getUserAccount();
	}

}
