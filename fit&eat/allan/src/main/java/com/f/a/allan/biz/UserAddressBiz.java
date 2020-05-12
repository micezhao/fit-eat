package com.f.a.allan.biz;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.beanutils.BeanIntrospector;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;
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

@Service
public class UserAddressBiz {
	
	private final static String ADDRESS_ID = "_id";
	
	private final static String USER_ACCOUNT = "userAccount";
	
	private final static String MERCHANT_ID = "merchantId";
	
	private final static String CONTACT_NAME = "contactName";
	
	private final static String CONTACT_PHONE=  "contactPhone";
	
	private final static String ADDR_DETAIL = "addrDetail";
	
	private final static String DR = "dr";
	
	private final static String CDT = "cdt";
	
	private final static String MDT = "mdt"; 

	@Autowired
	MongoTemplate mongoTemplate;
	
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
