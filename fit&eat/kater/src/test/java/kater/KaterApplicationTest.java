package kater;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.ws.rs.POST;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fa.kater.KaterApplication8764;
import com.fa.kater.biz.auth.third.WxHandler;
import com.fa.kater.entity.bo.MerchantBo;
import com.fa.kater.entity.requset.SysConfigRequest;
import com.fa.kater.pojo.AccessLog;
import com.fa.kater.pojo.MerchantThirdConfig;
import com.fa.kater.service.impl.AccessLogServiceImpl;
import com.fa.kater.service.impl.MerchantInfoServiceImpl;
import com.fa.kater.service.impl.MerchantThirdConfigServiceImpl;


/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KaterApplication8764.class)
@WebAppConfiguration
@AutoConfigureMockMvc //该注解将会自动配置mockMvc的单元测试
public class KaterApplicationTest {

	@Autowired
	WxHandler wxAuthHandler;
	
	@Autowired
	MerchantThirdConfigServiceImpl merchantThirdConfigServiceImpl;
	
	@Autowired
	MerchantInfoServiceImpl merchantInfoServiceImpl;
	
	@Autowired
	AccessLogServiceImpl accessLogServiceImpl;

	@Autowired
	private MockMvc mvc; //自动注入mockMvc的对象

	
	
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
	
	
	@Test
	public void pageAccessLog() {
		Page<AccessLog> page =new Page<AccessLog>();
		page.setCurrent(5L);
		page.setSize(5L);
		page.addOrder(OrderItem.ascs("gmt_create"));
		page = accessLogServiceImpl.page(page, new QueryWrapper<AccessLog>());
		System.out.println("total_page:"+page.getTotal());
		System.out.println("pageNum:"+page.getCurrent());
		for (AccessLog item : page.getRecords()) {
			System.out.println(item.toString());
		} 
	}
	
	@Test
	public void addSysParentDict() throws Exception {
		String url = "/admin/sysconfig/dict/parent";
		SysConfigRequest uriVars = new SysConfigRequest();
		uriVars.setGroup("certifications");
		
		MvcResult mvcResult = this.mvc
				.perform(
						MockMvcRequestBuilders
							.post(url)
							.content(JSONObject.toJSONString(uriVars))
							.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		System.out.println("mvc请求结束");
		
	}
	
	@Test
	public void addSysSubDict() throws Exception {
		String url = "/admin/sysconfig/dict/sub";
		SysConfigRequest uriVars = new SysConfigRequest();
		uriVars.setPid("1298179239002013697");
		uriVars.setGroup("certification");
		uriVars.setKey("idCard");
		uriVars.setKeyName("身份证");
		MvcResult mvcResult = this.mvc
				.perform(
						MockMvcRequestBuilders
							.post(url)
							.content(JSONObject.toJSONString(uriVars))
							.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		System.out.println("mvc请求结束");
		
	}
	
	
	

}
