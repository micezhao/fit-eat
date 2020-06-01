package com.f.a.allan.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.request.GoodsItemQueryRequest;
import com.f.a.allan.utils.MongoAggrerationUtils;

@Service
public class GoodsItemService {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	/**
	 * 商品库存扣减失败时候的补偿流程
	 * @param goodsId
	 */
	public void stockMakeUp(String goodsId) {
		
	}
	
	public List<GoodsItem> findByReqeust(GoodsItemQueryRequest request) {
		Criteria criteria = new Criteria();
		if(StringUtils.isNotBlank(request.getSkuId())) {
			criteria.and(FieldConstants.GOODS_ID).is(request.getSkuId());
		}
		if(StringUtils.isNotBlank(request.getSpuId())) {
			criteria.and(FieldConstants.SPU_ID).is(request.getSpuId());
		}
		if(StringUtils.isNotBlank(request.getMerchantId())) {
			criteria.and(FieldConstants.MERCHANT_ID).is(request.getMerchantId());
		}
		AggregationResults<GoodsItem> result =mongoTemplate.aggregate(aggregation(criteria),"goods" , GoodsItem.class);
		return result.getMappedResults();
	}
	
	public GoodsItem findBySkuId(String goodsId) {
		Criteria criteria = new Criteria();
		criteria.and(FieldConstants.GOODS_ID).is(goodsId);
		
		AggregationResults<GoodsItem> result =mongoTemplate.aggregate(aggregation(criteria),"goods" , GoodsItem.class);
		return result.getUniqueMappedResult();
	}
	
	private Aggregation aggregation(Criteria criteria){
		return Aggregation.newAggregation(
				MongoAggrerationUtils.aggregateAddFields("spu_oid", "$toObjectId", "$spuId"),
				Aggregation.lookup("commodity", "spu_oid", "_id", "spu"),
				Aggregation.unwind("$spu"),
				MongoAggrerationUtils.aggregateAddFields("merchantId", "$toObjectId", "$spu.merchantId"),
				Aggregation.lookup("merchant", "merchantId", "_id", "merchant"),
				Aggregation.unwind("$merchant"),
				Aggregation.project()
					.and(MongoAggrerationUtils.aggregationExpression("$toString", "$_id")).as("goodsId")
					.and(MongoAggrerationUtils.aggregationExpression("$toString", "$merchantId")).as("merchantId")
					.and("$merchant.merchantName").as("merchantName")
					.and("$spu.category").as("category")
					.andInclude("goodsName","spuId","itemOutline","stock","goodsStatus","discountPrice","price")
					.andExclude("_id"),
				Aggregation.match(criteria));
	}
}
