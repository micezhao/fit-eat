package com.fa.kater.biz.auth.third;

import org.springframework.stereotype.Service;

import com.fa.kater.pojo.MerchantThirdConfig;

@Service
public class AlipayHandler implements ThirdAuthInterface{

	@Override
	public String getOpenId(MerchantThirdConfig merchantConfig, String thirdAuthId) {
		// TODO Auto-generated method stub
		return "8dfaedffdkjkjf";
	}
	
}
