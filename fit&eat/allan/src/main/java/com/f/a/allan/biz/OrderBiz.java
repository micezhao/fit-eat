package com.f.a.allan.biz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.f.a.allan.entity.bo.DeliveryInfo;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.pojo.Order;
import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.allan.entity.request.OrderQueryRequst;
import com.f.a.allan.entity.response.OrderGoodsItemView;
import com.f.a.allan.entity.response.OrderPackageView;
import com.f.a.allan.enums.PackageStatusEnum;
import com.f.a.allan.service.CalculatorService;
import com.f.a.allan.service.CalculatorService.PriceProccessor;
import com.f.a.allan.service.GoodsItemService;
import com.f.a.allan.service.OrderDetailService;
import com.f.a.allan.service.impl.OrderServiceImpl;
import com.f.a.allan.utils.ObjectUtils;
import com.mongodb.client.result.UpdateResult;
import com.netflix.discovery.converters.Auto;

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
	
	@Autowired
	private CalculatorService calculatorService;
	
	@Autowired
	private GoodsBiz goodsBiz;

	private static final Long DELAY_MIN = 15L;

	@Autowired
	private OrderServiceImpl orderServiceImpl;
	
	@Autowired
	private RedisTemplate<String,Object> redisTemplate; 
	
	@Autowired
	private GoodsItemService goodsItemService;
	
	private final static String TEMP_DELIVERY = "temp_delivery";
	
	@Autowired
	private OrderDetailService orderDetailService;

	
	public OrderGoodsItemView r2Dto(String goodsId,int num) {
		OrderGoodsItemView view = new OrderGoodsItemView();
		if(StringUtils.isNotBlank(goodsId) ) {
			GoodsItem g =goodsItemService.findBySkuId(goodsId);
			ObjectUtils.copy(view, g);
			view.setNum(num);
		}
		return view;
	}
	
	// 通过购物车生成订单包 此时商品的结算价格不再变化
	public boolean packItem(String cartId, JSONArray arr, String userAccount, DeliveryInfo delivery) {
		log.info("packaging goodsItem...");
		boolean result = true;
		List<OrderGoodsItemView> goodsViewList = new ArrayList<OrderGoodsItemView>();
		int executed_index = 0;
		try {
			for (int i = 0; i < arr.size(); i++) {
				String goodsId = arr.getJSONObject(i).getString("goodsId");
				int num = arr.getJSONObject(i).getIntValue("num"); 
				//  占用库存，不直接扣减库存
				goodsBiz.occupyByGoodsId(goodsId, num);
				executed_index ++;
				goodsViewList.add(r2Dto(goodsId,num));
			}
			// 开始计算价格
			PriceProccessor cal = calculatorService.priceCalculator(cartId, goodsViewList);
			OrderPackage packItem = OrderPackage.builder().userAccount(userAccount)
					.cartId(cartId).itemList(goodsViewList)
					.delivery(delivery)
					.totalAmount(cal.getTotalPrice()).discountPrice(cal.getDiscountPrice())
					.settlePrice(cal.getSettlePrice()).packageStatus(PackageStatusEnum.CTEATE.getCode())
					.cdt(LocalDateTime.now()).expireTime(getExpireTime(DELAY_MIN)).build();
			OrderPackage packInfo = mongoTemplate.insert(packItem);
			redisTemplate.opsForHash().put(TEMP_DELIVERY, packInfo.getOrderPackageId(), delivery); // 将订单的配送信息先缓存起来
			
			// TODO 存入延迟队列，如果过期就执行关闭订单的动作
			
		}catch (Exception e) {
			// 对已经执行了进扣减
			for (int i = 0; i < arr.subList(0, executed_index).size(); i++) {
				String goodsId = arr.getJSONObject(i).getString("goodsId");
				GoodsItem g =goodsItemService.findBySkuId(goodsId);
				int returnAmount = arr.getJSONObject(i).getIntValue("num"); 
				goodsBiz.returnBack(g.getMerchantId(), goodsId, returnAmount);
			}
			result = false;
		}
		return result;
		

	}

	private List<Order>  distributOrder(OrderPackage packageItem) {
		List<OrderGoodsItemView> goodsItemList = packageItem.getItemList();
		if (goodsItemList.isEmpty()) {
			log.debug("orderPackageId:{} has no goodsItem ", packageItem.getOrderPackageId());
			throw new RuntimeException("订单商品列表为空");
		}
		String userAccount = packageItem.getUserAccount();
		LocalDateTime orderTime = packageItem.getPayTime();
		List<Order> orderItemList = orderServiceImpl.batchDistribute(goodsItemList, userAccount, orderTime);
		return orderItemList;
	}
	
	private DeliveryInfo getDeliveryInfo(String orderPackageId) {
		Object obj = redisTemplate.opsForHash().get(TEMP_DELIVERY, orderPackageId);
		String objStr = JSONObject.toJSONString(obj);
		DeliveryInfo deliveryInfo = JSONObject.parseObject(objStr, DeliveryInfo.class);
		removeTempDelivery(orderPackageId); // 从redis中删除这条记录
		return deliveryInfo;
	}

	// 支付成功后：更新订单包 -> 分配子订单 -> 回传结果，从购物车中清除已购买的商品
	// TODO 从 mongo 到 mysql 的事务一致性问题
	//	@Transactional  此注解，在 多类型数据源情况下，不生效
	public OrderPackage paySucccessed(String orderPackageId,String fundTransferId) {
		OrderPackage item = mongoTemplate.findOne(Query.query(Criteria.where(FieldConstants.ORDER_PACKAGE_ID).is(orderPackageId))
								.addCriteria(Criteria.where(FieldConstants.PACKAGE_STATUS).is(PackageStatusEnum.PAID.getCode()))
								, OrderPackage.class);
		if(item != null) {
			log.debug("this orderPackage:{} has been paid ",orderPackageId);
			return item;
		}
		
		Query query = new Query();
		query.addCriteria(Criteria.where(FieldConstants.ORDER_PACKAGE_ID).is(orderPackageId))
				.addCriteria(Criteria.where(FieldConstants.PACKAGE_STATUS).is(PackageStatusEnum.CTEATE.getCode()));
		
		Update update = new Update();
		update.set(FieldConstants.PACKAGE_STATUS, PackageStatusEnum.PAID.getCode());
		update.set(FieldConstants.MDT, LocalDateTime.now());
		OrderPackage packageItem  = mongoTemplate.findAndModify(query, update,new FindAndModifyOptions().returnNew(true), OrderPackage.class);
		List<Order> orderItemList = distributOrder(packageItem); // 分配子订单
		
		DeliveryInfo deliveryInfo= getDeliveryInfo(orderPackageId);
		orderDetailService.insertBatch(orderItemList, deliveryInfo,fundTransferId); // 批量插入订单详情
		
		return packageItem;
	}

	// 关闭订单
	public void closePackage(String orderPackageId) {
		// 幂等性检查
		boolean existed = mongoTemplate.exists(
				new Query().addCriteria(Criteria.where(FieldConstants.ORDER_PACKAGE_ID).is(orderPackageId))
							.addCriteria(Criteria.where(FieldConstants.PACKAGE_STATUS).is(PackageStatusEnum.CLOSED.getCode())),
							OrderPackage.class);		
		if(existed) {
			log.debug("orderPackageId : {} 订单包已关闭",orderPackageId);
			return; 
		}
		Query query = new Query();
		query.addCriteria(Criteria.where(FieldConstants.ORDER_PACKAGE_ID).is(orderPackageId))
				.addCriteria(Criteria.where(FieldConstants.PACKAGE_STATUS).is(PackageStatusEnum.CTEATE.getCode()));
		
		Update update = new Update();
		update.set(FieldConstants.PACKAGE_STATUS, PackageStatusEnum.CLOSED.getCode())
				.set(FieldConstants.MDT, LocalDateTime.now());
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
			query.addCriteria(Criteria.where(FieldConstants.ORDER_PACKAGE_ID).is(request.getOrderPackageId()));
		}
		if (StringUtils.isNotBlank(request.getPackageStatus())) {
			query.addCriteria(Criteria.where(FieldConstants.PACKAGE_STATUS).is(request.getPackageStatus()));
		} else {
			query.addCriteria(Criteria.where(FieldConstants.PACKAGE_STATUS).is(PackageStatusEnum.CTEATE.getCode()));
		}
		if(StringUtils.isNotBlank(request.getUserAccount())) {
			query.addCriteria(Criteria.where(FieldConstants.USER_ACCOUNT).is(request.getUserAccount()));
		}
		return mongoTemplate.find(query, OrderPackage.class);
	}
	
	
	public OrderPackage findById(String orderPackageId) {
		Query query = new Query();
		query.addCriteria(Criteria.where(FieldConstants.ORDER_PACKAGE_ID).is(orderPackageId));
		OrderPackage record = mongoTemplate.findOne(query, OrderPackage.class);
		return record;
	}
	
	//  通过JSONObject 做桥梁的方案可解决内嵌的对象id无法映射的问题
	public OrderPackage findById2(String orderPackageId) {
		Query query = new Query();
		query.addCriteria(Criteria.where(FieldConstants.ORDER_PACKAGE_ID).is(orderPackageId));
		JSONObject record = mongoTemplate.findOne(query, JSONObject.class,"orderPackage");
		return JSONObject.toJavaObject(record, OrderPackage.class);
	}
	
	
	/**
	 * 从缓存中清除临时的配送信息记录
	 * @param orderPackageId
	 */
	private void removeTempDelivery(String orderPackageId) {
		redisTemplate.opsForHash().delete(TEMP_DELIVERY, orderPackageId); 
	}
	
	public OrderPackageView rebuildPackageRender(OrderPackage orderPackage) {
		List<OrderGoodsItemView> list= orderPackage.getItemList();
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
