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
public class GoodsItem {
	
	/**
	 * 商品编号
	 */
	private String goodsId;
	
	/**
	 * 商品分类
	 */
	private String category;

	/**
	 * 商品概况 json 格式
	 */
	private String itemOutline;
	/**
	 * 商品数量
	 */
	private Integer num;
	
	/**
	 * 优惠金额
	 */
	private String discountPrice; 
	
	/**
	 * 单价
	 */
	private String price;
	
	/**
	 * 商户号
	 */
	private String merchantId;
	
	/**
	 * 名称
	 */
	private String merchantName;
}
