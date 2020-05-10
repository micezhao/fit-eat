package com.f.a.allan.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.f.a.allan.dao.OrderMapper;
import com.f.a.allan.entity.pojo.Order;
import com.f.a.allan.service.OrderService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author micezhao
 * @since 2020-05-06
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
	
}
