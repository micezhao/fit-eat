package allan;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.f.a.allan.entity.pojo.Order;
import com.f.a.allan.service.impl.OrderServiceImpl;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AllanApplicationTest.class})
public class AllanApplicationTest {
	
	
	@Autowired
	OrderServiceImpl impl;
	
	@Test
	public void test1() {
//		OrderServiceImpl impl = new OrderServiceImpl();
		List<Order> list= impl.list();
		System.out.println(list.toString());
	}
}
