package com.f.a.allan.entity.request;

import java.util.List;

import com.f.a.allan.entity.request.GoodsItemQueryRequest.GoodsItemQueryRequestBuilder;

import lombok.Builder;
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
	
	private int pageNum = 1;
	
	private String dr;
	
	private String orderBy ;
	
	private String sort;
	
	
}
