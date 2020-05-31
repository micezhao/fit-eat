package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.f.a.allan.entity.pojo.GoodsItem.GoodsItemBuilder;

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
@Document("goods")
/**
 * 货品SPU
 * @author micezhao
 *
 */
public class Goods {

	@Id
	private String goodsId;
	
	private String goodsName;

	private String spuId;

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

	/**
	 * 单价 [单位：分]
	 */
	private Integer price;

	private LocalDateTime cdt;

	private LocalDateTime mdt;

}
