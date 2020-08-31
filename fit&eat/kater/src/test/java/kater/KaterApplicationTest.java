package kater;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.POST;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
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

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Unit test for simple App.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KaterApplication8764.class)
@WebAppConfiguration
@Slf4j
@AutoConfigureMockMvc // 该注解将会自动配置mockMvc的单元测试
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
	private MockMvc mvc; // 自动注入mockMvc的对象

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	public void getWXopenId() {
		MerchantThirdConfig config = new MerchantThirdConfig();
		config.setMerchantId("0");
		config = config.selectOne(new QueryWrapper<MerchantThirdConfig>(config));
		System.out.println("secretKey:" + config.getAppSecretkey());
		String openId = wxAuthHandler.getOpenId(config, "091Lg71w3r0hKU2REX0w3lzMqT2Lg71E");
		System.out.println("openId:" + openId);
	}

	@Test
	public void getAppsecretkey() {
		MerchantThirdConfig config = new MerchantThirdConfig();
		config.setMerchantId("0");
		config = config.selectOne(new QueryWrapper<MerchantThirdConfig>(config));

		System.out.println("appsecretkey:" + config.getAppSecretkey());
	}

	@Test
	public void getMerchantBo() {
		MerchantBo bo = merchantInfoServiceImpl.getBoByMerId("0");
		System.out.println("holdername:" + bo.getMerchantHolderName());
	}

	@Test
	public void pageAccessLog() {
		Page<AccessLog> page = new Page<AccessLog>();
		page.setCurrent(5L);
		page.setSize(5L);
		page.addOrder(OrderItem.ascs("gmt_create"));
		page = accessLogServiceImpl.page(page, new QueryWrapper<AccessLog>());
		System.out.println("total_page:" + page.getTotal());
		System.out.println("pageNum:" + page.getCurrent());
		for (AccessLog item : page.getRecords()) {
			System.out.println(item.toString());
		}
	}

	@Test
	public void addSysParentDict() throws Exception {
		String url = "/admin/sysconfig/dict/parent";
		SysConfigRequest uriVars = new SysConfigRequest();
		uriVars.setGroup("certifications");

		MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post(url)
				.content(JSONObject.toJSONString(uriVars)).contentType(MediaType.APPLICATION_JSON))
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
		MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post(url)
				.content(JSONObject.toJSONString(uriVars)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		System.out.println("mvc请求结束");

	}

	@Test
	public void readData_0() {
		for (int i = 0; i < 100000; i++) {
			// 模拟缓存穿透
			AccessLog logItem = this.getDataFromDb2Redis(125546433L);
			// 正常情况下
//			AccessLog logItem = this.getDataFromDb2Redis(1255431277511778306L); 
			String result = "";
			if (logItem == null) {
				result = "null data attained";
			} else {
				result = logItem.toString();
			}
			System.out.println("logItem:" + result);
		}
	}

	/**
	 * 采用 设为 null的方式，解决缓存穿透问题的办法
	 * 
	 * @param id
	 * @return
	 */
	AccessLog getDataFromDb2Redis(Long id) {
		boolean has = redisTemplate.hasKey("accesslog:" + String.valueOf(id));
		if (has) {
			Object obj = redisTemplate.opsForValue().get("accesslog:" + String.valueOf(id));
			if (obj != null) {
				String jstr = JSONObject.toJSONString(obj);
				System.out.println("read data from redis value = " + jstr);
				return JSONObject.parseObject(jstr, AccessLog.class);
			} else {
				System.out.println("read data from redis but data is null ...return directly for protecting db");
				return null;
			}
		}
		AccessLog logItem = accessLogServiceImpl.getById(id);
		System.out.println("read data from db");
		redisTemplate.opsForValue().set("accesslog:" + String.valueOf(id), logItem, 30, TimeUnit.SECONDS);
		System.out.println("set data to db: value = " + logItem);
		return logItem;
	}
	
	/**
	 * 通过布隆过滤器来处理穿透问题
	 */
	@Test
	public void readData_1() {
		for (int i = 0; i < 3; i++) {
			// 模拟缓存穿透
			AccessLog logItem = this.getDataFromDb2RedisCheckByBloom(1255431277511778306L);
			// 正常情况下
//			AccessLog logItem = this.getDataFromDb2Redis(1255431277511778306L); 
			String result = "";
			if (logItem == null) {
				result = "null data attained";
			} else {
				result = logItem.toString();
			}
			System.out.println("logItem:" + result);
		}
	}
	
	
	/**
	 * 通过布隆过滤器的方案来解决穿透问题
	 * 
	 * @param id
	 * @return
	 */
	AccessLog getDataFromDb2RedisCheckByBloom(Long id) {
		Long offset = 890L;
		boolean has = redisTemplate.opsForValue().getBit("accesslog:" + String.valueOf(id), offset);
		if (has) {
			Object obj = redisTemplate.opsForValue().get("accesslog:" + String.valueOf(id));
			String jstr = JSONObject.toJSONString(obj);
			System.out.println("read data from redis value = " + jstr);
			return JSONObject.parseObject(jstr, AccessLog.class);
		}
		AccessLog logItem = accessLogServiceImpl.getById(id);
		System.out.println("read data from db");
		redisTemplate.opsForValue().set("accesslog:" + String.valueOf(id), logItem, 5, TimeUnit.MINUTES);
		redisTemplate.opsForValue().setBit("accesslog:" + String.valueOf(id), offset, true);
		return logItem;
	}
	
	/**
	 * 模拟缓存击穿情况
	 */
	@Test
	public void readData_2() {
		for (int i = 0; i < 3; i++) {
			getDataByExclusiveLock(1255431277511778306L);
		}
	}
	
	CountDownLatch countDownLatch = new CountDownLatch(5);

    private static Integer count = 100;
    
	@Test
	public void multiThreadReadData() throws InterruptedException {
		 ExecutorService executorService = Executors.newFixedThreadPool(5);
		 for (int i = 1; i <= 100; i++) {
	            try {  
	                executorService.execute(() -> {
	                  // 执行redis查询
	                	 AccessLog log = getDataByExclusiveLock(1255431277511778306L);
	                	System.out.println(log);
	                });
	            } catch (Throwable e) {
	                //TODO
	            } finally {
	                // 很关键, 无论上面程序是否异常必须执行countDown,否则await无法释放
	                countDownLatch.countDown();
	            }
	        }
	        // 5个线程countDown()都执行之后才会释放当前线程,程序才能继续往后执行
	        countDownLatch.await();
	        //关闭线程池
	        executorService.shutdown();
	        System.out.println(count);
	        System.out.println(Thread.currentThread().getName() + ":这是最后一个线程!"); 
	}
	
	
	
	
	/**
	 * 通过排它锁解决，解决缓存击穿的问题
	 * @param id
	 * @return
	 */
	AccessLog getDataByExclusiveLock(Long id) {
		AccessLog logItem = this.getDataFromReids(id);
		if(logItem != null ) {
			return logItem;
		}
		String lockKey = "lock_key";
		boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, id, Duration.ofSeconds(300L));
		if(locked) {
			log.info(Thread.currentThread().getName()+"拿到了排它锁，准备从数据库中查询数据，并写入缓存");
			logItem = getDataFromDb(id);
			SetData2Reids(logItem);
			redisTemplate.delete(lockKey);
		}else {
			try {
				while(logItem == null ) {
					logItem = this.getDataFromReids(id);
					if(logItem!=null) {
						log.info(Thread.currentThread().getName()+"虽然没有抢到锁，但是已经从redis中获得到了数据");
						break;
					}else {
						log.info(Thread.currentThread().getName()+"还未从redis中获取到数据，稍等一会再尝试");
						Thread.sleep(200L);
						continue;
					}
				}
			} catch (InterruptedException e) {
				log.error("线程被打断");
			}
		}
		return logItem;
	}
	
	/**
	 * 从redis读数据
	 * @param id
	 * @return
	 */
	AccessLog getDataFromReids(Long id){
		Object obj = redisTemplate.opsForValue().get("accesslog:" + String.valueOf(id));
		String jstr = JSONObject.toJSONString(obj);
//		System.out.println("read data from redis value = " + jstr);
		return JSONObject.parseObject(jstr, AccessLog.class);
	}
	
	/**
	 * 从数据库度数据
	 * @param id
	 * @return
	 */
	AccessLog getDataFromDb(Long id) {
		AccessLog logItem = accessLogServiceImpl.getById(id);
		System.out.println("read data from db");
		return logItem;
	}
	
	void SetData2Reids(AccessLog logItem) {
		redisTemplate.opsForValue().set("accesslog:" + String.valueOf(logItem.getId()), logItem, 5, TimeUnit.MINUTES);
		System.out.println("set data from db");
	}
	
	@Test
	public void addRedisLock_1() {
		 boolean reuslt= redisTemplate.opsForValue().setIfAbsent("lock_key_1", 1, Duration.ofMinutes(3));
		log.info("addRedisLock_1 加锁结果：{}",reuslt);
	}
	
	@Test
	public void addRedisLock_2() {
		 boolean reuslt= redisTemplate.opsForValue().setIfAbsent("lock_key_2", 2, Duration.ofMinutes(3));
		log.info("addRedisLock_2 加锁结果：{}",reuslt);
	}
	
	@Test
	public void redisPublisher() {
		
	}
}
