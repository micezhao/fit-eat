package com.f.a.allan.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.allan.dao.OrderPackageMapper;
import com.f.a.allan.entity.pojo.Order;
import com.f.a.allan.entity.pojo.OrderPackage;

@Service
public class OrderPackageService {
	
	@Autowired
	private OrderPackageMapper mapper;
	
	public OrderPackage packageOrders(String userAccount, List<Order> orderList) {
//		OrderPackage item = OrderPackage.builder().id(222222L).cartId("dfdfdfdfdfdf").OrderPackageId("93dfdfdf")
//		.mdt(null).cdt(LocalDateTime.now()).expireTime(setExpireTime()).orderList(orderList).build();
//		mapper.insert(item);
		return null;
	}
	
	private static LocalDateTime setExpireTime() {
		return LocalDateTime.now().plusMinutes(5L);
	}
}
