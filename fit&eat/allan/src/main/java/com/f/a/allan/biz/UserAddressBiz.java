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
import com.f.a.allan.entity.pojo.UserAddress;
import com.f.a.allan.entity.request.UserAddressQueryRequest;
import com.f.a.allan.enums.DrEnum;
import com.mongodb.client.result.DeleteResult;

@Service
public class UserAddressBiz {
	
	private final static String ADDRESS_ID = "_id";
	
	private final static String USER_ACCOUNT = "userAccount";
	
	private final static String MERCHANT_ID = "merchantId";
	
	private final static String CONTACT_NAME = "contactName";
	
	private final static String CONTACT_PHONE=  "contactPhone";
	
	private final static String ADDR_DETAIL = "addrDetail";
	
	private final static String DEFAULT_ADDR = "defaultAddr";
	
	private final static String DR = "dr";
	
	private final static String CDT = "cdt";
	
	private final static String MDT = "mdt"; 

	@Autowired
	MongoTemplate mongoTemplate;
	
	public UserAddress findUserDefaultAddress(String userAccount,String merchantId) {
		Query query = new Query();
		query.addCriteria(new Criteria(USER_ACCOUNT).is(userAccount))
		.addCriteria(new Criteria(DEFAULT_ADDR).is(true));
		 return mongoTemplate.findOne(query, UserAddress.class);
	}
	
	public List<UserAddress> listUserAddress(UserAddressQueryRequest request){
		Query query = new Query();
		if(StringUtils.isNotBlank(request.getMerchantId())) {
			query.addCriteria(new Criteria(MERCHANT_ID).is(request.getMerchantId()));
		}
		if(StringUtils.isNotBlank(request.getUserAccount())) {
			query.addCriteria(new Criteria(ADDRESS_ID).is(request.getUserAddressId()));
		}
		if(StringUtils.isNotBlank(request.getUserAccount())) {
			query.addCriteria(new Criteria(USER_ACCOUNT).is(request.getUserAccount()));
		}
		if(request.getDefaultAddr() != null) {
			query.addCriteria(new Criteria(DEFAULT_ADDR).is(request.getDefaultAddr().booleanValue()));
		}
		if(StringUtils.isNotBlank(request.getDr()) ) {
			query.addCriteria(new Criteria(DR).is(request.getDr()));
		}
		return mongoTemplate.find(query, UserAddress.class);
	}
	
	public UserAddress findById(String userAddressId) {
		Query query = new Query()
							.addCriteria(new Criteria(ADDRESS_ID).is(userAddressId));
		return mongoTemplate.findOne(query
					,UserAddress.class);
	}
	
	public void insert(UserAddress userAddress) {
		userAddress.setDr(DrEnum.AVAILABLE.getCode());
		userAddress.setCdt(LocalDateTime.now());
		mongoTemplate.insert(userAddress);
	}
	
	// 更新
	public UserAddress updateById(UserAddress replacement) {
		Query query = new Query();
		query.addCriteria(new Criteria(ADDRESS_ID).is(replacement.getUserAddressId()));
		Update update = new Update();
		if(StringUtils.isNotBlank(replacement.getContactName())) {
			update.set(CONTACT_NAME, replacement.getContactName());
		}
		if(StringUtils.isNotBlank(replacement.getContactPhone())) {
			update.set(CONTACT_PHONE, replacement.getContactPhone());
		}
		if(StringUtils.isNotBlank(replacement.getMerchantId())) {
			update.set(MERCHANT_ID, replacement.getMerchantId());
		}
		if(StringUtils.isNotBlank(replacement.getUserAccount())) {
			update.set(USER_ACCOUNT, replacement.getUserAccount());
		}
		if(StringUtils.isNotBlank(replacement.getAddrDetail())) {
			update.set(ADDR_DETAIL, replacement.getAddrDetail());
		}
		if(StringUtils.isNotBlank(replacement.getDr())) {
			update.set(DR, replacement.getDr());
		}
		update.set(MDT, LocalDateTime.now());
		UserAddress modified =  mongoTemplate.findAndModify(query, update,new FindAndModifyOptions().returnNew(true), UserAddress.class);
		return modified;
	}
	
	// TODO 将之前的 默认清空，换成当前的
	public UserAddress setDefault(String userAccount,String userAddresssId) {
		Query query = new Query();
		query.addCriteria(new Criteria(USER_ACCOUNT).is(userAccount))
				.addCriteria(new Criteria(DEFAULT_ADDR).is(true));
		mongoTemplate.findAndModify(	// 先将之前的默认收货地址置空
							query, 
							new Update().set(DEFAULT_ADDR, false).set(MDT, LocalDateTime.now()),
							UserAddress.class); 
		Query query2 = new Query();
		query2.addCriteria(new Criteria(ADDRESS_ID).is(userAddresssId));
		UserAddress resetUserAddress = mongoTemplate.findAndModify(
										query2,
										new Update().set(DEFAULT_ADDR, true).set(MDT, LocalDateTime.now()),
										new FindAndModifyOptions().returnNew(true), 
										UserAddress.class);
//		UserAddress resetUserAddress = mongoTemplate.update(UserAddress.class).matching(query2).replaceWith(replacement);
		return resetUserAddress;
	}
	
	//  切换可用状态
	public UserAddress switchDr(String userAddresssId) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and(ADDRESS_ID).is(userAddresssId);
		query.addCriteria(criteria);
		UserAddress userAddress = mongoTemplate.findOne(query, UserAddress.class);
		Update update = new Update();
		if(StringUtils.isBlank(userAddress.getDr())) {
			update.set(DR, DrEnum.AVAILABLE.getCode());
		}else if( DrEnum.getEnumByCode(userAddress.getDr())== DrEnum.AVAILABLE) {
			update.set(DR, DrEnum.DISABLE.getCode());
		}else {
			update.set(DR, DrEnum.AVAILABLE.getCode());
		}
		update.set(MDT, LocalDateTime.now());
		return mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().remove(true), UserAddress.class);
	}
	
	//  删除
	public void deleteById(String userAddresssId) {
		Query query = new Query();
		Criteria criteria = new Criteria();
		criteria.and(ADDRESS_ID).is(userAddresssId);
		criteria.and(DR).is(false);
		query.addCriteria(criteria);
		DeleteResult deleteResult = mongoTemplate.remove(query, UserAddress.class);
		if(!(deleteResult.getDeletedCount() > 0)) {
			throw new RuntimeException("当前地址处于可用状态，请先禁用再删除");
		}
		return ;
	}
	
	@Deprecated
	public UserAddress updateById2(UserAddress replacement) {
		Query query = new Query();
		query.addCriteria(new Criteria(ADDRESS_ID).is(replacement.getUserAddressId()));
		replacement.setMdt(LocalDateTime.now());
		FindAndReplaceOptions.options().returnNew();
		UserAddress modified =  mongoTemplate.update(UserAddress.class).matching(query).replaceWith(replacement)
				.withOptions(FindAndReplaceOptions.none()).withOptions(FindAndReplaceOptions.options().returnNew()).findAndReplaceValue();
		return modified;
	} 
	
}
