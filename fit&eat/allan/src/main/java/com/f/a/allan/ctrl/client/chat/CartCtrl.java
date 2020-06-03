package com.f.a.allan.ctrl.client.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.allan.biz.CartBiz;
import com.f.a.allan.entity.bo.CartItem;
import com.f.a.allan.entity.request.CartRequest;
import com.f.a.allan.entity.response.CartView;
import com.f.a.allan.service.CartService;
import com.f.a.kobe.view.UserAgent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cart")
@Api(tags = "client-客户端购物车")
public class CartCtrl {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartBiz cartBiz;
	
	@ApiOperation("加入购物车")
	@PostMapping("/add")
	public ResponseEntity<Object> joinChat(UserAgent userAgent,@RequestBody CartRequest request) {
		CartItem cartItem = CartItem.builder().goodsId(request.getGoodsId()).num(request.getNum()).build();
		cartBiz.addItemToCart(userAgent.getUserAccount(), request.getCartMerchantId(),cartItem);
		return new ResponseEntity<Object>(true, HttpStatus.OK);
	}
	
	
	@ApiOperation("查询用户的购物车")
	@ApiImplicitParam(name="购物车所在的商户编号",value="merchantId")
	@GetMapping("/cartMerchantId/{merchantId}")
	public ResponseEntity<Object> getChatByUserAccount(@PathVariable("merchantId") String cartMerchantId,UserAgent userAgent) {
		List<CartView> list=cartService.getCartViewByUser(userAgent.getUserAccount(),cartMerchantId);
		if(list.isEmpty()) {
			return new ResponseEntity<Object>(null, HttpStatus.OK);
		}
		Map<String,List<CartView>>  map = renderProcess(list);
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}
	
	@ApiOperation("从购物车中清除指定商品")
	@ApiImplicitParams({
		@ApiImplicitParam(name="购物车编号",value="cartId"),
		@ApiImplicitParam(name="商品编号",value="goodsId")
	})
	@DeleteMapping("/{cartId}/{goodsId}")
	public ResponseEntity<Object> removeChatItem(@PathVariable("cartId") String cartId,@PathVariable("goodsId") String goodsId){
		cartBiz.removeChatItem(cartId, goodsId);
		return new ResponseEntity<Object>(true, HttpStatus.OK);
	}
	
	@ApiOperation("删除购物车")
	@ApiImplicitParam(name="购物车编号",value="cartId")
	@DeleteMapping("/{cartId}")
	public ResponseEntity<Object> removeChat(@PathVariable("cartId") String cartId){
		cartBiz.deleteChatById(cartId);
		return new ResponseEntity<Object>(true, HttpStatus.OK);
	}
	
	
	@ApiOperation("减少指定商品的数量")
	@ApiImplicitParams({
		@ApiImplicitParam(name="购物车编号",value="cartId"),
		@ApiImplicitParam(name="商品编号",value="goodsId"),	
		@ApiImplicitParam(name="当前数量",value="num")
	})
	@PutMapping("/{cartId}/{goodsId}/{num}")
	public ResponseEntity<Object> subItem(@PathVariable("cartId") String cartId,@PathVariable("goodsId") String goodsId){
		CartItem chatItem = CartItem.builder().goodsId(goodsId).build();
		cartBiz.subItemFromChat(cartId, chatItem);
		return new ResponseEntity<Object>(true, HttpStatus.OK);
	}
	
	private  Map<String,List<CartView>> renderProcess(List<CartView> cartViemList){
		Map<String,List<CartView>> map = new HashMap<String,List<CartView>>();
		for (CartView curGoodsItem : cartViemList) {
			String curKey = curGoodsItem.getMerchantId()+"|"+curGoodsItem.getMerchantName();
			if(map.containsKey(curKey)) { 
				List<CartView> ls= map.get(curKey);
				ls.add(curGoodsItem);
			}else {
				 List<CartView> temp= new ArrayList<CartView>();
				 temp.add(curGoodsItem);
				map.put(curKey, temp);
			}
		}
		return map;
	}
	
	
}
