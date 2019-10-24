package com.f.a.kobe.ctrl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KobeCtrl {

	@Value("${appname}")
	private String appname;
	
	@GetMapping("/kobe/getapp")
	public String getAppname() {
		return appname;
	}
}
