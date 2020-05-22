package com.f.a.allan.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderGoodsItemView {
	
	private String goodsId;
	
	private String goodsName;
	
	/**
	 * 商品分类
	 */
	private String category;
	
	private int num;

	/**
	 * 商品概况 json 格式
	 */
	private String itemOutline;
	
	/**
	 * 商品领域 jsonArray 格式
	 */
	private String domain;
	
	
	private String goodsStatus;
	
	/**
	 * 优惠金额 [单位：分]
	 */
	private Integer discountPrice; 
	
	/**
	 * 单价 [单位：分]
	 */
	private Integer price;
	
	/**
	 * 商户号
	 */
	private String merchantId;
	
	private String merchantName;
	
}
	
