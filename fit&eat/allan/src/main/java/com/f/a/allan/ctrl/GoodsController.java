package com.f.a.allan.ctrl;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.f.a.allan.entity.pojo.Goods;
import com.f.a.allan.service.impl.GoodsServiceImpl;
import com.f.a.kobe.view.UserAgent;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author micezhao
 * @since 2019-12-09
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
	
	private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);
	
	@Autowired
	private GoodsServiceImpl goodsService;
	
	@PostMapping
	public ResponseEntity<Goods> add(UserAgent userAgent){
		logger.info("登陆用户信息："+userAgent);
		Goods goods = new Goods();
		goods.setGoodsId("adb");
		goods.setName("ccc");
		goods.setMerchantNo("123");
		goods.setPrice("500");
		goods.setStatus("1");
		goods.setType("a");
		 goodsService.save(goods);
		return new ResponseEntity<Goods>(goods, HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<Object> get(UserAgent userAgent){
		logger.info("登陆用户信息："+userAgent);
		List<Goods> list	=  goodsService.list();
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}
	
}

