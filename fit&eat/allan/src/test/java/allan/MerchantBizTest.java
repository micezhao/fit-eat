package allan;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.f.a.allan.AllanApplication;
import com.f.a.allan.biz.MerchantBiz;
import com.f.a.allan.biz.OrderBiz;
import com.f.a.allan.biz.UserAddressBiz;
import com.f.a.allan.entity.pojo.Certificate;
import com.f.a.allan.entity.pojo.Merchant;
import com.f.a.allan.entity.request.MerchantQueryRequest;
import com.f.a.allan.entity.request.MerchantRequest;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AllanApplication.class)
@WebAppConfiguration
public class MerchantBizTest {

	@Autowired
	MerchantBiz merchantBiz;

	@Autowired
	OrderBiz orderBiz;

	@Autowired
	UserAddressBiz userAddressBiz;

	@org.junit.Test
	public void insertNewMerchant() {
		List<Certificate> list = new ArrayList<Certificate>();
		list.add(Certificate.builder().certType("LISENCE").certUrl("www.baidu.com").build());
		list.add(Certificate.builder().certType("TAX").certUrl("www.google.com").build());
		list.add(Certificate.builder().certType("HOLDER_IDENTITY_CARD").certUrl("www.github.com").build());
		MerchantRequest request= MerchantRequest.builder()
			.merchantName("micezhao 旗舰店")
			.holderName("micezhao")
			.holderPhone("8571732551")
			.description("test test test")
			.classification("便利店")
			.scope("日化,生活用品,体育用品")
			.certs(list).build();
		
		merchantBiz.enterApply(request);
	}
	
	@org.junit.Test
	public void reject_verify(){
		merchantBiz.rejectApply("5ec1fb217c71b741f4a4c7b8");
	}
	
	@org.junit.Test
	public void apply_verify_again(){
		MerchantRequest request= MerchantRequest.builder().merchantId("5ec1fb217c71b741f4a4c7b8").build();
		merchantBiz.enterApply(request);
	}
	
	@org.junit.Test
	public void approve_verify(){
		merchantBiz.approveApply("5ec25cdf19b8b53d7738d409");
	}
	
	@org.junit.Test
	public void merchant_operate(){
		merchantBiz.operateById("5ec1fb217c71b741f4a4c7b8", "suspension");
	}
	
	@org.junit.Test
	public void merchant_operate2(){
		merchantBiz.operateById("5ec1fb217c71b741f4a4c7b8", "opening");
	}
	
	@org.junit.Test
	public void merchant_list_query(){
		List<String> verifyStatusList = new ArrayList<String>();
		verifyStatusList.add("wait_verified");
		verifyStatusList.add("verified");
		MerchantQueryRequest request = MerchantQueryRequest.builder().build();
		List<Merchant> ms = merchantBiz.listMerchant(request);
		int i = 0;
		for (Merchant merchant : ms) {
			i++;
			System.out.println("第"+i+"商户条记录："+merchant.toString());
		}
	}
	
}
