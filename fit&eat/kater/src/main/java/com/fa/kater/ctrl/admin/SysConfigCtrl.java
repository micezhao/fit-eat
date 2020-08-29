package com.fa.kater.ctrl.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fa.kater.entity.requset.SysConfigRequest;
import com.fa.kater.enums.DrEnum;
import com.fa.kater.pojo.SysDict;
import com.fa.kater.service.impl.SysDictServiceImpl;


@RestController
@RequestMapping("/admin/sysconfig")
public class SysConfigCtrl {
	
	@Autowired
	private SysDictServiceImpl sysdictService;
	
	@PostMapping("dict/parent")
	public ResponseEntity<Object> addParentDict(@RequestBody SysConfigRequest request){
		SysDict dict = new SysDict();
		dict.setPid(0L);
		dict.setGroup(request.getGroup());
		dict.setDeleted(0);
		dict.setDr(DrEnum.AVAILABLE.getCode());
		if(StringUtils.isNotBlank(request.getSort())) {
			dict.setSort(Integer.parseInt(request.getSort()));
		}else {
			dict.setSort(99);
		}
		boolean result = sysdictService.save(dict);
		if(!result) {
			throw new RuntimeException("新增数据字典记录失败");
		}
		Map<String,Boolean> resultmap = new HashMap<String,Boolean>();
		resultmap.put("result", result);
		return new ResponseEntity<Object>(resultmap,HttpStatus.OK);
	}
	
	@PostMapping("dict/sub")
	public ResponseEntity<Object> addSubDict(@RequestBody SysConfigRequest request){
		Long pid = Long.parseLong(request.getPid());
		SysDict count = sysdictService.getOne(new QueryWrapper<SysDict>(new SysDict().setId(pid).setDr(DrEnum.AVAILABLE.getCode())));
		if(count == null) {
			throw new RuntimeException("数据字典中不存在当前父属性");
		}
		SysDict dict = new SysDict();
		dict.setPid(Long.parseLong(request.getPid()));
		dict.setGroup(request.getGroup());
		dict.setKey(request.getKey());
		dict.setKeyName(request.getKeyName());
		dict.setValue(request.getValue());
		dict.setDeleted(0);
		if(StringUtils.isNotBlank(request.getValueName())) {
			dict.setValueName(request.getValueName());
		}
		dict.setDr(DrEnum.AVAILABLE.getCode());
		if(StringUtils.isNotBlank(request.getSort())) {
			dict.setSort(Integer.parseInt(request.getSort()));
		}else {
			dict.setSort(99);
		}
		boolean result = sysdictService.save(dict);
		Map<String,Boolean> resultmap = new HashMap<String,Boolean>();
		resultmap.put("result", result);
		return new ResponseEntity<Object>(resultmap,HttpStatus.OK);
	}
	

	
	
	
	
	

	
	
	
}
