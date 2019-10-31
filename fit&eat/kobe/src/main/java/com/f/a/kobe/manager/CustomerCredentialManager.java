package com.f.a.kobe.manager;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.f.a.kobe.exceptions.ErrCodeEnum;
import com.f.a.kobe.manager.aop.ToMongoDB;
import com.f.a.kobe.mapper.CustomerCredentialMapper;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.CustomerCredentialExample;
import com.f.a.kobe.pojo.CustomerCredentialExample.Criteria;
import com.f.a.kobe.util.QueryParamTransUtil;

@Component
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
		Long userId = (Long) bizId;
		CustomerCredentialExample customerCredentialExample = new CustomerCredentialExample();
		customerCredentialExample.createCriteria().andCustomerIdEqualTo(userId);
		List<CustomerCredential> customerCredentialList = customerCredentialMapper
				.selectByExample(customerCredentialExample);
		if (0 < customerCredentialList.size()) {
			return customerCredentialList.get(0);
		}else if(customerCredentialList.size() > 1){
			throw new RuntimeException(ErrCodeEnum.REDUPICATE_RECORD.getErrMsg());
		}
		return null;
	}

	@Override
	public List<CustomerCredential> listByConditional(CustomerCredential conditional) {
		CustomerCredentialExample customerCredentialExample = new CustomerCredentialExample();
		Criteria criteria = customerCredentialExample.createCriteria();
		criteria = QueryParamTransUtil.formConditionalToCriteria(criteria, conditional);
		List<CustomerCredential> customerCredentialList = customerCredentialMapper
				.selectByExample(customerCredentialExample);
		return customerCredentialList;
	}
	
	@ToMongoDB
	@Override
	public int insert(CustomerCredential customerCredential) {
		customerCredential.setCdt(Calendar.getInstance().getTime());
		return customerCredentialMapper.insertSelective(customerCredential);
	}
	
	@ToMongoDB
	@Override
	public int update(CustomerCredential customerCredential) {
		customerCredential.setMdt(Calendar.getInstance().getTime());
		return customerCredentialMapper.updateByPrimaryKeySelective(customerCredential);
	}

	@ToMongoDB
	@Override
	public int delete(Long id) {
		return customerCredentialMapper.deleteByPrimaryKey(id);
	}

}
