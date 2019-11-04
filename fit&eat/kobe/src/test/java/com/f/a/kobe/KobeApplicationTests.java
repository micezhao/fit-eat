package com.f.a.kobe;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.f.a.kobe.manager.CustomerBaseInfoManager;
import com.f.a.kobe.pojo.CustomerBaseInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
class KobeApplicationTests {
	
	@Autowired
	public CustomerBaseInfoManager infoManager;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	void test1() {
		CustomerBaseInfo info = new CustomerBaseInfo();
		info.setId(432323L);
		info.setAge(12);
		info.setCustomerId(1234335234L);
		info.setRealname("test2");
		info.setMobile("123456dfdf7");
		infoManager.insert(info);
	}
	

}
