package com.f.a.allan.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2019-12-09
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
	@Autowired
	private OrderMapper mapper;
	
	   public boolean save(Order order) {
		   order.setStatus("unpay");
		   order.setExpireTime(LocalDateTime.now().plusMinutes(15));
		   order.setCdt(LocalDateTime.now());
	       return retBool(baseMapper.insert(order));
	    }
	   
	   public List<Order> listByStatus(String status){
		 return mapper.selectByStatus(status);
	   }

}
