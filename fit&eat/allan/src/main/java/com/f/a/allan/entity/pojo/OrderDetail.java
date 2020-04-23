package com.f.a.allan.entity.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class OrderDetail {
	/**
	 * 订单id
	 */
	private String orderId;
	/**
	 * 收货人姓名
	 */
	private String recevierName;
	/**
	 * 收货人联系电话
	 */
	private String recevierPhone;
	/**
	 * 收货人地址
	 */
	private String receiveAddr;
	/**
	 * 支付流水号
	 */
	private String payTransNo;
	/**
	 * 支付状态
	 */
	private String payStatus;
	
}
