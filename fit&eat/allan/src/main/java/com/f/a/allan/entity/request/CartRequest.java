package com.f.a.allan.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("购物车请求对象")
public class CartRequest {
	
	@ApiModelProperty("购物车编号")
	private String chatId;
	
	@ApiModelProperty("购物车所属的商户编号")
	private String cartMerchantId;
	
	@ApiModelProperty(value="商品编号")
	private String goodsId;
	
	@ApiModelProperty(value= "商品数量")
	private int num;
	
}
