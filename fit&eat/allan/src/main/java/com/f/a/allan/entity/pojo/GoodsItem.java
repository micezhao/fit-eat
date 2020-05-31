package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

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
/**
 * 货品SPU
 * @author micezhao
 *
 */
public class GoodsItem {
	
	/**
	 * 货品编号SKU
	 */
	@Id
	private String goodsId;
	
	private String goodsName;
	
	/**
	 * 商品分类
	 */
	private String category;

	/**
	 * 商品概况 json 格式
	 */
	private String itemOutline;
	
	/**
	 * 商品领域 jsonArray 格式
	 */
	private String domain;
	/**
	 * 商品数量
	 */
	private Integer stock;
	
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
	
	private LocalDateTime cdt;
	
	private LocalDateTime mdt;
}
