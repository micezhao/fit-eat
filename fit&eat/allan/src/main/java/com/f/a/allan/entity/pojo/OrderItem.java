package com.f.a.allan.entity.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 订单商品表
 * mongo 对象
 */
@Data
@Document
public class OrderItem {
	
	private String orderId;
	
	private String goodsId;
	
	private String goodsName;
	
	private String imgUrl;
	
	private String payPrice;
	
	private String count;
	
	private String subTotal;
	
	private String merchantNo;
	
}
