package com.f.a.kobe.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.f.a.kobe.manager.aop.ToMongoDB;
import com.f.a.kobe.mapper.CustomerBaseInfoMapper;
import com.f.a.kobe.pojo.CustomerBaseInfo;
import com.f.a.kobe.pojo.CustomerBaseInfoExample;
import com.f.a.kobe.pojo.CustomerBaseInfoExample.Criteria;
import com.f.a.kobe.util.DataUtil;

@Component
public class CustomerBaseInfoManager implements BaseManager<CustomerBaseInfo> {
	
	@Autowired
	private CustomerBaseInfoMapper customerBaseInfoMapper;
	
	@Override
	public CustomerBaseInfo queryById(Long id) {
		CustomerBaseInfo customerBaseInfo = customerBaseInfoMapper.selectByPrimaryKey(id);
		return customerBaseInfo;
	}

	@Override
	public CustomerBaseInfo queryByBiz(Object bizId) {
		Long customerId = (Long)bizId;
		CustomerBaseInfoExample customerBaseInfoExample = new CustomerBaseInfoExample();
		customerBaseInfoExample.createCriteria().andCustomerIdEqualTo(customerId);
		List<CustomerBaseInfo> customerBaseInfoList = customerBaseInfoMapper.selectByExample(customerBaseInfoExample);
		if(0 < customerBaseInfoList.size()) {
			return customerBaseInfoList.get(0);
		}
		return null;
	}

	@Override
	public List<CustomerBaseInfo> listByConditional(CustomerBaseInfo conditional) {
		CustomerBaseInfoExample customerBaseInfoExample = new CustomerBaseInfoExample();
		Criteria criteria = customerBaseInfoExample.createCriteria();
		criteria = DataUtil.formConditionalToCriteria(criteria, conditional);
		List<CustomerBaseInfo> customerBaseInfoList = customerBaseInfoMapper.selectByExample(customerBaseInfoExample);
		return customerBaseInfoList;
	}
	
	@ToMongoDB
	@Override
	public int insert(CustomerBaseInfo t) {
		return customerBaseInfoMapper.insertSelective(t);
	}

	@Override
	public int update(CustomerBaseInfo t) {
		return customerBaseInfoMapper.updateByPrimaryKeySelective(t);
	}

	@Override
	public int delete(Long id) {
		return customerBaseInfoMapper.deleteByPrimaryKey(id);
	}
	


}
