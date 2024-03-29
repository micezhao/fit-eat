package com.f.a.allan.entity.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ToString
/**
 * 货品视图
 * 
 * @author micezhao
 *
 */
public class GoodsItem {
	
	private String goodsId;

	private String goodsName;

	private String spuId;
	
	private String category;
	/**
	 * 商品规格 json 格式
	 */
	private String itemOutline;

	private Integer stock;

	private String goodsStatus;

	/**
	 * 优惠金额 [单位：分]
	 */
	private Integer discountPrice;
	
	private String merchantId;
	
	private String merchantName;

	/**
	 * 单价 [单位：分]
	 */
	private Integer price;
	
}
