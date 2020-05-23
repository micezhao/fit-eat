package com.f.a.allan.entity.request;

import java.util.List;

import com.f.a.allan.entity.bo.Certificate;
import com.f.a.allan.enums.DrEnum;
import com.f.a.allan.enums.MerchantStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

@ApiModel(value="商户请求对象")
public class MerchantRequest {
	
	@ApiModelProperty(value="商户id")
	private String merchantId;
	@ApiModelProperty(value="商户名")
	private String merchantName;

	// 商户入驻时间
	@ApiModelProperty(value="注册时间", hidden = true)
	private String registerDate;
	
	@ApiModelProperty(value="认证状态", allowableValues = "wait_verify,un_verified,verified",allowEmptyValue = true)
	private String verifyStatus;
	
	@ApiModelProperty(value="资质证明列表",allowEmptyValue = true)
	private List<Certificate> certs;
	
	@ApiModelProperty(value="运行状态", allowableValues = "opening,suspension,closed,off" ,allowEmptyValue = true)
	private String operationStatus;
	
	// 商户分类
	@ApiModelProperty(value="商户分类" ,allowEmptyValue = false)
	private String classification;

	// 商户负责人
	@ApiModelProperty(value="商户所有人姓名" ,allowEmptyValue = false)
	private String holderName;

	// 商户负责任联系方式
	@ApiModelProperty(value="商户所有人联系电话" ,allowEmptyValue = false)
	private String holderPhone;

	// 经营范围
	@ApiModelProperty(value="商户经营范围" ,allowEmptyValue = false)
	private String scope;
	
	@ApiModelProperty(value="商户logo地址" ,allowEmptyValue = false)
	private String logoUrl;

	// 商户详细描述
	@ApiModelProperty(value="商户描述" ,allowEmptyValue = true)
	private String description;
	
	// 备注信息
	@ApiModelProperty(value="请求时的备注信息" ,allowEmptyValue = true)
	private String memo;
}
