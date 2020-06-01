package com.f.a.allan.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartView {
	
	private String chatId;
	
	private String userAccount;
	
	private String chatMerchant;
	
	private String goodsId;
	
	private String goodsStatus;	
	
	private String goodsName;
	
	private String merchantId;
	
	private String merchantName;
	
	private String category;
	
	private String domain;
	
	private String itemOutline;
	
	private int num;

	private int price;
	
	private int discountPrice;
	
	
}
