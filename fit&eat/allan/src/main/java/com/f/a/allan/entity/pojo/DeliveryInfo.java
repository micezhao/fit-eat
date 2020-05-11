package com.f.a.allan.entity.pojo;

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
/**
 * 用于存储订单配送信息的临时对象
 * @author micezhao
 *
 */
public class DeliveryInfo {
	
	private UserAddress userAddress;
	
	// 配送地址
	private String deliveryTime;
	
	// 备注
	private String mome;
	
	
}
