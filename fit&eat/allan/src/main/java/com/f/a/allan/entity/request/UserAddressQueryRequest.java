package com.f.a.allan.entity.request;

import com.f.a.allan.entity.request.OrderQueryRequst.OrderQueryRequstBuilder;

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
 * 用户地址信息查询对象
 * @author micezhao
 *
 */
public class UserAddressQueryRequest extends BaseQueryRequest{
	
	private String userAccount;
	
	private String userAddressId;
	
	private String merchantId;
	
	private Boolean defaultAddr;
	
	private String contactName;
	
	private String contactPhone;
	
}
