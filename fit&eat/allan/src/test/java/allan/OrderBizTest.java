package allan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators.ToObjectId;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.AllanApplication;
import com.f.a.allan.biz.ChatBiz;
import com.f.a.allan.biz.MerchantBiz;
import com.f.a.allan.biz.OrderBiz;
import com.f.a.allan.biz.UserAddressBiz;
import com.f.a.allan.entity.bo.ChatItem;
import com.f.a.allan.entity.bo.DeliveryInfo;
import com.f.a.allan.entity.constants.FieldConstants;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AllanApplication.class)
@WebAppConfiguration
public class OrderBizTest {

	@Autowired
	MerchantBiz merchantBiz;

	@Autowired
	OrderBiz orderBiz;
	
	@Autowired
	ChatBiz chatBiz;

	@Autowired
	UserAddressBiz userAddressBiz;
	
	@Autowired
	MongoTemplate mongoTemplate;

	@org.junit.Test
	public void new_order_package() {
//		DeliveryInfo delivery = DeliveryInfo.builder().
//			receiveAddr("江岸区").deliveryTime("9:00").recevierName("zhuzhusong").recevierPhone("123").build();
//		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put(FieldConstants.GOODS_ID, "5ec201476135d4020e727da6");
//		map.put(FieldConstants.NUM, 3);
//		list.add(map);
//		orderBiz.packItem("78644", list, "93012130-96df-4025-9350-e503282c5dda", delivery);
	}
	
	@org.junit.Test
	public void chat_1() {
		ChatItem chatItem  = ChatItem.builder().goodsId("5ec201476135d4020e727da6").num(3).build();
		chatBiz.addItemToChat("93012130-96df-4025-9350-e503282c5dda", "test",chatItem);
	}
	
	
	@org.junit.Test
	public void chat_2() {
		chatBiz.removeChatItem("93012130-96df-4025-9350-e503282c5dda", "5ec201476135d4020e727da6");
	}
	
	@org.junit.Test
	public void chat_3() {
		List<String> goodsIdList = new ArrayList<String>();
		goodsIdList.add("5ec201476135d4020e727da6");
		goodsIdList.add("5ebf2e7ae6b378647fdc4a47");
		chatBiz.clearChatByGoodsIdList("93012130-96df-4025-9350-e503282c5dda", goodsIdList);
	}
	
	@org.junit.Test
	public void chat_4() {
		ChatItem item = ChatItem.builder().goodsId("5ebf2e7ae6b378647fdc4a47").num(0).build(); 
		chatBiz.subItemFromChat("5ec8a031bf1ef62fe4397404", item);
	}
	

	@org.junit.Test
	public void chat_5() {
		
//		AggregationOperation ao = new AggregationOperation() {
//			@Override
//			public Document toDocument(AggregationOperationContext context) {
//				JSONObject json = new JSONObject();
//				json.put("$toObjectId", "$itemList.goodsId");
//				return new Document("$addFields", new Document("goodsItemId",json));
//			}
//		};
	
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(new Criteria(FieldConstants.USER_ACCOUNT).is("93012130-96df-4025-9350-e503282c5dda")),
				Aggregation.unwind("itemList", "index", true),
				aggregateAddFields("goodsItemId", "$toObjectId", "$itemList.goodsId"),
				Aggregation.lookup("goodsItem", "goodsItemId", "_id", "chat_doc"),
				Aggregation.unwind("$chat_doc"),
				aggregateAddFields("moid", "$toObjectId", "$chat_doc.merchantId"),
				Aggregation.lookup("merchant", "moid", "_id", "merchant"),
				Aggregation.unwind("$merchant"),
				Aggregation.project().andInclude("_id","userAccount",
							"$itemList.num","$itemList.goodsId",
							"$chat_doc.goodsName","$chat_doc.category","$chat_doc.domain",
							"$chat_doc.goodsStatus","$chat_doc.discountPrice","$chat_doc.price","$chat_doc.merchantId",
							"$merchant.merchantName"),
				Aggregation.project().andExclude("cdt","mdt","moid","goodsItemId")
				);
		 AggregationResults<JSONObject> result = mongoTemplate.aggregate(aggregation, "chat", JSONObject.class);
		 List<JSONObject> ls= result.getMappedResults();
		
		 for (int i = 0; i < ls.size(); i++) {
			System.out.println("当前购物车的内容："+ls.get(i).toString());
		 }
	}
	
	private AggregationOperation aggregateAddFields(final String field, final String valueExpresion) {
		
		AggregationOperation ao = new AggregationOperation() {
			@Override
			public Document toDocument(AggregationOperationContext context) {
				return new Document("$addFields", new Document(field,valueExpresion));
			}
		};
		return ao;
	}
	
private AggregationOperation aggregateAddFields(final String field, String ops,String fieldExpresion) {
		
		AggregationOperation ao = new AggregationOperation() {
			@Override
			public Document toDocument(AggregationOperationContext context) {
				JSONObject json = new JSONObject();
				json.put(ops, fieldExpresion);
				return new Document("$addFields", new Document(field,json));
			}
		};
		return ao;
	}
	
}
