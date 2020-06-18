package com.f.a.allan.entity.pojo;

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
/**
 * SKU 的配置项
 * @author micezhao
 *
 */
@Document("skuConfig")
public class SkuConfig {
	
	@Id
	private String configId;
	
	
	private String spuId;
	
	/**
	 * 配置项的编码
	 */
	private String code;
	
	/**
	 * 配置项名称
	 */
	private String name;
	
	/**
	 * 配置项的值
	 */
	private String value;
	
}
