package com.f.a.kobe.ctrl;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.f.a.kobe.pojo.region.Areas;
import com.f.a.kobe.service.previous.RegionService;


@RestController
@RequestMapping("region")
public class RegionCtrl {
	
	@Autowired
	private RegionService regionService;
	
	@Autowired
	@Qualifier("regionRedisTemplate")
	private RedisTemplate<String, Object> regionRedisTemplate;
	
	/**
	 * 批量同步中国地址信息的接口
	 * @return
	 */
	@GetMapping("/batchSync")
	@ResponseBody
	public ResponseEntity<Object> sync() {
		try {
			regionService.synAreas();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@GetMapping("/{hashKey}")
	@ResponseBody
	public ResponseEntity<Object> region(@PathVariable("hashKey")String hashKey) {
		List<Areas> list = regionService.getReginByKey(hashKey);
		return new ResponseEntity<Object>(list,HttpStatus.OK);
	}
	
	// 测试接口
	@GetMapping("/test")
	@ResponseBody
	public ResponseEntity<Object> region() {
		regionRedisTemplate.opsForHash().put("test", "001", "test001");
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	// 测试接口
	@GetMapping("/get")
	@ResponseBody
	public ResponseEntity<Object> get() {
		Object object = regionRedisTemplate.opsForHash().get("test", "001");
		return new ResponseEntity<Object>(JSON.toJSONString(object),HttpStatus.OK);
	}
}
