package com.f.a.kobe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.f.a.kobe.manager.RegionManager;
import com.f.a.kobe.pojo.China;

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
	 * 批量同步地址信息
	 */
	public boolean sycnRegion2Redis() {
		List<China> provinceList = regionManager.getRegionByPid(0); //获得省
		
		Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
		List<Object> result = regionRedisTemplate.executePipelined(new RedisCallback<List<China>>() {
			@Override
			public List<China> doInRedis(RedisConnection connection) throws DataAccessException {
				 RedisSerializer<String> serializer = regionRedisTemplate.getStringSerializer();
				for (China province : provinceList) {
					connection.hSet(serializer.serialize("province"),
							serializer.serialize(String.valueOf(province.getPid())), 
							serializer.serialize(JSON.toJSONString(province)));
				}
				return null;
			}
		});
		if (result == null) {
			return false;
		}
		return true;
	}
	
	public void syn() {
		Map<String, List<China>> hashes = new HashMap<>();
		List<China> allRegion = regionManager.getAllRegion();
		List<China> provList = new ArrayList<>();
		List<China> cityList = new ArrayList<>();
		List<China> allCityList = new ArrayList<>();
		List<China> discList = new ArrayList<>();
		
		for(China china : allRegion) {
			if(china.getPid() != null && china.getId() != null)
			if(china.getPid().equals(new Integer(0)) && !china.getId().equals(new Integer(0))) {
				provList.add(china);
			}
		}
			
		hashes.put("0", provList);
		regionRedisTemplate.opsForHash().putAll(KEY,hashes);
		hashes = new HashMap<>();
		for(China prov : provList) {
			for(China china : allRegion) {
				if(china.getPid() != null) {
					if(china.getPid().equals(prov.getId())) {
						cityList.add(china);
						allCityList.add(china);
					}
				}
			}
			hashes.put(String.valueOf(prov.getId()), cityList);
			regionRedisTemplate.opsForHash().putAll(KEY,hashes);
			hashes = new HashMap<>();
			cityList = new ArrayList<>();
		}
		
		for(China city : allCityList) {
			for(China china : allRegion) {
				if(china.getPid() != null) {
					if(china.getPid().equals(city.getId())) {
						discList.add(china);
					}
				}
			}
			hashes.put(String.valueOf(city.getId()), discList);
			regionRedisTemplate.opsForHash().putAll(KEY,hashes);
			hashes = new HashMap<>();
			discList = new ArrayList<>();
		}
	}
	
	public List<China> listRegionByPId(Integer pid) {
		return regionManager.getRegionByPid(pid);
	}
	
	//从redis中查询指定的key
	public List<China> getReginByKey(String hashKey){
		 Object obj= regionRedisTemplate.opsForHash().get(KEY,hashKey);
		 return  JSON.parseArray(JSON.toJSONString(obj), China.class);
	}
	
	

}
