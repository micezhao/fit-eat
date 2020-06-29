package com.f.a.allan.entity.request.comment;

import java.util.List;

import com.f.a.allan.entity.bo.MediaAttachment;

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
@ApiModel("新增评价请求对象")
public class CommentAddRequest {
	
	@ApiModelProperty(value = "spu编码" ,required = true)
	private String spuId;
	
	@ApiModelProperty(value = "sku编码",required = true)
	private String goodsId;
	
	@ApiModelProperty(value = "商品评分" ,required = true,allowableValues ="range(1-5)")
	private String stars;
	
	@ApiModelProperty(value = "商品评价",required = true)
	private String content;
	
	@ApiModelProperty(value = "多媒体附件",required = false)
	private List<MediaAttachment> attachments; 
	
}
