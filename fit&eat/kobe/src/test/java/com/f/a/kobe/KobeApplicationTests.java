package com.f.a.kobe;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.f.a.kobe.manager.CustomerBaseInfoManager;
import com.f.a.kobe.manager.CustomerBodyInfoManager;
import com.f.a.kobe.pojo.CustomerBaseInfo;
import com.f.a.kobe.pojo.CustomerBodyInfo;
import com.f.a.kobe.pojo.view.CustomerBodyInfoView;
import com.f.a.kobe.util.IdWorker;

@RunWith(SpringRunner.class)
@SpringBootTest
class KobeApplicationTests {
	
	@Autowired
	public CustomerBaseInfoManager infoManager;
		
	@Autowired
	private CustomerBodyInfoManager manager;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private IdWorker idworker;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	void test1() {
		CustomerBaseInfo info = new CustomerBaseInfo();
		info.setId(5555L);
		info.setAge(12);
		info.setCustomerId(1234335234L);
		info.setRealname("test2");
		info.setMobile("123456dfdf7");
		infoManager.insert(info);
	}
	
	@Test
	void test2() {
		CountDownLatch countDownLatch = new CountDownLatch(3);
		try {
			Thread t1 = new Thread(new SyncBodyInfo(2019,countDownLatch));
			Thread t2 = new Thread(new SyncBodyInfo(2018,countDownLatch));
			Thread t3 = new Thread(new SyncBodyInfo(2017,countDownLatch));
			t1.start();
			t2.start();
			t3.start();
			countDownLatch.await();
		} catch (InterruptedException e) {
			
		}
	}
	
	public class SyncBodyInfo implements Runnable {
		
		private static final String COLLECTION_NAME = "CustomerBodyInfoView";
		
		private int yearStart;
		
		private CountDownLatch countDownLatch;
	
		public SyncBodyInfo(int yearStart,CountDownLatch countDownLatch) {
			super();
			this.yearStart = yearStart;
			this.countDownLatch = countDownLatch;
		}

		public void run() {
			int year = this.yearStart;
			for (int i = 1; i <= 12; i++) {
				for (int j = 0; j < 99; j++) {
					StringBuffer buffer = new StringBuffer();
					buffer.append(year).append("-");
					if(i<10) {
						buffer.append("0").append(i);
					}else {
						buffer.append(i);
					}
					buffer.append("-01");
					String registDate = buffer.toString();
					CustomerBodyInfo body = new CustomerBodyInfo();
					body.setId(idworker.nextId());
					body.setCustomerId(1198238904218607616L);
					String recordId = buffer.append(j).toString();
					body.setRecordId(recordId);
					body.setHeight("176");
					body.setRegisterDate(registDate);
					body.setWeight("76");
					body.setChest("100");
					body.setWaistline("85");
					body.setHipline("92");
					body.setLeftArmCircumference("30");
					body.setRightArmCircumference("30");
					body.setLeftThighCircumference("55");
					body.setRightThighCircumference("55");
					body.setFatPercentage("20");
					body.setHeartRate("93");
					body.setSdp("110");
					body.setDbp("90");
					body.setFatContent("20");
					body.setMuscleContent("35.9");
					registBodyInfoTest(body, "male");
				}
			}
			countDownLatch.countDown(); 
			System.out.println("子线程：【"+Thread.currentThread().getName()+"】执行完成");
		}
		/**
		 * 注册一条信息的体征记录
		 * @param customerBodyInfo
		 */
		public CustomerBodyInfoView registBodyInfoTest(CustomerBodyInfo customerBodyInfo,String gender) {
			insertTest(customerBodyInfo);
			CustomerBodyInfoView view = new CustomerBodyInfoView().build(customerBodyInfo, gender);
			mongoTemplate.insert(view, COLLECTION_NAME);
			return view;
		}
		
		private void insertTest(CustomerBodyInfo customerBodyInfo) {
			BigDecimal height = new BigDecimal(customerBodyInfo.getHeight());
			BigDecimal weight = new BigDecimal( customerBodyInfo.getWeight());
			// bim = 体重kg/身高(m)的二次幂 
			BigDecimal w = (height.divide(new BigDecimal(100))).pow(2);
	 		BigDecimal bmi = weight.divide(w,2,BigDecimal.ROUND_HALF_UP);
			customerBodyInfo.setBmi(bmi.toString());
			customerBodyInfo.setRegisterDate(customerBodyInfo.getRegisterDate());
			BigDecimal hipline = new BigDecimal(customerBodyInfo.getHipline()); // 臀围
			BigDecimal waistline = new BigDecimal(customerBodyInfo.getWaistline());//腰围
			BigDecimal waistHipRatio = waistline.divide(hipline,2,BigDecimal.ROUND_HALF_UP);
			customerBodyInfo.setWaistHipRatio(waistHipRatio.toString()); // 腰臀比
//			manager.insert(customerBodyInfo);
		}
		
	}
	

}
