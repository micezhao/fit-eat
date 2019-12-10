package com.f.a.allan.service.impl;

import com.f.a.allan.pojo.Order;
import com.f.a.allan.dao.OrderMapper;
import com.f.a.allan.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author micezhao
 * @since 2019-12-09
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
