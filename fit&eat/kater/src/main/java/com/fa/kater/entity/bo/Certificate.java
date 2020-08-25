package com.fa.kater.entity.bo;

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
