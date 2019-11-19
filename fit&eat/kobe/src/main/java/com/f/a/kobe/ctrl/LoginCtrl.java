package com.f.a.kobe.ctrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginCtrl {
	
	private final static  Logger logger = LoggerFactory.getLogger(LoginCtrl.class);
	
	@GetMapping("/getSequence")
	public String getSequence() {
		 return "";
	}
}
