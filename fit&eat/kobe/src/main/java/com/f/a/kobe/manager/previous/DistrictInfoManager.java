package com.f.a.kobe.manager.previous;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.mapper.previous.ChinaMapper;
import com.f.a.kobe.pojo.region.China;
import com.f.a.kobe.pojo.region.ChinaExample;

@Service
public class DistrictInfoManager {

	@Autowired
	private ChinaMapper chinaMapper;
	
	public List<China> listDistrictInfoByPid(Integer pid){
		ChinaExample chinaExample = new ChinaExample();
		chinaExample.createCriteria().andPidEqualTo(pid);
		return chinaMapper.selectByExample(chinaExample);
	}
}
