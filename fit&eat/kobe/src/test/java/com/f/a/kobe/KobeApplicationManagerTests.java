package com.f.a.kobe;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.f.a.kobe.KobeApplication;
import com.f.a.kobe.manager.CustomerCredentialManager;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.util.IdWorker;


@RunWith(SpringJUnit4ClassRunner.class)	
@SpringBootTest(classes = KobeApplication.class)
@WebAppConfiguration
class KobeApplicationManagerTests {

	@Autowired
	CustomerCredentialManager customerCredentialManager;
	
	@Autowired
	IdWorker idWorker;
	@Test
	void contextLoads() {
	}
	
	@Test
	void testQuery() {
		System.out.println("------");
		CustomerCredential customerCredential = new CustomerCredential();
		customerCredential.setEmail("123");
		customerCredential.setUsername("xxxx");
		customerCredentialManager.listByConditional(customerCredential);
	}
	
	@Test
	void add() {
		for(int i = 0;i<100;i++) {
			CustomerCredential customerCredential = new CustomerCredential();
			customerCredential.setUsername("kobe"+1);
			customerCredential.setAliOpenid("798dasd7kobe"+i+"sa98d");
			customerCredential.setCdt(new Date());
			customerCredential.setCustomerId(idWorker.nextId());
			customerCredential.setDr("1");
			customerCredential.setEmail("kobe"+i+"henniub@nb.com");
			customerCredential.setMdt(new Date());
			customerCredential.setMobile("18977993355");
			customerCredential.setWxOpenid("898kobe"+i+"asd");
			customerCredential.setPassword("3399kobe"+i);
			customerCredentialManager.insert(customerCredential);
		}
	}
	
	@Test
	void queryByBiz() {
		CustomerCredential customerCredential = customerCredentialManager.queryByBiz(1L);
	}
	
	@Test
	void queryById() {
		CustomerCredential customerCredential = customerCredentialManager.queryByBiz(1L);
	}
	
	@Test
	void queryByConditional() {
		CustomerCredential customerCredential = new CustomerCredential();
		customerCredential.setEmail("kobe5henniub@nb.com");
		List<CustomerCredential> listByConditional = customerCredentialManager.listByConditional(customerCredential);
		System.out.println(listByConditional);
	}

}
