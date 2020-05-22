package com.f.a.allan.ctrl.client;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/chat")
@Api(tags = "client-客户端购物车")

public class ChatCtrl {
	
	@ApiOperation("加入购物车")
	@PostMapping("/join")
	public ResponseEntity<Object> joinChat() {
		return new ResponseEntity<Object>(null, HttpStatus.OK);
	}
	
	
	
}
