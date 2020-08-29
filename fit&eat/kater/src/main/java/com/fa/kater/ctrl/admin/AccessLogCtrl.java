package com.fa.kater.ctrl.admin;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fa.kater.pojo.AccessLog;
import com.fa.kater.service.impl.AccessLogServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("admin/accessLog")
@Slf4j
public class AccessLogCtrl {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	AccessLogServiceImpl accessLogServiceImpl;

	
	@GetMapping("/{id}")
	public ResponseEntity<Object> getAccessLogById(@PathVariable("id") Long id){
		JSONObject json = getDataByExclusiveLock(id);
		if(json.get("accessLog") == null) {
			return new ResponseEntity<Object>(json, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(json, HttpStatus.OK);
	}
	
	
	JSONObject getDataByExclusiveLock(Long id) {
		JSONObject resultJson = new JSONObject();
		AccessLog logItem = this.getDataFromReids(id);
		if(logItem != null ) {
			resultJson.put("comment", "第一次从缓存中就获得了数据");
			resultJson.put("accessLog", logItem);
			return resultJson;
		}
		String lockKey = "lock_key_"+id;
		boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, id, Duration.ofSeconds(300L));
		String comment = "";
		if(locked) {
			System.out.println(Thread.currentThread().getName()+"拿到了排它锁"+lockKey+"，准备从数据库中查询数据，并写入缓存");
			logItem = getDataFromDb(id);
			SetData2Reids(logItem);
			redisTemplate.delete(lockKey);
			comment = "这是拿到锁以后从数据库返回的";
		}else {
			try {
				while(logItem == null ) {
					logItem = this.getDataFromReids(id);
					if(logItem!=null) {
						comment = "虽然没有抢到锁，但是已经从redis中获得到了数据";
						log.info("虽然没有抢到锁，但是已经从redis中获得到了数据");
						break;
					}else {
						log.info("还未从redis中获取到数据，稍等一会再尝试");
						Thread.sleep(200L);
						continue;
					}
				}
			} catch (InterruptedException e) {
				log.error("线程被打断");
			}
		}
		resultJson.put("accessLog", logItem);
		resultJson.put("comment", comment);
		return resultJson;
	}
	
	/**
	 * 从redis读数据
	 * @param id
	 * @return
	 */
	AccessLog getDataFromReids(Long id){
		Object obj = redisTemplate.opsForValue().get("accesslog:" + String.valueOf(id));
		String jstr = JSONObject.toJSONString(obj);
		log.info("read data from redis value = {}", jstr);
		return JSONObject.parseObject(jstr, AccessLog.class);
	}
	
	/**
	 * 从数据库度数据
	 * @param id
	 * @return
	 */
	AccessLog getDataFromDb(Long id) {
		AccessLog logItem = accessLogServiceImpl.getById(id);
		log.info("read data from db");
		return logItem;
	}
	
	void SetData2Reids(AccessLog logItem) {
		redisTemplate.opsForValue().set("accesslog:" + String.valueOf(logItem.getId()), logItem, 5, TimeUnit.MINUTES);
		log.info("set data from db");
	}
}
