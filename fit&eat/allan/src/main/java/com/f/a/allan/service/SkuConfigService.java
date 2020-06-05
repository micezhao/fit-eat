package com.f.a.allan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.SkuConfig;

@Service
public class SkuConfigService {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	public List<SkuConfig> addSkuConfig(List<SkuConfig> configs) {
		List<SkuConfig> list= (List<SkuConfig>) mongoTemplate.insertAll(configs);
		return list;
	}
	
	public List<SkuConfig> listBySpuId(String spuId){
		return mongoTemplate.find(new Query().addCriteria(new Criteria(FieldConstants.SKU_CONFIG_SPU_ID).is(spuId)), SkuConfig.class);
	} 
	
	public void deleteById(String id){
		mongoTemplate.findAndRemove(new Query().addCriteria(new Criteria(FieldConstants.SKU_CONFIG_ID).is(id)), SkuConfig.class);
	} 
	

	public void clearConfigBySpuId(String spuId) {
		mongoTemplate.findAllAndRemove(new Query().addCriteria(new Criteria(FieldConstants.SKU_CONFIG_SPU_ID).is(spuId)), SkuConfig.class);
	}
	
	public List<SkuConfig> listByCode(String code){
		return mongoTemplate.find(new Query().addCriteria(new Criteria(FieldConstants.SKU_CONFIG_CODE).is(code)), SkuConfig.class);
	}
	
}
