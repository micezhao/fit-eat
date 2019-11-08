package com.f.a.kobe.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.mapper.ChinaMapper;
import com.f.a.kobe.pojo.China;
import com.f.a.kobe.pojo.ChinaExample;

@Service
public class RegionManager  {

	@Autowired
	private ChinaMapper mapper;
	
	public List<China> getAllRegion(){
		ChinaExample example = new ChinaExample();
		return mapper.selectByExample(example);
	}
	
	public List<China> getRegionByPid(Integer pid){
		ChinaExample example = new ChinaExample();
		example.createCriteria().andPidEqualTo(pid);
		return mapper.selectByExample(example);
	}
	
	public China getRegionById(Integer id) {
		ChinaExample example = new ChinaExample();
		example.createCriteria().andIdEqualTo(id);
		return mapper.selectByExample(example).get(0);
	}
	

}
