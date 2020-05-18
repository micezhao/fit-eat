package com.f.a.allan.entity.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="merchantConfig")
@ToString
/**
 * 商户配置信息类
 * @author micezhao
 *
 */
public class MerchantConfig {
	
	@Id
	private String merConfigId;
	
	private String merchantId;
	
	private String configType;
	
	
	
}
