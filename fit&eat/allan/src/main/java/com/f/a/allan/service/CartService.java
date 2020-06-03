package com.f.a.allan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.response.CartView;
import com.f.a.allan.utils.MongoAggrerationUtils;

@Service
public class CartService {
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	public List<CartView> getCartViewByUser(String userAccount,String chatMerchant) {
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(new Criteria(FieldConstants.USER_ACCOUNT).is(userAccount)
						.and(FieldConstants.CART_MERCHANT).is(chatMerchant)),
				Aggregation.unwind("itemList", "index", true),
				MongoAggrerationUtils.aggregateAddFields("goodsItemId", "$toObjectId", "$itemList.goodsId"),
				MongoAggrerationUtils.aggregateAddFields("cartId", "$toString", "$_id"),
				Aggregation.lookup("goods", "goodsItemId", "_id", "cart_doc"),
				Aggregation.unwind("$cart_doc"),
				MongoAggrerationUtils.aggregateAddFields("moid", "$toObjectId", "$cart_doc.merchantId"),
				Aggregation.lookup("merchant", "moid", "_id", "merchant"),
				MongoAggrerationUtils.aggregateAddFields("spu_oid", "$toObjectId", "$cart_doc.spuId"),
				Aggregation.lookup("commodity", "spu_oid", "_id", "spu_doc"),
				Aggregation.unwind("$merchant"),
				Aggregation.unwind("$spu_doc"),
				Aggregation.project()
					.and(MongoAggrerationUtils.aggregationExpression("$toString", "$cart_doc._id")).as("goodsId")
					.and("$cart_doc.goodsName").as("goodsName")
					.and("$itemList.num").as("num")
					.and("$cart_doc.spuId").as("spuId")
					.and("$spu_doc.category").as("category")
					.and(MongoAggrerationUtils.aggregationExpression("$toString", "$merchant._id")).as("merchantId")
					.and(MongoAggrerationUtils.aggregationExpression("$toString", "$merchant.merchantName")).as("merchantName")
					.and("$cart_doc.goodsStatus").as("goodsStatus")
					.and("$cart_doc.itemOutline").as("itemOutline")
					.and("$cart_doc.discountPrice").as("discountPrice")
					.and("$cart_doc.price").as("price")
					.and("$spu_doc.domain").as("domain")
					.andInclude("cartId","cartMerchant","userAccount")
					.andExclude("_id")
				);
		 AggregationResults<CartView> result = mongoTemplate.aggregate(aggregation, "cart", CartView.class);
		return result.getMappedResults();
	}
	
}
