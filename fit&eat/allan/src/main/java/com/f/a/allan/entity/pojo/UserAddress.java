package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document("userAddress")
/**
 * 用户收货地址
 * @author micezhao
 *
 */
public class UserAddress {
	
	@Id
	private String userAddressId;
	
	private String userAccount;
	
	private String contactName;
	
	private String contactPhone;
	
	private String addrDetail;
	
	private boolean defaultAddr;
	
	private String merchantId;
	
	private String dr; // 是否禁用
	
	private LocalDateTime cdt;
	
	private LocalDateTime mdt ; 
	
}
