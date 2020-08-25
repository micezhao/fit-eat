package com.fa.kater.biz.auth.third;

import com.fa.kater.pojo.MerchantThirdConfig;

public interface ThirdAuthInterface {
	
	public String getOpenId(MerchantThirdConfig merchantConfig,String thirdAuthId);
	
}
