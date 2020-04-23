package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Data;

/**
 * 多媒体对象
 * @author micezhao
 * mongo 对象
 */
@Data
@Document
public class Media {
	
	/**
	 * 多媒体内容编号
	 */
	private String mediaNo;
	
	/**
	 * 多媒体类型
	 */
	private String mediaType;
	
	/**
	 * 多媒体资源
	 */
	private String resouceUrl;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime cdt;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime mdt;
}
