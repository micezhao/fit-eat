package com.f.a.allan.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("订单查询请求对象")
public class OrderQueryRequst extends BaseQueryRequest {
	
	//订单包id
	@ApiModelProperty("订单包编号")
	private String orderPackageId;
	
	@ApiModelProperty("用户编号")
	private String userAccount;
	
	@ApiModelProperty("支付单编号")
	private String fundTransferId;
	
	// 订单包状态
	@ApiModelProperty("订单包状态")
	private String packageStatus;
	
	// 子订单id
	@ApiModelProperty("订单编号")
	private String orderId;
	
	// 子订单状态
	@ApiModelProperty("订单状态")
	private String orderStatus;
	
	// 开始时间
	@ApiModelProperty("开始时间")
	private String startTime;
	
	// 结束时间
	@ApiModelProperty("结束时间")
	private String endTime;
	
}
