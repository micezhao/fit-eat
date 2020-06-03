package com.f.a.allan.ctrl.client.order;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.f.a.allan.biz.CartBiz;
import com.f.a.allan.biz.OrderBiz;
import com.f.a.allan.biz.UserAddressBiz;
import com.f.a.allan.entity.bo.DeliveryInfo;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.allan.entity.pojo.UserAddress;
import com.f.a.allan.entity.request.OrderQueryRequst;
import com.f.a.allan.entity.request.OrderRequset;
import com.f.a.allan.entity.response.OrderGoodsItemView;
import com.f.a.allan.enums.PackageStatusEnum;
import com.f.a.kobe.view.UserAgent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author micezhao
 * @since 2020-05-06
 */
@Slf4j
@Api(tags = "client-订单功能接口")
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
	CartBiz cartBiz;
	
	/**
	 * 接受提交的商品列表，并将其组装成订单包
	 * @param goodItemList
	 * @param userAgent
	 * @return
	 */
	@PostMapping("package/charge")
	@ApiOperation("生成支付包")
	public ResponseEntity<Object> packGoodItem(@RequestBody OrderRequset request,UserAgent userAgent) {		
		String userAccount= userAgent.getUserAccount();
		
		UserAddress userAddress = userAddressBiz.findById(request.getUserAddressId());
		DeliveryInfo info = DeliveryInfo.builder().deliveryTime(request.getDeliveryTime()).
			receiveAddr(userAddress.getAddrDetail()).
			recevierName(userAddress.getContactName()).recevierPhone(userAddress.getContactPhone()).build();
		JSONArray arr = request.getPackItemArr(); // 获取结算内容
		orderBiz.packItem(request.getCartId(),arr,userAccount,info);
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@ApiOperation("查询用户的订单包列表")
	@GetMapping("/package")
	public ResponseEntity<Object> listOrderPackage(UserAgent userAgent,@RequestBody OrderQueryRequst orderQueryRequst){
		 String userAccount= userAgent.getUserAccount();
		 orderQueryRequst.setUserAccount(userAccount);
		 List<OrderPackage> list=  orderBiz.listOrderPackage(orderQueryRequst);
		 return new ResponseEntity<Object>(list,HttpStatus.OK);
	}
	
	@ApiOperation("查询指定订单包")
	@ApiImplicitParam(name = "订单包编号",value = "id")
	@GetMapping("/package/{id}")
	public ResponseEntity<Object> listOrderPackage(@PathVariable("id") String orderPackageId ,UserAgent userAgent){
		 OrderPackage packItem= orderBiz.findById2(orderPackageId);
		 return new ResponseEntity<Object>( orderBiz.rebuildPackageRender(packItem),HttpStatus.OK);
		   
	}
	
	/**
	 * TODO 对未支付的订单进行支付的接口
	 * @param userAgent
	 * @param packageId
	 * @return
	 */
	@ApiOperation("支付订单包")
	@PutMapping("/package/pay/{packageId}")
	public ResponseEntity<Object> payById(UserAgent userAgent,@PathVariable("packageId")String packageId){
		Query query = new Query();
		query.addCriteria(Criteria.where(FieldConstants.ORDER_PACKAGE_ID).is(packageId))
				.addCriteria(Criteria.where(FieldConstants.PACKAGE_STATUS).is(PackageStatusEnum.CTEATE));
		boolean existed = mongoTemplate.exists(query, OrderPackage.class);
		
		if(!existed) {
			log.info("当前订单包{}不处于待支付状态",packageId);
			return new ResponseEntity<Object>( false,HttpStatus.OK);
		}
		// TODO 获取当前订单，组织支付参数，调用支付接口
		
		return new ResponseEntity<Object>( null,HttpStatus.OK);
	}
	
	
	@ApiOperation("订单包支付成功后分发订单，后端调用的远程接口")
	@PutMapping("/package/paySuccessed")
	public ResponseEntity<Object> paySuccessById(UserAgent userAgent,@RequestBody OrderQueryRequst orderQueryRequst){
		Query query = new Query();
		query.addCriteria(Criteria.where(FieldConstants.ORDER_PACKAGE_ID).is(orderQueryRequst.getOrderPackageId()))
				.addCriteria(Criteria.where(FieldConstants.PACKAGE_STATUS).is(PackageStatusEnum.PAID));
		boolean paid = mongoTemplate.exists(query, OrderPackage.class);
		OrderPackage opk = new OrderPackage();
		// 幂等性检查
		if(paid) { 
			opk = mongoTemplate.findOne(query, OrderPackage.class);
			return new ResponseEntity<Object>( opk,HttpStatus.OK);
		} 
		opk = orderBiz.paySucccessed(orderQueryRequst.getOrderPackageId(), orderQueryRequst.getFundTransferId());
		List<String> goodsIdList = new ArrayList<String>();
		for (OrderGoodsItemView  item: opk.getItemList()) {
			goodsIdList.add(item.getGoodsId());
		}
		if(StringUtils.isNotBlank(opk.getCartId()) ) { // 如果订单包中的购物车编号不为空，就去清除购物车中的指定内容
			cartBiz.clearChatByGoodsIdList(opk.getCartId(),goodsIdList);
		}
		return new ResponseEntity<Object>( opk,HttpStatus.OK);
	}
}	



