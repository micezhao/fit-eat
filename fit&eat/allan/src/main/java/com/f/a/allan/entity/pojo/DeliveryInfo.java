package com.f.a.allan.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * 用于存储订单配送信息的临时对象
 * @author micezhao
 *
 */
public class DeliveryInfo {
	
	// 收货人地址
	private String recevierName;
	// 收货人联系方式
	private String recevierPhone;
	// 收货地址
	private String receiveAddr;
	// 配送地址
	private String deliveryTime;
	// 备注
	private String memo;
	
	
}
