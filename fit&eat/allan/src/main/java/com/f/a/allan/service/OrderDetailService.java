package com.f.a.allan.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.f.a.allan.entity.pojo.DeliveryInfo;
import com.f.a.allan.entity.pojo.Order;
import com.f.a.allan.entity.pojo.OrderDetail;
import com.f.a.allan.entity.pojo.OrderDetail.OrderDetailBuilder;

@Service
public class OrderDetailService {
	
//	private final static String ID= "id";
	
	private final static String ORDER_ID= "orderId";
	
//	private final static String RECEVIER_NAME = "recevierName";
//	
//	private final static String RECEVIER_PHONE = "recevierPhone";
//	
//	private final static String RECEVIER_ADDR = "receiveAddr";
//	
//	private final static String GOOD_NAME = "goodsName";
//	
//	private final static String IMG_URL = "imgUrl";
	
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public OrderDetail getByOrderId(String orderId) {
		
		return mongoTemplate.findOne(new Query().addCriteria(new Criteria(ORDER_ID).is(orderId)), OrderDetail.class);
	}
		
	public void insert(OrderDetail orderDetail) {
		mongoTemplate.insert(orderDetail);
	}
	
	/**
	 * 批量插入
	 * 存在多条商品订单记录共享一个订单号的情况，但是一个订单信息只需要对应一条配送信息即可，因此通过set对orderList 对orderId进行去重
	 * @param list
	 */
	public void insertBatch(List<Order> list,DeliveryInfo delivery,String fundTransferId) {
		List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
		Set<String> orderIdSet = new HashSet<String>();
		for (Order order : list) {
			orderIdSet.add(order.getOrderId());
		}
		
		OrderDetail orderDetail = null;
		for (String os : orderIdSet) {
			
			orderDetail = buildOrderDetail(os, delivery,fundTransferId);
			orderDetailList.add(orderDetail);
		}
		mongoTemplate.insertAll(orderDetailList);
	}
	
	public OrderDetail buildOrderDetail(String orderId, DeliveryInfo delivery,String fundTransferId) {
		// TODO 查询商品详情 （方案一：调用接口查询产品 / 方案二：从请求中获取）
		
		OrderDetailBuilder odb = OrderDetail.builder().goodsName("测试商品名称").orderId(orderId).fundTransferId(fundTransferId)
							.recevierName(delivery.getRecevierName())
							.receiveAddr(delivery.getReceiveAddr())
							.recevierPhone(delivery.getRecevierPhone())
							.expectDeliveryTime(delivery.getDeliveryTime());
		if(StringUtils.isNotBlank(delivery.getMemo()) ) {
			odb.memo(delivery.getMemo());
		}
		return odb.build();
		
	}
	
}
