package com.fa.kater.entity.requset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysConfigRequest {
	
	/** 数据字典请求字段 开始 **/
	private String pid;
	
	private String group;
	
	private String key;
	
	private String keyName;
	
	private String value;
	
	private String valueName;
	
	private String sort;
	/** 数据字典请求字段 结束 **/
		
}
