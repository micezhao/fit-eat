package com.f.a.kobe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.f.a.kobe.manager.DistrictInfoManager;
import com.f.a.kobe.pojo.China;
@Service
public class DistrictInfoService {
	
	@Autowired
	DistrictInfoManager districtInfoManager;
	
	@Cacheable(cacheNames="districtListInfo", key = "#pid")
	public List<China> listDistrictByPid(Integer pid){
		List<China> districtList = districtInfoManager.listDistrictInfoByPid(pid);
		return districtList;
	}
}
