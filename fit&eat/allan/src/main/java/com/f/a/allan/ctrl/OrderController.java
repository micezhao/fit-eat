package com.f.a.allan.ctrl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.f.a.allan.entity.pojo.Order;
import com.f.a.allan.service.impl.OrderServiceImpl;

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
	
	@PostMapping
	public ResponseEntity<Object> add(){
		Order entity = new Order();
		entity.setOrderId("dfdfdfdf");
		entity.setCustomerId("99988");
		entity.setTotal("10000");
		entity.setDiscountPrice("20");
		entity.setSettlementPrice("80");
		impl.save(entity);
		return new ResponseEntity<Object>(entity, HttpStatus.OK);
	}
	
	@GetMapping("/{status}")
	public ResponseEntity<Object> getByStatus(@PathVariable("status") String status){
		return new ResponseEntity<Object>(impl.listByStatus("unpay"), HttpStatus.OK);
	}
	
}

