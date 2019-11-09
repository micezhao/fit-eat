package com.f.a.kobe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public boolean sycnRegion2Redis1() {
		List<China> all = regionManager.getAllRegion();
		
		
		Map<String,China> map= new HashMap<String,China>();
		for (China item : all) {
			map.put(String.valueOf(item.getPid())+"_"+String.valueOf(item.getId()), item);
		}
		regionRedisTemplate.opsForHash().putAll("china", map);
		return true;
	}

	public China getReginByKey(String key,String hashKey){
		 Object obj= regionRedisTemplate.opsForHash().get("region1","371701");
		 return (China)obj;
		
	}
	
	

}
