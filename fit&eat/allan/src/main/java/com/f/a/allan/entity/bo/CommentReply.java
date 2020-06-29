package com.f.a.allan.entity.bo;

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
 * 商户对评价的回复
 * @author micezhao
 *
 */
public class CommentReply {
	
	private String commentId;
	
	private String merchantId;
	
	private String content;
	
	private String states;
	
	private String cdt;
	
}
