package com.f.a.allan.entity.request;

import com.f.a.allan.enums.DrEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
/**
 * 用户地址信息管理对象
 * @author micezhao
 *
 */
@ApiModel(value="用户地址请求对象")
public class UserAddressRequest {
	
	@ApiModelProperty(value="地址id")
	private String userAddressId;
	
	@ApiModelProperty(value="用户账号")
	private String userAccount;
	
	@ApiModelProperty(value="联系人姓名")
	private String contactName;
	
	@ApiModelProperty(value="联系人电话")
	private String contactPhone;
	
	@ApiModelProperty(value="收货地址详情")
	private String addrDetail;
	
	@ApiModelProperty(value="是否为默认地址")
	private boolean defaultAddr;
	
	@ApiModelProperty(value="商户id")
	private String merchantId;
	
	@ApiModelProperty(value="地址状态",allowableValues="1,0" )
	private String dr = DrEnum.AVAILABLE.getCode(); // 是否禁用
}
