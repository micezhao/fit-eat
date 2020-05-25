package com.f.a.allan.entity.request;

import java.util.Map;

import lombok.Data;

@Data
public class OrderRequset {
	
	private String chatId;
	
	private String deliveryTime;
	
	private String userAddressId;
	
	private Map[] packItemMapArr;
	
}
