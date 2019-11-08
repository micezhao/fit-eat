package com.f.a.kobe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.f.a.kobe.manager.RegionManager;
import com.f.a.kobe.pojo.China;

/**
 * 中国地理位置信息服务类
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
		 List<China> regionChina= regionManager.getAllRegion();
		 List<Object> result =regionRedisTemplate.executePipelined(new RedisCallback<List<China>>() {
		    @Override
		    public List<China> doInRedis(RedisConnection connection) throws DataAccessException {
		        for (China item : regionChina) {
		            connection.hSet( "region".getBytes(),
		            		item.getId().toString().getBytes(),
		            		JSON.toJSONString(item).getBytes());
		        }
		       return null;
		    }
		});
		 if(result == null ) {
			 return false;
		 }
		 return true;
	}
	
	public List<China> listRegionByPId(Integer pid){
		return regionManager.getRegionByPid(pid);
	}
	
	public China getRegionById(Integer id) {
		return regionManager.getRegionById(id);
	}
	
}
