package com.f.a.allan.dao;

import java.util.Calendar;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.f.a.allan.entity.pojo.Order;

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
	// 通过这种方式可以有效的降低xml文件带来的系统复杂性
	@Select("select * from order_20191212 where `status` = '${status}'")
	public List<Order> selectByStatus(@Param("status") String status);
} 
