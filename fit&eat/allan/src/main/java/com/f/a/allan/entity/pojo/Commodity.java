package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("commodity")
/**
 * 商品 SPU
 * 
 * @author micezhao
 *
 */
public class Commodity {

	@Id
	private String spuId;

	private String merchantId;

	private String name;

	private String introduce;

	private List<Media> medias;
	
	
	/**
	 * 商品分类
	 */
	private String category;
	
	/**
	 * 商品应用领域 
	 */
	private String domain;
	
	/**
	 * 存储富文本编辑中的内容
	 */
	private String detail;
	
	private String status;
	
	private String[] goodsItemLink;

	private LocalDateTime cdt;

	private LocalDateTime mdt;

}
