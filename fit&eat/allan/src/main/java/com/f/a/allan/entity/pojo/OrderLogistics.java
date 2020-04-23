package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Data;

/**
 * 物流信息对象
 * @author micezhao
 * mongo 对象
 */
@Data
@Document
public class OrderLogistics {
	
	private String orderId;
	
	private String goodsId;
	/**
	 * 物流公司编号
	 */
    private String logisticsId;
    /**
     * 物流信息业务编号
     */
    private String bizNo;
    
    /**
	 * 商品所属商户号
	 */
	private String merchantNo;
    
	/**
	 * 物流单号
	 */
    private String trackingNo;
	
    /**
	 * 发货时间
	 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime sendOutTime;
	
    /**
	 * 收货时间
	 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime receiveTime;

}
