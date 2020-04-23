package com.f.a.allan.entity.bo;

import com.f.a.allan.entity.pojo.Dict;

import lombok.Data;

@Data
public class DictBo extends Dict{
	

	private int pageSize ;
	
	private Long pageNum ;
	
	private String orderBy;
}
