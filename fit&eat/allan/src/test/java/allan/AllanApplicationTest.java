package allan;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.AllanApplication;
import com.f.a.allan.biz.OrderBiz;
import com.f.a.allan.biz.UserAddressBiz;
import com.f.a.allan.entity.pojo.DeliveryInfo;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.allan.entity.pojo.UserAddress;
import com.f.a.allan.entity.request.OrderQueryRequst;
import com.f.a.allan.enums.DrEnum;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AllanApplication.class)
@WebAppConfiguration
public class AllanApplicationTest {
	

	
	@Autowired
	OrderBiz orderBiz;

	@Autowired
	UserAddressBiz userAddressBiz;
	
	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	@org.junit.Test
	public void test1() {
		String cart = String.valueOf((int) (Math.random() * 100000));
		JSONObject json = new JSONObject();
		json.put("规格", "18cm");
		List<GoodsItem> list = new ArrayList<GoodsItem>();
		GoodsItem g1 = new GoodsItem("123", "virtual", json.toJSONString(), 2, "2.3", "18", "7777", "测试商户777");
		GoodsItem g2 = new GoodsItem("456", "substance", json.toJSONString(), 2, "2.3", "18", "8888", "测试商户8888");
		GoodsItem g3 = new GoodsItem("789", "virtual", json.toJSONString(), 2, "2.3", "18", "000000", "测试商户");
		GoodsItem g4 = new GoodsItem("001", "substance", json.toJSONString(), 2, "2.3", "18", "000000", "测试商户");
		list.add(g1);
		list.add(g2);
		list.add(g3);
		list.add(g4);
		
		UserAddress userAddress= userAddressBiz.findUserDefaultAddress("dfsdf", "0");
		DeliveryInfo delivery = DeliveryInfo.builder().deliveryTime("2020-05-18 13:00-14:00").memo("放在家门口")
										.receiveAddr(userAddress.getAddrDetail()).recevierName(userAddress.getContactName())
										.recevierPhone(userAddress.getContactPhone()).build();
		
		orderBiz.packItem(cart, list, "dfsdf", delivery);
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
