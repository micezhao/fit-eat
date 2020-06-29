package com.f.a.allan.entity.request.comment;

import java.util.List;

import com.f.a.allan.entity.bo.MediaAttachment;
import com.f.a.allan.entity.request.comment.CommentAddRequest.CommentAddRequestBuilder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("查询评价请求对象")
public class CommentQueryRequest {
	
	@ApiModelProperty(value = "用户编号" ,required = false)
	private String userAccount;
	
	@ApiModelProperty(value = "商户编号" ,required = false)
	private String merchantId;
	
	@ApiModelProperty(value = "spu编码" ,required = false)
	private String spuId;
	
	@ApiModelProperty(value = "sku编码",required = false)
	private String goodsId;
	
	@ApiModelProperty(value = "评价状态" ,required = false)
	private String states;
	
	@ApiModelProperty(value = "有图评价" ,required = false)
	private String hasAttachment;
	
	@ApiModelProperty(value = "商品评分范围左边界" ,required = false)
	private String starsBegin;
	
	@ApiModelProperty(value = "商品评分范围右边界" ,required = false)
	private String starsEnd;
	
	@ApiModelProperty(value = "当前页" ,required = false)
	private Integer pageNum = 1;
	
	@ApiModelProperty(value = "每页条数" ,required = false)
	private Integer pageSize = 10;
	
	@ApiModelProperty(value = "排序规则" ,required = false)
	private int ordeyBy ;
}
