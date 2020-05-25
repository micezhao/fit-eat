package com.f.a.allan.ctrl.client.order;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.biz.ChatBiz;
import com.f.a.allan.biz.OrderBiz;
import com.f.a.allan.biz.UserAddressBiz;
import com.f.a.allan.entity.bo.DeliveryInfo;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.allan.entity.pojo.UserAddress;
import com.f.a.allan.entity.request.OrderQueryRequst;
import com.f.a.allan.entity.request.OrderRequset;
import com.f.a.allan.entity.response.OrderGoodsItemView;
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
	
	@Autowired
	UserAddressBiz userAddressBiz;
	
	@Autowired
	ChatBiz chatBiz;
	
	/**
	 * 接受提交的商品列表，并将其组装成订单包
	 * @param goodItemList
	 * @param userAgent
	 * @return
	 * userAddressId : 5ebb4a46f9706c6ab2c1385f
	 */
	@PostMapping("package")
	public OrderPackage packGoodItem(@RequestBody OrderRequset request,UserAgent userAgent) {
		// 生成订单接口
//		String userAddressId = (String)request.getAttribute("userAddressId");
//		String deliveryTime = "2020-05-09 10:00-11:00";
//		String mome ="请勿放到快递柜";
		
		String userAccount= userAgent.getUserAccount();
		
		UserAddress userAddress = userAddressBiz.findById(request.getUserAddressId());
		DeliveryInfo info = DeliveryInfo.builder().deliveryTime(request.getDeliveryTime()).
			receiveAddr(userAddress.getAddrDetail()).
			recevierName(userAddress.getContactName()).recevierPhone(userAddress.getContactPhone()).build();
		JSONArray arr = new JSONArray();
		JSONObject json = new JSONObject();
		json.put(FieldConstants.GOODS_ID, "5ebf2e7ae6b378647fdc4a47");
		json.put(FieldConstants.NUM, 3);
		arr.add(json);
		orderBiz.packItem2(request.getChatId(),arr,userAccount,info);
		
		return null;
	}
	
	
	@GetMapping("/package")
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
	
	@PutMapping("/package/pay")
	public OrderPackage payById(UserAgent userAgent,@RequestBody OrderQueryRequst orderQueryRequst){
		Query query = new Query();
		query.addCriteria(Criteria.where(FieldConstants.ORDER_PACKAGE_ID).is(orderQueryRequst.getOrderPackageId()))
				.addCriteria(Criteria.where(FieldConstants.PACKAGE_STATUS).is(PackageStatusEnum.PAID));
		boolean paid = mongoTemplate.exists(query, OrderPackage.class);
		// 幂等性检查
		if(paid) { 
			return mongoTemplate.findOne(query, OrderPackage.class);
		} 
		OrderPackage packItem = orderBiz.paySucccessed(orderQueryRequst.getOrderPackageId(), orderQueryRequst.getFundTransferId());
		List<String> goodsIdList = new ArrayList<String>();
		for (OrderGoodsItemView  item: packItem.getItemList()) {
			goodsIdList.add(item.getGoodsId());
		}
		chatBiz.clearChatByGoodsIdList(packItem.getCartId(),goodsIdList);
		 return packItem;
	}
}	



