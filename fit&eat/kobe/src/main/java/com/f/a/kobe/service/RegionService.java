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
	
	public void sycnRegion2Redis2(Integer pid) {
		List<China> list = regionIsNull(pid);
		if(list != null && list.size() > 0) {
			put(pid,list);
			for(China china : list) {
				if(china.getId() == 0) {
					continue;
				}
				sycnRegion2Redis2(china.getId());
			}
		}
		return ;
	}

	private void put(Integer pid,List<China> list) {
		Map<String, List<China>> hashes = new HashMap<>();
		hashes.put(String.valueOf(pid), list);
		regionRedisTemplate.opsForHash().putAll("region1",hashes);
	}

	private List<China> regionIsNull(Integer i) {
		return regionManager.getRegionByPid(i);
	}

	public List<China> getSycnRegion2Redis2(Integer pid) {
		Object object = regionRedisTemplate.opsForHash().get("province1", "430000");
		 return (List<China>)regionRedisTemplate.opsForHash().get("region1",String.valueOf(pid));
	}
	
	public List<China> listRegionByPId(Integer pid) {
		return regionManager.getRegionByPid(pid);
	}

	public China getRegionById(Integer id) {
		return regionManager.getRegionById(id);
	}

}
