package com.f.a.kobe.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.kobe.biz.LoginBiz;

@RestController
public class LoginCtrl {
	
	private final static  Logger logger = LoggerFactory.getLogger(LoginCtrl.class);
	
	@Autowired
	LoginBiz loginBiz;
	
	@GetMapping("/login/{loginType}")
	public String login(@PathVariable("loginType")String loginType,@RequestBody Object request) {
		 return loginBiz.login(request, loginType);
	}
}
