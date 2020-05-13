package com.f.a.allan.ctrl.client;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.allan.biz.OrderBiz;
import com.f.a.allan.dao.mongo.OrderPackageMapper;
import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.allan.entity.request.OrderQueryRequst;
import com.f.a.allan.entity.response.OrderPackageView;
import com.f.a.allan.enums.PackageStatusEnum;
import com.f.a.kobe.view.UserAgent;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author micezhao
 * @since 2020-05-06
 */
@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	OrderBiz orderBiz;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	/**
	 * 接受提交的商品列表，并将其组装成订单包
	 * @param goodItemList
	 * @param userAgent
	 * @return
	 */
	@PostMapping
	public OrderPackage packGoodItem(HttpServletRequest request,UserAgent userAgent) {
		// 生成订单接口
		String userAddressId = (String)request.getAttribute("userAddressId");
		
		String deliveryTime = "2020-05-09 10:00-11:00";
		String mome ="请勿放到快递柜";
		
		orderBiz.packItem(null,null,null,null);
		
		return null;
	}
	
	
	@GetMapping("/package/")
	public List<OrderPackage> listOrderPackage(UserAgent userAgent,OrderQueryRequst orderQueryRequst){
		 String userAccount= userAgent.getUserAccount();
		 orderQueryRequst.setUserAccount(userAccount);
		 return  orderBiz.listOrderPackage(orderQueryRequst);
	}
	
	@GetMapping("/package/{id}")
	public OrderPackageView listOrderPackage(@PathVariable("id") String orderPackageId ,UserAgent userAgent,OrderQueryRequst orderQueryRequst){
		 orderQueryRequst.setOrderPackageId(orderPackageId);
		 OrderPackage packItem= orderBiz.findById(orderQueryRequst);
		 return orderBiz.rebuildPackageRender(packItem);
		   
	}
	
	@GetMapping("/package/pay/{id}")
	public OrderPackage payById(@PathVariable("id") String orderPackageId ,UserAgent userAgent,OrderQueryRequst orderQueryRequst){
		Query query = new Query();
		query.addCriteria(Criteria.where(OrderPackageMapper.ORDER_PACKAGE_ID).is(orderPackageId))
				.addCriteria(Criteria.where(OrderPackageMapper.PACKAGE_STATUS).is(PackageStatusEnum.PAID));
		boolean paid = mongoTemplate.exists(query, OrderPackage.class);
		// 幂等性检查
		if(paid) { 
			return mongoTemplate.findOne(query, OrderPackage.class);
		} 
		OrderPackage packItem = orderBiz.paySucccessed(orderPackageId);
		// TODO 清除购物车中的已购买项目
		 return packItem;
	}
}	



