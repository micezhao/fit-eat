package kater;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fa.kater.KaterApplication8764;
import com.fa.kater.biz.auth.third.WxHandler;
import com.fa.kater.entity.bo.MerchantBo;
import com.fa.kater.pojo.MerchantThirdConfig;
import com.fa.kater.service.impl.MerchantInfoServiceImpl;
import com.fa.kater.service.impl.MerchantThirdConfigServiceImpl;


/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KaterApplication8764.class)
@WebAppConfiguration
public class KaterApplicationTest {

	@Autowired
	WxHandler wxAuthHandler;
	
	@Autowired
	MerchantThirdConfigServiceImpl merchantThirdConfigServiceImpl;
	
	@Autowired
	MerchantInfoServiceImpl merchantInfoServiceImpl;

	
	@Test
	public void getWXopenId() {
		MerchantThirdConfig config = new MerchantThirdConfig();
		config.setMerchantId("0");
		config = config.selectOne(new QueryWrapper<MerchantThirdConfig>(config));
		System.out.println("secretKey:"+config.getAppSecretkey());
		String openId = wxAuthHandler.getOpenId(config, "091Lg71w3r0hKU2REX0w3lzMqT2Lg71E");
		System.out.println("openId:"+openId);
	}
	
	@Test
	public void getAppsecretkey() {
		MerchantThirdConfig config = new MerchantThirdConfig();
		config.setMerchantId("0");
		config = config.selectOne(new QueryWrapper<MerchantThirdConfig>(config));

		System.out.println("appsecretkey:"+config.getAppSecretkey());
	}
	
	
	@Test
	public void getMerchantBo() {
		 MerchantBo bo= merchantInfoServiceImpl.getBoByMerId("0");
		System.out.println("holdername:"+bo.getMerchantHolderName());
	}

	

}
