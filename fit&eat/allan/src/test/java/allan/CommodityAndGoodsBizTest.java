package allan;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.AllanApplication;
import com.f.a.allan.biz.CommodityBiz;
import com.f.a.allan.biz.GoodsBiz;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.pojo.Commodity;
import com.f.a.allan.entity.request.GoodsItemRequest;
import com.f.a.allan.service.SkuConfigService;

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
	SkuConfigService skuConfigService;
	
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
	
	@org.junit.Test
	public void find_one() {
		Commodity json= mongoTemplate.findOne(new Query().addCriteria(new Criteria(FieldConstants.SPU_ID).is("5ed7a33bcd4b3933e1c2cc67"))
				, Commodity.class);
	}
	
	@org.junit.Test
	public void config_find_by_spuId() {
		List<JSONObject> json= skuConfigService.queryConfigBySpuId("5edb6296bcd8b94afd380c10");
		for (JSONObject jsonObject : json) {
			System.out.println("result-a:"+jsonObject);
		}
	}
}
