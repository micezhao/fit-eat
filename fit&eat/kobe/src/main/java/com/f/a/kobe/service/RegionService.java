package com.f.a.kobe.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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
	
	//存放在redis库中的key，存放内容是属于parentid下的区域集合
	private static final String KEY_REGION = "region";
	
	//存放在redis库中的key，存放内容是属于区域id与区域详细内容的键值对
	private static final String KEY_AREA= "area";
	
	private static final int TOTAL_LIST_SIZE = 400;
	
	//作为参数计算当总数在TOTAL_LIST_SIZE以内的集合长度，所需要开启执行线程数
	private static final int DISTRICTTASKNUM = 200;
	
	//作为参数计算当总数在TOTAL_LIST_SIZE以内的集合长度，所需要开启执行线程数
	private static final int CITYTASKNUM = 700;
	
	private static final String NATION_LEVEL_CODE = "0";
	
	private static final String PROVINCE_LEVEL_CODE = "1";
	
	private static final String CITY_LEVEL_CODE = "2";
	
	private static final String DISTRICT_LEVEL_CODE = "3";
	
	private static final String COUNTY_LEVEL_CODE = "4";
	
	@Autowired
	ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	private static ConcurrentHashMap<byte[],byte[]> totalHashes = new ConcurrentHashMap<>();
	
	//批量导入所有区域数据 以区域编码为key 区域详情作为内容
    public void synAreas() throws InterruptedException, ExecutionException {
    	List<Areas> allList = new ArrayList<>();
    	List<Areas> provList = regionManager.getRegionByLevel(PROVINCE_LEVEL_CODE);
    	List<Areas> cityList = regionManager.getRegionByLevel(CITY_LEVEL_CODE);
    	List<Areas> districtList = regionManager.getRegionByLevel(DISTRICT_LEVEL_CODE);
    	List<Areas> countyList = regionManager.getRegionByLevel(COUNTY_LEVEL_CODE);
    	
    	allList.addAll(countyList);
    	allList.addAll(provList);
    	allList.addAll(districtList);
    	allList.addAll(cityList);
    	
    	totalHashes.put(NATION_LEVEL_CODE.getBytes(), JSON.toJSONString(provList).getBytes());
    	extracted(provList, cityList);
		extracted(cityList, districtList);
		extracted(districtList, countyList);
		
        regionRedisTemplate.executePipelined(new RedisCallback<List<Areas>>() {
        	
			@Override
			public List<Areas> doInRedis(RedisConnection connection) throws DataAccessException {
				for (Areas area:allList) {
                    connection.hSet(KEY_AREA.getBytes(),area.getId().getBytes(),JSON.toJSONString(area).getBytes());
                }
				connection.hMSet(KEY_REGION.getBytes(), totalHashes);
				return null;
			}
        	
        });
    	
    }
    
	public String getAreaName(String areaKey) {
    	Areas areas = (Areas) regionRedisTemplate.opsForHash().get(KEY_AREA, areaKey);
    	return  areas.getAreaname();
    }
    
    //idList 要查询的地区编码
    //根据传入的区域编码集合 按照级别返回具体的区域名称
    public String getAreaName(List<String> idList) {
    	List<Areas> areasList = new ArrayList<>();
    	
    	//批量get数据
        List<Object> list = regionRedisTemplate.executePipelined(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                for (String key : idList) {
                    connection.hGet(KEY_AREA.getBytes(),key.getBytes());
                }
                return null;
            }
        });
        
        for(Object object : list) {
        	Areas area = JSON.parseObject(JSON.toJSONString(object), Areas.class);
        	areasList.add(area);
        }
        
        areasList.sort(new Comparator<Areas>() {
			@Override
			public int compare(Areas o1, Areas o2) {
				return Integer.parseInt(o1.getLevel())-Integer.parseInt(o2.getLevel());
			}
    		
    	});
        StringBuffer stringBuffer = new StringBuffer();
        for(Areas area : areasList) {
        	stringBuffer.append(area.getAreaname());
        }
    	return stringBuffer.toString();
    }
	
	

	private void extracted(List<Areas> discList, List<Areas> countyList)
			throws InterruptedException, ExecutionException {
		int dividNum = 0;
		int listSize = discList.size();
		
		if(listSize <= 40) {
			dividNum = 20;
		}else if(listSize > 40 && listSize <= 400) {
			dividNum = DISTRICTTASKNUM;
		}else {
			dividNum = CITYTASKNUM;
		}
		
		List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>();
		if (discList.size() == 0) {
			return;
		}
		int threadNum = discList.size() / dividNum;
		
		if (discList.size() % dividNum != 0) {
			threadNum = threadNum + 1;
		}
		List<Areas> tempList = null;
		int n = 0; // 计数器
		for (int i = 1; i <= threadNum; i++) {
			tempList = new ArrayList<Areas>();
			if (i == 1) {
				tempList.addAll(discList.subList(n * dividNum, i * dividNum));
			} else {
				if(i != threadNum) {
					tempList.addAll(discList.subList(n * dividNum + 1, i * dividNum));
				}
				else {
					tempList.addAll(discList.subList(n * dividNum + 1, discList.size()));
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
		Object obj= regionRedisTemplate.opsForHash().get(KEY_REGION,hashKey);
		return JSON.parseArray(JSON.toJSONString(obj), Areas.class);
	}
	
	
	
	private class TaskHanler implements Callable<Boolean> {

		private List<Areas> pAreas;

		private List<Areas> sAreas;
		
		private List<Areas> allList = new ArrayList<>();
		
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
					totalHashes.put(pArea.getId().getBytes(), JSON.toJSONString(allList).getBytes());
					allList = new ArrayList<>(); 
				}
			}
		}
	}
	
	private Future<Boolean> task(List<Areas> pAreas, List<Areas> sAreas) {
		return threadPoolTaskExecutor.submit(new TaskHanler(pAreas, sAreas));
	}

}
