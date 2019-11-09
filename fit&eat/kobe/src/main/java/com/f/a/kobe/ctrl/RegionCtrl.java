package com.f.a.kobe.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.kobe.pojo.China;
import com.f.a.kobe.service.RegionService;


@RestController
@RequestMapping("region")
public class RegionCtrl {
	
	@Autowired
	private RegionService regionService;
	
	/**
	 * 批量同步中国地址信息的接口
	 * @return
	 */
	@GetMapping("/batchSync")
	@ResponseBody
	public ResponseEntity<Object> sync() {
		regionService.sycnRegion2Redis();
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@GetMapping("/{key}/{hashKey}")
	@ResponseBody
	public ResponseEntity<Object> region(@PathVariable("key")String key,@PathVariable("hashKey")String hashKey) {
		China region = regionService.getReginByKey(key, hashKey);
		return new ResponseEntity<Object>(region,HttpStatus.OK);
	}
	
}
