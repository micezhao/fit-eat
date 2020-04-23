package com.f.a.allan.entity.pojo;


import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 商品详情
 * @author micezhao
 * mongo对象
 */
@Data
@Document
public class GoodsDetail {
	
	private String goods_id;
	
	private String introduce;
	/**
	 * 商品的主要成分
	 */
	private String[] content;
	/**
	 * 商品适用场景
	 */
	private String[] scenes;
	
	private String[] description;
	
	/**
	 * 多媒体信息
	 */
	private Media medias;

}
