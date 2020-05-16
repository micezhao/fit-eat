package allan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.joda.time.LocalDateTime;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
	
	
	
	@org.junit.Test
	public void goodsItem_1() {
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
	public void goodsItem_put_on() {
		goodsBiz.putGoodsItemOn("5ebf2e7ae6b378647fdc4a47");
	}
	
	@org.junit.Test
	public void goodsItem_replenish() {
		goodsBiz.replenish("5ebf2e7ae6b378647fdc4a47", 25);
	}
	
	@org.junit.Test
	public void goodsItem_pull_out() { // 预期：下架后，redis数据清除，同时，不允许进行补货操作
		goodsBiz.pullGoodsItemOff("5ebf2e7ae6b378647fdc4a47");
	}
	
	@org.junit.Test
	public void goodsItem_deduct() { //做边界测试
		goodsBiz.deduct("5ebf2e7ae6b378647fdc4a47", 100);
	}
	
	@org.junit.Test
	public void goodsItem_deduct_multi() throws InterruptedException, ExecutionException { //多线程测试
		
		List<MulitDeductTask> taskList = new ArrayList<MulitDeductTask>();
		for (int i = 0; i < 100; i++) {
			MulitDeductTask task = new MulitDeductTask("5ebf2e7ae6b378647fdc4a47",3,goodsBiz);
			taskList.add(task);
		}
		
		List<Future<Boolean>> fl = new ArrayList<Future<Boolean>>();
		for (MulitDeductTask t : taskList) {
			Future<Boolean> f = taskExecutor.submit(t);
			fl.add(f);
		}
		int i = 0;
		int s = 0;
		for (Future<Boolean> future : fl) {
			if(future.get()) {
				i++;
				System.out.println("成功生成订单，订单号：" + i);
			}else {
				s ++;
				System.out.println("扣减失败：" + s);
			}
			
		}
	}
	
	public class MulitDeductTask implements Callable<Boolean> {
		
		private String goodsId;
		
		private int deduction;
		
		private GoodsBiz goodsBiz;
		

		public String getGoodsId() {
			return goodsId;
		}

		public void setGoodsId(String goodsId) {
			this.goodsId = goodsId;
		}

		public int getDeduction() {
			return deduction;
		}

		public void setDeduction(int deduction) {
			this.deduction = deduction;
		}

		public GoodsBiz getGoodsBiz() {
			return goodsBiz;
		}

		public void setGoodsBiz(GoodsBiz goodsBiz) {
			this.goodsBiz = goodsBiz;
		}
		
		public MulitDeductTask(String goodsId, int deduction, GoodsBiz goodsBiz) {
			super();
			this.goodsId = goodsId;
			this.deduction = deduction;
			this.goodsBiz = goodsBiz;
		}

		@Override
		public Boolean call() throws Exception {
			log.info("执行线程：{}",Thread.currentThread().getName());
			return goodsBiz.deduct(goodsId, deduction);
		}
		
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
