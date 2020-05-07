package com.f.a.allan.service.impl;

import com.f.a.allan.dao.OrderMapper;
import com.f.a.allan.entity.pojo.Order;
import com.f.a.allan.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
