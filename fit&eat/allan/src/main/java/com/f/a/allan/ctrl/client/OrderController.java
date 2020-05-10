package com.f.a.allan.ctrl.client;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.kobe.view.UserAgent;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author micezhao
 * @since 2020-05-06
 */
@RestController
@RequestMapping("/order")
public class OrderController {
	
	/**
	 * 接受提交的商品列表，并将其组装成订单包
	 * @param goodItemList
	 * @param userAgent
	 * @return
	 */
	@PostMapping
	public OrderPackage packGoodItem( HttpServletRequest request,UserAgent userAgent) {
		
		return null;
	}
	
	@GetMapping("/${orderPackageId}")
	public void payOrder(@PathVariable("orderPackageId") String orderPackageId) {
		

		
	}
	
}

