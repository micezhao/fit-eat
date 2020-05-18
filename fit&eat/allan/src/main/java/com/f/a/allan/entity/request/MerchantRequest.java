package com.f.a.allan.entity.request;

import java.util.List;

import com.f.a.allan.entity.pojo.Certificate;
import com.f.a.allan.enums.DrEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * 用户地址信息管理对象
 * 
 * @author micezhao
 *
 */
public class MerchantRequest {
	
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
	
	// 备注信息
	private String memo;
}
