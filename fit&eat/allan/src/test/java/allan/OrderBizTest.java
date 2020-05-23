package allan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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

	@org.junit.Test
	public void new_order_package() {
		DeliveryInfo delivery = DeliveryInfo.builder().
			receiveAddr("江岸区").deliveryTime("9:00").recevierName("zhuzhusong").recevierPhone("123").build();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put(FieldConstants.GOODS_ID, "5ebf2e7ae6b378647fdc4a47");
		map.put(FieldConstants.NUM, 3);
		list.add(map);
		orderBiz.packItem("78644", list, "93012130-96df-4025-9350-e503282c5dda", delivery);
	}
	
	@org.junit.Test
	public void chat_1() {
		ChatItem chatItem  = ChatItem.builder().goodsId("5ebf2e7ae6b378647fdc4a47").num(3).build();
		chatBiz.addItemToChat("93012130-96df-4025-9350-e503282c5dda", chatItem);
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
	
	
	
}
