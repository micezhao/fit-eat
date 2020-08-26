package allan;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.f.a.allan.AllanApplication;
import com.f.a.allan.entity.bo.MerchantBo;
import com.f.a.allan.service.impl.MerchantInfoServiceImpl;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AllanApplication.class)
@WebAppConfiguration
public class MerchantBizTest2 {
	
	@Autowired
	private MerchantInfoServiceImpl merchantInfoServiceImpl ;
	
	@Test
	public void getMerchantBo() {
		 MerchantBo bo= merchantInfoServiceImpl.getBoByMerId("0");
		System.out.println("holdername:"+bo.getMerchantHolderName());
	}
	
	
	
}
