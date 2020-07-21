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
import com.f.a.allan.biz.CommentBiz;
import com.f.a.allan.biz.MerchantBiz;
import com.f.a.allan.biz.OrderBiz;
import com.f.a.allan.biz.UserAddressBiz;
import com.f.a.allan.entity.bo.CartItem;
import com.f.a.allan.entity.constants.FieldConstants;
import com.f.a.allan.entity.request.comment.CommentAddRequest;
import com.f.a.allan.entity.request.comment.CommentUpdateRequest;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AllanApplication.class)
@WebAppConfiguration
public class CommentBizTest {

	@Autowired
	CommentBiz commentBiz;


	@org.junit.Test
	public void addComment() {
		CommentAddRequest request = CommentAddRequest.builder().goodsId("5ee716bccbee8a3962835713").spuId("5ee0b0019a4b35707fbf9c38").content("这个产品还是不错的").stars("4").build();
		
		commentBiz.addComment(request, "98765");
	}
	
	@org.junit.Test
	public void replyComment() {
		CommentUpdateRequest request = CommentUpdateRequest.builder().commentId("5ef9ff715f61f3398e8b1aa0").replyContnet("谢谢好评").build();
		commentBiz.replyComment("5ec1fb217c71b741f4a4c7b8", request);
	}
	
	
}
