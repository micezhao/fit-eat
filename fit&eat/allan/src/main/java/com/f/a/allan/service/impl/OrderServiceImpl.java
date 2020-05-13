package com.f.a.allan.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.f.a.allan.dao.OrderMapper;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.pojo.Order;
import com.f.a.allan.enums.OrderEnum;
import com.f.a.allan.service.OrderService;
import com.f.a.allan.utils.RedisSequenceUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author micezhao
 * @since 2020-05-06
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

	@Autowired
	RedisSequenceUtils redisSequenceUtils;

	/**
	 * 根据商户分发订单
	 * 
	 * @param itemList
	 * @param userAccount
	 * @param orderTime
	 */

	public List<Order> batchDistribute(List<GoodsItem> itemList, String userAccount, LocalDateTime orderTime) {
		Map<String, List<GoodsItem>> map = new HashMap<String, List<GoodsItem>>();
		for (GoodsItem item : itemList) {
			if(map.containsKey(item.getMerchantId())){
				map.get(item.getMerchantId()).add(item);
			}else {
				List<GoodsItem> list = new ArrayList<GoodsItem>();
				list.add(item);
				map.put(item.getMerchantId(),list);
			}
		}
		List<Order> merchantOrderList = generateOrder(map,userAccount,orderTime);
		this.saveBatch(merchantOrderList);
		return merchantOrderList;
	}
	
	/**
	 * 接收根据商户分发的订单，并批量生成订单
	 * @param itemList
	 * @param userAccount
	 * @param orderTime
	 * @return
	 */
	public List<Order> generateOrder(Map<String, List<GoodsItem>> map, String userAccount, LocalDateTime orderTime) {
		List<Order> orderItemList = new ArrayList<Order>();
		for (Entry<String, List<GoodsItem>> element : map.entrySet()) {
			String orderId = redisSequenceUtils.orderSequence(); //  同一个商户的商品订单，共享一个订单号
			for (GoodsItem item : element.getValue()) {
				String settlePrice = new BigDecimal(item.getPrice()).subtract(new BigDecimal(item.getDiscountPrice()))
						.multiply(new BigDecimal(item.getNum())).setScale(2, RoundingMode.HALF_UP).toString();
				Order orderItem = Order.builder().orderId(orderId).userAccount(userAccount)
						.goodsId(item.getGoodsId()).merchantId(item.getMerchantId()).category(item.getCategory())
						.discountPrice(item.getDiscountPrice()).price(item.getPrice()).num(item.getNum())
						.settlementPrice(settlePrice).orderTime(orderTime).status(OrderEnum.NEED_DELIVERY.getCode())
						.build();
				orderItemList.add(orderItem);
			}
		}
		return orderItemList;
	}
}
