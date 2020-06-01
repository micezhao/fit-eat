package allan;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.f.a.allan.AllanApplication;
import com.f.a.allan.biz.CommodityBiz;
import com.f.a.allan.biz.GoodsBiz;
import com.f.a.allan.entity.request.GoodsItemRequest;

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
