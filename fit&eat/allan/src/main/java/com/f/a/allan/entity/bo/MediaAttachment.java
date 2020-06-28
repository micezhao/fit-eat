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
 * 多媒体附件对象
 * @author micezhao
 *
 */
public class MediaAttachment {
	
	// 资源类型
	private String type;
	
	// 资源相对路径
	private String uri;
	
}
