package com.f.a.allan.entity.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
/**
 * 请求基础类
 * @author micezhao
 *
 */
public class BaseQueryRequest {
	
	private String id;
	
	private int pageSize = 10;
	
	private Long pageNum = 1L;
	
	private String dr;
	
	
}
