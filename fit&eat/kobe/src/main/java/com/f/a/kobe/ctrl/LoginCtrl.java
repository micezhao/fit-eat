package com.f.a.kobe.ctrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.f.a.kobe.pojo.request.LoginRequest;
import com.f.a.kobe.pojo.view.UserAgent;

@RestController
public class LoginCtrl {
	
	private final static  Logger logger = LoggerFactory.getLogger(LoginCtrl.class);
	
	@GetMapping("/getSequence")
	public String getSequence() {
		 return "aaaaa";
	}
	
	
	@PostMapping("/testlogin")
	public ResponseEntity<UserAgent> login(@RequestBody LoginRequest loginRequest,HttpServletRequest request) {
		String str = request.getHeader("content-type");
		HttpSession session = request.getSession();
		UserAgent userAgent = new UserAgent();
		if(StringUtils.equals("micezhao", loginRequest.getWxOpenid()) && StringUtils.equals("wx", loginRequest.getLoginType())) {
			userAgent = new UserAgent();
			userAgent.setAge(29);
			userAgent.setWxOpenid("micezhao");
			userAgent.setCustomerId(123456L);
			userAgent.setNickname("肿眼的熊test");
			session.setAttribute("userDetail", userAgent);
		}
		return new ResponseEntity<UserAgent>(userAgent,HttpStatus.OK);
	}

}
