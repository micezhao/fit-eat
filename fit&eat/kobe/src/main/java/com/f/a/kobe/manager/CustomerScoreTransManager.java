package com.f.a.kobe.manager;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.manager.aop.ToMongoDB;
import com.f.a.kobe.mapper.CustomerScoreTransMapper;
import com.f.a.kobe.pojo.CustomerScoreTrans;
import com.f.a.kobe.pojo.CustomerScoreTransExample;
import com.f.a.kobe.pojo.CustomerScoreTransExample.Criteria;
import com.f.a.kobe.util.QueryParamTransUtil;

@Component
public class CustomerScoreTransManager implements BaseManager<CustomerScoreTrans> {

	@Autowired
	private CustomerScoreTransMapper customerScoreTransMapper;

	@Override
	public CustomerScoreTrans queryById(Long id) {
		return customerScoreTransMapper.selectByPrimaryKey(id);
	}

	@Override
	public CustomerScoreTrans queryByBiz(Object bizId) {
		Long customerId = (Long) bizId;
		CustomerScoreTransExample customerScoreTransExample = new CustomerScoreTransExample();
		customerScoreTransExample.createCriteria().andCustomerIdEqualTo(customerId);
		List<CustomerScoreTrans> customerScoreTransList = customerScoreTransMapper
				.selectByExample(customerScoreTransExample);
		if (0 < customerScoreTransList.size()) {
			return customerScoreTransList.get(0);
		} else if (1 < customerScoreTransList.size()) {
			throw new RuntimeException(ErrEnum.REDUPICATE_RECORD.getErrMsg());
		}
		return null;
	}

	@Override
	public List<CustomerScoreTrans> listByConditional(CustomerScoreTrans conditional) {
		CustomerScoreTransExample customerScoreTransExample = new CustomerScoreTransExample();
		Criteria criteria = customerScoreTransExample.createCriteria();
		criteria = QueryParamTransUtil.formConditionalToCriteria(criteria, conditional);
		List<CustomerScoreTrans> customerScoreTransList = customerScoreTransMapper
				.selectByExample(customerScoreTransExample);
		return customerScoreTransList;
	}
	
//	@ToMongoDB
	@Override
	public int insert(CustomerScoreTrans t) {
		t.setCdt(Calendar.getInstance().getTime());
		return customerScoreTransMapper.insertSelective(t);
	}
	
//	@ToMongoDB
	@Override
	public int update(CustomerScoreTrans t) {
		t.setMdt(Calendar.getInstance().getTime());
		return customerScoreTransMapper.updateByPrimaryKeySelective(t);
	}
	
//	@ToMongoDB
	@Override
	public int delete(Long id) {
		return customerScoreTransMapper.deleteByPrimaryKey(id);
	}

}
