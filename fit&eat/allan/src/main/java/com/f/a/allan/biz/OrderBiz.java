package com.f.a.allan.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.f.a.allan.dao.mongo.OrderPackageMapper;
import com.f.a.allan.entity.pojo.DeliveryInfo;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.pojo.Order;
import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.allan.entity.request.OrderQueryRequst;
import com.f.a.allan.entity.response.OrderView;
import com.f.a.allan.enums.OrderEnum;
import com.f.a.allan.enums.PackageStatusEnum;
import com.f.a.allan.service.impl.OrderServiceImpl;
import com.f.a.allan.utils.RedisSequenceUtils;
import com.mongodb.client.result.UpdateResult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单的业务处理类
 * 
 * @author zhaochen
 *
 */
@Slf4j
@Service
public class OrderBiz {

	@Autowired
	RedisSequenceUtils redisSequenceUtils;

	@Autowired
	private MongoTemplate mongoTemplate;

	private static final Long DELAY_MIN = 15L;

	@Autowired
	private OrderServiceImpl orderServiceImpl;

	@Setter
	@Getter
	@AllArgsConstructor 
	/**
	 * 订单包价格计算处理器
	 * @author micezhao
	 *
	 */
	private class PackagePriceProccessor {

		String packageTotalPrice;

		String packageDiscountPrice;

		String packageSettlePrice;
	}

	public void packItem(String cartId, List<GoodsItem> list, String userAccount, DeliveryInfo delivery) {
		log.info("packaging goodsItem...");
		PackagePriceProccessor cal = priceCalculator(cartId, list);
		OrderPackage packItem = OrderPackage.builder().userAccount(userAccount)
				.cartId(cartId).itemList(list)
				.delivery(delivery)
				.totalAmount(cal.getPackageTotalPrice()).discountPrice(cal.getPackageDiscountPrice())
				.settlePrice(cal.getPackageSettlePrice()).packageStatus(PackageStatusEnum.CTEATE.getCode())
				.cdt(LocalDateTime.now()).expireTime(getExpireTime(DELAY_MIN)).build();
		mongoTemplate.insert(packItem);
		// TODO 存入延迟队列

	}

	private PackagePriceProccessor priceCalculator(String cartId, List<GoodsItem> list) {
		log.info("price of shopcart: {}  is calculating ... ", cartId);
		BigDecimal total = new BigDecimal(0);
		BigDecimal discountTotal = new BigDecimal(0);
		for (GoodsItem goodItem : list) {
			BigDecimal currentItemPrice = new BigDecimal(goodItem.getPrice())
					.multiply(new BigDecimal(goodItem.getNum())).setScale(2, RoundingMode.HALF_UP);
			total = total.add(currentItemPrice);
			BigDecimal currentItemDiscountPrice = new BigDecimal(goodItem.getDiscountPrice())
					.multiply(new BigDecimal(goodItem.getNum())).setScale(2, RoundingMode.HALF_UP);
			discountTotal = discountTotal.add(currentItemDiscountPrice);
		}
		BigDecimal settlePrice = total.subtract(discountTotal);

		return new PackagePriceProccessor(total.toString(), discountTotal.toString(), settlePrice.toString());
	}

	private void distributOrder(OrderPackage packageItem) {
		List<GoodsItem> itemList = packageItem.getItemList();
		if (itemList.isEmpty()) {
			log.debug("orderPackageId:{} has no goodsItem ", packageItem.getOrderPackageId());
		}
		String userAccount = packageItem.getUserAccount();
		LocalDateTime orderTime = packageItem.getPayTime();
		List<Order> orderItemList = new ArrayList<Order>();
		for (GoodsItem item : itemList) {
			String settlePrice = new BigDecimal(item.getPrice()).subtract(new BigDecimal(item.getDiscountPrice()))
					.multiply(new BigDecimal(item.getNum())).setScale(2, RoundingMode.HALF_UP).toString();
			Order orderItem = Order.builder().orderId(redisSequenceUtils.orderSequence()).userAccount(userAccount)
					.goodsId(item.getGoodsId()).merchantId(item.getMerchantId()).category(item.getCategory())
					.discountPrice(item.getDiscountPrice()).price(item.getPrice()).num(item.getNum())
					.settlementPrice(settlePrice).orderTime(orderTime).status(OrderEnum.NEED_DELIVERY.getCode())
					.build();
			orderItemList.add(orderItem);
		}
		// 批量生产订单
		orderServiceImpl.saveBatch(orderItemList);
	}

	// 支付成功后：更新订单包 -> 分配子订单 -> 回传结果，从购物车中清除已购买的商品
	// TODO 从mongo 到 mysql 的事务一致性问题
	public OrderPackage paySucccessed(String orderPackageId) {
		Query query = new Query();
		query.addCriteria(Criteria.where(OrderPackageMapper.ORDER_PACKAGE_ID).is(orderPackageId))
				.addCriteria(Criteria.where(OrderPackageMapper.PACKAGE_STATUS).is(PackageStatusEnum.CTEATE));
		Update update = new Update();
		update.set(OrderPackageMapper.PACKAGE_STATUS, PackageStatusEnum.PAID.getCode());
		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, OrderPackage.class);
		// TODO 注意幂等性处理
		if (updateResult.wasAcknowledged()) {
			OrderPackage packageItem = mongoTemplate.findOne(query, OrderPackage.class);
			distributOrder(packageItem); // 分配子订单
		}
		String objJsonStr = updateResult.getUpsertedId().asDocument().toJson();
		return JSONObject.parseObject(objJsonStr, OrderPackage.class);
	}

	// 关闭订单
	public void closePackage(String orderPackageId) {
		// 幂等性检查
		boolean existed = mongoTemplate.exists(new Query().addCriteria(
				Criteria.where(OrderPackageMapper.ORDER_PACKAGE_ID).is(PackageStatusEnum.CLOSED.getCode())),  OrderPackage.class);
		if(existed) {
			log.debug("orderPackageId : {} 订单包已关闭",orderPackageId);
			return; 
		}
		Query query = new Query();
		query.addCriteria(Criteria.where(OrderPackageMapper.ORDER_PACKAGE_ID).is(orderPackageId))
				.addCriteria(Criteria.where(OrderPackageMapper.PACKAGE_STATUS).is(PackageStatusEnum.CTEATE.getCode()));
		
		Update update = new Update();
		update.set(OrderPackageMapper.PACKAGE_STATUS, PackageStatusEnum.CLOSED.getCode())
				.set(OrderPackageMapper.MDT, LocalDateTime.now());
		UpdateResult updateResult = mongoTemplate.updateFirst(query, update, OrderPackage.class);
		if(!updateResult.wasAcknowledged()) {
			throw new RuntimeException("订单过期关闭失败");
		}
		 
	}

	private LocalDateTime getExpireTime(Long delay) {
		return LocalDateTime.now().plusMinutes(delay);
	}

	/**
	 * 订单包的查询接口
	 * 
	 * @param request
	 * @return
	 */
	public List<OrderPackage> listOrderPackage(OrderQueryRequst request) {
		Query query = new Query();
		if (StringUtils.isNotBlank(request.getOrderPackageId())) {
			query.addCriteria(Criteria.where(OrderPackageMapper.ORDER_PACKAGE_ID).is(request.getOrderPackageId()));
		}
		if (StringUtils.isNotBlank(request.getPackageStatus())) {
			query.addCriteria(Criteria.where(OrderPackageMapper.PACKAGE_STATUS).is(request.getPackageStatus()));
		} else {
			query.addCriteria(Criteria.where(OrderPackageMapper.PACKAGE_STATUS).is(PackageStatusEnum.CTEATE.getCode()));
		}
		if(StringUtils.isNotBlank(request.getUserAccount())) {
			query.addCriteria(Criteria.where(OrderPackageMapper.USER_ACCOUNT).is(request.getUserAccount()));
		}
		return mongoTemplate.find(query, OrderPackage.class);
	}
	
	public OrderPackage findById(OrderQueryRequst request) {
		Query query = new Query();
		query.addCriteria(Criteria.where(OrderPackageMapper.ORDER_PACKAGE_ID).is(request.getOrderPackageId()));
		return mongoTemplate.findOne(query, OrderPackage.class);
	}
	
	public List<OrderView> listOrder(OrderQueryRequst request) {
		
		return null;
	}

}
