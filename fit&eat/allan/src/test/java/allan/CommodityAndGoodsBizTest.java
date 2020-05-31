package allan;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.AllanApplication;
import com.f.a.allan.biz.CartBiz;
import com.f.a.allan.biz.CommodityBiz;
import com.f.a.allan.biz.GoodsBiz;
import com.f.a.allan.biz.MerchantBiz;
import com.f.a.allan.biz.OrderBiz;
import com.f.a.allan.biz.UserAddressBiz;
import com.f.a.allan.entity.bo.ChatItem;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.request.GoodsItemRequest;
import com.netflix.discovery.converters.Auto;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AllanApplication.class)
@WebAppConfiguration
public class CommodityAndGoodsBizTest {

	@Autowired
	CommodityBiz commodityBiz;
	
	@Autowired
	GoodsBiz goodsBiz;
	
	@Autowired
	MongoTemplate mongoTemplate;

	@org.junit.Test
	public void goods_insert_one() {
		GoodsItemRequest request = new GoodsItemRequest();
		request.setSpuId("5ed1f446c273b87faae50cf4");
		request.setItemOutline("黑色");
		request.setStock(40);
		request.setPrice(1100);
		request.setDiscountPrice(100);
		goodsBiz.insertSku(request);
	}
	
	@org.junit.Test
	public void goods_put_on() {
		goodsBiz.putGoodsOn("5ed3bfe5710cef0697406149");
	}

	
}
