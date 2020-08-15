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
import com.fa.kater.pojo.AgentThirdConfig;
import com.fa.kater.service.impl.AgentThirdConfigServiceImpl;


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
	AgentThirdConfigServiceImpl agentThirdConfigServiceImpl;
	
	
	@Test
	public void getWXopenId() {
		AgentThirdConfig config = new AgentThirdConfig();
		config.setAgentId("0");
		config = config.selectOne(new QueryWrapper<AgentThirdConfig>(config));
		System.out.println("secretKey:"+config.getAppSecretkey());
		String openId = wxAuthHandler.getOpenId(config, "091Lg71w3r0hKU2REX0w3lzMqT2Lg71E");
		System.out.println("openId:"+openId);
	}
	

	

}
