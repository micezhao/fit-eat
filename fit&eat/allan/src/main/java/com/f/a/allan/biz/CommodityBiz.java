package com.f.a.allan.biz;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.Commodity;
import com.f.a.allan.entity.pojo.SkuConfig;
import com.f.a.allan.entity.pojo.SkuConfigItem;
import com.f.a.allan.entity.request.CommodityRequest;
import com.f.a.allan.enums.GoodsStatusEnum;
import com.f.a.allan.utils.ObjectUtils;

@Service
public class CommodityBiz {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public Commodity insertOne(CommodityRequest request) {
		Commodity record = new Commodity();
		ObjectUtils.copy(record, request);
		record.setStatus(GoodsStatusEnum.UN_SOLD.getCode());
		record.setCdt(LocalDateTime.now());
		return mongoTemplate.insert(record);
	}
	
	public Commodity findById(String spuId) {
		return mongoTemplate.findOne(new Query().addCriteria(new Criteria(FieldConstants.SPU_ID).is(spuId))
				, Commodity.class);
	}
	
	public Commodity addSkuConfig(String spuId, List<SkuConfig> configs) {
		Update update = new Update() ;
		update.set(FieldConstants.SKU_CONFIG, configs);	
		update.set(FieldConstants.MDT, LocalDateTime.now());
		Commodity record = mongoTemplate.findAndModify(new Query().addCriteria(new Criteria(FieldConstants.SPU_ID).is(spuId)),
									update, 
									FindAndModifyOptions.options().returnNew(true),
									Commodity.class);
		return record;
	}
	
	
//	public Commodity updateSkuConfigByIndex(String spuId,String name,String index,String configValue) {
//		List<SkuConfig> list= findById(spuId).getSkus();
//		for (SkuConfig skuConfig : list) {
//			if(StringUtils.equals(skuConfig.getName(), name) ) {
//				List<SkuConfigItem> itemList  = skuConfig.getValue();
//				for (SkuConfigItem item : itemList) {
//					if(StringUtils.equals(item.getIndex(), index)) {
//						item.setConfigValue(configValue);
//					}
//				}
//			}
//		}
//		Update update = new Update();
//		update.set(FieldConstants.SKU_CONFIG, list);
//		update.set(FieldConstants.MDT, LocalDateTime.now());
//		Commodity record = mongoTemplate.findAndModify(
//				new Query().addCriteria(new Criteria(FieldConstants.SPU_ID).is(spuId)),
//				update,
//				FindAndModifyOptions.options().returnNew(true),
//				Commodity.class);
//		return record;
//	}
	
	public Commodity updateSkuConfigByIndex(String spuId,String index,String configValue) {
		List<SkuConfig> list= findById(spuId).getSkus();
		for (SkuConfig skuConfig : list) {
			List<Map<String,String>> mapList =skuConfig.getValue();
			for (Map<String,String> itemMap : mapList) {
				if(itemMap.containsKey(index)) {
					itemMap.replace(index, itemMap.get(index), configValue);
					break;
				}
			}
		}
		Update update = new Update();
		update.set(FieldConstants.SKU_CONFIG, list);
		update.set(FieldConstants.MDT, LocalDateTime.now());
		Commodity record = mongoTemplate.findAndModify(
				new Query().addCriteria(new Criteria(FieldConstants.SPU_ID).is(spuId)),
				update,
				FindAndModifyOptions.options().returnNew(true),
				Commodity.class);
		return record;
	}
	
	public Commodity deleteSkuConfigByIndex(String spuId,String index) {
		List<SkuConfig> list= findById(spuId).getSkus();
		for (SkuConfig skuConfig : list) {
			List<Map<String,String>> mapList =skuConfig.getValue();
			for (Map<String,String> itemMap : mapList) {
				if(itemMap.containsKey(index)) {
					mapList.remove(itemMap);
					break;
				}
			}
		}
		Update update = new Update();
		update.set(FieldConstants.SKU_CONFIG, list);
		update.set(FieldConstants.MDT, LocalDateTime.now());
		Commodity record = mongoTemplate.findAndModify(
				new Query().addCriteria(new Criteria(FieldConstants.SPU_ID).is(spuId)),
				update,
				FindAndModifyOptions.options().returnNew(true),
				Commodity.class);
		return record;
	}
}
