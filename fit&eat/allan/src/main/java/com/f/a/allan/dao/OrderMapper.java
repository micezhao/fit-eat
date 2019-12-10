package com.f.a.allan.dao;

import com.f.a.allan.pojo.Order;

import java.util.Calendar;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author micezhao
 * @since 2019-12-09
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
