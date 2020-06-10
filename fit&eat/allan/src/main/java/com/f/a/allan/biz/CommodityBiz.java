package com.f.a.allan.biz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.Commodity;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.pojo.SkuConfig;
import com.f.a.allan.entity.pojo.SkuConfigItem;
import com.f.a.allan.entity.request.CommodityRequest;
import com.f.a.allan.entity.request.GoodsItemQueryRequest;
import com.f.a.allan.enums.GoodsStatusEnum;
import com.f.a.allan.utils.ObjectUtils;

@Service
public class CommodityBiz {

	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * 新增一个商品记录
	 * 
	 * @param request
	 * @return
	 */
	public Commodity insertOne(CommodityRequest request) {
		Commodity record = new Commodity();
		ObjectUtils.copy(record, request);
		record.setStatus(GoodsStatusEnum.UN_SOLD.getCode());
		record.setCdt(LocalDateTime.now());
		return mongoTemplate.insert(record);
	}

	/**
	 * 根据spu查询商品
	 * 
	 * @param spuId
	 * @return
	 */
	public Commodity findById(String spuId) {
		Commodity record = mongoTemplate.findOne(new Query().addCriteria(new Criteria(FieldConstants.SPU_ID).is(spuId)),
				Commodity.class);
		return record;
	}

	public Page<Commodity> pageListCommodity(GoodsItemQueryRequest request) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		if (StringUtils.isNotBlank(request.getSpuName())) {
			criteria.and(FieldConstants.SPU_NAME)
					.regex(Pattern.compile("^.*" + request.getSpuName() + ".*$", Pattern.CASE_INSENSITIVE));
		}
		if (StringUtils.isNotBlank(request.getMerchantId())) {
			criteria.and(FieldConstants.MERCHANT_ID).is(request.getMerchantId());
		}
		if (StringUtils.isNotBlank(request.getSpuId())) {
			criteria.and(FieldConstants.SPU_ID).is(request.getSpuId());
		}

		if (StringUtils.isNotBlank(request.getCategory())) {
			criteria.and(FieldConstants.CATEGORY).is(request.getCategory());
		}
		if (StringUtils.isNotBlank(request.getStatus())) {
			criteria.and(FieldConstants.SPU_STATUS).is(request.getStatus());
		}
		if (request.getCategoryList() != null && !request.getCategoryList().isEmpty()) {
			criteria.and(FieldConstants.CATEGORY).in(request.getCategoryList());
		}
		if (request.getStatusList() != null && !request.getStatusList().isEmpty()) {
			criteria.and(FieldConstants.SPU_STATUS).in(request.getStatusList());
		}
		query.addCriteria(criteria);
		long total = mongoTemplate.count(query, Commodity.class);

		Sort sort = Sort.by(Sort.Direction.DESC, "_id");
		Pageable pageable = PageRequest.of((request.getPageNum() - 1), request.getPageSize(), sort);
		query.with(pageable);
		List<Commodity> list = mongoTemplate.find(query, Commodity.class);
		return new PageImpl(list, pageable, total);
	}

	public void updateGoodsLink(String spuId, String skuId) {
		mongoTemplate.findAndModify(new Query().addCriteria(new Criteria(FieldConstants.SPU_ID).is(spuId)),
				new Update().push(FieldConstants.SPU_LINK_SKU, skuId), Commodity.class);
	}
	
	
	public Commodity updateCommodity(CommodityRequest request) {
		Query query = new Query().addCriteria(new Criteria(FieldConstants.SPU_ID).is(request.getSpuId()));
		Update update = new Update();
		if(StringUtils.isNotBlank(request.getName())) {
			update.set(FieldConstants.SPU_NAME, request.getName());
		}
		if(StringUtils.isNotBlank(request.getIntroduce())) {
			update.set(FieldConstants.SPU_INTRODUCE, request.getIntroduce());
		}
		if(StringUtils.isNotBlank(request.getCategory())) {
			update.set(FieldConstants.CATEGORY, request.getCategory());
		}
		if(StringUtils.isNotBlank(request.getDomain())) {
			update.set(FieldConstants.DOMAIN, request.getIntroduce());
		}
		if(StringUtils.isNotBlank(request.getDetail())) {
			update.set(FieldConstants.SPU_DETAIL, request.getDetail());
		}
		update.set(FieldConstants.MDT, LocalDateTime.now());
		Commodity updatedRecord= mongoTemplate.findAndModify(
				query
				,update
				,FindAndModifyOptions.options().returnNew(true)
				,Commodity.class);
		return updatedRecord;
	}

}
