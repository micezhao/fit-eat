package com.f.a.kobe.manager;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.f.a.kobe.exceptions.ErrCodeEnum;
import com.f.a.kobe.mapper.CustomerAddrMapper;
import com.f.a.kobe.pojo.CustomerAddr;
import com.f.a.kobe.pojo.CustomerAddrExample;
import com.f.a.kobe.pojo.CustomerAddrExample.Criteria;
import com.f.a.kobe.util.QueryParamTransUtil;

@Component
public class CustomerAddrManager implements BaseManager<CustomerAddr> {

	@Autowired
	private CustomerAddrMapper customerAddrMapper;

	@Override
	public CustomerAddr queryById(Long id) {
		return customerAddrMapper.selectByPrimaryKey(id);
	}

	@Override
	public CustomerAddr queryByBiz(Object bizId) {
		Long customerId = (Long) bizId;
		CustomerAddrExample customerAddrExample = new CustomerAddrExample();
		customerAddrExample.createCriteria().andCustomerIdEqualTo(customerId);
		List<CustomerAddr> customerAddrList = customerAddrMapper.selectByExample(customerAddrExample);
		if (0 < customerAddrList.size()) {
			return customerAddrList.get(0);
		} else if (1 < customerAddrList.size()) {
			throw new RuntimeException(ErrCodeEnum.REDUPICATE_RECORD.getErrMsg());
		}
		return null;
	}

	@Override
	public List<CustomerAddr> listByConditional(CustomerAddr conditional) {
		CustomerAddrExample customerAddrExample = new CustomerAddrExample();
		Criteria criteria = customerAddrExample.createCriteria();
		criteria = QueryParamTransUtil.formConditionalToCriteria(criteria, conditional);
		List<CustomerAddr> customerAddrList = customerAddrMapper.selectByExample(customerAddrExample);
		return customerAddrList;
	}

	@Override
	public int insert(CustomerAddr t) {
		t.setCdt(Calendar.getInstance().getTime());
		return customerAddrMapper.insertSelective(t);
	}

	@Override
	public int update(CustomerAddr t) {
		t.setMdt(Calendar.getInstance().getTime());
		return customerAddrMapper.updateByPrimaryKeySelective(t);
	}

	@Override
	public int delete(Long id) {
		return customerAddrMapper.deleteByPrimaryKey(id);
	}

}
