package com.f.a.allan.ctrl;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.f.a.allan.pojo.Goods;
import com.f.a.allan.service.impl.GoodsServiceImpl;

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
@RequestMapping("/goods")
public class GoodsController {
	
	@Autowired
	private GoodsServiceImpl goodsService;
	
	@PostMapping
	public ResponseEntity<Goods> add(){
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
	
}

