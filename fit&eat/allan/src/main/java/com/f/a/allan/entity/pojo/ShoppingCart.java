package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Data;

/**
 * 购物车对象
 * @author micezhao
 * mongo 对象
 */
@Data
@Document
public class ShoppingCart {
	
	private String cartId;
	
	private Long customerId;
	
	private String goodsId;
	
	private String merchantId;
	/**
	 * 数量
	 */
	private String count;
	/**
	 * 商品单价
	 */
	private String price;
	
	/**
	 * 购物车中商品的状态
	 */
	private String status;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime cdt;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime mdt;

}
