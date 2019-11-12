package com.f.a.kobe.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.f.a.kobe.manager.RegionManager;
import com.f.a.kobe.pojo.Areas;
import com.f.a.kobe.pojo.CustomerBaseInfo;

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
	
	private static final int DISTRICTTASKNUM = 150;
	
	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;
	/**
	 * 
	 * 批量同步地址信息
	 */
	public void syn()  throws InterruptedException, ExecutionException{
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
		/*
		 * for(Areas city : cityList) { for(Areas dist : discList) {
		 * if(dist.getParentid().equals(city.getId())) { allDiscList.add(dist); } }
		 * hashes.put(city.getId(), allDiscList);
		 * regionRedisTemplate.opsForHash().putAll(KEY,hashes); allDiscList = new
		 * ArrayList<>(); hashes = new HashMap<>(); }
		 */
		
		countyList = regionManager.getRegionByLevel("4");
		/*
		 * countyList = regionManager.getRegionByLevel("4"); for(Areas disc : discList)
		 * { for(Areas county : countyList) {
		 * if(county.getParentid().equals(disc.getId())) { allCountyList.add(county); }
		 * } if(allCountyList.size()>0) { hashes.put(disc.getId(), allCountyList);
		 * regionRedisTemplate.opsForHash().putAll(KEY,hashes); allCountyList = new
		 * ArrayList<>(); hashes = new HashMap<>(); }
		 * 
		 * }
		 */
		//extracted(provList, cityList);
		extracted(cityList, discList);
		//判断有多少个区
		extracted(discList, countyList);
	}

	private void extracted(List<Areas> discList, List<Areas> countyList)
			throws InterruptedException, ExecutionException {
		List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>();
		if (discList.size() == 0) {
			return;
		}
		int threadNum = discList.size() / DISTRICTTASKNUM;
		if (discList.size() % DISTRICTTASKNUM != 0) {
			threadNum = threadNum + 1;
		}
		List<Areas> tempList = null;
		int n = 0; // 计数器
		for (int i = 1; i <= threadNum; i++) {
			tempList = new ArrayList<Areas>();
			if (i == 1) {
				tempList.addAll(discList.subList(n * DISTRICTTASKNUM, i * DISTRICTTASKNUM));
			} else {
				if(i != threadNum) {
					tempList.addAll(discList.subList(n * DISTRICTTASKNUM + 1, i * DISTRICTTASKNUM));
				}
				else {
					tempList.addAll(discList.subList(n * DISTRICTTASKNUM + 1, discList.size()));
				}
			}
			Future<Boolean> f = task(discList,countyList);
			futureList.add(f);
			n++;
		}
		for (Future<Boolean> resultList : futureList) {
			resultList.get();
		}
	}
	
	public List<Areas> getReginByKey(String hashKey){ 
		Object obj= regionRedisTemplate.opsForHash().get(KEY,hashKey);
		return JSON.parseArray(JSON.toJSONString(obj), Areas.class);
	}
	
	private class TaskHanler implements Callable<Boolean> {

		private List<Areas> pAreas;

		private List<Areas> sAreas;
		
		private List<Areas> allList = new ArrayList<>();
		
		private Map<String,List<Areas>> hashes = new HashMap<>();

		public TaskHanler(List<Areas> pAreas, List<Areas> sAreas) {
			this.pAreas = pAreas;
			this.sAreas = sAreas;
		}

		@Override
		public Boolean call() throws Exception {
			execute(pAreas,sAreas);
			return true;
		}

		public void execute(List<Areas> pAreas,List<Areas> sAreas) {
			for(Areas pArea : pAreas) {
				for(Areas sArea : sAreas) {
					if(sArea.getParentid().equals(pArea.getId())) {
						allList.add(sArea);
					}
				}
				if(allList.size() > 0) {
					hashes.put(pArea.getId(), allList);
					regionRedisTemplate.opsForHash().putAll(KEY,hashes);
					allList = new ArrayList<>(); 
					hashes = new HashMap<>();
				}
			}
		}
	}
	
	private Future<Boolean> task(List<Areas> pAreas, List<Areas> sAreas) {
		return threadPoolTaskExecutor.submit(new TaskHanler(pAreas, sAreas));
	}

}
