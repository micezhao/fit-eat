package com.f.a.allan.entity.pojo;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;

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
public class SkuConfig {
	
	/**
	 * 配置项名称
	 */
	private String name;
	
	/**
	 * 配置项的值
	 */
	private List<Map<String,String>> value;
	
}
