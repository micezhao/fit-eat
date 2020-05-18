package com.f.a.allan.entity.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
//@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantQueryRequest extends BaseQueryRequest{
	
	private String merchantId;

	private String merchantName;

	// 商户入驻时间
	private String registerDateStart;
	
	private String registerDateEnd;

	private String verifyStatus;
	
	private String classification;
	
	private List<String> classificationList;
	
	private List<String> verifyStatusList;
	
	private String operationStatus;
	
	private List<String > operationStatusList;

	// 商户负责人
	private String holderName;

	// 商户负责任联系方式
	private String holderPhone;

	// 经营范围
	private String scope;	
	
}
