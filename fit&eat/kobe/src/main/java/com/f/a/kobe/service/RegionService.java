package com.f.a.kobe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.f.a.kobe.manager.RegionManager;
import com.f.a.kobe.pojo.Areas;

/**
 * 中国地理位置信息服务类
 * 
 * @author micezhao
 *
 */
@Service
public class RegionService {

	@Autowired
	@Qualifier("regionRedisTemplate")
	private RedisTemplate<String, Object> regionRedisTemplate;

	@Autowired
	private RegionManager regionManager;
	
	private static final String KEY = "region";

	/**
	 * @deprecated
	 * 批量同步地址信息
	 */
	/*
	 * public boolean sycnRegion2Redis() { List<China> provinceList =
	 * regionManager.getRegionByPid(0); //获得省
	 * 
	 * Map<byte[], byte[]> map = new HashMap<byte[], byte[]>(); List<Object> result
	 * = regionRedisTemplate.executePipelined(new RedisCallback<List<China>>() {
	 * 
	 * @Override public List<China> doInRedis(RedisConnection connection) throws
	 * DataAccessException { RedisSerializer<String> serializer =
	 * regionRedisTemplate.getStringSerializer(); for (China province :
	 * provinceList) { connection.hSet(serializer.serialize("province"),
	 * serializer.serialize(String.valueOf(province.getPid())),
	 * serializer.serialize(JSON.toJSONString(province))); } return null; } }); if
	 * (result == null) { return false; } return true; }
	 */
	
	/**
	 * 
	 * 批量同步地址信息
	 */
	public void syn() {
		Map<String, List<Areas>> hashes = new HashMap<>();
		List<Areas> provList = new ArrayList<>();
		List<Areas> cityList = new ArrayList<>(); 
		List<Areas>	allCityList = new ArrayList<>(); 
		List<Areas> discList = new ArrayList<>();
		List<Areas> allDiscList = new ArrayList<>();
		List<Areas> countyList = new ArrayList<>();
		List<Areas> allCountyList = new ArrayList<>();
		
		
		//获得所有省
		provList = regionManager.getRegionByLevel("1");
		hashes.put("0", provList);
		regionRedisTemplate.opsForHash().putAll(KEY,hashes);
		hashes = new HashMap<>();
		
		cityList = regionManager.getRegionByLevel("2");
		for(Areas prov : provList) {
			for(Areas city : cityList) {
				if(city.getParentid().equals(prov.getId())) {
					allCityList.add(city);
				}
			}
			hashes.put(prov.getId(), allCityList);
			regionRedisTemplate.opsForHash().putAll(KEY,hashes);
			allCityList = new ArrayList<>(); 
			hashes = new HashMap<>();
		}
		
		discList = regionManager.getRegionByLevel("3");
		for(Areas city : cityList) {
			for(Areas dist : discList) {
				if(dist.getParentid().equals(city.getId())) {
					allDiscList.add(dist);
				}
			}
			hashes.put(city.getId(), allDiscList);
			regionRedisTemplate.opsForHash().putAll(KEY,hashes);
			allDiscList = new ArrayList<>(); 
			hashes = new HashMap<>();
		}
		
		countyList = regionManager.getRegionByLevel("4");
		for(Areas disc : discList) {
			for(Areas county : countyList) {
				if(county.getParentid().equals(disc.getId())) {
					allCountyList.add(county);
				}
			}
			if(allCountyList.size()>0) {
				hashes.put(disc.getId(), allCountyList);
				regionRedisTemplate.opsForHash().putAll(KEY,hashes);
				allCountyList = new ArrayList<>(); 
				hashes = new HashMap<>();
			}
			
		}
	}
	
	public List<Areas> getReginByKey(String hashKey){ 
		Object obj= regionRedisTemplate.opsForHash().get(KEY,hashKey);
		return JSON.parseArray(JSON.toJSONString(obj), Areas.class);
	}

}
