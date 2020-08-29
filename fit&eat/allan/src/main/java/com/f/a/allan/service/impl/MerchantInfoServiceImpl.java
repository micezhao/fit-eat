package com.f.a.allan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.f.a.allan.entity.bo.MerchantBo;
import com.f.a.allan.entity.pojo.MerchantInfo;
import com.f.a.allan.mapper.MerchantInfoMapper;
import com.f.a.allan.service.MerchantInfoService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author micezhao
 * @since 2020-08-24
 */
@Service
public class MerchantInfoServiceImpl extends ServiceImpl<MerchantInfoMapper, MerchantInfo> implements MerchantInfoService {
	
	@Autowired
	MerchantInfoMapper mapper;
	
	public MerchantBo getBoByMerId(String merchantId) {
		return mapper.getMerchantBoByMerId(merchantId);
	}
}
