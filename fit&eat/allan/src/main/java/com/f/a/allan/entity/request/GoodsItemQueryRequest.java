package com.f.a.allan.entity.request;

import java.util.List;

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
//@ApiModel("商品查询请求对象")
public class GoodsItemQueryRequest extends BaseQueryRequest {
	
//	@ApiModelProperty(value = "商品id",allowEmptyValue = true)
	private String spuId;
	
	private String skuId;

//	@ApiModelProperty(value = "商品名称",allowEmptyValue = true)
	private String spuName;
	
	private String skuName;

//	@ApiModelProperty(value = "商品分类",allowEmptyValue = true )
	private String category;
	
//	@ApiModelProperty(value = "商品分类列表",allowEmptyValue = true )
	private List<String> categoryList;
	
//	@ApiModelProperty(value = "商品状态",allowEmptyValue = true )
	private String status;
	
//	@ApiModelProperty(value = "商品状态列表",allowEmptyValue = true)
	private List<String> statusList;

//	@ApiModelProperty(value = "单价最大值",allowEmptyValue = true)
	private String price_max;
	
//	@ApiModelProperty(value = "单价最小值",allowEmptyValue = true)
	private String price_min;

//	@ApiModelProperty(value = "单价",allowEmptyValue = true)
	private String price;

//	@ApiModelProperty(value = "是否有优惠",allowEmptyValue = true)
	private Boolean hasDiscount;

//	@ApiModelProperty(value = "所属商户号",allowEmptyValue = true)
	private String merchantId;

//	@ApiModelProperty(value = "所属商户名称",allowEmptyValue = true)
	private String merchantName;
}
