package com.f.a.kobe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
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
		List<China> regionChina = regionManager.getAllRegion();
		List<China> regionByPid = regionManager.getRegionByPid(0);
		List<Object> result = regionRedisTemplate.executePipelined(new RedisCallback<List<China>>() {
			@Override
			public List<China> doInRedis(RedisConnection connection) throws DataAccessException {
				/*
				 * for (China item : regionChina) { connection.hSet( "region".getBytes(),
				 * item.getPid().toString().getBytes(), JSON.toJSONString(item).getBytes()); }
				 */
				Map<byte[], byte[]> hashes = new HashMap<byte[], byte[]>();
				hashes.put("0".getBytes(), JSON.toJSONString(regionByPid).getBytes());
				connection.hMSet("region".getBytes(), hashes);
				return null;
			}
		});
		if (result == null) {
			return false;
		}
		return true;
	}
	
	public void sycnRegion2Redis2() {
		List<China> regionByPid = regionManager.getRegionByPid(0);
		Map<String, List<China>> hashes = new HashMap<>();
		hashes.put("0", regionByPid);
		regionRedisTemplate.opsForHash().putAll("region1",hashes);
	}
	
	public void getSycnRegion2Redis2() {
		List<China> chinaList = (List<China>)regionRedisTemplate.opsForHash().get("region1","0");
	}
	
	public List<China> listRegionByPId(Integer pid) {
		return regionManager.getRegionByPid(pid);
	}

	public China getRegionById(Integer id) {
		return regionManager.getRegionById(id);
	}

}
