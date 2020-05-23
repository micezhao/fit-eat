package com.f.a.allan.entity.bo;

import com.f.a.allan.entity.bo.DeliveryInfo.DeliveryInfoBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatItem {
	
	private String goodsId;
	
	private int num;
	
}
