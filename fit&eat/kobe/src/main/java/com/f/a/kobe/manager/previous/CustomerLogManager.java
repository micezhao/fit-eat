package com.f.a.kobe.manager.previous;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.mapper.previous.CustomerLogMapper;
import com.f.a.kobe.pojo.previous.CustomerLog;
import com.f.a.kobe.pojo.previous.CustomerLogExample;
import com.f.a.kobe.pojo.previous.CustomerLogExample.Criteria;
import com.f.a.kobe.util.QueryParamTransUtil;

@Component
public class CustomerLogManager implements BaseManager<CustomerLog> {

	@Autowired
	private CustomerLogMapper customerLogMapper;

	@Override
	public CustomerLog queryById(Long id) {
		return customerLogMapper.selectByPrimaryKey(id);
	}

	@Override
	public CustomerLog queryByBiz(Object bizId) {
		Long customerId = (Long) bizId;
		CustomerLogExample customerLogExample = new CustomerLogExample();
		customerLogExample.createCriteria().andCustomerIdEqualTo(customerId);
		List<CustomerLog> customerLogList = customerLogMapper.selectByExample(customerLogExample);
		if (0 < customerLogList.size()) {
			return customerLogList.get(0);
		} else if (1 < customerLogList.size()) {
			throw new RuntimeException(ErrEnum.REDUPICATE_RECORD.getErrMsg());
		}
		return null;
	}

	@Override
	public List<CustomerLog> listByConditional(CustomerLog conditional) {
		CustomerLogExample customerLogExample = new CustomerLogExample();
		Criteria criteria = customerLogExample.createCriteria();
		criteria = QueryParamTransUtil.formConditionalToCriteria(criteria, conditional);
		List<CustomerLog> customerLogList = customerLogMapper.selectByExample(customerLogExample);
		return customerLogList;
	}
	
	//@ToMongoDB
	@Override
	public int insert(CustomerLog t) {
		t.setCdt(Calendar.getInstance().getTime());
		return customerLogMapper.insertSelective(t);
	}
	
	//@ToMongoDB
	@Override
	public int update(CustomerLog t) {
		t.setMdt(Calendar.getInstance().getTime());
		return customerLogMapper.updateByPrimaryKeySelective(t);
	}
	
	//@ToMongoDB
	@Override
	public int delete(Long id) {
		return customerLogMapper.deleteByPrimaryKey(id);
	}

}
