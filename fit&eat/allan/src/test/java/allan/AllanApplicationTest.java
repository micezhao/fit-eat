package allan;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.f.a.allan.AllanApplication;
import com.f.a.allan.dao.OrderPackageMapper;
import com.f.a.allan.entity.pojo.Order;
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
		
	}
	
	
	
	@org.junit.Test
	public void test4() {
		 
	}
}
