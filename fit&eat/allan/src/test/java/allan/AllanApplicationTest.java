package allan;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.AllanApplication;
import com.f.a.allan.biz.OrderBiz;
import com.f.a.allan.biz.UserAddressBiz;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.allan.entity.pojo.UserAddress;
import com.f.a.allan.entity.request.OrderQueryRequst;
import com.f.a.allan.entity.request.UserAddressQueryRequest;
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

	@org.junit.Test
	public void test1() {
		String cart = String.valueOf((int) (Math.random() * 100000));
		JSONObject json = new JSONObject();
		json.put("规格", "18cm");
		List<GoodsItem> list = new ArrayList<GoodsItem>();
		GoodsItem g1 = new GoodsItem("123", "virtual", json.toJSONString(), 2, "2.3", "18", "000000", "测试商户");
		GoodsItem g2 = new GoodsItem("456", "substance", json.toJSONString(), 2, "2.3", "18", "000000", "测试商户");
		GoodsItem g3 = new GoodsItem("789", "virtual", json.toJSONString(), 2, "2.3", "18", "000000", "测试商户");
		GoodsItem g4 = new GoodsItem("001", "substance", json.toJSONString(), 2, "2.3", "18", "000000", "测试商户");
		list.add(g1);
		list.add(g2);
		list.add(g3);
		list.add(g4);
//		orderBiz.packItem(cart, list, "micezhao");
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
	
}
