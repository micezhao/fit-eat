package com.f.a.kobe.manager.previous;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.f.a.kobe.mapper.previous.AreasMapper;
import com.f.a.kobe.pojo.region.Areas;
import com.f.a.kobe.pojo.region.AreasExample;

@Service
public class RegionManager  {

	@Autowired
	private AreasMapper mapper;
	
	public List<Areas> getAllRegion(){
		AreasExample example = new AreasExample();
		return mapper.selectByExample(example);
	}
	
	public List<Areas> getRegionByPid(String pid){
		AreasExample example = new AreasExample();
		example.createCriteria().andParentidEqualTo(pid);
		return mapper.selectByExample(example);
	}
	
	public Areas getRegionById(String id) {
		AreasExample example = new AreasExample();
		example.createCriteria().andIdEqualTo(id);
		return mapper.selectByExample(example).get(0);
	}

	public List<Areas> getRegionByLevel(String level) {
		AreasExample example = new AreasExample();
		example.createCriteria().andLevelEqualTo(level);
		return mapper.selectByExample(example);
	}
	

}
