package com.fa.kater.ctrl;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.kobe.contants.Contants;
import com.f.a.kobe.view.UserAgent;
import com.fa.kater.biz.auth.LoginBizInterface;
import com.fa.kater.exceptions.ErrEnum;
import com.fa.kater.exceptions.InvaildException;

@RestController
@RequestMapping("logout")
public class LoginOutCtrl {
	
	@Autowired
	private Map<String,LoginBizInterface>  loginBizMap;

	private static final Logger logger = LoggerFactory.getLogger(LoginCtrl.class);
	
	
	@PostMapping("/third")
	public ResponseEntity<Boolean> logout(UserAgent userAgent, HttpSession session) {
		if (userAgent == null) {
			throw new InvaildException(ErrEnum.UNLOGIN_ERROR.getErrCode(), ErrEnum.UNLOGIN_ERROR.getErrMsg());
		}
		boolean logouted = false;
		loginBizMap.get("thirdLoginBiz").logout(userAgent);
		session.setAttribute(Contants.USER_AGENT, null);
		session.invalidate();
		logouted = true;
		logger.info("用户id：[{}]执行登出操作，已清空会话", userAgent.getUserAccount());
		return new ResponseEntity<Boolean>(logouted, HttpStatus.OK);
	}
	
}	
