package com.f.a.allan.entity.pojo;

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
 * 多媒体对象
 * @author micezhao
 *
 */
public class Media {
	
	private String mediaId;
	
	private String scpoe;
	
	private String type;
	
	private String resourceUrl;
	
	private String sort;
}
