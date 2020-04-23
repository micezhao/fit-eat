package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Data;

/**
 * 数据字典表，用户维护配置项
 * @author micezhao
 * mongo对象
 */
@Data
@Document
public class Dict {
	
	@Id
	private String id;
	/**
	 * 业务组别
	 */
	private String group;
	/**
	 * 组别细分
	 */
	private String key;
	/**
	 * 键
	 */
	private String keyName;
	/**
	 * 值
	 */
	private String value;
	/**
	 * 排序
	 */
	private String sort;
	
	/**
	 * 状态
	 */
	private String status;
	
	private String cdt;

	private String mdt;
	

}
