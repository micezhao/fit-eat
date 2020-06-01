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
				MongoAggrerationUtils.aggregateAddFields("chatId", "$toString", "$_id"),
				Aggregation.lookup("goodsItem", "goodsItemId", "_id", "chat_doc"),
				Aggregation.unwind("$chat_doc"),
				MongoAggrerationUtils.aggregateAddFields("moid", "$toObjectId", "$chat_doc.merchantId"),
				Aggregation.lookup("merchant", "moid", "_id", "merchant"),
				Aggregation.unwind("$merchant"),
				Aggregation.project().andInclude("_id","userAccount","chatMerchant","chatId",
							"$itemList.num","$itemList.goodsId",
							"$chat_doc.goodsName","$chat_doc.category","$chat_doc.domain","$chat_doc.itemOutline",
							"$chat_doc.goodsStatus","$chat_doc.discountPrice","$chat_doc.price","$chat_doc.merchantId",
							"$merchant.merchantName"),
				Aggregation.project().andExclude("cdt","mdt","moid","goodsItemId")
				);
		 AggregationResults<CartView> result = mongoTemplate.aggregate(aggregation, "chat", CartView.class);
		return result.getMappedResults();
	}
	
}
