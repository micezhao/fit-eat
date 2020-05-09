package com.f.a.allan.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.f.a.allan.entity.pojo.Merchant;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author micezhao
 * @since 2019-12-09
 */
@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {

}