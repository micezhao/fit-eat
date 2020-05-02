package com.f.a.kobe.manager.previous;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.f.a.kobe.exceptions.ErrEnum;
import com.f.a.kobe.exceptions.InvaildException;
import com.f.a.kobe.mapper.previous.CustomerBaseInfoMapper;
import com.f.a.kobe.pojo.previous.CustomerBaseInfo;
import com.f.a.kobe.pojo.previous.CustomerBaseInfoExample;
import com.f.a.kobe.pojo.previous.CustomerBaseInfoExample.Criteria;
import com.f.a.kobe.util.DateUtils;
import com.f.a.kobe.util.QueryParamTransUtil;

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
		Long customerId = (Long) bizId;
		CustomerBaseInfoExample customerBaseInfoExample = new CustomerBaseInfoExample();
		customerBaseInfoExample.createCriteria().andCustomerIdEqualTo(customerId);
		List<CustomerBaseInfo> customerBaseInfoList = customerBaseInfoMapper.selectByExample(customerBaseInfoExample);
		if (0 < customerBaseInfoList.size()) {
			return customerBaseInfoList.get(0);
		} else if (customerBaseInfoList.size() > 1) {
			throw new RuntimeException(ErrEnum.REDUPICATE_RECORD.getErrMsg());
		}
		return null;
	}

	@Override
	public List<CustomerBaseInfo> listByConditional(CustomerBaseInfo conditional) {
		CustomerBaseInfoExample customerBaseInfoExample = new CustomerBaseInfoExample();
		Criteria criteria = customerBaseInfoExample.createCriteria();
		criteria = QueryParamTransUtil.formConditionalToCriteria(criteria, conditional);
		List<CustomerBaseInfo> customerBaseInfoList = customerBaseInfoMapper.selectByExample(customerBaseInfoExample);
		return customerBaseInfoList;
	}

//	@ToMongoDB
	@Override
	public int insert(CustomerBaseInfo t) {
		t.setCdt(Calendar.getInstance().getTime());
		return customerBaseInfoMapper.insertSelective(t);
	}
	
//	@ToMongoDB
	@Override
	public int update(CustomerBaseInfo t) {
		t.setMdt(Calendar.getInstance().getTime());
		return customerBaseInfoMapper.updateByPrimaryKeySelective(t);
	}
	
	/**
	 * 不更新
	 * @param record
	 * @param updateContent
	 * @return
	 */
	public int update(CustomerBaseInfo record,CustomerBaseInfo updateContent) {
		 CustomerBaseInfoExample example = new CustomerBaseInfoExample();
		 Criteria criteria =  example.createCriteria();
		 criteria.andCustomerIdEqualTo(record.getCustomerId());
		
		 if(StringUtils.isNotBlank(updateContent.getGender()) ) {
			 criteria.andGenderEqualTo(updateContent.getGender());
		 }
		 if(StringUtils.isNotBlank(updateContent.getHeadimg())) {
			 criteria.andHeadimgEqualTo(updateContent.getHeadimg());
		 }
		 if(StringUtils.isNotBlank(updateContent.getNickname())) {
			 criteria.andNicknameEqualTo(updateContent.getNickname());
		 }
		 if(StringUtils.isNotBlank(updateContent.getRealname())) {
			 criteria.andRealnameEqualTo(updateContent.getRealname());
		 }
		 if(updateContent.getScore() != null && updateContent.getScore() != 0) {
			 criteria.andSorceEqualTo(updateContent.getScore());
		 }
		 criteria.andMdtEqualTo(Calendar.getInstance().getTime());
		return customerBaseInfoMapper.updateByExampleSelective(record, example);
	}
	
	
//	@ToMongoDB
	@Override
	public int delete(Long id) {
		return customerBaseInfoMapper.deleteByPrimaryKey(id);
	}
	
}
