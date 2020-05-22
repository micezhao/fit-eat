package com.f.a.allan.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商品请求对象")
public class GoodsItemRequest {
	
	@ApiModelProperty(value="商品id")
	private String goodsId;
	
	@ApiModelProperty(value="商品名称")
	private String goodsName;

	/**
	 * 商品分类
	 */
	@ApiModelProperty(value="商品分类")
	private String category;

	/**
	 * 商品概况 json 格式
	 */
	@ApiModelProperty(value="商品概况")
	private String itemOutline;

	/**
	 * 商品领域 jsonArray 格式
	 */
	@ApiModelProperty(value="所属领域")
	private String domain;
	
	/**
	 * 商品库存
	 */
	@ApiModelProperty(value="当前库存")
	private Integer stock;
	
	@ApiModelProperty(value="商品状态")
	private String goodsStatus;

	/**
	 * 优惠金额
	 */
	@ApiModelProperty(value="优惠金额")
	private Integer discountPrice;

	/**
	 * 单价
	 */
	@ApiModelProperty(value="单价")
	private Integer price;

	/**
	 * 商户号
	 */
	@ApiModelProperty(value="所属商户号")
	private String merchantId;
	
	@ApiModelProperty(value="所属商户名称")
	private String merchantName;

}
