package com.f.a.allan.entity.pojo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.f.a.allan.entity.bo.CartItem;
import com.f.a.allan.entity.response.CartView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("cart")
public class Cart {
	
	@Id
	private String chatId;
	
	private String chatMerchant;
	
	private String userAccount;
	
	private List<CartItem> itemList;
	
	private LocalDateTime cdt;
	
	private LocalDateTime mdt;
}
