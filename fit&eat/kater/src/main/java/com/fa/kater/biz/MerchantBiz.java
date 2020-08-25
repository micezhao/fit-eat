package com.fa.kater.biz;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fa.kater.entity.requset.MerchantRequest;
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
	
	
	// TODO 商户申请
	
	// TODO 操作商户状态
	
	// TODO 移除商户
	
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
