package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Data;

/**
 * 物流信息跟踪对象
 * 
 * @author micezhao mongo 对象
 */
@Data
@Document
public class OrderTracking {

	/**
	 * 订单编号
	 */
	private String orderId;
	/**
	 * 商品编号
	 */
	private String goodsId;
	
	/**
	 * 物流信息业务编号
	 */
	private String logisticsBizNo;

	/**
	 * 物流单号
	 */
	private String trackingNo;

	/**
	 * 物流详情
	 */
	private String remark;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime cdt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime mdt;

}
