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
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="merchant")
@ToString
public class Merchant {

	@Id
	private String merchantId;
	
	private String merchantName;
	
	// 商户入驻时间
	private String registerDate;
	
	private String verifyStatus;
	
	private List<Certificate> certs;

	private String operationStatus;
	
	// 商户负责人 
	private String holderName;
	
	// 商户负责任联系方式
	private String holderPhone;
	
	// 经营范围
	private String scope;
	
	private String logoUrl;
	
	// 商户详细描述
	private String description;
	
	private LocalDateTime cdt;
	
	private LocalDateTime mdt;
	
}
