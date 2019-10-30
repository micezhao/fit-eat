package com.f.a.kobe.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.f.a.kobe.mapper.CustomerCredentialMapper;
import com.f.a.kobe.pojo.CustomerCredential;
import com.f.a.kobe.pojo.CustomerCredentialExample;
import com.f.a.kobe.pojo.CustomerCredentialExample.Criteria;

public class CustomerCredentialManager implements BaseManager<CustomerCredential> {

	@Autowired
	private CustomerCredentialMapper customerCredentialMapper;

	@Override
	public CustomerCredential queryById(Long id) {
		CustomerCredentialExample customerCredentialExample = new CustomerCredentialExample();
		customerCredentialExample.createCriteria().andIdEqualTo(id);
		CustomerCredential customerCredential = customerCredentialMapper.selectByPrimaryKey(id);
		return customerCredential;
	}

	@Override
	public CustomerCredential queryByBiz(Object bizId) {
		Long userid = 0L;
		if (bizId instanceof Long) {
			userid = (Long) bizId;
		}
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
		if (StringUtils.isEmpty(conditional.getWxOpenid())) {
			criteria.andWxOpenidEqualTo(conditional.getWxOpenid());
		}
		method(criteria, conditional);
		return null;
	}

	private void method(Criteria criteria, CustomerCredential conditional) {
		// TODO Auto-generated method stub
		// 1 遍历conditional满足条件的属性
		// 找到所有get方法
		Method[] methods = CustomerCredential.class.getMethods();
		criteria.getClass().getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			Class<?> returnType = method.getReturnType();
			if (methodName.contains("get")) {
				try {
					method.invoke(conditional, null);
					//criteria
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		Method[] methods = CustomerCredential.class.getMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			if(methodName.contains("get")&&!methodName.contains("Class")) {
				Class<?> returnType = method.getReturnType();
				System.out.println(method.getName().substring(3));
				System.out.println(returnType.getName());
			}
		}
	}

	@Override
	public int insert(CustomerCredential customerCredential) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(CustomerCredential customerCredential) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Long id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
