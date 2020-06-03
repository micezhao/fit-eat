package com.f.a.allan.entity.request;

import com.alibaba.fastjson.JSONArray;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="订单请求对象")
public class OrderRequset {
	
	@ApiModelProperty(value="购物车编号")
	private String cartId;
	@ApiModelProperty(value="期望的配送时间")
	private String deliveryTime;
	@ApiModelProperty(value="用户地址编号")
	private String userAddressId;
	@ApiModelProperty(value="结算的商品编号与数量集合",example = "[{'goodsId':'12454','num':4}]")
	private JSONArray packItemArr;
	
}
