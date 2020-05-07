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
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="orderPackage")
@ToString
public class OrderPackage {
	
	@Id
	private String id;
	
	private String OrderPackageId;
	
	private String cartId;
	
	private List<Order> orderList;
	
	private String userAccount;
	
	private String totalAmount;
	
	private LocalDateTime expireTime;
	
	private LocalDateTime payTime;
	
	private LocalDateTime cdt;
	
	private LocalDateTime mdt;

}
