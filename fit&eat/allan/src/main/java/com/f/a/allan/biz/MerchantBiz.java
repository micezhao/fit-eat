package com.f.a.allan.biz;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.Merchant;
import com.f.a.allan.entity.request.MerchantQueryRequest;
import com.f.a.allan.entity.request.MerchantRequest;
import com.f.a.allan.enums.GoodsStatusEnum;
import com.f.a.allan.enums.MerchantStatus;
import com.f.a.allan.utils.ObjectUtils;
import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;

/**
 * 商户管理业务服务
 * 
 * @author micezhao
 *
 */
@Service
@Slf4j
/**
 * 商户操作业务类，TODO 需要通过后置切面记录操作记录
 * 
 * @author micezhao
 *
 */
public class MerchantBiz {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private GoodsBiz goodsBiz;
	

	/**
	 * 申请成为商户
	 * 
	 * @param request
	 * @return
	 */
	public Merchant enterApply(final MerchantRequest request) {
		// 分为 首次申请 或 非首次申请的业务处理
		if (StringUtils.isBlank(request.getMerchantId())) {
			return newMerchantSave(request);
		} else {
			request.setVerifyStatus(MerchantStatus.WAIT_VERIFY.getCode());
			return updateMerchantById(request);
		}
	}

	public Merchant updateMerchantById(final MerchantRequest request) {
		Update update = new Update();
		if (StringUtils.isNotBlank(request.getDescription())) {
			update.set(FieldConstants.MER_DESCRIPTION, request.getDescription());
		}
		if (StringUtils.isNotBlank(request.getHolderName())) {
			update.set(FieldConstants.HOLDER_NAME, request.getHolderName());
		}
		if (StringUtils.isNotBlank(request.getHolderPhone())) {
			update.set(FieldConstants.HOLDER_PHONE, request.getHolderPhone());
		}
		if (StringUtils.isNotBlank(request.getLogoUrl())) {
			update.set(FieldConstants.LOGO_URL, request.getLogoUrl());
		}
		if (StringUtils.isNotBlank(request.getMerchantName())) {
			update.set(FieldConstants.MERCHANT_NAME, request.getMerchantName());
		}
		if (StringUtils.isNotBlank(request.getScope())) {
			update.set(FieldConstants.SCOPE, request.getScope());
		}
		if (request.getCerts() != null && !request.getCerts().isEmpty()) {
			update.set(FieldConstants.CERT_LIST, request.getCerts());
		}
		if (StringUtils.isNotBlank(request.getOperationStatus())) {
			update.set(FieldConstants.OPERATION_STATUS, request.getOperationStatus());
		}
		if (StringUtils.isNotBlank(request.getVerifyStatus())) {
			update.set(FieldConstants.VERIFY_STATUS, request.getVerifyStatus());
		}
		update.set(FieldConstants.MDT, LocalDateTime.now());
		return mongoTemplate.findAndModify(
				new Query().addCriteria(new Criteria(FieldConstants.MERCHANT_ID).is(request.getMerchantId())), update,
				FindAndModifyOptions.options().returnNew(true), Merchant.class);
	}

	private Merchant newMerchantSave(final MerchantRequest request) {
		final Merchant dest = new Merchant();
		ObjectUtils.copy(dest, request);
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dest.setRegisterDate(sdf.format(new Date()));
		dest.setVerifyStatus(MerchantStatus.WAIT_VERIFY.getCode());
		dest.setCdt(LocalDateTime.now());
		return mongoTemplate.insert(dest);
	}

	/**
	 * 打回申请
	 * 
	 * @param request:主要要在 memo 填写打回的原因
	 * @return
	 */
	public Merchant rejectApply(String id) {
		final Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.MERCHANT_ID).is(id));
		final Merchant updatedRecord = mongoTemplate.findAndModify(query,
				new Update().set(FieldConstants.VERIFY_STATUS, MerchantStatus.UN_VERIFIED.getCode())
						.set(FieldConstants.MDT, LocalDateTime.now()),
				FindAndModifyOptions.options().returnNew(true), Merchant.class);
		return updatedRecord;
	}

	public Merchant approveApply(String id) {
		final Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.MERCHANT_ID).is(id));
		final Merchant updatedRecord = mongoTemplate.findAndModify(query,
				new Update().set(FieldConstants.VERIFY_STATUS, MerchantStatus.VERIFIED.getCode())
						.set(FieldConstants.OPERATION_STATUS, MerchantStatus.OPENING.getCode()).set(FieldConstants.MDT,
								LocalDateTime.now()),
				FindAndModifyOptions.options().returnNew(true), Merchant.class);
		return updatedRecord;
	}

	/**
	 * 
	 * @param id      商户标识
	 * @param command 操作命令
	 */
	public void operateById(final String id, final String command) {

		final Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.MERCHANT_ID).is(id));
		final Merchant item = mongoTemplate.findOne(query, Merchant.class);
		if (MerchantStatus.getEnumByCode(item.getVerifyStatus()) != MerchantStatus.VERIFIED) {
			log.error("商户：{}当前验证状态为：{},无法进行其他操作", item.getMerchantId(), item.getVerifyStatus());
			throw new RuntimeException("当前商户并未通过验证，无法进行其他操作");
		}
		final MerchantStatus targetStatus = MerchantStatus.getEnumByCode(command);
		mongoTemplate.findAndModify(query, new Update().set(FieldConstants.OPERATION_STATUS, targetStatus.getCode()),
				FindAndModifyOptions.options().returnNew(true), Merchant.class);
		new Runnable() { // 异步线程，将当前商户商品全部下架
			public void run() {
				GoodsStatusEnum goodsTargetStatus = null;
				if (targetStatus == MerchantStatus.OPENING) {
					goodsTargetStatus = GoodsStatusEnum.ON_SALE;
				} else if (targetStatus == MerchantStatus.SUSPENSION) {
					goodsTargetStatus = GoodsStatusEnum.SUSPENSION;
				} else if (targetStatus == MerchantStatus.CLOSED) {
					goodsTargetStatus = GoodsStatusEnum.UN_SOLD;
				} else if (targetStatus == MerchantStatus.OFF) {
					goodsTargetStatus = GoodsStatusEnum.UN_SOLD;
					goodsBiz.removeGoodsByMerchant(id);
					return;
				} else {
					return;
				}
				goodsBiz.updateGoodsStatusByMerchant(id, goodsTargetStatus.getCode());
			}
		}.run();
	}

	/**
	 * 根据merchantId 查询商户
	 * 
	 * @param request
	 * @return
	 */
	public Merchant queryById(final String id) {
		return mongoTemplate.findOne(new Query().addCriteria(new Criteria(FieldConstants.MERCHANT_ID).is(id)),
				Merchant.class);
	}

	public List<Merchant> listMerchant(final MerchantQueryRequest request) {
		final Query query = new Query();
		final Criteria criteria = new Criteria();
		if (StringUtils.isNotBlank(request.getMerchantId())) {
			criteria.and(FieldConstants.MERCHANT_ID).is(request.getMerchantId());
		}
		if (StringUtils.isNotBlank(request.getMerchantName())) {
			criteria.and(FieldConstants.MERCHANT_NAME).is(request.getMerchantName());
		}
		if (StringUtils.isNotBlank(request.getHolderName())) {
			criteria.and(FieldConstants.HOLDER_NAME).is(request.getHolderName());
		}
		if (StringUtils.isNotBlank(request.getHolderPhone())) {
			criteria.and(FieldConstants.HOLDER_PHONE).is(request.getHolderPhone());
		}
		if (request.getClassificationList() != null && !request.getClassificationList().isEmpty()) {
			criteria.and(FieldConstants.MER_CLASSIFICATION).in(request.getClassificationList());
		}
		if (request.getOperationStatusList() != null && !request.getOperationStatusList().isEmpty()) {
			criteria.and(FieldConstants.OPERATION_STATUS).in(request.getOperationStatusList());
		}
		if (request.getVerifyStatusList() != null && !request.getVerifyStatusList().isEmpty()) {
			criteria.and(FieldConstants.VERIFY_STATUS).in(request.getVerifyStatusList());
		}
		query.addCriteria(criteria);

		return mongoTemplate.find(query, Merchant.class);

	}

}
