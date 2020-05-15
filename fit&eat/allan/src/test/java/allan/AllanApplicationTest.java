package allan;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.AllanApplication;
import com.f.a.allan.biz.GoodsBiz;
import com.f.a.allan.biz.OrderBiz;
import com.f.a.allan.biz.UserAddressBiz;
import com.f.a.allan.entity.pojo.DeliveryInfo;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.allan.entity.pojo.UserAddress;
import com.f.a.allan.entity.request.OrderQueryRequst;
import com.f.a.allan.enums.DrEnum;
import com.f.a.allan.enums.GoodsItemCategoryEnum;
import com.f.a.allan.enums.GoodsStatusEnum;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AllanApplication.class)
@WebAppConfiguration
public class AllanApplicationTest {
	
	@Autowired
	GoodsBiz goodsBiz;
	
	@Autowired
	OrderBiz orderBiz;

	@Autowired
	UserAddressBiz userAddressBiz;
	
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	@org.junit.Test
	public void test1() {
		JSONObject itemOutline = new JSONObject();
		itemOutline.put("规格", "1.8m * 1.2m");
		itemOutline.put("安装方式", "挂装 或 座装");
		JSONArray domain = new JSONArray();
		domain.add("家具");
		domain.add("生活用品");
		GoodsItem goodsItem = GoodsItem.builder().category(GoodsItemCategoryEnum.SUBSTAINTIAL.getCode())
						.goodsName("测试商品1").merchantId("909090")
						.merchantName("测试商品旗舰店").stock(90).price("68")
						.discountPrice("11").itemOutline(itemOutline.toJSONString())
						.domain(domain.toJSONString()).build();
		goodsBiz.insert(goodsItem);
	}

	@org.junit.Test
	public void test2() {
		OrderQueryRequst r = OrderQueryRequst.builder().orderPackageId("5eb77acae7b12b53a30fee0f").build();
		OrderPackage p = orderBiz.findById(r);
		System.out.println(p.getOrderPackageId());
//		orderBiz.closePackage("5eb77d169ad6660f58cb744d");
	}

	@org.junit.Test
	public void test4() {
		OrderQueryRequst r = OrderQueryRequst.builder().build();
		List<OrderPackage> list = orderBiz.listOrderPackage(r);
		for (OrderPackage orderPackage : list) {
			System.out.println("订单包:" + orderPackage.toString());
		}
		System.out.println("共输出" + list.size() + "个订单包");
	}

	@org.junit.Test
	public void test3() {
		UserAddress userAddress = UserAddress.builder().userAccount("99887").contactName("micezhao222")
								.contactPhone("8888").addrDetail("dfdfdfdfdf").merchantId("0").build();
		userAddressBiz.insert(userAddress);
	}
	
	@org.junit.Test
	public void test5() {
		UserAddress u=  userAddressBiz.findById("5eb8e5eb8f52645153a4c616");
		System.out.println("用户地址编码:"+u.getUserAddressId());
	}
	
	@org.junit.Test
	public void test6() {
		UserAddress userAddress = UserAddress.builder().userAddressId("5eb91891cb6a997a12ee5c98").userAccount("test").contactName("tttttt")
				.contactPhone("oiuttjm").addrDetail("dfdfdf").merchantId("0").dr(DrEnum.AVAILABLE.getCode()).build();
		UserAddress u = userAddressBiz.updateById(userAddress);
		System.out.println("更新后用户的联系方式:"+u.getContactName());
	}
	
	@org.junit.Test
	public void test7() {
		UserAddress u = userAddressBiz.setDefault("dfsdf", "5eb904d1aac4727f112df9ab");
		System.out.println("更新后用户地址详情"+u.toString());
	}
	
	@org.junit.Test
	public void test8() {
		UserAddress u = userAddressBiz.switchDr("5eb8f4ccba80746250cdbaf5");
		System.out.println("更新后用户详情"+u.toString());
	}
	
	@org.junit.Test
	public void test9() {
		UserAddress u = userAddressBiz.findUserDefaultAddress("dfsdf","0");
		System.out.println("用户默认地址详情"+u.toString());
	}
	
	@org.junit.Test
	public void test10() {
		orderBiz.paySucccessed("5ebbf8aaf391bf31ec296e0a","fund000202020");
	}
	
	@org.junit.Test
	public void test11() {
		orderBiz.closePackage("5ebb4a46f9706c6ab2c1385e");
	}
	
	
}
