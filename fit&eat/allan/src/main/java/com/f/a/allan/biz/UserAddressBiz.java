package com.f.a.allan.biz;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.UserAddress;
import com.f.a.allan.entity.request.UserAddressQueryRequest;
import com.f.a.allan.enums.DrEnum;
import com.mongodb.client.result.DeleteResult;

@Service
public class UserAddressBiz {
	

	@Autowired
	MongoTemplate mongoTemplate;
	
	public UserAddress findUserDefaultAddress(String userAccount,String merchantId) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.USER_ACCOUNT).is(userAccount))
		.addCriteria(new Criteria(FieldConstants.DEFAULT_ADDR).is(true));
		 return mongoTemplate.findOne(query, UserAddress.class);
	}
	
	public List<UserAddress> listUserAddress(UserAddressQueryRequest request){
		Query query = new Query();
		if(StringUtils.isNotBlank(request.getMerchantId())) {
			query.addCriteria(new Criteria(FieldConstants.MERCHANT_ID).is(request.getMerchantId()));
		}
		if(StringUtils.isNotBlank(request.getUserAccount())) {
			query.addCriteria(new Criteria(FieldConstants.ADDRESS_ID).is(request.getUserAddressId()));
		}
		if(StringUtils.isNotBlank(request.getUserAccount())) {
			query.addCriteria(new Criteria(FieldConstants.USER_ACCOUNT).is(request.getUserAccount()));
		}
		if(request.getDefaultAddr() != null) {
			query.addCriteria(new Criteria(FieldConstants.DEFAULT_ADDR).is(request.getDefaultAddr().booleanValue()));
		}
		if(StringUtils.isNotBlank(request.getDr()) ) {
			query.addCriteria(new Criteria(FieldConstants.DR).is(request.getDr()));
		}
		return mongoTemplate.find(query, UserAddress.class);
	}
	
	public UserAddress findById(String userAddressId) {
		Query query = new Query()
							.addCriteria(new Criteria(FieldConstants.ADDRESS_ID).is(userAddressId));
		return mongoTemplate.findOne(query
					,UserAddress.class);
	}
	
	public UserAddress insert(UserAddress userAddress) {
		userAddress.setDr(DrEnum.AVAILABLE.getCode());
		userAddress.setCdt(LocalDateTime.now());
		return mongoTemplate.insert(userAddress);
	}
	
	// 更新
	public UserAddress updateById(UserAddress replacement) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.ADDRESS_ID).is(replacement.getUserAddressId()));
		Update update = new Update();
		if(StringUtils.isNotBlank(replacement.getContactName())) {
			update.set(FieldConstants.CONTACT_NAME, replacement.getContactName());
		}
		if(StringUtils.isNotBlank(replacement.getContactPhone())) {
			update.set(FieldConstants.CONTACT_PHONE, replacement.getContactPhone());
		}
		if(StringUtils.isNotBlank(replacement.getMerchantId())) {
			update.set(FieldConstants.MERCHANT_ID, replacement.getMerchantId());
		}
		if(StringUtils.isNotBlank(replacement.getUserAccount())) {
			update.set(FieldConstants.USER_ACCOUNT, replacement.getUserAccount());
		}
		if(StringUtils.isNotBlank(replacement.getAddrDetail())) {
			update.set(FieldConstants.ADDR_DETAIL, replacement.getAddrDetail());
		}
		if(StringUtils.isNotBlank(replacement.getDr())) {
			update.set(FieldConstants.DR, replacement.getDr());
		}
		update.set(FieldConstants.MDT, LocalDateTime.now());
		UserAddress modified =  mongoTemplate.findAndModify(query, update,new FindAndModifyOptions().returnNew(true), UserAddress.class);
		return modified;
	}
	
	// TODO 将之前的 默认清空，换成当前的
	public UserAddress setDefault(String userAccount,String userAddresssId) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.USER_ACCOUNT).is(userAccount))
				.addCriteria(new Criteria(FieldConstants.DEFAULT_ADDR).is(true));
		mongoTemplate.findAndModify(	// 先将之前的默认收货地址置空
							query, 
							new Update().set(FieldConstants.DEFAULT_ADDR, false).set(FieldConstants.MDT, LocalDateTime.now()),
							UserAddress.class); 
		Query query2 = new Query();
		query2.addCriteria(new Criteria(FieldConstants.ADDRESS_ID).is(userAddresssId));
		UserAddress resetUserAddress = mongoTemplate.findAndModify(
										query2,
										new Update().set(FieldConstants.DEFAULT_ADDR, true).set(FieldConstants.MDT, LocalDateTime.now()),
										new FindAndModifyOptions().returnNew(true), 
										UserAddress.class);
//		UserAddress resetUserAddress = mongoTemplate.update(UserAddress.class).matching(query2).replaceWith(replacement);
		return resetUserAddress;
	}
	
	//  切换可用状态
	public UserAddress switchDr(String userAddresssId) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and(FieldConstants.ADDRESS_ID).is(userAddresssId);
		query.addCriteria(criteria);
		UserAddress userAddress = mongoTemplate.findOne(query, UserAddress.class);
		Update update = new Update();
		if(StringUtils.isBlank(userAddress.getDr())) {
			update.set(FieldConstants.DR, DrEnum.AVAILABLE.getCode());
		}else if( DrEnum.getEnumByCode(userAddress.getDr())== DrEnum.AVAILABLE) {
			update.set(FieldConstants.DR, DrEnum.DISABLE.getCode());
		}else {
			update.set(FieldConstants.DR, DrEnum.AVAILABLE.getCode());
		}
		update.set(FieldConstants.MDT, LocalDateTime.now());
		return mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().remove(true), UserAddress.class);
	}
	
	//  删除
	public void deleteById(String userAddresssId) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and(FieldConstants.ADDRESS_ID).is(userAddresssId);
		criteria.and(FieldConstants.DR).is(false);
		query.addCriteria(criteria);
		DeleteResult deleteResult = mongoTemplate.remove(query, UserAddress.class);
		if(!(deleteResult.getDeletedCount() > 0)) {
			throw new RuntimeException("当前地址处于可用状态，请先禁用再删除");
		}
		return ;
	}
	
}
