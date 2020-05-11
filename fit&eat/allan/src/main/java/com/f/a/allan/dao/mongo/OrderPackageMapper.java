package com.f.a.allan.dao.mongo;

import org.springframework.stereotype.Service;

import com.f.a.allan.entity.pojo.OrderPackage;

@Service
public class OrderPackageMapper extends MongoBaseMapper<OrderPackage> {
	
	public static final String ORDER_PACKAGE_ID = "_id";
	
	public static final String USER_ACCOUNT = "userAccount";
	
	public static final String PACKAGE_STATUS = "packageStatus";
	
	public static final String EXPIRE_TIME = "expireTime";
	
	public static final String PAY_TIME = "payTime";
	
}
