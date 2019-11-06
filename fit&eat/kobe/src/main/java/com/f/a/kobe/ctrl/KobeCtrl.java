package com.f.a.kobe.ctrl;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KobeCtrl {
	
	private final static  Logger logger = LoggerFactory.getLogger(KobeCtrl.class);
	

	@Value("${appname}")
	private String appname;

	@GetMapping("/kobe/getapp")
	public String getAppname(HttpSession session) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(appname);
		UUID uuid =(UUID)session.getAttribute("uuid");
		if(uuid != null) {
			buffer.append("|session:").append(uuid.toString());
		}
		return buffer.toString();
	}

	@GetMapping("/uid")
	public String testUid(HttpSession session) {
		UUID uuid = (UUID) session.getAttribute("uuid");
		if (uuid == null) {
			uuid = UUID.randomUUID();
			session.setAttribute("uuid", uuid);
		}
		return session.getId() + ":[UUID]" + uuid;
	}
	
	
}
