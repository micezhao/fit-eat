package com.f.a.allan.ctrl.client.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.allan.entity.response.ChatView;
import com.f.a.allan.entity.response.OrderGoodsItemView;
import com.f.a.allan.service.ChatService;
import com.f.a.kobe.view.UserAgent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/chat")
@Api(tags = "client-客户端购物车")

public class ChatCtrl {
	
	@Autowired
	private ChatService chatService;
	
	@ApiOperation("加入购物车")
	@PostMapping("/join")
	public ResponseEntity<Object> joinChat() {
		return new ResponseEntity<Object>(null, HttpStatus.OK);
	}
	
	@GetMapping("/user/{merchantId}")
	public ResponseEntity<Object> getChatByUserAccount(@PathVariable("merchantId") String merchantId,UserAgent userAgent) {
		List<ChatView> list=chatService.getChatViewByUser(userAgent.getUserAccount(),merchantId);
		Map<String,List<ChatView>>  map = renderProcess(list);
		return new ResponseEntity<Object>(map, HttpStatus.OK);
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
