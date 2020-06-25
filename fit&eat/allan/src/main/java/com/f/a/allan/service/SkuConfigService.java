package com.f.a.allan.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.SkuConfig;
import com.mongodb.client.result.UpdateResult;

@Service
public class SkuConfigService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public static final String CODE = "code";

	public static final String NAME = "name";

	public static final String VALUES = "values";

	public static final String CONFIG_ID = "configId";

	public static final String VALUE = "value";

	public List<SkuConfig> addSkuConfig(List<SkuConfig> configs) {
		List<SkuConfig> list = (List<SkuConfig>) mongoTemplate.insertAll(configs);
		return list;
	}
	
	public SkuConfig addSkuConfigByCode(String code,String configValue) {
		 List<SkuConfig> list= listByCode(code);
		 if(list==null || list.isEmpty()) {
			 throw new RuntimeException("当前spuId对应的配置项不存在");
		 }
		 SkuConfig baseItem = list.get(0);
		 SkuConfig appendConfig = SkuConfig.builder().code(baseItem.getCode()).name(baseItem.getName()).spuId(baseItem.getSpuId()).value(configValue).build();
		 return mongoTemplate.insert(appendConfig);
	}
	
	public List<SkuConfig> listBySpuId(String spuId) {
		return mongoTemplate.find(new Query().addCriteria(new Criteria(FieldConstants.SKU_CONFIG_SPU_ID).is(spuId)),
				SkuConfig.class);
	}

	public void deleteById(String id) {
		mongoTemplate.findAndRemove(new Query().addCriteria(new Criteria(FieldConstants.SKU_CONFIG_ID).is(id)),
				SkuConfig.class);
	}

	public void clearConfigBySpuId(String spuId) {
		mongoTemplate.findAllAndRemove(
				new Query().addCriteria(new Criteria(FieldConstants.SKU_CONFIG_SPU_ID).is(spuId)), SkuConfig.class);
	}

	public List<SkuConfig> listByCode(String code) {
		return mongoTemplate.find(new Query().addCriteria(new Criteria(FieldConstants.SKU_CONFIG_CODE).is(code)),
				SkuConfig.class);
	}

	public boolean updateConfigNameByCode(String code, String name) {
		UpdateResult result = mongoTemplate.updateMulti(
				new Query().addCriteria(new Criteria(FieldConstants.SKU_CONFIG_CODE).is(code)),
				Update.update(FieldConstants.SKU_CONFIG_NAME, name), SkuConfig.class);
		return result.wasAcknowledged();
	}

	public SkuConfig updateConfigValueById(String id, String value) {
		SkuConfig result = mongoTemplate.findAndModify(
				new Query().addCriteria(new Criteria(FieldConstants.SKU_CONFIG_ID).is(id)),
				Update.update(FieldConstants.SKU_CONFIG_VALUE, value), FindAndModifyOptions.options().returnNew(true),
				SkuConfig.class);
		return result;
	}

	public List<JSONObject> queryConfigBySpuId(String spuId) {
		List<SkuConfig> configList = listBySpuId(spuId);
		Set<String> configCodeSet = new HashSet<String>();
		List<JSONObject> jsonList = new ArrayList<JSONObject>();
		// 做去重处理
		for (SkuConfig config : configList) {
			boolean addResult=configCodeSet.add(config.getCode());
			if(addResult) {
				JSONObject json = new JSONObject();
				json.put(CODE, config.getCode());
				json.put(NAME, config.getName());
				List<SkuConfig> cl = listByCode(config.getCode());
				JSONArray arr = new JSONArray();
				for (SkuConfig obj : cl) {
					JSONObject sub = new JSONObject();
					sub.put(CONFIG_ID, obj.getConfigId());
					sub.put(VALUE, obj.getValue());
					arr.add(sub);
				}
				json.put(VALUES, arr);
				jsonList.add(json);
			}
			
		}
		
		return jsonList;
	}
	
}
