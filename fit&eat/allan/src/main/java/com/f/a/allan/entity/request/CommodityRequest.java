package com.f.a.allan.entity.request;

import java.util.List;
import java.util.Map;

import com.f.a.allan.entity.pojo.Media;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class CommodityRequest {
	
	@ApiModelProperty(value = "spu编号")
	private String spuId;
	@ApiModelProperty(value = "商品所属商户号")
	private String merchantId;
	@ApiModelProperty(value = "商品名称")
	private String name;
	@ApiModelProperty(value = "简介")
	private String introduce;
	@ApiModelProperty(value = "多媒体资料")
	private List<Media> medias;   
	
	@ApiModelProperty(value = "商品SKU配置项")
	private List<Map<String,String[]>> skus;
	
	/**
	 * 商品分类
	 */
	@ApiModelProperty(value = "商品分类", allowableValues = "virtual,substantial")
	private String category;
	
	/**
	 * 商品应用领域 
	 */
	@ApiModelProperty(value = "商品领域")
	private String domain;
	
	/**
	 * 存储富文本编辑中的内容
	 */
	@ApiModelProperty(value = "商品详情")
	private String detail;
	@ApiModelProperty(value = "商品状态")
	private String status;

	
}
