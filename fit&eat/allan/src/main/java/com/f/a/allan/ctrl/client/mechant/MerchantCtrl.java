package com.f.a.allan.ctrl.client.mechant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.allan.biz.MerchantBiz;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(tags = "client-商户功能")
@RestController
@RequestMapping("/merchant")
public class MerchantCtrl {
	
	@Autowired
	private MerchantBiz biz;
	
	
	
	
	
}
