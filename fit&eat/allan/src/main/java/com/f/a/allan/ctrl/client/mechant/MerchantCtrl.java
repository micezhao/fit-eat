package com.f.a.allan.ctrl.client.mechant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.allan.biz.MerchantBiz;

@RestController
@RequestMapping("/merchant")
public class MerchantCtrl {
	
	@Autowired
	private MerchantBiz biz;
	
	@PutMapping("{id}")
	public boolean suspense(@PathVariable("id") String id) {
		biz.operateById(id, "suspension");
		return true;
	}
	
	
}
