package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.f.a.allan.entity.response.OrderGoodsItemView;

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
	private String OrderPackageId;
	
	private String cartId;
	
	private DeliveryInfo delivery;
		
	private List<OrderGoodsItemView> itemList;
	
	private String userAccount;

	private int totalAmount;
	
	private int discountPrice;
	
	private int settlePrice;
	
	private String packageStatus;
	
	private LocalDateTime expireTime;
	
	private LocalDateTime payTime;
	
	private LocalDateTime cdt;
	
	private LocalDateTime mdt;

}
