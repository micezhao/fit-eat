package com.f.a.allan.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.f.a.allan.entity.bo.CartItem;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.response.OrderGoodsItemView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CalculatorService {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Setter
	@Getter
	@AllArgsConstructor 
	/**
	 * 订单包价格计算处理器
	 * @author micezhao
	 *
	 */
	public class PriceProccessor {

		int totalPrice;

		int discountPrice;

		int settlePrice;
	}
	
	public  PriceProccessor priceCalculator(String cartId, List<OrderGoodsItemView> list) {
		BigDecimal total = new BigDecimal(0);
		BigDecimal discountTotal = new BigDecimal(0);
		for (OrderGoodsItemView goodItem : list) {
			BigDecimal currentItemPrice = new BigDecimal(goodItem.getPrice())
					.multiply(new BigDecimal(goodItem.getNum())).setScale(2, RoundingMode.HALF_UP);
			total = total.add(currentItemPrice);
			
			if(goodItem.getDiscountPrice() !=null && goodItem.getDiscountPrice() != 0) {
				BigDecimal currentItemDiscountPrice = new BigDecimal(goodItem.getDiscountPrice())
						.multiply(new BigDecimal(goodItem.getNum())).setScale(2, RoundingMode.HALF_UP);
				discountTotal = discountTotal.add(currentItemDiscountPrice);
			}
		}
		BigDecimal settlePrice = total.subtract(discountTotal);

		return new PriceProccessor(total.intValue(), discountTotal.intValue(), settlePrice.intValue());
	}
	
	public PriceProccessor priceCalculator(List<CartItem> list) {
		BigDecimal total = new BigDecimal(0);
		BigDecimal discountTotal = new BigDecimal(0);
		for (CartItem item : list) {
			GoodsItem goodItem = mongoTemplate.findOne(
					new Query().addCriteria(new Criteria(FieldConstants.GOODS_ID).is(item.getGoodsId())), 
					GoodsItem.class);
			
			BigDecimal currentItemPrice = new BigDecimal(goodItem.getPrice())
					.multiply(new BigDecimal(item.getNum())).setScale(2, RoundingMode.HALF_UP);
			total = total.add(currentItemPrice);
			
			if(goodItem.getDiscountPrice() !=null && goodItem.getDiscountPrice() != 0) {
				BigDecimal currentItemDiscountPrice = new BigDecimal(goodItem.getDiscountPrice())
						.multiply(new BigDecimal(item.getNum())).setScale(2, RoundingMode.HALF_UP);
				discountTotal = discountTotal.add(currentItemDiscountPrice);
			}
		}
		BigDecimal settlePrice = total.subtract(discountTotal);

		return new PriceProccessor(total.intValue(), discountTotal.intValue(), settlePrice.intValue());
	}
}
