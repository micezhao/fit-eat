package com.f.a.allan.ctrl;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.f.a.allan.service.impl.OrderServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author micezhao
 * @since 2019-12-09
 */
@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private OrderServiceImpl impl ;
	
	@GetMapping
	public ResponseEntity<Object> get(){
		
		return new ResponseEntity<Object>(impl.list(), HttpStatus.OK);
	}
	
	
}

