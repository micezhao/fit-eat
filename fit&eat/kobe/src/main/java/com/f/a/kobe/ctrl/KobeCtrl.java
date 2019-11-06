package com.f.a.kobe.ctrl;

import java.util.UUID;

import javax.servlet.http.HttpSession;

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
