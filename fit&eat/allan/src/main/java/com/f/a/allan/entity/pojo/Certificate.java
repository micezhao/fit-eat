package com.f.a.allan.entity.pojo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
/**
 * 证书类
 * @author micezhao
 *
 */
public class Certificate {
	
	private String certType;
	
	private String certUrl;
}
