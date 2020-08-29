package com.f.a.allan.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.f.a.allan.entity.bo.MerchantBo;
import com.f.a.allan.entity.pojo.MerchantInfo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author micezhao
 * @since 2020-08-24
 */

public interface MerchantInfoMapper extends BaseMapper<MerchantInfo> {
	
	public MerchantBo getMerchantBoByMerId(@Param("merchantId")String merchantId);
	
	public List<MerchantBo> getMerchantBoByMerAgentId(@Param("agentId")String agentId);
	
}
