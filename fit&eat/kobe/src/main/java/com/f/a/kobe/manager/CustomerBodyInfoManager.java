package com.f.a.kobe.manager;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.manager.aop.ToMongoDB;
import com.f.a.kobe.mapper.CustomerBodyInfoMapper;
import com.f.a.kobe.pojo.CustomerBodyInfo;
import com.f.a.kobe.pojo.CustomerBodyInfoExample;
import com.f.a.kobe.pojo.CustomerBodyInfoExample.Criteria;
import com.f.a.kobe.util.QueryParamTransUtil;

@Component
public class CustomerBodyInfoManager implements BaseManager<CustomerBodyInfo> {

	@Autowired
	private CustomerBodyInfoMapper CustomerBodyInfoMapper;

	@Override
	public CustomerBodyInfo queryById(Long id) {
		CustomerBodyInfo CustomerBodyInfo = CustomerBodyInfoMapper.selectByPrimaryKey(id);
		return CustomerBodyInfo;
	}

	@Override
	public CustomerBodyInfo queryByBiz(Object bizId) {
		Long userId = (Long) bizId;
		CustomerBodyInfoExample CustomerBodyInfoExample = new CustomerBodyInfoExample();
		CustomerBodyInfoExample.createCriteria().andCustomerIdEqualTo(userId);
		List<CustomerBodyInfo> CustomerBodyInfoList = CustomerBodyInfoMapper
				.selectByExample(CustomerBodyInfoExample);
		if (0 < CustomerBodyInfoList.size()) {
			return CustomerBodyInfoList.get(0);
		}else if(CustomerBodyInfoList.size() > 1){
			throw new RuntimeException(ErrEnum.REDUPICATE_RECORD.getErrMsg());
		}
		return null;
	}

	@Override
	public List<CustomerBodyInfo> listByConditional(CustomerBodyInfo conditional) {
		CustomerBodyInfoExample CustomerBodyInfoExample = new CustomerBodyInfoExample();
		Criteria criteria = CustomerBodyInfoExample.createCriteria();
		criteria = QueryParamTransUtil.formConditionalToCriteria(criteria, conditional);
		List<CustomerBodyInfo> CustomerBodyInfoList = CustomerBodyInfoMapper
				.selectByExample(CustomerBodyInfoExample);
		return CustomerBodyInfoList;
	}
	
//	@ToMongoDB
	@Override
	public int insert(CustomerBodyInfo CustomerBodyInfo) {
		CustomerBodyInfo.setCdt(Calendar.getInstance().getTime());
		return CustomerBodyInfoMapper.insertSelective(CustomerBodyInfo);
	}
	
//	@ToMongoDB
	@Override
	public int update(CustomerBodyInfo CustomerBodyInfo) {
		CustomerBodyInfo.setMdt(Calendar.getInstance().getTime());
		return CustomerBodyInfoMapper.updateByPrimaryKeySelective(CustomerBodyInfo);
	}

//	@ToMongoDB
	@Override
	public int delete(Long id) {
		return CustomerBodyInfoMapper.deleteByPrimaryKey(id);
	}

}
