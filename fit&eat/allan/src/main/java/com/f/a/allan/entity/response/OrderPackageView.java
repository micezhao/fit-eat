package com.f.a.allan.entity.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.f.a.allan.entity.pojo.DeliveryInfo;
import com.f.a.allan.entity.pojo.GoodsItem;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
public class OrderPackageView {
	
	@Setter
	@Getter
	private String orderPackageId;
	@Setter
	@Getter
	private String cartId;
	
	@Setter
	@Getter
	private DeliveryInfo delivery;
	
	@Setter
	private List<OrderGoodsItemView> goodsItemList;
	
	@Getter
	@Setter
	private Map<String,List<OrderGoodsItemView>>  merchantVsOrder;
	
	@Setter
	@Getter
	private String userAccount;
	
	@Setter
	@Getter
	private int totalAmount;
	
	@Setter
	@Getter
	private int discountPrice;
	
	@Setter
	@Getter
	private int settlePrice;
	
	@Setter
	@Getter
	private String packageStatus;
	
	@Setter
	@Getter
	private LocalDateTime expireTime;
	
	@Setter
	@Getter
	private LocalDateTime payTime;
	
	@Setter
	@Getter
	private LocalDateTime cdt;
	
	
	public OrderPackageView(String orderPackageId, String cartId, DeliveryInfo delivery, List<OrderGoodsItemView> goodsItemList,
			Map<String, List<OrderGoodsItemView>> merchantVsOrder, String userAccount, int totalAmount, int discountPrice,
			int settlePrice, String packageStatus, LocalDateTime expireTime, LocalDateTime payTime,
			LocalDateTime cdt) {
		super();
		this.orderPackageId = orderPackageId;
		this.cartId = cartId;
		this.delivery = delivery;
		this.goodsItemList = goodsItemList;
		this.merchantVsOrder = renderProcess(goodsItemList);
		this.userAccount = userAccount;
		this.totalAmount = totalAmount;
		this.discountPrice = discountPrice;
		this.settlePrice = settlePrice;
		this.packageStatus = packageStatus;
		this.expireTime = expireTime;
		this.payTime = payTime;
		this.cdt = cdt;
	}
	
	private  Map<String,List<OrderGoodsItemView>> renderProcess(List<OrderGoodsItemView> goodsItemList){
		Map<String,List<OrderGoodsItemView>> map = new HashMap<String,List<OrderGoodsItemView>>();
		for (OrderGoodsItemView curGoodsItem : goodsItemList) {
			String curKey = curGoodsItem.getMerchantId()+"|"+curGoodsItem.getMerchantName();
			if(map.containsKey(curKey)) { 
				List<OrderGoodsItemView> ls= map.get(curKey);
				ls.add(curGoodsItem);
			}else {
				 List<OrderGoodsItemView> temp= new ArrayList<OrderGoodsItemView>();
				 temp.add(curGoodsItem);
				map.put(curKey, temp);
			}
		}
		return map;
	}
	
}
