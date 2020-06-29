package com.f.a.allan.entity.request.comment;

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
@ApiModel("更新评价请求对象")
public class CommentUpdateRequest {
	
	@ApiModelProperty(value = "评价编号" ,required = true)
	private String commentId;
	
	@ApiModelProperty(value = "评价状态",required = true,allowableValues = "un_verified,verified")
	private String states;
	
	@ApiModelProperty(value = "评价回复",required = false)
	private String replyContnet;
	
}
