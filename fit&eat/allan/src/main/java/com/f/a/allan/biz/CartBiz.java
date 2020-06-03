package com.f.a.allan.biz;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.f.a.allan.entity.bo.CartItem;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.Cart;
import com.f.a.allan.entity.response.CartView;

@Service
public class CartBiz {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	
	/**
	 * 将商品加入购物车
	 * @param userAccount
	 */
	public void addItemToCart(String userAccount,String chatMerchant,CartItem cartItem) {
		Cart chat = getChatByuserAccount(userAccount,chatMerchant); // 先查询当前用户是否存在购物车
		List<CartItem> chatItemList = null;
		if(chat == null) {
			chatItemList = new ArrayList<CartItem>();
			chatItemList.add(cartItem);
			chat = Cart.builder()
						.userAccount(userAccount)
						.cartMerchant(chatMerchant)
						.itemList(chatItemList)
						.cdt(LocalDateTime.now()).build();
			mongoTemplate.insert(chat);
		}else {
			// 先获取到 用户已经存在的购物车内容，然后比对 新传入的商品 和 购物车中已经有的 是否有重复，如果有重复的内容就要合并
			chatItemList = chat.getItemList();
			if(chatItemList.size() >= 99) {
				throw new RuntimeException("购物车中的项目不能超过99个");
			}
			boolean repeat = false;
			for (CartItem element : chatItemList) {
				if(StringUtils.equals(cartItem.getGoodsId(),element.getGoodsId() )) {
					element.setNum(cartItem.getNum());
					repeat = true;
					break;
				}
			}
			if(!repeat) {
				chatItemList.add(cartItem);
			}
			// 重新计算购物车价格
//			PriceProccessor priceProccessor= calculatorService.priceCalculator(chatItemList);
			chat.setMdt(LocalDateTime.now());
			// 执行购物车更新
			mongoTemplate.findAndReplace(new Query().addCriteria(new Criteria(FieldConstants.CART_ID).is(chat.getCartId())), chat);
		}
	}
	
	/**
	 * 从购物车中减去 某个商品
	 * @param chatId
	 * @param chatItem [goodsId:商品id，num：当前商品的数量]
	 */
	public void subItemFromChat(String cartId,CartItem chatItem) {
		Cart chat = getCartById(cartId);
		Iterator<CartItem> iterator= chat.getItemList().iterator();
		while (iterator.hasNext()) {
			CartItem element = iterator.next();
			if(StringUtils.equals(chatItem.getGoodsId(),element.getGoodsId())) { 
				if(chatItem.getNum() == 0) {
					iterator.remove(); // 如果当前商品数量为0，那么就从购物车中删除这个商品
				}else {
					element.setNum(chatItem.getNum());
				}
			}
		}
		if(chat.getItemList().isEmpty()) { // 如果购物车中的所有的商品数量被减0，就删除购物车
			deleteChatById(cartId);
			return;
		}
		chat.setItemList(chat.getItemList());
		chat.setMdt(LocalDateTime.now());
		// 执行购物车更新
		mongoTemplate.findAndReplace(new Query().addCriteria(new Criteria(FieldConstants.CART_ID).is(cartId)), chat);
		
	}
	
	public Cart getChatByuserAccount(String userAccount,String chatMerchant) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.USER_ACCOUNT).is(userAccount));
		query.addCriteria(new Criteria(FieldConstants.CART_MERCHANT).is(chatMerchant));
		return mongoTemplate.findOne(query, Cart.class);
	}
	
	public Cart getCartById(String cartId) {
		Query query = new Query();
		query.addCriteria(new Criteria(FieldConstants.CART_ID).is(cartId));
		return mongoTemplate.findOne(query, Cart.class);
	}
	
	
	/**
	 * 从购物车中移除指定产品
	 * @param userAccount
	 * @param goodsId
	 */
	public void removeChatItem(String cartId,String goodsId) {
		Cart chat = getCartById(cartId);
		if(chat.getItemList().size() == 1) { // 如果当前购物车就一个商品，那么就直接删除当前商品
			mongoTemplate.remove(new Query().addCriteria(new Criteria(FieldConstants.CART_ID).is(chat.getCartId())), Cart.class);
			return ;
		}
		Iterator<CartItem>  Iterator = chat.getItemList().iterator();
		while(Iterator.hasNext()) {
			String curId = Iterator.next().getGoodsId();
			if(StringUtils.equals(curId, goodsId)) {
				Iterator.remove();;
				break;
			}
		}
		if(chat.getItemList().isEmpty()) { // 如果购物车中的所有的商品数量被减0，就删除购物车
			deleteChatById(cartId);
			return;
		}
		chat.setItemList(chat.getItemList());
		chat.setMdt(LocalDateTime.now());
		// 执行购物车更新
		mongoTemplate.findAndReplace(new Query().addCriteria(new Criteria(FieldConstants.CART_ID).is(chat.getCartId())), chat);
	}
	
	
	/**
	 * 当商品购买成功后，清空购物车的服务
	 * @param chatId 购物车编号
	 * @param goodsIdList 需要被清除的商品列表
	 */
	public void clearChatByGoodsIdList(String cartId,List<String> goodsIdList) {
		Cart chat = getCartById(cartId);
		Query query = new Query().addCriteria(new Criteria(FieldConstants.CART_ID).is(chat.getCartId()));
		
		for (String goodsId : goodsIdList) {
			Iterator<CartItem>  Iterator = chat.getItemList().iterator();
			while(Iterator.hasNext()) {
				String curId = Iterator.next().getGoodsId();
				if(StringUtils.equals(curId, goodsId)) {
					Iterator.remove();
					break;
				}
			}
		}
		if(chat.getItemList().isEmpty()) { // 如果此时购物车为空了，那么就删除掉整个购物车
			deleteChatById(cartId);
		}else {	// 执行购物车更新
			chat.setItemList(chat.getItemList());
			chat.setMdt(LocalDateTime.now());
			mongoTemplate.findAndReplace(query, chat);
		}
	}
	
	public void deleteChatById(String chatId) {
		mongoTemplate.findAllAndRemove(new Query().addCriteria(new Criteria(FieldConstants.CART_ID).is(chatId)), Cart.class);
	}
	
	
	
}
