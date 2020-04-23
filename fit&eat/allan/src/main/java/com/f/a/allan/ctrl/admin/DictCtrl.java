package com.f.a.allan.ctrl.admin;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.f.a.allan.entity.bo.DictBo;
import com.f.a.allan.entity.pojo.Dict;
import com.f.a.allan.entity.request.DictRequest;
import com.f.a.allan.entity.request.DictRequest.DictInsert;
import com.f.a.allan.entity.request.DictRequest.DictUpdate;
import com.f.a.allan.service.DictService;
import com.f.a.allan.utils.ObjectUtils;

@RestController
@RequestMapping("/admin/dict")
public class DictCtrl {
	
	@Autowired
	private DictService dictService;
	
	@PostMapping
	public ResponseEntity<Object> dictAdd(@RequestBody @Validated(value =DictInsert.class) DictRequest request){
		DictBo dictBo = new DictBo();
		ObjectUtils.copy(dictBo, request);
		Dict dict = dictService.insert(dictBo);
		return new ResponseEntity<Object>(dict,HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> dictById(@PathVariable("id") String id){
		if(!StringUtils.isNotBlank(id)) {
			return new ResponseEntity<Object>("id blank is not allowed ",HttpStatus.BAD_REQUEST);
		}
		Dict dict = dictService.getById(id);
		return new ResponseEntity<Object>(dict,HttpStatus.OK);
	}
	

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> dictDelete(@PathVariable("id") String id){
		if(!StringUtils.isNotBlank(id)) {
			return new ResponseEntity<Object>("id blank is not allowed ",HttpStatus.BAD_REQUEST);
		}
		boolean flag = dictService.removeById(id);
		if(!flag) {
			new ResponseEntity<Object>(null,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(null,HttpStatus.OK);
	}
	
	@PutMapping
	public  ResponseEntity<Object> dictUpdate(@RequestBody @Validated(value = DictUpdate.class) DictRequest request){
		DictBo dictBo = new DictBo();
		ObjectUtils.copy(dictBo, request);
		boolean flag =  dictService.update(dictBo);
		if(!flag) {
			return new ResponseEntity<Object>("update failed",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(dictBo,HttpStatus.OK);
	}
	
	
	
}
