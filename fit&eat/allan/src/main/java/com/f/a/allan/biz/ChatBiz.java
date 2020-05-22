package com.f.a.allan.biz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.Chat;
import com.f.a.allan.entity.pojo.ChatItem;
import com.f.a.allan.service.CalculatorService;
import com.f.a.allan.service.CalculatorService.PriceProccessor;

@Service
public class ChatBiz {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private CalculatorService calculatorService;
	
	/**
	 * 将商品加入购物车
	 * @param userAccount
	 * @param chatItem
	 */
	public void addItemToChat(String userAccount,ChatItem chatItem) {
		Chat chat = getChatByuserAccount(userAccount); // 先查询当前用户是否存在购物车
		List<ChatItem> chatItemList = null;
		if(chat == null) {
			// TODO 新增购物车，并将当前商品放入购物车中
			chatItemList = new ArrayList<ChatItem>();
			chatItemList.add(chatItem);
//			PriceProccessor priceProccessor= calculatorService.priceCalculator(chatItemList);
			chat = Chat.builder()
						.userAccount(userAccount)
						.itemList(chatItemList)
						.cdt(LocalDateTime.now()).build();
			mongoTemplate.insert(chat);
		}else {
			// 先获取到 用户已经存在的购物车内容，然后比对 新传入的商品 和 购物车中已经有的 是否有重复，如果有重复的内容就要合并
			chatItemList = chat.getItemList();
			boolean repeat = false;
			for (ChatItem element : chatItemList) {
				if(StringUtils.equals(chatItem.getGoodsId(),element.getGoodsId() )) {
					element.setNum(element.getNum() + chatItem.getNum());
					repeat = true;
					break;
				}
			}
			if(!repeat) {
				chatItemList.add(chatItem);
			}
			// 重新计算购物车价格
//			PriceProccessor priceProccessor= calculatorService.priceCalculator(chatItemList);
			chat.setMdt(LocalDateTime.now());
			// 执行购物车更新
			mongoTemplate.findAndReplace(new Query().addCriteria(new Criteria(FieldConstants.CHAT_ID).is(chat.getChatId())), chat);
		}
		
	}
	
	public Chat getChatByuserAccount(String userAccount) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.USER_ACCOUNT).is(userAccount));
		return mongoTemplate.findOne(query, Chat.class);
	}
	
	/**
	 * 从购物车中移除指定产品
	 * @param userAccount
	 * @param goodsId
	 */
	public void removeChatItem(String userAccount,String goodsId) {
		Chat chat = getChatByuserAccount(userAccount);
		if(chat.getItemList().size() == 1) { // 如果当前购物车就一个商品，那么就直接删除当前商品
			mongoTemplate.remove(new Query().addCriteria(new Criteria(FieldConstants.CHAT_ID).is(chat.getChatId())), Chat.class);
			return ;
		}
		Iterator<ChatItem>  Iterator = chat.getItemList().iterator();
		while(Iterator.hasNext()) {
			String curId = Iterator.next().getGoodsId();
			if(StringUtils.equals(curId, goodsId)) {
				Iterator.remove();;
				break;
			}
		}
		chat.setItemList(chat.getItemList());
		chat.setMdt(LocalDateTime.now());
		// 执行购物车更新
		mongoTemplate.findAndReplace(new Query().addCriteria(new Criteria(FieldConstants.CHAT_ID).is(chat.getChatId())), chat);
	}
	

	
	
	
}
