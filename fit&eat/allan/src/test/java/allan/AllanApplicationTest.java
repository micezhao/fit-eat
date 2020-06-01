package allan;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;
import com.f.a.allan.AllanApplication;
import com.f.a.allan.biz.GoodsBiz;
import com.f.a.allan.biz.OrderBiz;
import com.f.a.allan.biz.UserAddressBiz;
import com.f.a.allan.entity.pojo.Goods;
import com.f.a.allan.entity.pojo.GoodsItem;
import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.allan.entity.pojo.UserAddress;
import com.f.a.allan.entity.request.GoodsItemQueryRequest;
import com.f.a.allan.entity.request.OrderQueryRequst;
import com.f.a.allan.enums.DrEnum;
import com.f.a.allan.service.GoodsItemService;

import lombok.extern.slf4j.Slf4j;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AllanApplication.class)
@WebAppConfiguration
@Slf4j
public class AllanApplicationTest {

	@Autowired
	GoodsBiz goodsBiz;

	@Autowired
	OrderBiz orderBiz;

	@Autowired
	UserAddressBiz userAddressBiz;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private GoodsItemService goodsItemService;

	@org.junit.Test
	public void goodsItem_1() {
		
	}
	@org.junit.Test
	public void goodsItem_deduct() { // 做边界测试
		goodsBiz.deduct("5ebf2e7ae6b378647fdc4a47", 100);
	}
	
	@org.junit.Test
	public void mulit() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(10);
		IntStream.rangeClosed(1, 10).forEach(i -> new Thread(() -> {
			System.out.println(Thread.currentThread().getName() + " is working.");
			goodsBiz.deduct("5ebf2e7ae6b378647fdc4a47", 7);
			latch.countDown();
		}, String.valueOf(i)).start());
		latch.await();
	}

	@org.junit.Test
	public void test2() {
		 List<GoodsItem> s= goodsItemService.findByReqeust(new GoodsItemQueryRequest());
		for (GoodsItem g : s) {
			System.out.println("当前查询的结果："+ g);
		}
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
		UserAddress u = userAddressBiz.findById("5eb8e5eb8f52645153a4c616");
		System.out.println("用户地址编码:" + u.getUserAddressId());
	}

	@org.junit.Test
	public void test6() {
		UserAddress userAddress = UserAddress.builder().userAddressId("5eb91891cb6a997a12ee5c98").userAccount("test")
				.contactName("tttttt").contactPhone("oiuttjm").addrDetail("dfdfdf").merchantId("0")
				.dr(DrEnum.AVAILABLE.getCode()).build();
		UserAddress u = userAddressBiz.updateById(userAddress);
		System.out.println("更新后用户的联系方式:" + u.getContactName());
	}

	@org.junit.Test
	public void test7() {
		UserAddress u = userAddressBiz.setDefault("dfsdf", "5eb904d1aac4727f112df9ab");
		System.out.println("更新后用户地址详情" + u.toString());
	}

	@org.junit.Test
	public void test8() {
		UserAddress u = userAddressBiz.switchDr("5eb8f4ccba80746250cdbaf5");
		System.out.println("更新后用户详情" + u.toString());
	}

	@org.junit.Test
	public void test9() {
		UserAddress u = userAddressBiz.findUserDefaultAddress("dfsdf", "0");
		System.out.println("用户默认地址详情" + u.toString());
	}

	@org.junit.Test
	public void test10() {
		orderBiz.paySucccessed("5ebbf8aaf391bf31ec296e0a", "fund000202020");
	}

	@org.junit.Test
	public void test11() {
		orderBiz.closePackage("5ebb4a46f9706c6ab2c1385e");
	}

}
