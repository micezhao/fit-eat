package com.fa.kater.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fa.kater.entity.bo.MerchantBo;
import com.fa.kater.mapper.MerchantInfoMapper;
import com.fa.kater.pojo.MerchantInfo;
import com.fa.kater.service.MerchantInfoService;

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
