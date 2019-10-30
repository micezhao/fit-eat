package com.f.a.kobe.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.mapper.CustomerCredentialMapper;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.CustomerCredentialExample;
import com.f.a.kobe.pojo.CustomerCredentialExample.Criteria;
import com.f.a.kobe.util.DataUtil;

@Service
public class CustomerCredentialManager implements BaseManager<CustomerCredential> {

	@Autowired
	private CustomerCredentialMapper customerCredentialMapper;

	@Override
	public CustomerCredential queryById(Long id) {
		CustomerCredential customerCredential = customerCredentialMapper.selectByPrimaryKey(id);
		return customerCredential;
	}

	@Override
	public CustomerCredential queryByBiz(Object bizId) {
		Long userid = (Long) bizId;
		CustomerCredentialExample customerCredentialExample = new CustomerCredentialExample();
		customerCredentialExample.createCriteria().andCustomerIdEqualTo(userid);
		List<CustomerCredential> customerCredentialList = customerCredentialMapper
				.selectByExample(customerCredentialExample);
		if (0 < customerCredentialList.size()) {
			return customerCredentialList.get(0);
		}
		return null;
	}

	@Override
	public List<CustomerCredential> listByConditional(CustomerCredential conditional) {
		CustomerCredentialExample customerCredentialExample = new CustomerCredentialExample();
		Criteria criteria = customerCredentialExample.createCriteria();
		criteria = DataUtil.formConditionalToCriteria(criteria, conditional);
		List<CustomerCredential> customerCredentialList = customerCredentialMapper
				.selectByExample(customerCredentialExample);
		return customerCredentialList;
	}

	@Override
	public int insert(CustomerCredential customerCredential) {
		return customerCredentialMapper.insertSelective(customerCredential);
	}

	@Override
	public int update(CustomerCredential customerCredential) {
		return customerCredentialMapper.updateByPrimaryKeySelective(customerCredential);
	}

	@Override
	public int delete(Long id) {
		return customerCredentialMapper.deleteByPrimaryKey(id);
	}

}
