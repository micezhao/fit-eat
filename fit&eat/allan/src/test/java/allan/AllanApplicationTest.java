package allan;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.quartz.LocalDataSourceJobStore;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.f.a.allan.AllanApplication;
import com.f.a.allan.dao.OrderPackageMapper;
import com.f.a.allan.entity.pojo.Book;
import com.f.a.allan.entity.pojo.Order;
import com.f.a.allan.entity.pojo.Order2;
import com.f.a.allan.entity.pojo.OrderPackage;
import com.f.a.allan.service.impl.OrderServiceImpl;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AllanApplication.class)
@WebAppConfiguration
public class AllanApplicationTest {
	
	@Autowired
	OrderServiceImpl service;
	
	@Autowired
	OrderPackageMapper orderPackageMapper;
	
	@Autowired
	MongoTemplate template;
	
	@org.junit.Test
	public void test1() {
		
		Order order = Order.builder().goodsId("123445")
					.category("crouse").status("success")
					.discountPrice("3.4").merchantId("0")
					.settlementPrice("98").userAccount("aaaaa").num(2).orderId("order202005061012").build();
		service.save(order);
	}
	
	@org.junit.Test
	public void test2() {
		
		Order order = Order.builder().goodsId("rtfgfgrg")
				.category("crouse").status("success")
				.discountPrice("3.4").merchantId("0")
				.settlementPrice("98").userAccount("sdsdsdsd").num(2).orderId("erererererre")
				.build();
		service.save(order);
		
		OrderPackage p = new OrderPackage();
		p.setCartId("54545454");
		List<Order> list = new ArrayList<Order>();
		list.add(order);
		p.setOrderList(list);
		template.insert(p);
	}
	
	@org.junit.Test
	public void test3() {
		Book book = new Book();
		book.setName("dfdfdfd");
		book.setPrice(67);
		book = template.insert(book);
	}
	
	@org.junit.Test
	public void test4() {
		 
		OrderPackage i = template.findOne(new Query(Criteria.where("_id").is("5eb3b51245f86531f3074734")), OrderPackage.class);
		System.out.println("查询结果："+i);
		System.out.println("完成时间："+ i.getOrderList().get(0).getFinishTime());
	}
}
