package com.f.a.kobe.manager;

import java.util.List;

import org.springframework.stereotype.Component;

import com.f.a.kobe.manager.aop.ToMongoDB;
import com.f.a.kobe.pojo.CustomerBaseInfo;

@Component
public class CustomerBaseInfoManager implements BaseManager<CustomerBaseInfo> {
	
	@Override
	public CustomerBaseInfo queryById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerBaseInfo queryByBiz(Object bizId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CustomerBaseInfo> listByConditional(CustomerBaseInfo t) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@ToMongoDB
	@Override
	public int insert(CustomerBaseInfo t) {
		return 0;
	}

	@Override
	public int update(CustomerBaseInfo t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}
	


}
