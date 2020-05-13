package com.f.a.allan.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.f.a.allan.dao.mongo.OrderPackageMapper;
import com.f.a.allan.entity.pojo.DeliveryInfo;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.pojo.Order;
import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.allan.entity.request.OrderQueryRequst;
import com.f.a.allan.entity.response.OrderPackageView;
import com.f.a.allan.entity.response.OrderPackageView.OrderPackageViewBuilder;
import com.f.a.allan.enums.PackageStatusEnum;
import com.f.a.allan.service.OrderDetailService;
import com.f.a.allan.service.impl.OrderServiceImpl;
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
	private MongoTemplate mongoTemplate;

	private static final Long DELAY_MIN = 15L;

	@Autowired
	private OrderServiceImpl orderServiceImpl;
	
	@Autowired
	private RedisTemplate<String,Object> redisTemplate; 
	
	private final static String TEMP_DELIVERY = "temp_delivery";
	
	@Autowired
	private OrderDetailService orderDetailService;

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
		// TODO 库存扣减的动作放在请求端？还是通过远程调用？
		log.info("packaging goodsItem...");
		PackagePriceProccessor cal = priceCalculator(cartId, list);
		OrderPackage packItem = OrderPackage.builder().userAccount(userAccount)
				.cartId(cartId).itemList(list)
				.delivery(delivery)
				.totalAmount(cal.getPackageTotalPrice()).discountPrice(cal.getPackageDiscountPrice())
				.settlePrice(cal.getPackageSettlePrice()).packageStatus(PackageStatusEnum.CTEATE.getCode())
				.cdt(LocalDateTime.now()).expireTime(getExpireTime(DELAY_MIN)).build();
		OrderPackage packInfo = mongoTemplate.insert(packItem);
		redisTemplate.opsForHash().put(TEMP_DELIVERY, packInfo.getOrderPackageId(), delivery); // 将订单的配送信息先缓存起来
		
		// TODO 存入延迟队列，如果过期就执行关闭订单的动作

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
		List<GoodsItem> goodsItemList = packageItem.getItemList();
		if (goodsItemList.isEmpty()) {
			log.debug("orderPackageId:{} has no goodsItem ", packageItem.getOrderPackageId());
			return ;
		}
		String userAccount = packageItem.getUserAccount();
		LocalDateTime orderTime = packageItem.getPayTime();
		Object obj = redisTemplate.opsForHash().get(TEMP_DELIVERY, packageItem.getOrderPackageId());
		String objStr = JSONObject.toJSONString(obj);
		DeliveryInfo deliveryInfo = JSONObject.parseObject(objStr, DeliveryInfo.class);
		List<Order> orderItemList = orderServiceImpl.batchDistribute(goodsItemList, userAccount, orderTime);
		orderDetailService.insertBatch(orderItemList, deliveryInfo); // 批量插入订单详情
		removeTempDelivery(packageItem.getOrderPackageId());
	}

	// 支付成功后：更新订单包 -> 分配子订单 -> 回传结果，从购物车中清除已购买的商品
	// TODO 从mongo 到 mysql 的事务一致性问题
	//	@Transactional  此注解，在 多类型数据源情况下，不生效
	public OrderPackage paySucccessed(String orderPackageId) {
		OrderPackage item = mongoTemplate.findOne(Query.query(Criteria.where(OrderPackageMapper.ORDER_PACKAGE_ID).is(orderPackageId))
								.addCriteria(Criteria.where(OrderPackageMapper.PACKAGE_STATUS).is(PackageStatusEnum.PAID.getCode()))
								, OrderPackage.class);
		if(item != null) {
			log.debug("this orderPackage:{} has been paid ",orderPackageId);
			return item;
		}
		
		Query query = new Query();
		query.addCriteria(Criteria.where(OrderPackageMapper.ORDER_PACKAGE_ID).is(orderPackageId))
				.addCriteria(Criteria.where(OrderPackageMapper.PACKAGE_STATUS).is(PackageStatusEnum.CTEATE.getCode()));
		
		Update update = new Update();
		update.set(OrderPackageMapper.PACKAGE_STATUS, PackageStatusEnum.PAID.getCode());
		update.set(OrderPackageMapper.MDT, LocalDateTime.now());
		OrderPackage packageItem  = mongoTemplate.findAndModify(query, update,new FindAndModifyOptions().returnNew(true), OrderPackage.class);
		distributOrder(packageItem); // 分配子订单
		
		return packageItem;
	}

	// 关闭订单
	public void closePackage(String orderPackageId) {
		// 幂等性检查
		boolean existed = mongoTemplate.exists(
				new Query().addCriteria(Criteria.where(OrderPackageMapper.ORDER_PACKAGE_ID).is(orderPackageId))
							.addCriteria(Criteria.where(OrderPackageMapper.PACKAGE_STATUS).is(PackageStatusEnum.CLOSED.getCode())),
							OrderPackage.class);		
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
		if(updateResult.getModifiedCount() < 1) {
			throw new RuntimeException("订单关闭失败");
		}
		removeTempDelivery(orderPackageId);
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
	
	
	
	/**
	 * 从缓存中清除临时的配送信息记录
	 * @param orderPackageId
	 */
	private void removeTempDelivery(String orderPackageId) {
		redisTemplate.opsForHash().delete(TEMP_DELIVERY, orderPackageId); 
	}
	
	public OrderPackageView rebuildPackageRender(OrderPackage orderPackage) {
		List<GoodsItem> list= orderPackage.getItemList();
		if(list.isEmpty()) {
			log.error("null goodsItem in this orderPackage [orderPackageId:{}]",orderPackage.getOrderPackageId());
			throw new RuntimeException("订单包中商品内容为空");  
		}
		OrderPackageView view =  OrderPackageView.builder()
									.orderPackageId(orderPackage.getOrderPackageId())
									.cartId(orderPackage.getCartId())
									.userAccount(orderPackage.getUserAccount())
									.delivery(orderPackage.getDelivery())
									.totalAmount(orderPackage.getTotalAmount())
									.discountPrice(orderPackage.getDiscountPrice())
									.settlePrice(orderPackage.getSettlePrice())
									.expireTime(orderPackage.getExpireTime())
									.packageStatus(orderPackage.getPackageStatus())
									.payTime(orderPackage.getPayTime()).goodsItemList(list).cdt(orderPackage.getCdt()).build();
		return view;
	}

}
