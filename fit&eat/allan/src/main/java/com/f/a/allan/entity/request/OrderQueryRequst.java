package com.f.a.allan.entity.request;

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
public class OrderQueryRequst extends BaseRequest {
	
	//订单包id
	private String orderPackageId;
	
	private String userAccount;
	
	// 订单包状态
	private String packageStatus;
	
	// 子订单id
	private String orderId;
	
	// 子订单状态
	private String orderStatus;
	
	// 开始时间
	private String startTime;
	
	// 结束时间
	private String endTime;
	
}
