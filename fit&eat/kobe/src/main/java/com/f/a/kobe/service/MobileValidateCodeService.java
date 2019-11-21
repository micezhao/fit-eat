package com.f.a.kobe.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MobileValidateCodeService {
	
	private static final int EXPIRE_TIME = 100;
	
	@Autowired
	RedisTemplate<String,String> redisTemplate;
	
	public void sendMobileValidateCode(String mobile) {
		String validateCode = "891122";
		redisTemplate.opsForValue().set(mobile, validateCode);
		redisTemplate.expire(mobile, EXPIRE_TIME, TimeUnit.SECONDS);
	}
	
	public boolean checkMobileValidateCode(String mobile,String validateCode) {
		String correctValidateCode = redisTemplate.opsForValue().get(mobile);
		if(correctValidateCode == null || !correctValidateCode.equals(validateCode)) {
			return false;
		}
		return true;
	}
	
}
