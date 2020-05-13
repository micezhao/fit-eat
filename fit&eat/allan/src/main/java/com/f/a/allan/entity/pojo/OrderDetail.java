package com.f.a.allan.entity.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("orderDetail")
public class OrderDetail {
	
	@Id
	private String id;
	
	private String orderId;
	
	private String recevierName;
	
	private String recevierPhone;
	
	private String receiveAddr;
	
	private String goodsName;
	
	// 期望送货时间
	private String expectDeliveryTime;
	
	private String memo;
	
	private String imgUrl;
	
	// 支付的订单号
	private String fundTransferId;
	
	
}
