package com.f.a.allan.entity.request;

import com.f.a.allan.entity.request.UserAddressQueryRequest.UserAddressQueryRequestBuilder;
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
@NoArgsConstructor
@AllArgsConstructor
/**
 * 用户地址信息管理对象
 * @author micezhao
 *
 */
public class UserAddressRequest {
	
	private String userAddressId;
	
	private String userAccount;
	
	private String contactName;
	
	private String contactPhone;
	
	private String addrDetail;
	
	private boolean defaultAddr;
	
	private String merchantId;
	
	private String dr = DrEnum.AVAILABLE.getCode(); // 是否禁用
}
