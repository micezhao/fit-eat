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
import com.f.a.allan.entity.bo.ChatItem;
import com.f.a.allan.entity.request.ChatRequest;
import com.f.a.allan.entity.response.ChatView;
import com.f.a.allan.service.CartService;
import com.f.a.kobe.view.UserAgent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/cart")
@Api(tags = "client-客户端购物车")
public class ChatCtrl {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartBiz cartBiz;
	
	@ApiOperation("加入购物车")
	@PostMapping("/add")
	public ResponseEntity<Object> joinChat(UserAgent userAgent,@RequestBody ChatRequest request) {
		ChatItem chatItem = ChatItem.builder().goodsId(request.getGoodsId()).num(request.getNum()).build();
		cartBiz.addItemToChat(userAgent.getUserAccount(), request.getChatMerchantId(),chatItem);
		return new ResponseEntity<Object>(true, HttpStatus.OK);
	}
	
	
	@ApiOperation("查询用户的购物车")
	@ApiImplicitParam(name="购物车所在的商户编号",value="chatMerchantId")
	@GetMapping("/{chatMerchantId}")
	public ResponseEntity<Object> getChatByUserAccount(@PathVariable("chatMerchantId") String chatMerchantId,UserAgent userAgent) {
		List<ChatView> list=cartService.getChatViewByUser(userAgent.getUserAccount(),chatMerchantId);
		if(list.isEmpty()) {
			return new ResponseEntity<Object>(null, HttpStatus.OK);
		}
		Map<String,List<ChatView>>  map = renderProcess(list);
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}
	
	@ApiOperation("从购物车中清除指定商品")
	@ApiImplicitParams({
		@ApiImplicitParam(name="购物车编号",value="chatId"),
		@ApiImplicitParam(name="商品编号",value="goodsId")
	})
	@DeleteMapping("/{chatId}/{goodsId}")
	public ResponseEntity<Object> removeChatItem(@PathVariable("chatId") String chatId,@PathVariable("goodsId") String goodsId){
		cartBiz.removeChatItem(chatId, goodsId);
		return new ResponseEntity<Object>(true, HttpStatus.OK);
	}
	
	@ApiOperation("删除购物车")
	@ApiImplicitParam(name="购物车编号",value="chatId")
	@DeleteMapping("/{chatId}")
	public ResponseEntity<Object> removeChat(@PathVariable("chatId") String chatId){
		cartBiz.deleteChatById(chatId);
		return new ResponseEntity<Object>(true, HttpStatus.OK);
	}
	
	
	@ApiOperation("减少指定商品的数量")
	@ApiImplicitParams({
		@ApiImplicitParam(name="购物车编号",value="chatId"),
		@ApiImplicitParam(name="商品编号",value="goodsId"),	
		@ApiImplicitParam(name="当前数量",value="num")
	})
	@PutMapping("/{chatId}/{goodsId}/{num}")
	public ResponseEntity<Object> subItem(@PathVariable("chatId") String chatId,@PathVariable("goodsId") String goodsId){
		ChatItem chatItem = ChatItem.builder().goodsId(goodsId).build();
		cartBiz.subItemFromChat(chatId, chatItem);
		return new ResponseEntity<Object>(true, HttpStatus.OK);
	}
	
	private  Map<String,List<ChatView>> renderProcess(List<ChatView> chatViemList){
		Map<String,List<ChatView>> map = new HashMap<String,List<ChatView>>();
		for (ChatView curGoodsItem : chatViemList) {
			String curKey = curGoodsItem.getMerchantId()+"|"+curGoodsItem.getMerchantName();
			if(map.containsKey(curKey)) { 
				List<ChatView> ls= map.get(curKey);
				ls.add(curGoodsItem);
			}else {
				 List<ChatView> temp= new ArrayList<ChatView>();
				 temp.add(curGoodsItem);
				map.put(curKey, temp);
			}
		}
		return map;
	}
	
	
}
