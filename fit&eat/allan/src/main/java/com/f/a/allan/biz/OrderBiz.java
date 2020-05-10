package com.f.a.allan.biz;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.pojo.Order;
import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.allan.enums.OrderEnum;
import com.f.a.allan.enums.PackageStatusEnum;
import com.f.a.allan.utils.RedisSequenceUtils;
import com.mongodb.client.result.UpdateResult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单的业务处理类
 * @author zhaochen
 *
 */
@Slf4j
public class OrderBiz {
	
	@Autowired
	RedisSequenceUtils redisSequenceUtils;
	
	@Autowired
	protected MongoTemplate mongoTemplate;
	
	private static final Long DELAY_MIN = 15L ;
	
	@Setter
	@Getter
	@AllArgsConstructor
	public class PackagePriceProccessor{
		
		String packageTotalPrice;
		
		String packageDiscountPrice;
		
		String packageSettlePrice;
	}
	
	public void packItem(String cartId,List<GoodsItem> list,String userAccount) {
		log.info("packaging goodsItem...");
		PackagePriceProccessor cal= priceCalculator(cartId,list);
		OrderPackage packItem = OrderPackage.builder().userAccount(userAccount).cartId(cartId).itemList(list)
						.totalAmount(cal.getPackageTotalPrice())
						.discountPrice(cal.getPackageDiscountPrice())
						.settlePrice(cal.getPackageSettlePrice())
						.packageStatus(PackageStatusEnum.CTEATE.getCode())
						.cdt(LocalDateTime.now()).expireTime(getExpireTime(DELAY_MIN)).build();
		mongoTemplate.insert(packItem);
	}
	
	private PackagePriceProccessor priceCalculator(String cartId,List<GoodsItem> list) {
		log.info("price of shopcart: {}  is calculating ... ",cartId);
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal discountTotal = BigDecimal.ZERO;
		for (GoodsItem goodItem : list) {
			BigDecimal currentItemPrice = new BigDecimal(goodItem.getPrice()).multiply(new BigDecimal(goodItem.getNum())).setScale(2, RoundingMode.HALF_UP);
			total.add(currentItemPrice) ;
			BigDecimal currentItemDiscountPrice = new BigDecimal(goodItem.getDiscountPrice()).multiply(new BigDecimal(goodItem.getNum())).setScale(2, RoundingMode.HALF_UP);
			discountTotal.add(currentItemDiscountPrice) ;
		}
		BigDecimal settlePrice =  total.subtract(discountTotal);
		
		return new PackagePriceProccessor(total.toString(),discountTotal.toString(),settlePrice.toString());
	}
	
	private void distributOrder(OrderPackage packageItem) {
		 List<GoodsItem> itemList= packageItem.getItemList();
		 String userAccount = packageItem.getUserAccount();
		 LocalDateTime orderTime = packageItem.getPayTime();
		 for (GoodsItem item : itemList) {
			String settlePrice = new BigDecimal(item.getPrice()).subtract(new BigDecimal(item.getDiscountPrice()))
						.multiply(new BigDecimal(item.getNum())).setScale(2, RoundingMode.HALF_UP).toString();
			Order.builder().orderId(redisSequenceUtils.orderSequence()).userAccount(userAccount)
							.goodsId(item.getGoodsId()).merchantId(item.getMerchantId())
							.category(item.getCategory())
							.discountPrice(item.getDiscountPrice())
							.price(item.getPrice()).num(item.getNum()).settlementPrice(settlePrice)
							.orderTime(orderTime).status(OrderEnum.NEED_DELIVERY.getCode());
		}
		 
	}
	
	// 支付成功后：更新订单包 -> 分配子订单 -> 回传结果，从购物车中清除已购买的商品
	public void paySucccessed(String cartId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("cartId").is(cartId));
		Update update = new Update();
		update.set("packageStatus", PackageStatusEnum.PAYED.getCode());
		UpdateResult updateResult= mongoTemplate.updateFirst(query, update, OrderPackage.class);
		if(updateResult.wasAcknowledged()) {
			OrderPackage packageItem = mongoTemplate.findOne(query, OrderPackage.class);
			distributOrder(packageItem); // 分配子订单
		}
	}
	
	
	
	// 关闭订单
	public void closePackage(String cartId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("cartId").is(cartId));
		Update update = new Update();
		update.set("packageStatus", PackageStatusEnum.CLOSED.getCode()).set("payTime", LocalDateTime.now());
		mongoTemplate.updateFirst(query, update, OrderPackage.class);
	}
	
	private LocalDateTime getExpireTime(Long delay) {
		return LocalDateTime.now().plusMinutes(delay);
	}
	
	
	
	
}
