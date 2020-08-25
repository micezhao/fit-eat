package com.fa.kater.biz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fa.kater.entity.bo.Certificate;
import com.fa.kater.entity.requset.MerchantRequest;
import com.fa.kater.enums.MerchantStatusEnum;
import com.fa.kater.pojo.AgentInfo;
import com.fa.kater.pojo.MerchantInfo;
import com.fa.kater.pojo.MerchantThirdConfig;
import com.fa.kater.service.impl.AgentInfoServiceImpl;
import com.fa.kater.service.impl.MerchantInfoServiceImpl;
import com.fa.kater.service.impl.MerchantThirdConfigServiceImpl;

@Service
public class MerchantBiz {
	
	@Autowired
	private MerchantInfoServiceImpl merchantService;
	
	@Autowired
	private MerchantThirdConfigServiceImpl merchantThirdConfigService;
	
	@Autowired
	private AgentInfoServiceImpl agentInfoService;
	
	
	public AgentInfo addAgent(MerchantRequest request) {
		AgentInfo agentInfo = new AgentInfo();
		if(StringUtils.isNotBlank(request.getAgentName())) {
			agentInfo.setAgentName(request.getAgentName());
		}
		if(StringUtils.isNotBlank(request.getHolderName())) {
			agentInfo.setHolderName(request.getHolderName());
		}
		if(StringUtils.isNotBlank(request.getHolderPhone())) {
			agentInfo.setHolderContact(request.getHolderPhone());
		}
		if(StringUtils.isNotBlank(request.getTerritory())) {
			agentInfo.setTerritory(request.getTerritory());
		}
		if(StringUtils.isNotBlank(request.getIntroduce())) {
			agentInfo.setIntroduce(request.getIntroduce());
		}
		if(StringUtils.isNotBlank(request.getLogoUrl())) {
			agentInfo.setLogoImg(request.getLogoUrl());
		}
		agentInfoService.save(agentInfo);
		return agentInfo;
	}
	
	
	/**
	 * 申请商户入驻
	 * @param request
	 * @return
	 */
	@Transactional
	public MerchantInfo apply(MerchantRequest request) {
		MerchantInfo merchant  = new MerchantInfo();
		merchant.setAgentId(request.getAgentId())
			.setMerchantName(request.getMerchantName())
			.setMerchantId(UUID.randomUUID().toString())
			.setDeleted(0)
			.setState(MerchantStatusEnum.WAIT_VERIFY.getCode())
			.setRegisterDate(new SimpleDateFormat("yyyy-MM-dd ").format(new Date()));
		if(!request.getCerts().isEmpty()) {
			List<Certificate> list = request.getCerts();
			for (Certificate certificate : list) {
				// TODO 插入商户证件表
			}
		}
		if(StringUtils.isNotBlank(request.getHolderName())) {
			merchant.setHolderName(request.getHolderName());
		}
		if(StringUtils.isNotBlank(request.getHolderPhone())) {
			merchant.setHolderContact(request.getHolderPhone());
		}
		if(StringUtils.isNotBlank(request.getLogoUrl())) {
			merchant.setLogoImgUrl(request.getLogoUrl());
		}
		if(StringUtils.isNotBlank(request.getLogoUrl())) {
			merchant.setLogoImgUrl(request.getLogoUrl());
		}
		if(StringUtils.isNotBlank(request.getIntroduce())) {
			merchant.setIntroduce(request.getIntroduce());
		}
		if(StringUtils.isNotBlank(request.getScope())) {
			merchant.setScope(request.getScope());
		}
		if(StringUtils.isNotBlank(request.getType())) {
			merchant.setType(request.getType());
		}
		 merchant.insert();
		return merchant;
	}
	
	
	/**
	 * 审核操作
	 * @param merchantId
	 * @param merchantStatus
	 * @return
	 */
	public MerchantInfo verifyOperate(String merchantId,MerchantStatusEnum merchantStatus) {
		MerchantInfo merchant = new MerchantInfo();
		boolean result = merchantService.update(merchant.setState(merchantStatus.getCode()), 
				new UpdateWrapper<MerchantInfo>(
						new MerchantInfo().setMerchantId(merchantId)
						)
				);
		if(!result) {
			throw new RuntimeException("商户状态更新失败");
		}
		return merchant;
	}
	
	
	public MerchantInfo setOperationStatus(String merchantId,MerchantStatusEnum merchantStatus) {
		MerchantInfo merchant = new MerchantInfo();
		boolean result = merchantService.update(merchant.setState(merchantStatus.getCode()), 
				new UpdateWrapper<MerchantInfo>(
						new MerchantInfo().setMerchantId(merchantId)
						)
				);
		if(!result) {
			throw new RuntimeException("商户状态更新失败");
		}
		return merchant;
	}
	
	
	
	public MerchantInfo getMerchantInfoByMerId(String merchantId) {
		return merchantService.getOne(
				new QueryWrapper<MerchantInfo>(new MerchantInfo().setMerchantId(merchantId))
			);
	}
	
	public MerchantThirdConfig getMerchantThirdConfigByMerId(String merchantId) {
		return merchantThirdConfigService.getOne(
				new QueryWrapper<MerchantThirdConfig>(new MerchantThirdConfig().setMerchantId(merchantId))
			);
	}
	
	
	
}
